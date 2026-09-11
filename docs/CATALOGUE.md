# CryptoCraft — Catalogue

Every block, item and fluid in the mod. *TBD* means deferred to balancing.

The last column points at where the behaviour is defined: **CC** = `CRYPTOCRAFT.md`,
**ORES** = `ORES.md`, **FAB** = `FABRICATION.md`, **HW** = `HARDWARE.md`.

## Ores

Generation figures are in `ORES.md` §2; the material economy in §6.

| Name | Yields | Y range | Biome |
|---|---|---|---|
| Bauxite | Aluminium | Surface layer | Any |
| Mineral Sand | Titanium | Surface, beaches | Where sand meets water |
| Zircon | Zirconium, hafnium | -60-0 | Any |
| Chromite | Chromium, iron | -30-20 | Any |
| Nickel Ore | Nickel | 0-50 | Any |
| Borax | Boron | Surface layer | Desert, badlands, savanna, windswept peaks |
| Bastnasite | Neodymium | -60-0 | Any |
| Wolframite | Tungsten | -60-0 | Any |
| Cassiterite | Tin, tantalum | 0-50 | Any |

## Racks

Hardware installs into these. All crafted at a crafting table.

| Name | Description | Recipe | § |
|---|---|---|---|
| Rack | Placed empty, connects to adjacent racks to form one structure. 4 rows | Iron + casing | CC 2.1 |
| Liquid-cooled rack | Rack variant filled with water or liquid coolant. 4 rows | TBD | CC 5.1 |
| Immersion tank | Multiblock; hardware installs as in a rack, submerged in liquid | TBD | CC 5.1 |

## Cooling

| Name | Description | Made in | Recipe | § |
|---|---|---|---|---|
| Rack fan | Installed in a rack row, renders on its rear face. Watts removed TBD | Crafting table | Fan + casing | CC 5.1 |
| Room AC unit | Placed block, cools all racks in radius | Crafting table | TBD | CC 5.1 |
| Liquid coolant | Dielectric fluid, cools better than water. Multiplier TBD | TBD | TBD | CC 5.1 |

Water also works in liquid-cooled racks and immersion tanks.

## Blocks

All crafted at a crafting table, except the Creative Power Cell which is creative-only.

| Name | Description | Recipe | § |
|---|---|---|---|
| Mining Controller | Holds address, pool and coin; runs the mining. Serves unlimited racks | Circuit board + chip + glass + casing | CC 2.2 |
| Cable | Links racks to the controller; carries no power | Copper wire | CC 2.3 |
| Creative Power Cell | Unlimited power, so the mod runs without a tech mod | *creative only, no recipe* | CC 6 |
| Electrolysis Cell | Refines ores that cannot be carbon-smelted | TBD | ORES 4 |
| Crystal Furnace | Silicon → ingot | TBD | FAB 1 |
| Wafer Saw | Ingot → wafers | TBD | FAB 1 |
| Lithography Machine | Wafer → die, generations 1–3 | TBD | FAB 1 |
| EUV Lithography Machine | As above, required for generation 4 | TBD | FAB 1 |
| Packaging Machine | Die → chip | TBD | FAB 1 |
| Semiconductor Bench | Dopants, and silicon → transistors | TBD | FAB 5 |
| Photomask Table | Patterns a blank photomask with a chosen chip design | TBD | FAB 3 |
| EUV Photomask Table | As above, for EUV masks | TBD | FAB 3 |

## Items

### Raw items

Dropped when the ore is mined. There are no raw or metal storage blocks.

| Name | From |
|---|---|
| Raw Bauxite | Bauxite |
| Raw Cassiterite | Cassiterite |
| Raw Nickel | Nickel Ore |
| Raw Chromite | Chromite |
| Raw Zircon | Zircon |
| Raw Bastnäsite | Bastnäsite |
| Raw Wolframite | Wolframite |

Borax and Mineral Sand drop the block itself rather than a raw item — neither is a
smelted metal.

### Materials

