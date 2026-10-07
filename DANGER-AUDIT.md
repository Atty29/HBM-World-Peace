# HBM: World Peace — Danger Audit

> **Project rule:** Keep the engineering. Remove the apocalypse.

This document is the initial gameplay-safety audit for **HBM: World Peace**, a safety-focused fork of HBM Modernized for Minecraft 1.20.1.

The audit is based on upstream **Raptor324/HBM-Modernized** `main`, commit `0365cef5956016913095eeeb9366f26c601ca9a0`.

This is a scope document, not an implementation commit. No upstream gameplay code has been copied into this repository yet.

## 1. World Peace design rules

HBM: World Peace should preserve HBM's industrial identity:

- ores, minerals and metallurgy
- parts, alloys and material processing
- chemistry and fluids
- industrial machines and multiblocks
- power generation, storage and transmission
- reactors as engineering machines
- automation and logistics
- tools, protective equipment and industrial armour
- instrumentation, gauges and monitoring equipment
- normal industrial risk where it makes gameplay interesting

It should remove or neutralise content whose main purpose or likely result is large-scale destruction:

- ballistic/strategic missiles
- nuclear bombs and nuclear warheads
- antimatter/singularity/superweapon effects
- nuclear grenades, nuclear mines and air-delivered nuclear weapons
- black holes and comparable map-deleting effects
- catastrophic reactor explosions
- persistent radiation contamination
- fallout and crater-biome contamination
- radiation-driven block/biome mutation
- world-generation features whose purpose is nuclear destruction

World Peace should prefer these consequences for industrial mistakes:

1. automatic shutdown / SCRAM
2. lost efficiency or lost production
3. damaged machine components
4. consumed or ruined fuel/components
5. steam/smoke/particles/sound warnings
6. repair work
7. small, capped local damage only where an explosion is genuinely useful gameplay

It should **not** prefer giant craters, persistent contaminated chunks, biome conversion or chain reactions that can destroy a base or server world.

---

## 2. Classification meanings

| Class | Meaning |
|---|---|
| **KEEP** | Retain substantially as normal HBM gameplay. |
| **REDUCE** | Retain the system, but lower destructive/world-impact behaviour and add safety limits. |
| **DISABLE** | Keep code/assets initially if useful for compatibility, but World Peace defaults prevent the feature from operating. |
| **REMOVE** | Do not expose as usable World Peace gameplay; remove registrations/recipes/worldgen or exclude the feature family. |
| **DECIDE** | Ambiguous content requiring a later gameplay decision. |

---

# 3. Immediate REMOVE list — strategic and catastrophic weapons

## 3.1 Ballistic missile system — REMOVE

Upstream has a dedicated missile family under paths such as:

- `src/main/java/com/hbm_m/entity/missile/`
- `MissileBaseEntity`
- `MissileTier0` through `MissileTier4`
- `MissileStealthEntity`
- `MissileABMEntity`
- missile item/render/network tracking infrastructure
- `LaunchPadBaseBlockEntity` missile-launch integration
- `MissileWarheadEffects`

The missile code is connected to nuclear, incendiary, EMP and exotic warhead behaviour. `MissileTier0` also references `NuclearExplosionAPI`, `FleijaExplosionAPI` and black-hole effects.

**World Peace action:** remove ballistic/weapon missile registrations, recipes, creative-tab exposure and launch capability.

### Peaceful launch exception — DECIDE

`SoyuzEntity` and `SoyuzCapsuleEntity` live in the missile package but may represent peaceful spaceflight rather than a weapon. They should **not** be deleted merely because of their package location.

Proposed rule:

- weapon missiles: **REMOVE**
- civilian/spaceflight vehicle functionality: **KEEP if independently useful**, after separating it from weapon-only launch code

---

## 3.2 Nuclear bomb blocks — REMOVE

Upstream has a dedicated bomb package containing at least:

- `NukeBaseBlock`
- `NukeFatManBlock`
- `NukePrototypeBlock`

`NukeFatManBlock` invokes `NuclearExplosionAPI` when detonated.

**World Peace action:** remove nuclear bomb gameplay entirely, including recipes, registration exposure and detonation paths.

Do not replace these with smaller nuclear bombs. The point of World Peace is that they are not part of the gameplay loop.

---

