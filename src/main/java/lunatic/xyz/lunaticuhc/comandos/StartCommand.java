package lunatic.xyz.lunaticuhc.comandos;

import lunatic.xyz.lunaticuhc.LunaticUHC;
import lunatic.xyz.lunaticuhc.managers.GeneralManager;
import lunatic.xyz.lunaticuhc.status.StateUhc;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class StartCommand implements CommandExecutor {
    private final GeneralManager gm;

    public StartCommand(GeneralManager gm) {
        this.gm = gm;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) return true;
        Player player = (Player) sender;

        if (!player.hasPermission("lunaticuhc.start")) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8[&fUHC&8] &cNo tienes permisos para ejecutar ese comando."));
            return true;
        } else if (!gm.getState().equals(StateUhc.ESPERANDO)) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8[&fUHC&8] &c¡El UHC ya se encuentra en juego!"));
            return true;
        } else {
            Bukkit.getScheduler().runTaskLater(LunaticUHC.getPlugin(), gm::startGame, 20);
        }

        return true;
    }
}