| Name | Used for | Made in | Recipe | § |
|---|---|---|---|---|
| Aluminium | Interconnects (gen 1), heatsinks, casing | Electrolysis Cell | 2 raw bauxite → 1 aluminium | ORES 4 |
| Boron | p-type dopant source | Electrolysis Cell | 5 borax → 1 boron | ORES 4 |
| Phosphorus | n-type dopant source | Furnace | bones, bone blocks, rotten flesh or kelp → phosphorus | ORES 4 |
| Tin | Solder | Furnace | 1 raw cassiterite → 1 tin, 20% tantalum | ORES 4 |
| Nickel | Contact plating | Electrolysis Cell | 1 raw nickel → 1 nickel, 40% cobalt, 15% ruthenium | ORES 4 |
| Silver | Silver solder | Electrolysis Cell | by-product of vanilla copper, 20% | ORES 5 |
| Chromium | Photomask light-blocking layer | Electrolysis Cell | 1 raw chromite → 1 chromium + 50% iron | ORES 4 |
| Titanium | Metal gate, gen 3+ | Electrolysis Cell | 2 mineral sand → 1 titanium | ORES 4 |
| Zirconium | Equipment | Electrolysis Cell | 1 raw zircon → 1 zirconium + 10% hafnium | ORES 4 |
| Hafnium | Gate dielectric, gen 3+ | Electrolysis Cell | by-product of zircon, 10% | ORES 5 |
| Neodymium | Fan magnets | Electrolysis Cell | 5 raw bastnäsite → 1 neodymium | ORES 4 |
| Tungsten | Vias and contacts | Furnace | 1 raw wolframite → 1 tungsten | ORES 4 |
| Tantalum | Copper barrier layer, gen 2+ | Furnace | by-product of tin smelting, 20% | ORES 5 |
| Molybdenum | EUV mask multilayer | Electrolysis Cell | by-product of vanilla copper, 5% | ORES 5 |
| Cobalt | Vias, gen 4 | Electrolysis Cell | by-product of nickel, 40% | ORES 5 |
| Ruthenium | Leading-edge interconnects, gen 4 | Electrolysis Cell | by-product of nickel, 15% | ORES 5 |

Copper, gold, iron and Nether Quartz come from vanilla.

### Fab chain

| Name | Description | Made in | Recipe | § |
|---|---|---|---|---|
| Silicon | Feedstock for everything semiconductor | Furnace | sand or quartz + carbon → silicon; quartz yields more | ORES 4 |
| N-type dopant | Adds free electrons to silicon | Semiconductor Bench | phosphorus → n-type dopant | FAB 4 |
| P-type dopant | Adds electron holes to silicon | Semiconductor Bench | boron → p-type dopant | FAB 4 |
| Silicon ingot | Grown crystal | Crystal Furnace | silicon → 1 ingot | FAB 1 |
| Wafer | Sliced ingot | Wafer Saw | 1 ingot → 8 wafers | FAB 1 |
| Die | Etched wafer, specific to one chip design | Lithography Machine, EUV for gen 4 | 1 wafer + photomask + both dopants + generation materials → 4 dies. Generation materials are consumed per run, not per die | FAB 1 |
| Chip | Packaged die, specific to one chip design. Recipes naming "CPU chip", "GPU chip", "ASIC chip" or "memory chip" all mean this item carrying that design | Packaging Machine | 1 die → 1 chip | FAB 1 |
| Memory chip | DRAM for GPUs; one of the chip designs, cheaper in materials as real DRAM is simpler than logic | Lithography + Packaging | as any chip | FAB 1 |
| Transistor | Pre-fab semiconductor; also found in world loot | Semiconductor Bench | silicon + both dopants → transistors | FAB 5 |

### Photomasks

| Name | Description | Made in | Recipe | § |
|---|---|---|---|---|
| Blank photomask | Conventional mask substrate, unpatterned | Crafting table | Quartz + chromium | FAB 3 |
| Blank EUV photomask | Reflective Mo/Si multilayer, unpatterned | Crafting table | Molybdenum + silicon | FAB 3 |
| Photomask | Patterned for one specific chip design | Photomask Table | Blank + design selection | FAB 3 |
| EUV photomask | As above, for generation 4 | EUV Photomask Table | EUV blank + design selection | FAB 3 |

Masks wear out after a set number of uses — count TBD.

### Components

