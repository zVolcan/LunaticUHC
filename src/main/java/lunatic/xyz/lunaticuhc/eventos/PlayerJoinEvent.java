package lunatic.xyz.lunaticuhc.eventos;

import lunatic.xyz.lunaticuhc.managers.GeneralManager;
import lunatic.xyz.lunaticuhc.players.UhcPlayer;
import lunatic.xyz.lunaticuhc.status.PlayerState;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Objects;

public class PlayerJoinEvent implements Listener {

    private final GeneralManager gm;

    public PlayerJoinEvent(GeneralManager gm) {
        this.gm = gm;
    }

    @EventHandler
    public void onJoin (org.bukkit.event.player.PlayerJoinEvent e) {
        UhcPlayer uhcPlayer = gm.getPlayer(e.getPlayer());
        if (uhcPlayer.getState().equals(PlayerState.ESPERANDO)) {
            e.getPlayer().teleport(Objects.requireNonNull(Bukkit.getWorld("world")).getSpawnLocation());
        }
        e.getPlayer().setPlayerListHeaderFooter(ChatColor.translateAlternateColorCodes('&', "&f&lʟᴜɴᴀᴛɪᴄ ᴜʜᴄ"), ChatColor.translateAlternateColorCodes('&', "&7lunaticuhc.xyz"));
        e.setJoinMessage(ChatColor.translateAlternateColorCodes('&', "&8[&a+&8] &f"+e.getPlayer().getName()));
    }

    @EventHandler
    public void onLeave (PlayerQuitEvent e) {
        e.setQuitMessage(ChatColor.translateAlternateColorCodes('&', "&8[&c-&8] &f"+e.getPlayer().getName()));
    }
}
