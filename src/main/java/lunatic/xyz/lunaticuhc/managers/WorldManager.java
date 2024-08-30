package lunatic.xyz.lunaticuhc.managers;

import lunatic.xyz.lunaticuhc.LunaticUHC;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Objects;

public class WorldManager {

    public static World createWorld (FileConfiguration config) {
        World world = Bukkit.getWorld("uhc");
        Objects.requireNonNull(world).setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false);
        Objects.requireNonNull(world).setGameRule(GameRule.NATURAL_REGENERATION, false);
        Objects.requireNonNull(world).setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
        world.setMonsterSpawnLimit(0);
        world.setWaterAnimalSpawnLimit(0);
        world.setAutoSave(false);
        world.setDifficulty(Difficulty.HARD);
        world.setTime(1000);
        world.getWorldBorder().setSize(config.getInt("borde.tamaño") * 2);
        return world;
    }

    public static void generateChunks(World world) {

    }

}