All crafted at a crafting table.

| Name | Used for | Recipe | Output | § |
|---|---|---|---|---|
| Solder | Joining, generations 1–2 | 2 tin | 4 | FAB 7 |
| Silver solder | Joining, generations 3–4 | 1 solder + 1 silver | 2 | FAB 7 |
| Copper wire | Wiring | 3 copper, vertical | 6 | FAB 7 |
| Circuit board | Boards that chips mount to | 3 copper + 3 terracotta + 2 solder | 3 | FAB 7 |
| Connectors | Pins and contacts | 1 gold + 1 nickel + 1 copper | 2 | FAB 7 |
| Electric motor | Fans, and anything with moving parts | 4 copper wire + 1 neodymium | 1 | FAB 7 |
| Fan | Active cooling | 4 aluminium + 1 electric motor | 1 | FAB 7 |
| Heatsink | Passive cooling | 5 aluminium | 1 | FAB 7 |
| Casing | Housing for PSUs, ASICs and racks | 8 aluminium | 2 | FAB 7 |

**Grids**

```
Solder                Silver solder         Copper wire
[Tin][Tin]            [Solder][Silver]      [Cu]
                                            [Cu]
                                            [Cu]

Circuit board         Connectors            Electric motor
[Cu ][Cu ][Cu ]       [Gold  ]                  [Wire]
[Tc ][Tc ][Tc ]       [Nickel]              [Wire][Neod][Wire]
[Sol][   ][Sol]       [Copper]                  [Wire]

Fan                   Heatsink              Casing
[Alu][   ][Alu]       [Alu][Alu][Alu]       [Alu][Alu][Alu]
[   ][Mot][   ]       [   ][Alu][   ]       [Alu][   ][Alu]
[Alu][   ][Alu]       [   ][Alu][   ]       [Alu][Alu][Alu]
```

Tc = terracotta, Sol = solder, Neod = neodymium, Mot = electric motor.

### Mining hardware

Installed into rack rows, not placed. All crafted at a crafting table. Hashrate and
wattage TBD throughout.

| Name | Type | Gen | Rows | Recipe | § |
|---|---|---|---|---|---|
| Transistor board | Pre-fab | — | 4 | Transistors + circuit board + copper wire + solder | FAB 5 |
| Untel Pentium | CPU | 1 | 1 | CPU chip + circuit board + connectors + heatsink + solder | HW 4 |
| AND K6 | CPU | 1 | 1 | CPU chip + circuit board + connectors + heatsink + solder | HW 4 |
| Untel Core 2 Duo | CPU | 2 | 1 | CPU chip + circuit board + connectors + heatsink + solder | HW 4 |
| AND Phenom | CPU | 2 | 1 | CPU chip + circuit board + connectors + heatsink + solder | HW 4 |
| Untel Xenon | CPU, server | 2 | 1 | CPU chip + circuit board + connectors + heatsink + solder | HW 4 |
| Untel Core i5 | CPU | 3 | 1 | CPU chip + circuit board + connectors + heatsink + silver solder | HW 4 |
| AND FX Bulldozer | CPU | 3 | 1 | CPU chip + circuit board + connectors + heatsink + silver solder | HW 4 |
| AND Opteryn | CPU, server | 3 | 1 | CPU chip + circuit board + connectors + heatsink + silver solder | HW 4 |
| Untel Core i9 | CPU | 4 | 1 | CPU chip + circuit board + connectors + heatsink + silver solder | HW 4 |
| AND Ryzem 9 | CPU | 4 | 1 | CPU chip + circuit board + connectors + heatsink + silver solder | HW 4 |
| AND Epyk | CPU, server | 4 | 1 | CPU chip + circuit board + connectors + heatsink + silver solder | HW 4 |
| 3DFX Vudu | GPU | 1 | 2 | GPU chip + memory chips + circuit board + connectors + heatsink + fan + copper wire + solder | HW 4 |
| Envidia GForce | GPU | 2 | 2 | GPU chip + memory chips + circuit board + connectors + heatsink + fan + copper wire + solder | HW 4 |
| AND Radion | GPU | 2 | 2 | GPU chip + memory chips + circuit board + connectors + heatsink + fan + copper wire + solder | HW 4 |
| Envidia GForce GTX | GPU | 3 | 2 | GPU chip + memory chips + circuit board + connectors + heatsink + fan + copper wire + silver solder | HW 4 |
| AND Radion HD | GPU | 3 | 2 | GPU chip + memory chips + circuit board + connectors + heatsink + fan + copper wire + silver solder | HW 4 |
| Envidia GForce RTX | GPU | 4 | 2 | GPU chip + memory chips + circuit board + connectors + heatsink + fan + copper wire + silver solder | HW 4 |
| AND Radion RX | GPU | 4 | 2 | GPU chip + memory chips + circuit board + connectors + heatsink + fan + copper wire + silver solder | HW 4 |
| Bitmine Andminer S9 | ASIC, Bitcoin | 3 | 4 | ASIC chips + circuit board + connectors + heatsink + fan + casing + silver solder | HW 4 |
| Canaam Avalom A6 | ASIC, Bitcoin | 3 | 4 | ASIC chips + circuit board + connectors + heatsink + fan + casing + silver solder | HW 4 |
| Bitmine Andminer S21 | ASIC, Bitcoin | 4 | 4 | ASIC chips + circuit board + connectors + heatsink + fan + casing + silver solder | HW 4 |
| MacroBT Whatzminer M60 | ASIC, Bitcoin | 4 | 4 | ASIC chips + circuit board + connectors + heatsink + fan + casing + silver solder | HW 4 |
| Bitmine Andminer X5 | ASIC, Monero | 4 | 4 | ASIC chips + circuit board + connectors + heatsink + fan + casing + silver solder | HW 4 |