## 3.3 Nuclear explosion engine as player-accessible gameplay — REMOVE / INTERNAL-ONLY

Upstream contains:

- `NuclearExplosionAPI`
- `NuclearExplosionConfig`
- `ExplosionNukeGeneric`
- MK-style nuclear explosion entities/effects
- fallout rain
- crater/biome effects
- a command-side `ExplosionType` family including nuclear MK5, Fat Man, generic, charge, dud, mine and grenade variants

The current upstream config exposes large nuclear radii and dedicated fallout/crater behaviour.

**World Peace action:**

- no player-accessible nuclear explosion commands
- no survival or creative item should call the nuclear explosion APIs
- keep only whatever internal pieces are temporarily required to compile the fork during early development
- later delete dead nuclear-explosion code once dependencies are mapped

---

## 3.4 FLEIJA / exotic superweapon effects — REMOVE

Upstream includes `FleijaExplosionAPI` and `EntityCloudFleija`, with missile code capable of invoking the effect.

**World Peace action:** remove from gameplay and remove all recipes/registrations/commands that can trigger it.

---

## 3.5 Black holes / singularities / exotic map destruction — REMOVE

Upstream includes:

- `BlackHoleEntity`
- `MissileWarheadEffects.blackHole(...)`
- a `dropSingularity` configuration option that currently defaults to `true`
- a `dropCell` option for antimatter-style dropped-item detonation that currently defaults to `true`

**World Peace action:**

- black-hole/singularity weapon effects: **REMOVE**
- dropped-item singularity behaviour: **DISABLE and later remove**
- dropped antimatter-cell detonation behaviour: **DISABLE and later remove**
- World Peace defaults for any retained transitional config must be `false`

---

## 3.6 Nuclear grenades / nuclear mines / nuclear air bombs — REMOVE

Upstream has separate grenade/projectile content including:

- `GrenadeNucItem`
- `GrenadeNucProjectileEntity`
- `AirNukeBombItem`
- nuclear grenade explosion types
- nuclear mine explosion types

**World Peace action:** remove these completely from gameplay.

---

## 3.7 Nuclear/weapon worldgen — REMOVE

Upstream worldgen includes a `dud_nuke` placed/configured feature.

**World Peace action:** remove nuclear-weapon worldgen. Existing industrial ruins/structures can be audited separately and retained if they do not introduce excluded weapons or persistent contamination.

---

## 3.8 Nuclear Creeper and similar catastrophe mobs — REMOVE

`EntityCreeperNuclear` directly references nuclear explosion/radiation systems.

**World Peace action:** remove catastrophe-themed mobs whose primary mechanic is large destructive detonation or contamination.

---

# 4. Radiation system — DISABLE by default

The user's World Peace requirement is **no radiation**, not merely weaker radiation.

Upstream radiation is deeply integrated through systems including:

- `ChunkRadiationManager`
- chunk-radiation capabilities/storage
- player radiation tracking and effects
- radioactive ore/hazard blocks
- Geiger counter integration
- fallout entities
- crater-biome radiation
- radiation fog/visual effects
- radiation-driven world effects/mutation

Upstream configuration currently defaults to:

- `enableRadiation = true`
- `enableChunkRads = true`
- `enableRadFogEffect = true`
- `worldRadEffects = true`
- chunk spread/decay values that allow persistent environmental radiation

The upstream README also explicitly describes chunk-based spread and grass/foliage mutation at high radiation.

### World Peace policy

Default behaviour:

- `enableRadiation = false`
- `enableChunkRads = false`
- `enableRadFogEffect = false`
- `worldRadEffects = false`
- no radiation damage to players
- no radiation accumulation on players
- no chunk contamination
- no chunk-to-chunk radiation spread
- no radioactive fallout
- no radiation-driven vegetation/block mutation
- no crater-biome radiation

### Compatibility strategy

Do **not** immediately rip the radiation classes out if machines/materials still refer to them. First make the no-radiation state authoritative and test that the industrial progression still works.

After dependencies are known, dead radiation-only content can be removed or simplified.

### Radiation-themed equipment — DECIDE / REPURPOSE

- Geiger counters/dosimeters become functionally pointless if there is truly no radiation.
- Hazmat/HEV/liquidator/protective equipment may still have value for chemical, heat, toxic-gas or industrial protection.

Proposed action:

