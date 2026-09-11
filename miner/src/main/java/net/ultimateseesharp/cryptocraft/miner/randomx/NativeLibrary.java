package net.ultimateseesharp.cryptocraft.miner.randomx;

import net.ultimateseesharp.cryptocraft.miner.ffm.Ffm;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Locale;

/**
 * Locates {@code librandomx} and opens it for symbol lookup. The binaries are built from
 * the pinned upstream tag by {@code .github/workflows/randomx.yml} and vendored under
 * {@code native/}; {@code cryptocraft.randomx.path} overrides for local builds.
 */
public final class NativeLibrary {
    public static final String PATH_PROPERTY = "cryptocraft.randomx.path";
    private static final String RESOURCE_ROOT = "/native";

    private NativeLibrary() {
    }

    /** Opens the library against {@code arena}, extracting the bundled copy if needed. */
    static Object load(Object arena) {
        return Ffm.libraryLookup(locate(), arena);
    }

    static Path locate() {
        String override = System.getProperty(PATH_PROPERTY);
        if (override != null && !override.isBlank()) {
            Path path = Path.of(override);
            if (!Files.isRegularFile(path)) {
                throw new IllegalStateException(PATH_PROPERTY + " does not point at a file: " + path);
            }
            return path;
        }
        return extractBundled();
    }

    private static Path extractBundled() {
        String resource = RESOURCE_ROOT + "/" + platformDirectory() + "/" + libraryFileName();
        try (InputStream in = NativeLibrary.class.getResourceAsStream(resource)) {
            if (in == null) {
                throw new IllegalStateException(
                        "No bundled RandomX library at " + resource
                                + ". Build it with the randomx workflow, or set -D" + PATH_PROPERTY);
            }
            Path dir = Files.createTempDirectory("cryptocraft-randomx");
            dir.toFile().deleteOnExit();
            Path target = dir.resolve(libraryFileName());
            Files.copy(in, target, StandardCopyOption.REPLACE_EXISTING);
            target.toFile().deleteOnExit();
            return target;
        } catch (IOException e) {
            throw new UncheckedIOException("Could not unpack " + resource, e);
        }
    }

    /** Resource directory for the running platform, e.g. {@code windows-x86_64}. */
    static String platformDirectory() {
        return osName() + "-" + archName();
    }

    static String libraryFileName() {
        return switch (osName()) {
            case "windows" -> "randomx.dll";
            case "macos" -> "librandomx.dylib";
            default -> "librandomx.so";
        };
    }

    private static String osName() {
        String os = System.getProperty("os.name", "").toLowerCase(Locale.ROOT);
        if (os.contains("win")) {
            return "windows";
        }
        if (os.contains("mac") || os.contains("darwin")) {
            return "macos";
        }
        return "linux";
    }

    private static String archName() {
        String arch = System.getProperty("os.arch", "").toLowerCase(Locale.ROOT);
        return switch (arch) {
            case "amd64", "x86_64" -> "x86_64";
            case "aarch64", "arm64" -> "aarch64";
            default -> arch;
        };
    }
}