### Power supplies

Installed into rack rows. All crafted at a crafting table. Efficiency affects waste heat.

| Name | Grade | Capacity | Rows | Recipe | § |
|---|---|---|---|---|---|
| Crosshair XC | Bronze | 500W | 1 | Casing + copper wire + circuit board + solder | HW 5 |
| Crosshair RN | Gold | 850W | 1 | Casing + copper wire + circuit board + solder | HW 5 |
| Crosshair XA | Titanium | 1600W | 1 | Casing + copper wire + circuit board + silver solder | HW 5 |
| Seezonic Z12 | Bronze | 550W | 1 | Casing + copper wire + circuit board + solder | HW 5 |
| Seezonic Lokus | Gold | 750W | 1 | Casing + copper wire + circuit board + solder | HW 5 |
| Seezonic PRYME | Titanium | 1300W | 1 | Casing + copper wire + circuit board + silver solder | HW 5 |
| EVGV RB | Bronze | 600W | 1 | Casing + copper wire + circuit board + solder | HW 5 |
| EVGV SupaNova 850 | Gold | 850W | 1 | Casing + copper wire + circuit board + solder | HW 5 |
| EVGV SupaNova 1600 | Titanium | 1600W | 1 | Casing + copper wire + circuit board + silver solder | HW 5 |
| be kwiet! System Powder | Bronze | 500W | 1 | Casing + copper wire + circuit board + solder | HW 5 |
| be kwiet! Poor Power | Gold | 750W | 1 | Casing + copper wire + circuit board + solder | HW 5 |
| be kwiet! Dark Powder Pro | Titanium | 1500W | 1 | Casing + copper wire + circuit board + silver solder | HW 5 |
| Logisis | Fake-rated | Claimed TBD, real ~50% | 1 | Casing + copper wire + minimal materials | HW 5 |

Logisis claims a high wattage and is cheap. Its tooltip flags the rating as unverified,
the rack GUI shows real capacity once installed, and sustained overload destroys it and
starts a fire.

### Equipment

All crafted at a crafting table.

| Name | Description | Recipe | Stats | § |
|---|---|---|---|---|
| Zirconium armour | Prevents heat damage; zirconium's real use is nuclear fuel cladding | TBD — zirconium | Armour values TBD | — |
| Zirconium tools | Slightly better than iron | TBD — zirconium | TBD | — |

### Other

| Name | Description | Recipe | § |
|---|---|---|---|
| Guide book | Explains the fab chain, generations and progression. Patchouli | TBD | CC 10 |

