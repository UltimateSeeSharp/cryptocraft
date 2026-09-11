# CryptoCraft — Mining hardware

Hardware types, brands and generations. See `CRYPTOCRAFT.md` §2 for the summary, and
`CATALOGUE.md` for every variant with its recipe.

## 1. Types

Four hardware types: **transistor board**, **CPU**, **GPU**, **ASIC**. All install into
racks the same way. Rigs are grown by adding hardware to a rack, farms by building more
racks.

The **transistor board** is the pre-fab early option (`FABRICATION.md` §5). The other
three require fabricated chips.

**Hardware never wears out.** Once built it runs indefinitely. Photomasks are the only
consumable.

## 2. Per coin

Each coin's algorithm favours different silicon, so **hardware ladder, wattage and
efficiency are all per coin**:

| Coin | Algorithm | Hardware ladder |
|---|---|---|
| Monero | RandomX | CPU is the best hardware. GPUs are poor at it. RandomX ASICs barely exist. |
| Bitcoin | SHA-256 | CPU → GPU → ASIC, each step dramatically better. |

All hardware stays usable for every coin — what changes is how much it is worth.
**Mismatched hardware works badly rather than refusing**: a SHA-256 ASIC set to mine
Monero contributes almost nothing while drawing its full wattage, as it would in reality.

## 3. Brands

Hardware is sold under parody brands.

| Type | Brand | Real counterpart |
|---|---|---|
| CPU | Untel | Intel |
| CPU, GPU | AND | AMD |
| GPU | Envidia | Nvidia |
| GPU | 3DFX | 3dfx |
| ASIC | Bitmine Andminer | Bitmain Antminer |
| ASIC | MacroBT Whatzminer | MicroBT Whatsminer |
| ASIC | Canaam Avalom | Canaan Avalon |
| PSU | Crosshair | Corsair |
| PSU | Seezonic | Seasonic |
| PSU | EVGV | EVGA |
| PSU | be kwiet! | be quiet! |
| PSU | Logisis | Logisys — the fake-rated one |

## 4. Coverage by generation

Generations are defined in `FABRICATION.md` §2. Coverage follows real history — not every
hardware type exists in every generation.

**CPUs** — three parallel lines: a mainstream Untel, a mainstream AND, and a server
option. There is no server CPU in generation 1, as Xeon launched in 1998, after the
aluminium era.

**Server CPUs are the Monero endgame.** RandomX rewards large L3 cache over clock speed,
so a server CPU with huge cache beats even an ASIC — as it does in reality. Where a
generation has one, it is the strongest option in that generation and the most expensive
to build.

**GPUs** — one per brand per generation, each a distinct real product line. Generation 1's
GPU is 3DFX rather than Envidia or AND, as neither had a consumer 3D card before 1997.
GPUs also consume memory chips, which CPUs do not.

**ASICs** — start in generation 3, as the first commercial SHA-256 ASICs shipped in 2013.
Two Bitcoin ASICs exist in each of generations 3 and 4. RandomX was designed to resist
ASICs, so only one Monero ASIC was ever built; it sits in generation 4 and does not beat
server CPUs.

## 5. Power supplies

PSUs have no generations. They are rated by **wattage** and **efficiency**, as real units
are. Each brand has a budget (Bronze), mainstream (Gold), and flagship (Titanium) line.

**Efficiency affects heat.** A lower-rated unit wastes more of its draw as heat, tying PSU
choice into the cooling system.

**PSUs are the energy sinks.** A tech mod's cables run to the PSU installed in a rack, as
a real PSU is what plugs into the wall. The PSU then covers the draw of the hardware in
its rack.

### The fake-rated PSU

Fake-rated PSUs lie about their capacity, as cheap real ones do — independent testing of
no-name units typically finds they deliver 40–60% of their claimed wattage. One such PSU
exists in the mod.

The rating is **unverified rather than hidden**: the tooltip says so, and once installed
the rack GUI reports the real capacity, so building within the true figure is safe.
**Sustained load above that real capacity destroys the PSU and starts a fire**, taking the
hardware in its rack with it — failure follows prolonged overload rather than arriving
instantly, as cheap PSUs really die.

It is the only source of fire in the mod.

## 6. Art direction

**Hardware textures are distinct per generation and tinted per brand.**

Generations read as **physical evolution** — generation 1 looks like old beige hardware,
generation 4 sleek and black, following how each era actually looked.

Brands use **their real colours**: Untel blue, AND red, Envidia green.
