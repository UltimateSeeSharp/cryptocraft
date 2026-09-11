# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project state

CryptoCraft is a NeoForge Minecraft mod where building and running a **real** cryptocurrency
miner is the gameplay. Design is complete and written down in `docs/`. The MDK template
content has been removed (phase 2.1); `CryptoCraft.java` is a bare `@Mod` entry point and
`Config.java` holds a single placeholder setting until phase 2.3. No blocks, items or
registries exist yet — phase 2.2 adds the registration scaffolding.

**Start every session by reading `PLAN.md`**, then the subsystem doc the step names. `PLAN.md`
holds a STATUS block naming the current step; work the first step not marked done, then update
STATUS before stopping.

## Build and run

Gradle wrapper, NeoForge ModDevGradle. Java 21 toolchain (Mojang ships 21 for 1.21.1).

```
./gradlew build           # compile + jar
./gradlew runClient       # launch client with the mod
./gradlew runServer       # launch dedicated server (--nogui)
./gradlew runData         # data generators -> src/generated/resources
./gradlew runGameTestServer   # run all registered gametests headless, then exit
./gradlew --refresh-dependencies   # fix missing libraries in the IDE
```

Gametests are namespaced to `cryptocraft` (`neoforge.enabledGameTestNamespaces`) and are also
reachable in-game via `/test`. `runGameTestServer` crashes when no gametests are registered —
expected until the first one exists.

Version and mod metadata live in `gradle.properties` (`minecraft_version`, `neo_version`,
`mod_id`, `mod_version`, …). `src/main/templates/META-INF/neoforge.mods.toml` is a Groovy
`expand` template — edit it, not a generated copy, and reference properties as `${mod_id}`.
Adding a property used there means adding it to `replaceProperties` in `build.gradle`; a
missing one fails the build.

Declare optional runtime-only mod deps in the `localRuntime` configuration, not `runtimeOnly`,
so they are not published as dependencies.

## Architecture

The design docs are the specification. Each fact lives in exactly one document:

| Doc | Covers |
|---|---|
| `docs/CRYPTOCRAFT.md` | System overview, racks, multiblock rules, heat/cooling, power, GUIs, progression |
| `docs/MINING.md` | Pools, client-side hashing, real-mining mapping, RandomX/FFM, config |
| `docs/FABRICATION.md` | Chip chain, generations, photomasks, dopants, pre-fab route |
| `docs/HARDWARE.md` | Hardware types, brands, per-coin ladders, PSUs, art direction |
| `docs/ORES.md` | Worldgen figures, refining, by-products, per-chunk supply |
| `docs/CATALOGUE.md` | Every block, item and fluid with its recipe; holds the TBD balancing values |

Load-bearing structural decisions:

- **Coins are pluggable.** A coin supplies an algorithm implementation and a pool list; nothing
  in the rack, fab, heat or power systems is coin-specific. Monero/RandomX ships first,
  Bitcoin/SHA-256 follows. Hardware value, wattage and efficiency are per coin.
- **Two-sided split.** The server owns rig state and computes effective hashrate; the **client**
  owns the payout address, the pool socket and the hashing threads. The server must never open a
  socket or see an address.
- **The Stratum/RandomX client is Minecraft-free** (PLAN phase 1) — a subproject or separate
  source set, runnable and unit-testable without launching the game. RandomX binds
  `librandomx` through Java 21's Foreign Function & Memory API; memory mode (fast 2GB / light
  256MB) is auto-selected.
- **Racks vs controller.** Racks are a structural multiblock: any touching racks form one
  structure, no size limit, no PSU/controller/power required for validity. Each rack has 4 rows
  (transistor board 4, CPU 1, GPU 2, ASIC 4, PSU 1). Mining additionally requires PSU wattage
  covering the draw, a controller link (adjacency or the mod's data-only cable), and power.
  Losing a requirement just stops mining; only overheat shutdown needs a manual restart.
- **Power in real watts.** The mod generates and stores no energy — PSUs are the sinks, accepting
  energy via NeoForge's energy capability from Mekanism et al. The Creative Power Cell exists so
  the mod works without a tech mod.
- **Heat comes from actual wattage and hardware grade**, not tier, since progression is not
  always CPU→ASIC. Throttle first, then shutdown. Fire has exactly one source: a fake-rated PSU
  failing under sustained real-capacity overload.
- **GUI scope split.** Rack screen = interact with this rack's rows/fans plus its structure's
  totals. Controller screen = whole operation, and the only place an address is entered.

Hard dependency: Patchouli (guide book). Distribution is GitHub Releases — Modrinth and
CurseForge prohibit cryptocurrency mining.

## Rules for this project

These come from `PLAN.md`; they override convenience.

- **Do not fake the mining.** A step is not done until a real pool accepts a share.
- **Never hash on the server tick.** Background threads only.
- **The address is the only secret.** It never leaves the client, is never logged, and anything
  resembling a private key is refused.
- **Do not invent design decisions.** The docs are decided; a gap is a mistake, not licence.
  Write the question into `PLAN.md` under "Blocked on" instead.
- **Keep `docs/` accurate.** If the implementation diverges from the design, update the document
  in the same session.
- Balancing values marked TBD in `docs/CATALOGUE.md` are phase 9 work and want a running mod.
