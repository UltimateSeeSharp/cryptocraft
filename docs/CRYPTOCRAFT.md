# CryptoCraft

How the mod works. Each subsystem has its own document; this one explains what they are
and why they behave as they do.

| Document | Covers |
|---|---|
| `ORES.md` | Worldgen, refining, the material economy |
| `FABRICATION.md` | The chip chain, photomasks, dopants, generations |
| `HARDWARE.md` | Hardware types, brands, generation coverage, PSUs |
| `MINING.md` | Pools, payout, client-side hashing, real-mining mapping |
| `CATALOGUE.md` | Every block, item and fluid, with recipes |

Every fact lives in exactly one place. Nothing enters these documents that has not been
approved.

**Status:** design complete. Recipe quantities and balancing values outstanding.

---

## 1. Overview

A NeoForge mod where building and running a real cryptocurrency miner is the gameplay.
Mining is real, not simulated.

**The mod is multi-coin.** Each coin brings its own algorithm, its own hardware ladder,
and its own pools. Adding a coin means adding those three things — nothing in the rack,
fab or heat systems is coin-specific.

| Coin | Algorithm | Status |
|---|---|---|
| Monero (XMR) | RandomX | Ships first |
| Bitcoin (BTC) | SHA-256 | Follows |

Monero ships first because RandomX is deliberately CPU-friendly, so a home CPU is a real
fraction of the network and pools exist with thresholds it reaches in days. Bitcoin is
orders of magnitude harder at the same hardware, and the GUI's earnings estimate shows
that plainly rather than hiding it.

See `MINING.md`.

## 2. Hardware and racks

Four hardware types — transistor board, CPU, GPU, ASIC — installed into racks. What each
is worth depends on the coin being mined, because each algorithm favours different
silicon in reality.

Hardware is sold under parody brands across four generations, each generation marking a
real material transition in chip manufacturing.

See `HARDWARE.md`.

### 2.1 Racks

A **Rack** is placed empty. Racks connect to adjacent racks, vertically and horizontally,
forming one larger rack. There is no size limit.

**Each rack has 4 rows.** A row holds hardware or a PSU, and types can be mixed freely
within a rack.

| Installed | Rows used | Per rack |
|---|---|---|
| Transistor board | 4 | 1 |
| CPU | 1 | 4 |
| GPU | 2 | 2 |
| ASIC | 4 | 1 |
| PSU | 1 | 4 |

**Installing** — right-click the rack while holding the hardware, or install it through
the rack's GUI. The rack renders its rows filling as hardware goes in.

PSUs supply wattage to the rack they are part of. A rack needs enough PSUs to cover the
total draw of everything installed in it.

### 2.2 Mining Controller

A separate block, not part of a rack. Configures and runs the mining, and serves any
number of racks. Racks connect by direct adjacency or by cable run from the controller.

It connects to the pool on its own — there are no networking blocks.

### 2.3 Cables

Link racks to the Mining Controller. They carry no power — power comes from a tech mod.

## 3. Materials and ores

Vanilla materials are reused where they exist. The mod adds only **8 ores and one beach
surface block**, deriving everything else from vanilla materials or as refining
by-products, so worldgen stays uncluttered.

Rarity follows real crustal abundance, and vein size follows real deposit form. The two
together decide supply, so no ore's rarity has to be faked.

See `ORES.md`.

## 4. Processing

The mod adds its own machines. Integration with other tech mods may come later. Anything
that needs power in reality needs power in game.

**Ore refining** uses the vanilla furnace where the metal is carbon-reduced in reality,
and an **Electrolysis Cell** where it is not.

**Chip fabrication** is four steps — crystal growth, wafer slicing, lithography,
packaging — of which only lithography is generation-sensitive. Photomasks are specific to
one chip design and are the mod's only consumable.

See `FABRICATION.md`.

## 5. Heat and cooling

Heat is driven by **actual watt consumption and hardware grade**, not by tier. A cooling
block removes a number of watts. Progression is not always CPU→ASIC, so cooling
requirements follow the wattage a rig actually draws and how efficient its hardware is.

Figures are adjusted for Minecraft scale — one 1 m³ block stands in for what would really
be several units of hardware.

### 5.1 Cooling blocks

**Rack fan** — renders on the rear face of the rack block. Draws power. Installed through
the rack's GUI.

**Liquid-cooled rack** — a rack variant filled by right-clicking with a bucket. Otherwise
behaves as a normal rack. Draws power.

**Coolant** — water works and is cheap. **Liquid coolant**, a crafted dielectric fluid,
cools considerably better. Both the liquid-cooled rack and the immersion tank accept
either.

**Room AC unit** — cools all racks within a radius. Draws power.

**Immersion tank** — a multiblock. Hardware is installed in it as in a rack, and it is
filled with liquid. Draws power, but far less than air cooling.

### 5.2 Overheating

Hardware throttles first, losing hashrate, and warns in the GUI. Escalation is slow,
taking minutes. If heat keeps rising, the hardware **shuts down** and must be restarted
manually from the controller, so the player has to notice and fix the cause.

Overheating never causes fire. Fire comes only from fake-rated PSUs failing under load.

### 5.3 Environment