- radiation-only instruments: **REMOVE or repurpose as industrial hazard meters**
- protective suits: **KEEP**, but retain useful non-radiation protection where available

---

# 5. Fallout, crater biomes and environmental mutation — DISABLE / REMOVE

Upstream nuclear explosion types support crater generation, biome effects and in some cases fallout. The config also has:

- `enableCraterBiomes = true`
- crater-biome radiation values
- `falloutRangePercent = 100`
- fallout processing controls

`EntityNukeExplosionMK5` can create `EntityFalloutRain` based on fallout range.

**World Peace action:**

- crater biome conversion: **DISABLE**
- fallout rain: **REMOVE from reachable gameplay**
- nuclear crater generation: **REMOVE from reachable gameplay**
- radiation world mutation: **DISABLE**
- taint trails/world corruption: **DISABLE**

Industrial mistakes must not permanently re-theme or contaminate chunks.

---

# 6. Reactors — KEEP, but safe-failure redesign

Reactors are central to HBM's engineering identity and should remain.

Known upstream systems include at least:

- Zirnox reactor (`MachineZirnoxBlockEntity`)
- RBMK reactor columns and console
- breeding/small reactor-related content and fuel infrastructure

## 6.1 Zirnox — REDUCE

Upstream Zirnox tracks heat and pressure and calls a `meltdown()` path if limits are exceeded. The class imports `ExplosionNukeGeneric`, so the full failure path must be audited carefully.

**World Peace target failure:**

1. reactor shuts down
2. fuel inventory may be damaged/ruined
3. water/CO2/steam may be dumped or vented
4. machine enters a faulted state
5. player must repair/reset components
6. optional steam/smoke effects and warning sound
7. no nuclear blast
8. no persistent radiation
9. no biome/chunk contamination

## 6.2 RBMK — REDUCE

RBMK already contains a meltdown concept and an upstream `getMeltdownsDisabled(...)` check; the console also contains an AZ-5 emergency-shutdown control.

This is a strong basis for World Peace.

**World Peace target:**

- preserve reactor physics/heat/neutron management as gameplay
- preserve AZ-5/SCRAM
- make catastrophic meltdown destruction disabled by default and preferably impossible in the World Peace ruleset
- overheat should damage or ruin reactor columns/fuel and shut down the plant
- no large explosion
- no persistent radiation field

## 6.3 Global reactor safety policy — HIGH PRIORITY

Rather than independently patching every reactor forever, create a World Peace safety abstraction/config that all reactor failure paths use.

Proposed modes:

- `SAFE_SHUTDOWN` — World Peace default
- `LOCAL_DAMAGE` — optional server mode with tightly capped machine-area damage
- never expose an upstream catastrophic nuclear-failure mode in standard World Peace gameplay

---

# 7. General explosion engine — REDUCE globally

Even after strategic weapons are removed, ordinary machines, gases and materials may still invoke Minecraft/HBM explosions.

World Peace should add a **central explosion safety policy** for retained industrial systems.

Proposed rules:

- hard cap terrain-damaging industrial blast radius
- distinguish entity damage from block destruction
- allow a server option for `mobGriefing`-style block protection
- prevent industrial accidents from force-loading huge areas for blast processing
- prevent blast systems from spawning fallout or converting biomes
- chain explosions should have a recursion/energy cap

Suggested default target:

- retained industrial explosion radius: small/local only (exact number to be tuned in testing)
- machine failures should usually prefer block-state damage/repair over `Level.explode`

This safety layer should be implemented **before** enabling all retained machines in survival.

---

# 8. Conventional grenades, mines, airstrikes and weapon explosives — REMOVE by default

Upstream has a broad grenade family (`entity/grenades`, `GrenadeItem`, IF grenade types, airstrike entities, etc.). Some variants are conventional rather than nuclear, but they are still primarily weapon content.

For **World Peace**, the default direction should be:

- combat grenades: **REMOVE**
- high-explosive grenade variants: **REMOVE**
- landmines: **REMOVE**
- airstrike items/entities: **REMOVE**
- nuclear grenade/air-bomb variants: **REMOVE absolutely**

### Utility explosives — DECIDE

There may be HBM explosives whose genuine use is mining/demolition rather than combat.

These may be worth retaining as **REDUCE** if:

- blast radius is capped
- no radiation/fallout
- no exotic effects
- server can disable block damage
- recipes fit industrial progression

