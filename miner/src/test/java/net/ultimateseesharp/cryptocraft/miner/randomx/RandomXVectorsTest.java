package net.ultimateseesharp.cryptocraft.miner.randomx;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.nio.charset.StandardCharsets;
import java.util.HexFormat;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * The official vectors from upstream {@code src/tests/tests.cpp} (v1.2.3), tests 1a–1f.
 * Light and fast mode must produce identical hashes.
 *
 * <p>Skipped when no native library is vendored for this platform — run the
 * <em>Build RandomX</em> workflow, which opens a PR adding them.
 */
class RandomXVectorsTest {
    private static final HexFormat HEX = HexFormat.of();

    @BeforeAll
    static void requireNativeLibrary() {
        Assumptions.assumeTrue(NativeLibrary.isAvailable(),
                () -> "No RandomX library for " + NativeLibrary.platformDirectory()
                        + "; run the Build RandomX workflow");
    }

    static Stream<Arguments> vectors() {
        return Stream.of(
                Arguments.of("1a", "test key 000",
                        "This is a test".getBytes(StandardCharsets.UTF_8),
                        "639183aae1bf4c9a35884cb46b09cad9175f04efd7684e7262a0ac1c2f0b4e3f"),
                Arguments.of("1b", "test key 000",
                        "Lorem ipsum dolor sit amet".getBytes(StandardCharsets.UTF_8),
                        "300a0adb47603dedb42228ccb2b211104f4da45af709cd7547cd049e9489c969"),
                Arguments.of("1c", "test key 000",
                        "sed do eiusmod tempor incididunt ut labore et dolore magna aliqua"
                                .getBytes(StandardCharsets.UTF_8),
                        "c36d4ed4191e617309867ed66a443be4075014e2b061bcdaf9ce7b721d2b77a8"),
                Arguments.of("1d", "test key 001",
                        "sed do eiusmod tempor incididunt ut labore et dolore magna aliqua"
                                .getBytes(StandardCharsets.UTF_8),
                        "e9ff4503201c0c2cca26d285c93ae883f9b1d30c9eb240b820756f2d5a7905fc"),
                Arguments.of("1e", "test key 001",
                        HEX.parseHex("0b0b98bea7e805e0010a2126d287a2a0cc833d312cb786385a7c2f9de69d2553"
                                + "7f584a9bc9977b00000000666fd8753bf61a8631f12984e3fd44f4014eca6292768"
                                + "17b56f32e9b68bd82f416"),
                        "c56414121acda1713c2f2a819d8ae38aed7c80c35c2a769298d34f03833cd5f1"));
    }

    @ParameterizedTest(name = "light mode, test {0}")
    @MethodSource("vectors")
    void lightMode(String name, String key, byte[] input, String expected) {
        assertHash(MemoryMode.LIGHT, RandomXContext.key(key), input, expected);
    }

    @ParameterizedTest(name = "fast mode, test {0}")
    @MethodSource("vectors")
    void fastMode(String name, String key, byte[] input, String expected) {
        assertHash(MemoryMode.FAST, RandomXContext.key(key), input, expected);
    }

    /** Test 1f — the ISUB_R edge case, whose key is raw bytes rather than a string. */
    @Test
    void isubEdgeCase() {
        byte[] key = HEX.parseHex("7797373ea4633194640bf8d8c3b66724d6aa7bd2dc20e009df2f8f1710abe8");
        byte[] input = HEX.parseHex(
                "1010e1eaf8cf067b37b5f0ee031ab23ed1755e090a3af4415830145853e2be3e1f6821fed84dae58d0"
                        + "0e00da5214d6c1f2d0622e0abd51f9373d04e0b0f8e6d6514d90689721c4aac5a9bb0d");
        assertHash(MemoryMode.LIGHT, key, input,
                "78af2a1864c42abce36d2e8983e13df99b2af0ce1362999af09fab004d4435a8");
    }

    /** Different keys must give different hashes — guards against a silently dead cache. */
    @Test
    void keyChangesHash() {
        byte[] input = "This is a test".getBytes(StandardCharsets.UTF_8);
        byte[] a = hash(MemoryMode.LIGHT, RandomXContext.key("test key 000"), input);
        byte[] b = hash(MemoryMode.LIGHT, RandomXContext.key("test key 001"), input);
        assertNotEquals(HEX.formatHex(a), HEX.formatHex(b));
    }

    /** One context serves many VMs; each must hash independently and agree. */
    @Test
    void multipleVmsShareOneContext() {
        byte[] input = "This is a test".getBytes(StandardCharsets.UTF_8);
        try (RandomXContext context = RandomXContext.create(RandomXContext.key("test key 000"), MemoryMode.LIGHT);
             RandomXVm first = context.newVm();
             RandomXVm second = context.newVm()) {
            assertArrayEquals(first.hash(input), second.hash(input));
        }
    }

    private static void assertHash(MemoryMode mode, byte[] key, byte[] input, String expected) {
        assertEquals(expected, HEX.formatHex(hash(mode, key, input)));
    }

    private static byte[] hash(MemoryMode mode, byte[] key, byte[] input) {
        try (RandomXContext context = RandomXContext.create(key, mode);
             RandomXVm vm = context.newVm()) {
            return vm.hash(input);
        }
    }
}
