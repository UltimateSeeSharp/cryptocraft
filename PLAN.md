# CryptoCraft — Build plan

Steps to build the mod described in `docs/`. Each step is sized for one agent session:
pick the first step not marked done, complete it against its acceptance criteria, update
STATUS, stop.

**Read first:** `docs/CRYPTOCRAFT.md` for the system overview, then whichever subsystem
document the step names.

---

## STATUS

**Current step:** 1.1
**Last updated:** 2026-09-11 — 2.1 done

| Phase | Steps | State |
|---|---|---|
| 1 — Stratum client | 1.1–1.5 | not started |
| 2 — Mod skeleton | 2.1–2.3 | 2.1 done |
| 3 — Racks and hardware | 3.1–3.5 | not started |
| 4 — Mining integration | 4.1–4.4 | not started |
| 5 — Processing chain | 5.1–5.5 | not started |
| 6 — Worldgen and materials | 6.1–6.3 | not started |
| 7 — Heat and power | 7.1–7.3 | not started |
| 8 — Content and polish | 8.1–8.5 | not started |
| 9 — Balancing | 9.1–9.3 | not started |
| 10 — Release | 10.1–10.3 | not started |

**Blocked on:** nothing

**Notes for the next agent:**

- 1.1 starts with the CI workflow that builds and vendors the binaries (`docs/MINING.md`
  §5); no C++ toolchain on this machine, so the binding can't be tested locally until then.
- Patchouli is not declared in `neoforge.mods.toml` — a required dep absent from the dev
  runtime breaks `runClient`. Add it with a `localRuntime` dep in 8.4.

---

## Phase 1 — Stratum client

Standalone Java, no Minecraft. This is the piece that either works or doesn't; everything
else is ordinary mod work. Build it as a subproject or separate source set so it can be
run and tested without launching the game.

Reference: `docs/MINING.md`.

### 1.1 — RandomX native binding

Bind `librandomx` through Java 21's Foreign Function & Memory API.

**Done when:** a JUnit test hashes a known input and produces the expected RandomX output,
matching the reference implementation's test vectors. Both light and fast mode work, and
the mode is selected from available memory.

### 1.2 — Stratum protocol

Implement Stratum for Monero: `login`, `job`, `submit`, `keepalived`. Monero's Stratum
differs from Bitcoin's — it sends a complete blob rather than a coinbase to assemble.

**Done when:** a unit test against recorded pool traffic parses every message type and
produces correctly formed requests.

### 1.3 — Share validation

Build the hashing blob from the job, apply the nonce, hash, and compare against the
target.

**Done when:** known-good job data produces a share the test recognises as valid, and
byte order is verified against a captured real share rather than assumed.

### 1.4 — Live pool connection

Connect to C3Pool, authenticate with a real address, receive a job, submit a share.

**Done when:** the pool accepts a share. Record the accepted-share log line in STATUS.
Nothing downstream is trustworthy until this passes.

### 1.5 — Worker pool

Multi-threaded hashing with a configurable thread count and duty cycle, clean start and
stop, atomic counters for hashrate and accepted/rejected shares.

**Done when:** threads scale to a set count, stop cleanly, and counters are readable from
another thread without locking.

---

## Phase 2 — Mod skeleton

Reference: `docs/CRYPTOCRAFT.md`.

### 2.1 — Clean the template

Remove `ExampleModClient.java` and `assets/examplemod/`. Set up the mod's own package
structure, registries and asset paths.

**Done when:** `runClient` launches with the mod loaded and no example content present.

### 2.2 — Registration scaffolding

Deferred registers for blocks, items, block entities, menus, recipe types and creative
tabs.

**Done when:** a placeholder block registers, appears in its creative tab, and places in
world.

### 2.3 — Config

Config file with the four settings in `docs/MINING.md` §6.

**Done when:** values load, survive a restart, and are readable from game code.

---

## Phase 3 — Racks and hardware

Reference: `docs/CRYPTOCRAFT.md` §2, §8; `docs/HARDWARE.md`.

### 3.1 — Rack block and block entity

A rack that places, holds 4 rows of installed items, and persists them across save and
reload.

**Done when:** hardware installed in a rack is still there after a world reload.

### 3.2 — Multiblock structure

Adjacent racks form one structure. Breaking one splits it.

**Done when:** placing racks together forms a structure, breaking a middle one splits it
into two, and both halves behave independently.

### 3.3 — Hardware items

The four hardware types as items with generation and variant data, installable into rack
rows by right-click and through the GUI.

**Done when:** every variant in `docs/CATALOGUE.md` exists, installs, and respects its row
cost.

### 3.4 — PSUs and wattage

PSU items, capacity per variant, and the rule that a rack runs only when its PSUs cover
the installed draw.

**Done when:** a rack refuses to run when overloaded and runs when a PSU is added.

### 3.5 — Rack GUI

The rack screen from `docs/CRYPTOCRAFT.md` §9.2 — its own rows plus structure totals.

**Done when:** the screen shows live values that match server state, not client guesses.

---

## Phase 4 — Mining integration

Reference: `docs/MINING.md`.

### 4.1 — Mining Controller

Block, block entity, and the tabbed GUI from §9.1. Address validation, pool selection,
coin selection, Start/Stop.

**Done when:** an address is entered, validated, rejected if it looks like a private key,
and persists across reload.

### 4.2 — Controller-to-rack linking

Adjacency and cable linking, with the controller aware of every rack it serves.

**Done when:** racks connect and disconnect correctly, and the controller's totals match
what is installed.

### 4.3 — Client-side hashing

Wire Phase 1's client into the mod. The server computes the rig's effective hashrate; the
client runs the threads and connects to the pool.

**Done when:** a rig built in game produces shares a pool accepts, and the server never
opens a socket or sees an address.

