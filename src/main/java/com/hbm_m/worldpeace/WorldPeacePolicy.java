package com.hbm_m.worldpeace;

/**
 * Central safety policy for HBM: World Peace.
 *
 * <p>These are fork-level rules rather than ordinary gameplay options. Keeping them in one
 * place prevents catastrophic content from being re-enabled accidentally by unrelated HBM
 * configuration or registration code while we progressively remove/retune upstream systems.
 *
 * <p>Phase 1A wires the weapon/access rules. Radiation is declared disabled-by-policy here
 * and receives full enforcement in the dedicated radiation phase.
 */
public final class WorldPeacePolicy {

    private WorldPeacePolicy() {
    }

    /** Ballistic/strategic missile gameplay is not part of World Peace. */
    public static boolean allowStrategicMissiles() {
        return false;
    }

    /** Nuclear weapons, nuclear mines/grenades/air bombs and strategic nuclear effects are disabled. */
    public static boolean allowNuclearWeapons() {
        return false;
    }

    /** FLEIJA, black-hole, taint-catastrophe and similar exotic destructive effects are disabled. */
    public static boolean allowExoticDestruction() {
        return false;
    }

    /** Catastrophic debug/admin explosion launchers are disabled in normal World Peace builds. */
    public static boolean allowCatastrophicExplosionCommands() {
        return false;
    }

    /**
     * World Peace policy target for radiation. Phase 1A also flips the existing config defaults
     * off; later phases enforce this across every radiation source/sink.
     */
    public static boolean allowRadiation() {
        return false;
    }

    public static boolean allowBombCreativeContent() {
        return allowNuclearWeapons() || allowExoticDestruction();
    }
}