This should be audited item-by-item later rather than retaining the whole weapons package.

---

# 9. Guns / ranged weapons / military equipment — DECIDE, leaning REMOVE

World Peace is being defined primarily as an engineering fork, not a weapons pack.

Recommended default rule:

- dedicated firearms and battlefield weapons: **REMOVE from normal World Peace progression**
- nuclear/exotic weapons: **REMOVE absolutely**
- industrial tools that can incidentally damage entities: **KEEP**
- armour/protective gear: **KEEP**
- radar or military-looking machinery: keep only if it has a useful peaceful/industrial function

This boundary should be decided after the first upstream fork boots, because weapon registrations may be heavily interwoven with shared items/ammunition/creative tabs.

---

# 10. Antimatter / singularity materials — KEEP material only if industrially useful; REMOVE destructive behaviour

Some exotic materials may be ingredients in advanced progression even when their weapon use is removed.

Rule:

- material/resource/item required for peaceful advanced machinery: **KEEP**
- dropping the item causing an explosion/singularity: **DISABLE**
- dedicated bomb/warhead/weapon recipes using the material: **REMOVE**
- catastrophic storage accidents: **REDUCE to item loss/machine fault**, not map deletion

This is why World Peace should not simply delete every item whose name sounds dangerous.

---

# 11. Industrial hazard system — KEEP / REDUCE

HBM includes a hazard system covering radioactivity, pyrophoricity, explosive-on-fire behaviour, hydro-reactivity and related traits.

Radiation is disabled separately, but the remaining industrial hazards are valuable gameplay.

## KEEP

- hot materials
- toxic/irritant materials
- chemical protection
- fire risk
- warning labels/tooltips
- gas handling
- safe storage requirements
- protective clothing

## REDUCE

- explosive-on-fire responses should use the global small/local blast cap
- pyrophoric items should prefer fire/burning/item loss over large block explosions
- hydro-reactive accidents should be local
- gas explosions must be bounded and unable to chain across enormous areas

The goal is **factory accident**, not **base eraser**.

---

# 12. Gas systems — KEEP / REDUCE

Upstream includes gas/hazard content such as flammable gas, explosive gas, carbon monoxide and meltdown-related gas blocks/equipment.

World Peace direction:

- gas production/processing/burners/flares: **KEEP**
- toxic exposure mechanics: **KEEP at reasonable levels**
- flammable gas: **KEEP**
- explosive gas: **REDUCE through global explosion cap**
- radiation/meltdown gas effects: **REMOVE or repurpose without radiation**

Gas systems are industrial gameplay and should not be removed just because they can be hazardous.

---

# 13. Taint / world-corruption mechanics — DISABLE / REMOVE

`BlockTaint` exists in the bomb/world-damage area, and config includes `taintTrails`.

**World Peace action:**

- taint spreading/trails: disabled
- weapon-created taint: removed with its source weapon
- keep no mechanic whose normal result is uncontrolled world conversion

---

# 14. World generation — KEEP, with a hazardous-feature blacklist

## KEEP

- normal HBM ores
- industrial/resource deposits
- peaceful ruins/labs/factories/bunkers if they do not contain excluded weapon functionality
- material progression worldgen

## REMOVE

- `dud_nuke` generation
- weapon launch sites whose primary purpose is strategic weapons
- structures that automatically contaminate huge regions
- worldgen that gives excluded bombs/missiles as loot

## AUDIT LOOT

Crates/structure loot must be scrubbed so removed weapons do not remain obtainable indirectly. This includes grenade, missile, bomb and weapon-component loot entries.

---

# 15. Fracking / terrain-affecting industrial machines — REDUCE

Upstream config includes a `frackingTower` section with a `destructionRange` value.

World Peace should preserve resource extraction but audit any machine that destroys terrain as part of normal operation.

Preferred behaviour:

- consume a deposit/resource state
- use energy/fluids
- create waste/by-products
- avoid broad terrain deletion

If terrain modification is essential, cap it tightly and make it configurable.

---

# 16. Energy, metallurgy, chemistry and automation — KEEP

These are the heart of the fork.

Keep and prioritise:

- power generation
- batteries/storage
- cables/transmission
- ordinary generators
- steam systems
- turbines where present
- processing machines
- assembly machines
- presses
- furnaces
- chemical plants
- fluid tanks/pipes
- alloys and metallurgy
- ores/resources
- fuel production required for peaceful reactors
- automation/logistics

