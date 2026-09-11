package net.ultimateseesharp.cryptocraft.miner.ffm;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Array;
import java.nio.file.Path;
import java.util.Optional;

/**
 * Reflective access to the Foreign Function &amp; Memory API.
 *
 * <p>1.21.1 runs on Java 21, where FFM is still a preview API — compiling against it
 * requires {@code --enable-preview}, which version-locks class files so Minecraft's
 * launcher cannot load them. The runtime is complete on 21; only the compiler is gated.
 * So this class resolves the API by reflection once, and callers use the resulting
 * {@link MethodHandle}s, which the JIT treats like any other. {@code docs/MINING.md} §5.
 *
 * <p>Only methods with identical signatures on Java 21 and current JVMs are used, so one
 * build runs on both.
 */
public final class Ffm {
    private static final String PACKAGE = "java.lang.foreign.";

    public static final Class<?> ARENA = type("Arena");
    public static final Class<?> LINKER = type("Linker");
    public static final Class<?> SYMBOL_LOOKUP = type("SymbolLookup");
    public static final Class<?> MEMORY_SEGMENT = type("MemorySegment");
    public static final Class<?> MEMORY_LAYOUT = type("MemoryLayout");
    public static final Class<?> FUNCTION_DESCRIPTOR = type("FunctionDescriptor");
    public static final Class<?> VALUE_LAYOUT = type("ValueLayout");
    public static final Class<?> LINKER_OPTION = type("Linker$Option");

    /** The native linker, resolved once. */
    private static final Object NATIVE_LINKER = invokeStatic(LINKER, "nativeLinker");

    /** {@code MemorySegment.NULL} — returned by RandomX allocators on failure. */
    public static final Object NULL_SEGMENT = staticField(MEMORY_SEGMENT, "NULL");

    private Ffm() {
    }

    // --- Layouts -------------------------------------------------------------------

    public static Object layout(String name) {
        return staticField(VALUE_LAYOUT, name);
    }

    public static final Object JAVA_INT = layout("JAVA_INT");
    public static final Object JAVA_LONG = layout("JAVA_LONG");
    public static final Object JAVA_BYTE = layout("JAVA_BYTE");
    public static final Object ADDRESS = layout("ADDRESS");

    // --- Arenas --------------------------------------------------------------------

    /** {@code Arena.ofShared()} — usable from any thread until closed. */
    public static AutoCloseable sharedArena() {
        return (AutoCloseable) invokeStatic(ARENA, "ofShared");
    }

    /** {@code Arena.ofConfined()} — single-threaded, for short-lived scratch buffers. */
    public static AutoCloseable confinedArena() {
        return (AutoCloseable) invokeStatic(ARENA, "ofConfined");
    }

    /** {@code arena.allocate(byteSize)}. */
    public static Object allocate(Object arena, long byteSize) {
        return invoke(ARENA, arena, "allocate", new Class<?>[]{long.class}, byteSize);
    }

    // --- Library loading -----------------------------------------------------------

    /** {@code SymbolLookup.libraryLookup(path, arena)}. */
    public static Object libraryLookup(Path path, Object arena) {
        return invokeStatic(SYMBOL_LOOKUP, "libraryLookup",
                new Class<?>[]{Path.class, ARENA}, path, arena);
    }

    /** {@code Linker.defaultLookup()} — the C runtime. Used to smoke-test the binding. */
    public static Object defaultLookup() {
        return invoke(LINKER, NATIVE_LINKER, "defaultLookup", new Class<?>[0]);
    }

    /** {@code lookup.find(name)}, empty when the library does not export it. */
    @SuppressWarnings("unchecked")
    public static Optional<Object> find(Object lookup, String name) {
        return (Optional<Object>) invoke(SYMBOL_LOOKUP, lookup, "find",
                new Class<?>[]{String.class}, name);
    }

    // --- Downcalls -----------------------------------------------------------------

    /** {@code FunctionDescriptor.of(returnLayout, argLayouts...)}. */
    public static Object descriptorOf(Object returnLayout, Object... argLayouts) {
        return invokeStatic(FUNCTION_DESCRIPTOR, "of",
                new Class<?>[]{MEMORY_LAYOUT, layoutArrayType()},
                returnLayout, toLayoutArray(argLayouts));
    }

    /** {@code FunctionDescriptor.ofVoid(argLayouts...)}. */
    public static Object descriptorOfVoid(Object... argLayouts) {
        return invokeStatic(FUNCTION_DESCRIPTOR, "ofVoid",
                new Class<?>[]{layoutArrayType()}, (Object) toLayoutArray(argLayouts));
    }

