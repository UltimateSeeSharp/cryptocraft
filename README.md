# CryptoCraft

A NeoForge mod for Minecraft 1.21.1 where building and running a **real** cryptocurrency
miner is the gameplay. Mine ore, fabricate chips, rack the hardware, feed it power and
cooling, and point it at a pool with your own payout address.

The mining is real. The payout is real, goes to an address you control, and is worth very
little — electricity costs more than the mod earns. That is the point: the machine you
build in game drives an actual miner.

**Status: in development.** See `PLAN.md` for the build plan and current step.

## Design

`docs/` is the specification. Each fact lives in exactly one document.

| Doc | Covers |
|---|---|
| `docs/CRYPTOCRAFT.md` | System overview, racks, multiblock rules, heat/cooling, power, GUIs, progression |
| `docs/MINING.md` | Pools, client-side hashing, real-mining mapping, RandomX/FFM, config |
| `docs/FABRICATION.md` | Chip chain, generations, photomasks, dopants, pre-fab route |
| `docs/HARDWARE.md` | Hardware types, brands, per-coin ladders, PSUs, art direction |
| `docs/ORES.md` | Worldgen figures, refining, by-products, per-chunk supply |
| `docs/CATALOGUE.md` | Every block, item and fluid with its recipe |

## Building

Java 21 toolchain, Gradle wrapper.

```
./gradlew build           # compile + jar
./gradlew runClient       # launch client with the mod
./gradlew runServer       # launch dedicated server
./gradlew runData         # data generators -> src/generated/resources
./gradlew runGameTestServer   # run gametests headless
```

## Distribution

GitHub Releases only. Modrinth and CurseForge both prohibit cryptocurrency mining.

Antivirus software will flag the bundled RandomX library. Detection is behavioural — a
mining library is a mining library — and code signing would not prevent it.

## Licence

All Rights Reserved. Built from the NeoForge MDK; see `TEMPLATE_LICENSE.txt`.
