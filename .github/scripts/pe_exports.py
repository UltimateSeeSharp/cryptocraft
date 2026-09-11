"""Print the export names of a Windows PE/DLL, one per line.

MSVC emits no exports for RandomX unless CMAKE_WINDOWS_EXPORT_ALL_SYMBOLS is set, and the
binding resolves every function by name — so the build workflow checks the export table
rather than discovering the problem at runtime. dumpbin needs the MSVC dev shell; this
needs only the Python already on the runner.
"""

import struct
import sys


def exports(path):
    data = open(path, "rb").read()
    pe = struct.unpack_from("<I", data, 0x3C)[0]
    if data[pe:pe + 4] != b"PE\0\0":
        raise SystemExit(f"{path} is not a PE file")

    section_count, = struct.unpack_from("<H", data, pe + 6)
    optional_size, = struct.unpack_from("<H", data, pe + 20)
    magic, = struct.unpack_from("<H", data, pe + 24)
    # Data directories follow the optional header: PE32+ (0x20b) is 112 bytes, PE32 is 96.
    directory = pe + 24 + (112 if magic == 0x20B else 96)
    export_rva, _ = struct.unpack_from("<II", data, directory)
    if not export_rva:
        return []

    sections = []
    table = pe + 24 + optional_size
    for i in range(section_count):
        entry = table + 40 * i
        virtual_size, = struct.unpack_from("<I", data, entry + 8)
        virtual_addr, = struct.unpack_from("<I", data, entry + 12)
        raw_ptr, = struct.unpack_from("<I", data, entry + 20)
        sections.append((virtual_addr, virtual_size, raw_ptr))

    def offset(rva):
        for addr, size, raw in sections:
            if addr <= rva < addr + max(size, 1):
                return raw + (rva - addr)
        raise SystemExit(f"RVA {rva:#x} falls outside every section")

    header = offset(export_rva)
    count, = struct.unpack_from("<I", data, header + 24)
    names_rva, = struct.unpack_from("<I", data, header + 32)
    names = offset(names_rva)

    found = []
    for i in range(count):
        name_rva, = struct.unpack_from("<I", data, names + 4 * i)
        start = offset(name_rva)
        found.append(data[start:data.index(b"\0", start)].decode())
    return found


if __name__ == "__main__":
    if len(sys.argv) != 2:
        raise SystemExit("usage: pe_exports.py <file.dll>")
    for name in exports(sys.argv[1]):
        print(name)
