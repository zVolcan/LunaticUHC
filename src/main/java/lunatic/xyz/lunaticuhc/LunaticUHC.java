package lunatic.xyz.lunaticuhc;

import lunatic.xyz.lunaticuhc.managers.GeneralManager;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.plugin.java.JavaPlugin;

public final class LunaticUHC extends JavaPlugin {
    private static LunaticUHC pl;
    private GeneralManager gm;

    public LunaticUHC() {
        pl = null;
        gm = null;
    }

    @Override
    public void onEnable() {
        pl = this;
        WorldCreator worldCreator = new WorldCreator("uhc");
        worldCreator.environment(World.Environment.NORMAL);
        Bukkit.getScheduler().runTaskLater(this, () -> Bukkit.createWorld(worldCreator), 1);
        gm = new GeneralManager();
        gm.iniciarPlugin();
        saveDefaultConfig();
    }

    public static LunaticUHC getPlugin() {
        return pl;
    }

}
