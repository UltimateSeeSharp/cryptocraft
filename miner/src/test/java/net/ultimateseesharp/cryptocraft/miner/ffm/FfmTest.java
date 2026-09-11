package net.ultimateseesharp.cryptocraft.miner.ffm;

import org.junit.jupiter.api.Test;

import java.lang.invoke.MethodHandle;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Exercises the reflective FFM layer without RandomX, so a failure here points at the
 * binding plumbing rather than the native library. Uses the C runtime's {@code strlen},
 * which every platform has.
 */
class FfmTest {
    @Test
    void resolvesFfmTypes() {
        assertNotNull(Ffm.ARENA);
        assertNotNull(Ffm.LINKER);
        assertNotNull(Ffm.MEMORY_SEGMENT);
        assertNotNull(Ffm.NULL_SEGMENT);
    }

    @Test
    void allocatesAndRoundTripsBytes() throws Exception {
        byte[] source = {1, 2, 3, 4, 5, 6, 7, 8};
        byte[] target = new byte[source.length];
        AutoCloseable arena = Ffm.confinedArena();
        try {
            Object segment = Ffm.allocate(arena, source.length);
            Ffm.copyToNative(source, 0, segment, 0, source.length);
            Ffm.copyFromNative(segment, 0, target, 0, target.length);
        } finally {
            arena.close();
        }
        assertArrayEquals(source, target);
    }

    @Test
    void callsIntoTheCRuntime() throws Throwable {
        AutoCloseable arena = Ffm.sharedArena();
        try {
            Object lookup = Ffm.defaultLookup();
            Object symbol = Ffm.find(lookup, "strlen").orElseThrow();
            MethodHandle strlen = Ffm.asObjectHandle(
                    Ffm.downcallHandle(symbol, Ffm.descriptorOf(Ffm.JAVA_LONG, Ffm.ADDRESS)));

            byte[] text = "hello ffm\0".getBytes(java.nio.charset.StandardCharsets.US_ASCII);
            Object segment = Ffm.allocate(arena, text.length);
            Ffm.copyToNative(text, 0, segment, 0, text.length);

            assertEquals(9L, (long) strlen.invoke(segment));
        } finally {
            arena.close();
        }
    }

    @Test
    void detectsNullSegments() {
        assertFalse(Ffm.isNull("not a segment"));
    }
}
