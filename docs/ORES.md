# CryptoCraft — Ores and materials

Worldgen, refining, and the material economy. See `CRYPTOCRAFT.md` §3 for the summary.

## 1. Principle

**Rarity follows real crustal abundance; vein size follows real deposit form.** A metal
that is abundant but forms thin deposits ends up scarce in practice, and a rare one that
forms concentrated bodies ends up workable — as in reality. The two together decide
supply, so no ore's rarity has to be faked.

**The mod adds only 8 ores and one beach surface block.** Everything else comes from
vanilla materials or falls out as a refining by-product, so a modpack that installs
CryptoCraft does not find its worldgen littered with single-purpose ores.

Ores are named after the real mineral, with the metal they yield in the tooltip.

## 2. Generation

Vanilla reference: coal ~20 veins/chunk size 17, iron ~10 size 9, copper ~6 size 10,
gold ~2 size 9, diamond ~1 size 8.

| Ore | Yields | Y range | Biome | Veins/chunk | Vein size | Drops | Deposit form |
|---|---|---|---|---|---|---|---|
| Bauxite | Aluminium | Surface layer | Any | 20 | 16 | 1–2 | Vast horizontal blankets |
| Mineral Sand | Titanium | Surface, beaches | Where sand meets water | 10 | 12 | block itself | Dark heavy-mineral beach sands |
| Zircon | Zirconium, hafnium | -60–0 | Any | 8 | 8 | 1 | Accessory mineral, widespread |
| Chromite | Chromium, iron | -30–20 | Any | 4 | 14 | 1 | Massive layered intrusions |
| Nickel Ore | Nickel | 0–50 | Any | 5 | 12 | 1 | Large sulfide bodies |
| Borax | Boron | Surface layer | Desert, badlands, savanna, windswept peaks | 10 | 14 | 2–3 | Dried-lakebed evaporite beds |
| Bastnäsite | Neodymium | -60–0 | Any | 2 | 12 | 3–4 | Few deposits, highly concentrated |
| Wolframite | Tungsten | -60–0 | Any | 4 | 5 | 1 | Narrow quartz veins |
| Cassiterite | Tin, tantalum | 0–50 | Any | 4 | 6 | 2–3 | Narrow veins and placers |

**Borax and Bauxite generate as surface layers, not veins**, because both are real
surface deposits — borax an evaporite left by dried salt lakes, bauxite tropical
weathering. Borax is restricted to arid biomes as real borate beds are; bauxite is not,
so a material needed in almost every recipe is never gated behind biome luck.

**Mineral Sand** is a dark heavy-mineral sand found where sand meets water, exactly where
real ilmenite concentrates. Being surface-only, it is never encountered while mining.

## 3. Raw items

Metal ores drop a raw item when mined, as vanilla does, which is then smelted or
electrolysed. There are no raw or metal storage blocks, and no crushing step.

| Raw item | From |
|---|---|
| Raw Bauxite | Bauxite |
| Raw Cassiterite | Cassiterite |
| Raw Nickel | Nickel Ore |
| Raw Chromite | Chromite |
| Raw Zircon | Zircon |
| Raw Bastnäsite | Bastnäsite |
| Raw Wolframite | Wolframite |

Borax and Mineral Sand drop the block itself, as neither is a smelted metal.

## 4. Refining

Metals that are carbon-reduced in reality smelt in the **vanilla furnace**. Everything
else needs the **Electrolysis Cell**, because it cannot be carbon-smelted or is refined
electrolytically in reality.

**Ore richness varies, as it does in reality.** A rich ore like cassiterite or wolframite
refines one-for-one; a dilute one like bastnasite or borax takes several raw units to
yield one metal.

**Silicon** is produced by carbothermic reduction, the real industrial process. Quartz
gives a better yield than sand, as the purer input needs less refining.

**Phosphorus** comes from bone ash - the real historic source - and from organic matter,
which genuinely carries phosphates.

Refining recipes and ratios are in `CATALOGUE.md`.

## 5. By-products

Refining yields by-products as it does in reality, and only where the by-product is a
genuine constituent rather than a trace.

| Material | From | Rate | Real basis |
|---|---|---|---|
| Cobalt | Nickel | 40% | Recovered alongside nickel |
| Silver | Vanilla copper | 20% | Much of the world's silver is a copper by-product |
| Tantalum | Tin smelting | 20% | Recovered from tin slag |
| Ruthenium | Nickel | 15% | Recovered alongside nickel |
| Hafnium | Zirconium | 10% | Always present in zirconium; there is no hafnium mine on Earth |
| Molybdenum | Vanilla copper | 5% | Recovered from copper deposits |
| Iron | Chromite | 50% | Chromite is an iron-chromium oxide |

## 6. Supply

Approximate yield per chunk, after drops and refining:

| Material | Per chunk | Demand |
|---|---|---|
| Aluminium | ~240 | Nearly every recipe |
| Titanium | ~120 | One slot, generations 3–4 |
| Boron | ~70 | Dopant, every run — arid biomes only |
| Zirconium | ~64 | Equipment, and the hafnium source |
| Nickel | ~60 | Connectors, and the cobalt/ruthenium source |
| Tin | ~60 | Solder, every recipe |
| Chromium | ~56 | Photomask blanks, ongoing |
| Cobalt | ~24 | Vias, generation 4 |
| Tungsten | ~20 | Vias, every die |
| Neodymium | ~17 | Fan magnets |
| Tantalum | ~12 | Barrier layer, generations 2+ |
| Ruthenium | ~9 | Generation 4 |
| Hafnium | ~6.4 | Gate dielectric, generations 3+ |

Phosphorus, silicon, silver and molybdenum scale with how much the player gathers of
their source rather than with chunk generation.

**Hafnium, ruthenium and tantalum are the tightest**, and all three gate generations 2–4.
Late hardware is meant to be demanding.

## 7. Where the mod departs from reality

Real abundance ordering is aluminium ≫ titanium > phosphorus > zirconium > chromium >
nickel > boron > neodymium > tungsten > tin > tantalum > silver.

- **Tin, tungsten and tantalum** are far rarer in reality than here, because all three
  are consumed constantly.
- **Titanium** is more abundant in reality but fills only one recipe slot, so mineral
  sand is a surface deposit rather than a widespread ore.
- **Neodymium** is rarer in reality but every fan needs it, so bastnäsite forms large
  concentrated bodies.
- **Nickel and silver** sit close to their true scarcity.