Any recipe that currently requires a removed weapon-only intermediate should be repaired rather than deleting the industrial machine.

---

# 17. Tools, armour and protective equipment — KEEP

Keep:

- drills/mining tools
- industrial tools
- utility equipment
- protective suits
- hazmat-style suits (repurpose toward chemical/heat/gas protection where radiation is disabled)
- powered armour if its main role is protection/utility rather than a built-in strategic weapon

Audit separately:

- armour-integrated weapons
- weapon-only targeting systems
- ammunition-specific upgrades

---

# 18. Commands and creative/debug access — REDUCE / REMOVE

Upstream has command-side nuclear explosion support.

World Peace should not ship admin commands that make it trivial to bypass the entire safety philosophy and spawn nuclear devastation.

Action:

- nuclear explosion command: **REMOVE**
- radiation commands: **REMOVE or retain only as developer diagnostics while radiation code exists**
- safe machine/debug commands: **KEEP**

If destructive debug tools are needed during development, they should be dev-only and absent from production builds.

---

# 19. Initial dependency/knock-on risks

## 19.1 Registration dependencies

Removing an item is not enough. Check:

- block/item/entity registries
- creative tabs
- recipes
- advancements
- loot tables
- worldgen
- renderers/models/textures
- menus/screens
- packets
- sounds
- tags
- JEI/recipe integration if present
- commands

The first code phase should therefore **disable feature families cleanly at registration/progression boundaries**, then remove dead code after builds/tests reveal dependencies.

## 19.2 Progression dependencies

Weapon components may also be general industrial parts. Do not delete a material/component simply because a weapon recipe uses it.

Rule: remove the **weapon endpoint**, not automatically every ingredient feeding it.

## 19.3 Shared explosion code

Some industrial systems and weapon systems use the same explosion helpers. Do not delete a shared helper until every peaceful call site is understood.

## 19.4 Radiation dependencies

Fuel, ores, armour and UI may query radiation APIs even when radiation is disabled. First implement a stable no-radiation mode; then simplify code later.

---

# 20. Proposed World Peace defaults

These are policy defaults, not yet actual field names/API guarantees.

```text
strategicMissiles = false
nuclearWeapons = false
nuclearGrenades = false
landmines = false
airstrikes = false
blackHoles = false
singularityDropEffects = false
antimatterDropExplosions = false

radiation = false
chunkRadiation = false
radiationWorldEffects = false
radiationFog = false
fallout = false
craterBiomes = false
taintSpread = false

reactorFailureMode = SAFE_SHUTDOWN
industrialExplosionBlockDamage = LIMITED
industrialExplosionRadiusCap = SMALL_LOCAL_VALUE
explosionChunkLoading = false
```

Exact values and names will be designed during implementation.

---

# 21. Implementation priority

## Phase 0 — Fork/bootstrap

- import/preserve upstream GPL source and attribution
- add World Peace branding without unnecessary registry renames initially
- record upstream commit
- ensure untouched fork builds first

## Phase 1 — Hard-disable apocalypse content

- remove strategic missile exposure
- remove nuclear bombs/warheads
- remove nuclear grenades/mines/air bombs
- remove black-hole/FLEIJA/exotic superweapon triggers
- remove nuclear explosion commands
- remove destructive weapon worldgen and loot

**Goal:** no normal player can trigger catastrophic world destruction.

## Phase 2 — Radiation-off baseline

- make no-radiation policy authoritative
- disable player/chunk radiation
- disable spread/decay processing because no radiation should accumulate
- disable fallout, crater radiation, fog and mutation
- verify fuel/material/machine progression still works without radiation

**Goal:** ordinary World Peace worlds never become contaminated.

## Phase 3 — Global industrial explosion safety

- identify common retained explosion entry points
- add central cap/policy
- prevent force-loaded large blast processing
- prevent fallout/biome conversion from retained industrial paths
- bound chain reactions

**Goal:** industrial accidents remain local.

## Phase 4 — Reactor safe failures

- Zirnox safe failure
- RBMK safe failure
- audit every other reactor/generator with catastrophic failure logic
- implement shutdown/fault/repair states

**Goal:** reactors remain demanding engineering without deleting bases.