    /**
     * {@code linker.downcallHandle(symbol, descriptor)}. The returned handle is a normal
     * {@link MethodHandle}; calls through it are not reflective.
     */
    public static MethodHandle downcallHandle(Object symbol, Object descriptor) {
        Object options = Array.newInstance(LINKER_OPTION, 0);
        return (MethodHandle) invoke(LINKER, NATIVE_LINKER, "downcallHandle",
                new Class<?>[]{MEMORY_SEGMENT, FUNCTION_DESCRIPTOR, options.getClass()},
                symbol, descriptor, options);
    }

    /**
     * Downcall handles are typed in FFM terms ({@code MemorySegment}), which this code
     * cannot name. Casting them to {@code Object} parameters lets call sites use
     * {@code invoke} without the exact-type requirement of {@code invokeExact}.
     */
    public static MethodHandle asObjectHandle(MethodHandle handle) {
        MethodType type = handle.type();
        MethodType erased = type;
        for (int i = 0; i < type.parameterCount(); i++) {
            if (!type.parameterType(i).isPrimitive()) {
                erased = erased.changeParameterType(i, Object.class);
            }
        }
        if (!erased.returnType().isPrimitive() && erased.returnType() != void.class) {
            erased = erased.changeReturnType(Object.class);
        }
        return handle.asType(erased);
    }

    // --- Memory copying ------------------------------------------------------------

    /** {@code MemorySegment.copy(byte[], srcOffset, dst, JAVA_BYTE, dstOffset, count)}. */
    public static void copyToNative(byte[] src, int srcOffset, Object dst, long dstOffset, int count) {
        invokeStatic(MEMORY_SEGMENT, "copy",
                new Class<?>[]{Object.class, int.class, MEMORY_SEGMENT, VALUE_LAYOUT, long.class, int.class},
                src, srcOffset, dst, JAVA_BYTE, dstOffset, count);
    }

    /** {@code MemorySegment.copy(src, JAVA_BYTE, srcOffset, byte[], dstOffset, count)}. */
    public static void copyFromNative(Object src, long srcOffset, byte[] dst, int dstOffset, int count) {
        invokeStatic(MEMORY_SEGMENT, "copy",
                new Class<?>[]{MEMORY_SEGMENT, VALUE_LAYOUT, long.class, Object.class, int.class, int.class},
                src, JAVA_BYTE, srcOffset, dst, dstOffset, count);
    }

    /** True when a returned pointer is {@code MemorySegment.NULL}. */
    public static boolean isNull(Object segment) {
        return NULL_SEGMENT.equals(segment);
    }

    // --- Reflection plumbing -------------------------------------------------------

    private static Class<?> type(String simpleName) {
        try {
            return Class.forName(PACKAGE + simpleName);
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException(
                    "This JVM has no " + PACKAGE + simpleName + "; Java 21 or newer is required", e);
        }
    }

    private static Class<?> layoutArrayType() {
        return Array.newInstance(MEMORY_LAYOUT, 0).getClass();
    }

    private static Object toLayoutArray(Object... layouts) {
        Object array = Array.newInstance(MEMORY_LAYOUT, layouts.length);
        for (int i = 0; i < layouts.length; i++) {
            Array.set(array, i, layouts[i]);
        }
        return array;
    }

    private static Object staticField(Class<?> owner, String name) {
        try {
            return owner.getField(name).get(null);
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("No " + owner.getSimpleName() + "." + name + " on this JVM", e);
        }
    }

    private static Object invokeStatic(Class<?> owner, String name) {
        return invokeStatic(owner, name, new Class<?>[0]);
    }

    private static Object invokeStatic(Class<?> owner, String name, Class<?>[] signature, Object... args) {
        try {
            return owner.getMethod(name, signature).invoke(null, args);
        } catch (ReflectiveOperationException e) {
            throw unwrap(owner, name, e);
        }
    }

    private static Object invoke(Class<?> owner, Object receiver, String name, Class<?>[] signature, Object... args) {
        try {
            return owner.getMethod(name, signature).invoke(receiver, args);
        } catch (ReflectiveOperationException e) {
            throw unwrap(owner, name, e);
        }
    }

    private static RuntimeException unwrap(Class<?> owner, String name, ReflectiveOperationException e) {
        Throwable cause = e.getCause();
        if (cause instanceof RuntimeException runtime) {
            return runtime;
        }
        if (cause instanceof Error error) {
            throw error;
        }
        return new IllegalStateException(
                "Calling " + owner.getSimpleName() + "." + name + " failed", cause != null ? cause : e);
    }
}
