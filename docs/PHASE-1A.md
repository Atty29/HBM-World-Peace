# Phase 1A — World Peace safety policy

Phase 1A establishes the first authoritative World Peace rules without deleting upstream registries/classes.

## Disabled now

- Strategic/ballistic missile launch registration.
- Missile creative-tab contents.
- Nuclear/bomb creative-tab contents.
- Nuclear grenade, nuclear air-bomb, nuclear charge/duds and missile-development entries from the mixed weapons tab.
- Nuclear and taint-catastrophe spawn eggs.
- Catastrophic `/hbm_m explosion ...` access through the command branch permission predicate.
- FLEIJA destruction entry point.
- Black-hole and taint missile-warhead effects.
- Dud nuclear and salted-nuclear overworld generation.
- Upstream default settings for radiation, chunk radiation, radiation fog/world effects, crater biomes, dropped singularities/antimatter-cell explosions and missile network tracking.

## Intentionally retained in code

The underlying missile, bomb, explosion and entity registrations remain present for dependency compatibility. Phase 1A prevents normal access/use rather than deleting shared classes.

The bomb and missile creative tabs remain registered so upstream references do not break, but are empty and use a barrier icon while the World Peace policy disables their content.

## Not part of Phase 1A

- Reactor failure/meltdown redesign.
- Full radiation-system enforcement/removal.
- Global explosion radius caps.
- Conventional weapon balancing.
- Industrial hazards and chemistry retuning.
- Final recipe/progression cleanup.

Those remain later phases from `DANGER-AUDIT.md`.

## Policy source

The central fork-level policy lives in:

`src/main/java/com/hbm_m/worldpeace/WorldPeacePolicy.java`

The safety rules are deliberately fork policy rather than ordinary user toggles.