### 4.4 — Real-mining mapping

Benchmark, adaptive cap, mining power per variant, utilisation curve.

**Done when:** adding hardware measurably raises the effective hashrate, and the tick rate
does not drop under load.

---

## Phase 5 — Processing chain

Reference: `docs/FABRICATION.md`.

### 5.1 — Machine framework

A shared base for the processing machines: energy, input and output slots, progress,
recipe lookup.

**Done when:** one machine processes a recipe, consumes energy, and shows progress.

### 5.2 — Electrolysis Cell and refining

Ore refining with by-products and their rates.

**Done when:** every recipe in `docs/CATALOGUE.md` works and by-products appear at their
stated rate.

### 5.3 — Fab chain machines

Crystal Furnace, Wafer Saw, Lithography Machine, EUV Lithography Machine, Packaging
Machine, Semiconductor Bench — with batch outputs and per-run material costs.

**Done when:** one ingot yields 8 wafers yields 32 dies yields 32 chips, and generation
materials are consumed per run rather than per die.

### 5.4 — Photomask system

Blank masks, the two patterning tables, per-design masks, and durability.

**Done when:** a blank becomes a design-specific mask, that mask produces dies of that
design, and it wears out after its set uses.

### 5.5 — Recipe types and JEI

Custom recipe types with JEI or EMI integration.

**Done when:** every machine recipe is visible in the recipe viewer.

---

## Phase 6 — Worldgen and materials

Reference: `docs/ORES.md`.

### 6.1 — Ore blocks and raw items

The 8 ores plus Mineral Sand, with drops, and the raw items.

**Done when:** every ore places, drops the right item in the right quantity, and respects
its tool requirement.

### 6.2 — Worldgen

Placement to the figures in `docs/ORES.md` §2, including surface layers and biome
restrictions.

**Done when:** a generated world contains every ore at roughly its stated frequency, and
Borax appears only in arid biomes.

### 6.3 — Vanilla-derived materials

Phosphorus from organic matter, silicon from sand and quartz, silver and molybdenum from
copper.

**Done when:** each derived material is obtainable at its stated rate.

---

## Phase 7 — Heat and power

Reference: `docs/CRYPTOCRAFT.md` §5, §6.

### 7.1 — Energy capability

NeoForge energy on PSUs, so a tech mod's cables can power a rack.

**Done when:** a Mekanism cable powers a rack through its PSU, and the Creative Power Cell
does the same without a tech mod.

### 7.2 — Heat

Heat from wattage and hardware grade, throttling, shutdown, and manual restart.

**Done when:** an uncooled rack throttles, then shuts down, and restarts only from the
controller.

### 7.3 — Cooling

Rack fans, liquid-cooled racks, the Room AC unit, the immersion tank, and environmental
modifiers.

**Done when:** each cooling option measurably reduces heat, and biome, depth and adjacent
water shift the requirement.

---

## Phase 8 — Content and polish

### 8.1 — Models and textures

Every block and item, with hardware distinct per generation and tinted per brand
(`docs/HARDWARE.md` §6). Racks render their installed contents.

**Done when:** nothing renders as a missing texture, and a rack visually fills as hardware
is installed.

### 8.2 — Recipes

Crafting recipes for everything craftable. Components are already specified in
`docs/CATALOGUE.md`; the rest are decided during this step and written back to the
catalogue.

**Done when:** every item is obtainable in survival and the catalogue matches what ships.

### 8.3 — Loot

Transistors in world loot.

**Done when:** transistors appear in the chosen loot tables at a sensible rate.

### 8.4 — Guide book

Patchouli book with the chapters in `docs/CRYPTOCRAFT.md` §10.

**Done when:** the book is craftable and every chapter is written.

### 8.5 — Equipment

Zirconium armour and tools.

**Done when:** the armour prevents heat damage and the tools work.

---

## Phase 9 — Balancing

Everything marked TBD in `docs/CATALOGUE.md`. These are tuning values and want a running
mod, not a desk.

### 9.1 — Mining power and the curve

Per-variant mining power, and `k` in the utilisation curve.

**Done when:** upgrading hardware feels worthwhile, and a large farm still gains from
growth without reaching the ceiling.

### 9.2 — Wattage and heat

Per-variant wattage, cooling capacities, throttle and shutdown thresholds, and the
fake-rated PSU's claimed and real capacity.

**Done when:** cooling is a real constraint that a reasonable build can meet.

### 9.3 — Recipe costs

Quantities across the whole tree, checked against the per-chunk yields in `docs/ORES.md`
§6.

**Done when:** no material is a bottleneck out of proportion to its role, and the first
miner is reachable in the window `docs/CRYPTOCRAFT.md` §11 describes.

---

## Phase 10 — Release

### 10.1 — Build

A distributable JAR that installs into a real client.

**Done when:** the JAR loads in a fresh 1.21.1 NeoForge instance alongside Mekanism.

### 10.2 — README

Installation, the antivirus explanation, the honest earnings statement, and why it is not
on Modrinth or CurseForge.

**Done when:** a player can install and run the mod from the README alone.

### 10.3 — GitHub Release

Tag, build, publish.

**Done when:** the release is downloadable and the JAR in it works.

---

## Rules for agents

- **Do not fake the mining.** If a share is not accepted by a real pool, the step is not
  done.
- **Never hash on the server tick.** Background threads only.
- **The address is the only secret.** It never leaves the client, is never logged, and
  anything resembling a private key is refused.
- **Update STATUS before stopping**, including anything the next agent needs to know.
- **If a design decision is missing**, write the question into STATUS under "Blocked on"
  rather than inventing an answer. The design documents are decided; gaps are mistakes,
  not licence.
- **Keep `docs/` accurate.** If implementation diverges from the design, update the
  document in the same session.