## Phase 5 — Weapon/progression cleanup

- conventional grenade/firearm decision
- remove weapon-only recipes and loot
- repair peaceful recipes that referenced removed components
- retain utility/mining tools

## Phase 6 — Industrial hazard tuning

- gases
- flammable/pyrophoric/hydro-reactive materials
- fracking/terrain effects
- toxic chemistry
- local fire/explosion behaviour

## Phase 7 — Full safety test suite

Tests should include:

- no missile/nuke obtainable through normal recipes/loot/worldgen
- no destructive command available in production
- radiation remains zero in normal gameplay
- no radiation-driven world mutation
- deliberately abused reactors enter safe failure
- retained gas/material accidents obey blast cap
- no retained machine force-loads a giant explosion area
- peaceful progression remains completable

---

# 22. Items requiring later design decisions

1. **Civilian Soyuz/spaceflight:** keep if it can be cleanly separated from ballistic missiles.
2. **Conventional firearms:** likely remove from normal progression, but dependency cost needs audit.
3. **Mining/demolition explosives:** keep only if genuinely useful and safely capped.
4. **Geiger/dosimeter devices:** remove, make decorative, or repurpose as general hazard meters.
5. **Power armour offensive features:** keep armour, strip dedicated heavy-weapon functions if present.
6. **Radar:** keep only if it gains a peaceful machine/monitoring purpose after missiles are removed.
7. **Radiation code removal vs dormant compatibility:** initially dormant is safer; later cleanup can remove dead code.
8. **Existing contaminated HBM worlds:** decide whether World Peace should actively clean radiation/taint on import or simply prevent new contamination.

---

# 23. First-pass classification summary

| Subsystem | World Peace classification |
|---|---|
| Ores / materials / metallurgy | **KEEP** |
| Industrial machines / multiblocks | **KEEP** |
| Energy generation/storage/cables | **KEEP** |
| Chemistry / fluids / pipes | **KEEP** |
| Automation / processing | **KEEP** |
| Reactors | **KEEP + REDUCE failures** |
| Industrial fire/toxic/gas hazards | **KEEP + REDUCE** |
| Radiation/player RAD | **DISABLE** |
| Chunk radiation/spread | **DISABLE** |
| Fallout | **REMOVE/DISABLE** |
| Radiation block/biome mutation | **DISABLE** |
| Crater biomes | **DISABLE** |
| Taint/world corruption | **DISABLE/REMOVE** |
| Ballistic/strategic missiles | **REMOVE** |
| Nuclear bombs/warheads | **REMOVE** |
| Nuclear grenades/mines/air bombs | **REMOVE** |
| Black holes/singularities | **REMOVE** |
| FLEIJA/exotic superweapons | **REMOVE** |
| Nuclear Creeper/catastrophe mobs | **REMOVE** |
| Nuclear explosion commands | **REMOVE** |
| Nuclear weapon worldgen (`dud_nuke`) | **REMOVE** |
| Conventional grenades/mines/airstrikes | **REMOVE by default** |
| Firearms | **DECIDE; leaning REMOVE** |
| Utility/mining explosives | **DECIDE; retain only with cap** |
| Tools / industrial equipment | **KEEP** |
| Armour / protective suits | **KEEP / REPURPOSE** |
| Civilian Soyuz/spaceflight | **DECIDE; likely KEEP if separable** |

---

## Final test for every upstream feature

Before a feature enters HBM: World Peace, ask:

> **Does this feature primarily help the player build, power, process, automate, explore, protect or manage an industrial system?**

If yes, keep it where practical.

If its primary purpose is to destroy players, bases, chunks, biomes or worlds, remove it.

If it is useful engineering but can cause catastrophic damage, keep the engineering and replace the catastrophe with a bounded, repairable failure.


## Phase 1A implementation status

Phase 1A is implemented on the `phase1a-world-peace-policy` branch.

The first policy layer disables strategic missile launch registration, nuclear/bomb and missile creative access, catastrophic explosion commands, FLEIJA/black-hole/taint destructive entry points, and nuclear/salted dud worldgen while preserving upstream registries/classes for compatibility. Existing radiation-related configuration defaults are changed to the World Peace-safe state, but complete radiation enforcement remains Phase 2.

See `docs/PHASE-1A.md` for the exact boundary.
