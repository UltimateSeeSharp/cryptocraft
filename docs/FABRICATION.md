# CryptoCraft — Chip fabrication

The semiconductor chain, photomasks, and dopants. See `CRYPTOCRAFT.md` §4 for the
summary.

## 1. The chain

Four steps, one machine each. Depth sits between medium and deep — this is not a computer
mod, so the chain should not be exhaustive, but not trivial either.

| Machine | Input | Output | Generation-sensitive |
|---|---|---|---|
| Crystal Furnace | Silicon | 1 ingot | No |
| Wafer Saw | 1 ingot | 8 wafers | No |
| Lithography Machine | 1 wafer + photomask + both dopants + generation materials | 4 dies | **Yes** |
| Packaging Machine | 1 die | 1 chip | No |

So one ingot yields 8 wafers yields 32 dies yields 32 chips.

**Batches follow the real process.** Slicing an ingot yields many wafers and etching a
wafer yields many dies, as they do in reality; growing a crystal and packaging a die are
one-for-one.

**Processing takes time only where the real process does** — crystal growth, wafer
cutting and lithography. Simple assembly has no artificial timer.

**Generation materials are consumed per lithography run**, not per die, as a real wafer
is processed as a unit.

## 2. Generations

Hardware spans four generations, each marking a real material transition in chip
manufacturing. Every generation adds materials without removing the need for earlier
ones, so progression is strictly harder as it advances.

| Gen | Era | Transition | New material |
|---|---|---|---|
| 1 | pre-1997 | Aluminium interconnects | — |
| 2 | 1997–2007 | Copper interconnects replace aluminium | **Tantalum** — barrier layer, mandatory because copper otherwise poisons silicon |
| 3 | 2007–2016 | High-k metal gate replaces the grown silicon-dioxide gate | **Hafnium**, **titanium** |
| 4 | 2016+ | Cobalt vias replace tungsten at the tightest pitches | **Cobalt**, **ruthenium** |

**What a die consumes, by generation:**

| | Gen 1 | Gen 2 | Gen 3 | Gen 4 |
|---|---|---|---|---|
| Wafer | ✓ | ✓ | ✓ | ✓ |
| N-type and p-type dopant | ✓ | ✓ | ✓ | ✓ |
| Interconnect | Aluminium | Copper | Copper | Copper |
| Barrier layer | — | Tantalum | Tantalum | Tantalum |
| Vias | Tungsten | Tungsten | Tungsten | Tungsten + cobalt |
| Gate dielectric | *grown* | *grown* | Hafnium | Hafnium |
| Metal gate | — | — | Titanium | Titanium |
| Leading-edge interconnect | — | — | — | Ruthenium |

**Lithography is the only generation-sensitive step**, as in reality — doping,
interconnects, barrier layers and gate dielectric are all deposited there. Crystal
pulling, wafer slicing and packaging are the same process for every generation.

Generations 1–2 grow their gate dielectric from the wafer itself by oxidising silicon, so
it costs no input. Generation 3 onward supplies hafnium instead.

Tungsten stays in generation 4 alongside cobalt: cobalt replaced tungsten only at the
tightest pitches, and tungsten still serves the larger vias.

**EUV Lithography Machine** — required for generation 4. EUV entered volume production in
2019; earlier generations use the standard Lithography Machine.

## 3. Photomasks

A photomask carries the circuit pattern printed onto the wafer, so **a mask is specific to
one chip design** — a Core i5 mask makes Core i5 circuits, not merely "a CPU". The die
that comes out is already that exact variant, and stays so through packaging.

Real mask production uses a set of masks per design, one per layer. The mod abstracts that
to a single item representing the whole set.

**Blanks are built from real materials:**

| Mask type | Blank | Why |
|---|---|---|
| Conventional | Quartz + chromium | A transparent substrate with a light-blocking patterned layer |
| EUV | Molybdenum + silicon | EUV light at 13.5nm cannot pass through a transparent mask, so EUV masks reflect instead, using a Mo/Si multilayer |

**Patterning happens at a table, not the crafting grid.** A blank goes in, the player
selects a chip design, and the specific mask comes out. This avoids dozens of
near-identical crafting recipes. **Every design is always available** — the gate is
whether the player can supply that chip's materials, not a separate unlock.

Conventional and EUV masks use **separate tables**, as real EUV mask production is a
different process.

**Masks wear out after a set number of uses** (TBD), making them an ongoing cost rather
than a one-off unlock. They are the only consumable in the mod.

## 4. Dopants

Silicon is useless until doped. Two dopant items exist, as real silicon needs both:

| Item | From | Role |
|---|---|---|
| N-type dopant | Phosphorus | Adds free electrons |
| P-type dopant | Boron | Adds electron holes |

Both are made at the Semiconductor Bench, and both are consumed by every lithography run
and by transistor production.

## 5. The pre-fab route

**Semiconductor Bench** — makes transistors from silicon and dopants, with no wafer,
photomask or lithography.

**Transistor board** — built from transistors. Before integrated circuits, computers were
built from individual transistors on boards: bulky, slow, hot, and real. It installs into
a rack like any other hardware and is weak by comparison — the fab chain exists because
hand-soldering transistors does not scale.

**Transistors also appear in world loot**, salvaged rather than made. Which loot tables is
TBD.

## 6. Memory

**Memory chips** are one of the chip designs, made through the same lithography and
packaging as any other. They are **cheaper in materials**, as real DRAM is genuinely
simpler to fabricate than logic — a repeating array of one transistor and one capacitor
per cell, fewer metal layers, older process nodes.

Only GPUs consume them.

## 7. Components

Intermediate items used across hardware recipes, all crafted at a crafting table:
solder, silver solder, copper wire, circuit board, connectors, electric motor, fan,
heatsink and casing.

**Solder is generation-split**, mirroring the real move to lead-free alloys: basic tin
solder for generations 1–2, silver-bearing solder for 3–4.

Recipes are in `CATALOGUE.md`.
