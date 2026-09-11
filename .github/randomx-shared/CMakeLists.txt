# Wrapper that builds upstream RandomX as a usable shared library.
#
# Upstream's RANDOMX_EXPORT macro is empty and its CMakeLists declares
# cmake_minimum_required(VERSION 3.10), under which the CMAKE_WINDOWS_EXPORT_ALL_SYMBOLS
# variable does not reliably initialise the target property. MSVC then exports nothing:
# no import library, the bundled executables fail to link, and the DLL holds no symbols
# for the binding to resolve. Setting the property on the target is policy-independent.
#
# cmake -S .github/randomx-shared -B build -DRANDOMX_SOURCE_DIR=<path to randomx>

cmake_minimum_required(VERSION 3.15)
project(randomx-shared)

if(NOT RANDOMX_SOURCE_DIR)
  message(FATAL_ERROR "Set -DRANDOMX_SOURCE_DIR to the RandomX checkout")
endif()

set(BUILD_SHARED_LIBS ON CACHE BOOL "" FORCE)

add_subdirectory("${RANDOMX_SOURCE_DIR}" randomx-build)

if(NOT TARGET randomx)
  message(FATAL_ERROR "RandomX did not define the 'randomx' target")
endif()

set_target_properties(randomx PROPERTIES WINDOWS_EXPORT_ALL_SYMBOLS ON)
