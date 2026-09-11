# CryptoCraft — Mining and payout

Pools, hashing, and how the in-game rig maps to real work. See `CRYPTOCRAFT.md` §7 for
the summary.

## 1. Rules

**Real mining is opt-in per world.** Nothing runs until the player enters an address and
presses Start.

**Hashing is client-side.** Each player's own machine hashes for the rigs they own, and
connects to the pool directly — the server never mines and never handles an address.

**Mining stops when the player leaves the world**, and never runs after the game closes.
While the player is in the world, a **keep-mining toggle** lets rigs continue while they
are AFK or away from the rigs, including through chunk unloads. Nothing ever runs outside
Minecraft.

**The address lives on the Mining Controller**, so one player can split payouts across
rigs.

**One coin per Mining Controller.** All rigs served by a controller mine its selected
coin; mining a second coin means building a second controller.

**The real payout is the only reward.** Mining yields no in-game currency or items.

## 2. Pools

**Each coin has its own pool list.** The GUI offers a dropdown of known-good pools for the
selected coin, plus a free-text field for any other. All take the payout address as the
stratum username and need no account.

**Monero**

| Pool | Fee | Minimum payout | Notes |
|---|---|---|---|
| **C3Pool** *(default)* | 0% | 0.001 XMR, adjustable | Established, no account |
| Bluenose | 0% | 0.001 XMR | Smaller pool, more variance |
| MoneroOcean | 0% | 0.003 XMR | Largest, profit-switching |
| SupportXMR | 0.6% | 0.01 XMR | Most reliable, highest threshold |

P2Pool has the lowest threshold of any Monero pool (~0.0001 XMR) and 0% fees, but requires
a local P2Pool process alongside the game. The guide book covers it as an advanced option;
it is not in the dropdown.

**Bitcoin** — pool list set when Bitcoin support is implemented. The same requirements
apply: address as username, no account.

## 3. Earnings

**The GUI shows estimated earnings honestly** — estimated coin per day and progress toward
the pool's payout threshold. Nothing is oversold, and the estimate tells the player what a
coin is actually worth at their hashrate without the mod editorialising.

On Monero, a modest machine reaches C3Pool's 0.001 XMR threshold every few days and a
high-end one roughly daily, each payout worth a fraction of a dollar. Electricity costs
more than the mod earns; the point is that the mining and the payout are real.

## 4. Real-mining mapping

The in-game rig governs how hard the real miner runs. Mapping is based on the player's
actual measured hashrate, not an arbitrary virtual one.

**1. Benchmark.** Measure the machine's real maximum hashrate for the active coin's
algorithm.

**2. Cap.** Limit mining to a safe fraction of that, so the mod never consumes the whole
machine. The cap is adaptive, not a fixed percentage:

- **Core count** — detected at startup. An old quad-core takes a small share; a high-core
  machine takes much more.
- **Tick rate** — monitored while running, throttling back when the game struggles.

The cap is configurable. The capped figure is the ceiling available to in-game mining.

**3. Mining power.** Each hardware variant has a mining-power value. Better hardware is
worth dramatically more. No block is a fixed percentage of the ceiling.

**4. Utilisation curve.** Total mining power of all installed hardware converts to a
fraction of the ceiling with diminishing returns:

```
fraction = 1 - exp(-N / k)
```

where `N` is combined mining power and `k` controls the curve. Early hardware gives large
gains; later hardware squeezes out the last percentage points. The curve never reaches
100%, so farms can grow without an artificial limit.

**5. Result.** The resulting effective hashrate is what the native miner runs at, connected
to the pool and paid to the player's address.

| Example | |
|---|---|
| Real measured hardware | 20 kH/s |
| Safety cap | 10 kH/s |
| Rig utilisation | 80% |
| Effective hashrate | ~8 kH/s |

Hashing always runs on background threads, never on the server tick.

## 5. Implementation

**Target:** NeoForge, Minecraft 1.21.1, Java 21.

1.21.1 is where the big-mod ecosystem lives — Mekanism, Create and AE2 are all NeoForge
1.21.1, and none has a current Fabric build. Since the mod depends on a tech mod for
power, shipping into that ecosystem matters more than targeting the newest version.
Expect to port later; 1.21.1 is already a year old.

**Each coin supplies an algorithm implementation and a pool list.** Nothing else in the
mod is coin-specific.

**RandomX runs as a native library.** The reference `librandomx` (pinned to **v1.2.3** —
Monero mainnet runs RandomX v1, not upstream's v2.x line) is bundled in the JAR for
Windows, Linux and macOS, loaded through Java 21's Foreign Function & Memory API. A
pure-Java implementation would be 5–20× slower — RandomX is deliberately built to run fast
only on real CPUs, using runtime-compiled code, hardware AES and huge pages — and that
would push payouts from days to months.

**The binaries are built by CI and vendored.** Upstream ships source only, so a GitHub
Actions workflow builds all three platforms from the pinned tag and the results are
committed. Contributors need no C++ toolchain, and the provenance is ours rather than a
third party's — which matters for a library antivirus flags.

**FFM is reached reflectively.** 1.21.1 runs on Java 21, where FFM is a preview API:
`--enable-preview` would version-lock the class files so Minecraft's launcher could not
load them. The runtime is complete on 21 — only compiling against it is gated — so one
bootstrap class resolves `Linker`, `Arena` and `SymbolLookup` by reflection at startup and
everything above it uses the returned `MethodHandle`s, which cost nothing on the hashing
path. Every method used has the same shape on 21 and on current JVMs, so the same build
runs on both. Revisit when the mod ports past Java 21.

**RandomX memory mode is chosen automatically.** Fast mode needs 2GB for its dataset;
light mode needs 256MB and is roughly 5× slower. The mod detects available memory and
picks fast mode where there is room to spare alongside Minecraft.

**SHA-256 needs no native library.** Java's built-in implementation is adequate, since
Bitcoin's difficulty makes hashrate academic at these speeds.

**Antivirus will flag the native library.** Detection is behavioural — a mining library is
a mining library, and code signing would not prevent it. The README explains this rather
than trying to work around it.

**Distribution is GitHub Releases.** Modrinth and CurseForge both prohibit cryptocurrency
mining; whether an opt-in, player-controlled, own-address miner is an exception is worth
asking, but the assumption is no.

## 6. Config

| Setting | Purpose |
|---|---|
| CPU cap override | Set the hashing cap manually instead of auto-detection |
| Disable real mining | Pack or server switch turning real mining off entirely |
| Ore generation toggles | Disable or retune ore generation for pack compatibility |
| Memory mode override | Force RandomX light or fast mode instead of auto-detecting |