Where a rack is built shifts how much cooling it needs. The effect is **minor — a
modifier of roughly ±20%**, so siting matters but never replaces cooling blocks.

| Factor | Effect |
|---|---|
| Biome temperature | Uses Minecraft's own biome temperature value, so it works for every biome including modded ones. Snowy and cold biomes help; deserts, savannas and the Nether hurt |
| Depth | Below a threshold Y, ambient is cooler — real datacentres go underground for the same reason |
| Adjacent water or ice | Blocks touching the rack help cool it |

Exact magnitudes and the depth threshold are set with balancing.

## 6. Power

Power is measured in real watts. Each hardware variant draws the wattage its real
counterpart draws — per coin, since a RandomX ASIC and a SHA-256 ASIC are not comparable
machines. Specific figures are set with balancing.

**Power generation and storage are not part of this mod.** Mekanism and other tech mods
provide them. The mod accepts energy through NeoForge's energy capability.

**PSUs are the energy sinks.** A tech mod's cables run to the PSU installed in a rack, as
a real PSU is what plugs into the wall. The mod's own cables carry data only.

**Creative Power Cell** — supplies unlimited power. No fuel, no input, no mechanic behind
it. It exists so the mod runs without a tech mod installed.

## 7. Mining

Real mining is opt-in per world, hashes client-side on each player's own machine, and
never runs after the game closes. The in-game rig governs how hard the real miner runs,
based on the player's actual measured hashrate rather than an arbitrary virtual one.

See `MINING.md`.

## 8. Multiblock rules

**Assembly and running are separate.** A rack can be fully assembled and not mining.

### 8.1 Assembly

Assembly is purely structural: **any racks touching each other form one structure**,
vertically or horizontally, with no size limit. Nothing else is required to be a valid
multiblock — no PSU, no controller, no power.

Breaking a rack out of the middle **splits the structure**. What remains becomes separate
structures, each of which must cover its own draw.

### 8.2 Running

A rack mines only when all of these hold:

- **PSU wattage** in the rack covers the total draw of everything installed
- **Connected to a Mining Controller**, by adjacency or cable
- **Power supplied** — energy flowing from a tech mod or the Creative Power Cell

**Cooling is not required to start.** A rack with no cooling runs, heats up, and shuts
itself down. It stays a valid multiblock throughout — it is assembled, just not mining.

**Losing a requirement simply stops mining**, and regaining it resumes. Cutting power or
removing a PSU needs no special handling. The single exception is an overheat shutdown,
which is deliberate and needs a manual restart.

## 9. GUI screens

Scope decides placement: the **rack** is where you interact with hardware and see that
structure's figures, the **controller** shows the whole operation across every rack.

| | Rack | Controller |
|---|---|---|
| Hardware rows | Install and remove, this rack | — |
| Fans | Install and remove, this rack | — |
| Temperature | This rack | Hottest rack, and how many are overheating |
| Watts | This rack against its PSUs | Total across all racks |
| Hashrate | This rack's contribution | Total effective hashrate |
| Warnings | This rack's problems | Which racks have problems |
| Address, pool, coin | — | Config |
| Shares, job, uptime | — | Stats |

### 9.1 Mining Controller

One window, tabbed.

**Config** — payout address, pool URL, worker name, coin selection, Start/Stop.

**Stats** — current hashrate, accepted and rejected shares, current job, uptime.

**Rigs** — total watts, total heat, and which racks need attention.

**Warnings** — overheating, PSU overload, pool disconnected, hardware throttling. Any
hardware that shut down from overheating is restarted from here.

**Address entry** is only here. The address is validated against the active coin's format,
and anything resembling a private key is refused — a pool never needs one.

### 9.2 Rack

Opened by right-clicking a rack. Shows the rack you clicked, for interacting with it,
alongside the totals for the whole structure it belongs to.

**This rack** — its 4 rows, what is installed, install and remove; fans on this rack.

**This structure** — hashrate, watts against total PSU capacity, temperature, and warnings
(PSU overloaded, overheating, throttling).

## 10. In-game documentation

Item tooltips carry short explanations. A **craftable guide book**, built with
**Patchouli**, covers:

- Getting started — first rack, PSU, controller and transistor board
- The fab chain — silicon to chips, and what each machine does
- Generations and materials — why each generation needs what it does, and the real
  history behind it
- Heat, power and pools — cooling, wattage, and setting up a payout address
- Advanced — running a local P2Pool node for the lowest payout threshold, and why
  antivirus flags the native mining library

Patchouli is a hard dependency.

## 11. Progression curve

The first working miner sits between early and mid game. A player who has established a
base and been on a mining trip should be able to put up a basic rack, PSU, controller and
a transistor board. A player who just joined does not yet have the materials for computer
hardware.

**What gates the first miner**

- Power from a tech mod
- Finding the ores the hardware needs

**Deep ores are not required to start.** Generation 1 hardware predates every material
transition — it uses aluminium interconnects and needs no tantalum, hafnium or cobalt.
Each later generation adds materials found deeper, so deep ores unlock **better**
hardware, not the **first** hardware.

**The fab chain is not required to start either.** The transistor board is the pre-fab
route to a working miner — bulky, slow and weak, but real. CPUs, GPUs and ASICs come after
the fab chain is built.
