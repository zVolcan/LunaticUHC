package lunatic.xyz.lunaticuhc.managers;

import lunatic.xyz.lunaticuhc.LunaticUHC;
import lunatic.xyz.lunaticuhc.comandos.StartCommand;
import lunatic.xyz.lunaticuhc.eventos.PlayerJoinEvent;
import lunatic.xyz.lunaticuhc.players.UhcPlayer;
import lunatic.xyz.lunaticuhc.players.UhcTeam;
import lunatic.xyz.lunaticuhc.status.PlayerState;
import lunatic.xyz.lunaticuhc.status.StateUhc;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

public class GeneralManager {

    private final World world;
    private HashMap<UUID, UhcPlayer> playerList;
    private HashMap<UUID, UhcTeam> listTeams;
    private final FileConfiguration config;
    private StateUhc state;

    public GeneralManager () {
        this.playerList = new HashMap<>();
        this.listTeams = new HashMap<>();
        this.config = LunaticUHC.getPlugin().getConfig();
        state = StateUhc.ESPERANDO;
        this.world = Bukkit.getWorld("uhc");
    }

    public void startGame() {
        state = StateUhc.JUGANDO;
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, Integer.MAX_VALUE, 200));
            player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 200));
            player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 200));
            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 200));
            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, Integer.MAX_VALUE, 200));
            player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, 200));
            getPlayerTeam(player);
        }

        for (UhcPlayer player : playerList.values()) {
            player.setState(PlayerState.JUGANDO);
        }

        for (UhcTeam team : listTeams.values()) {
            if (team.getLocation() == null) {
                int borde = (int) (world.getWorldBorder().getSize() / 2);
                int x = 0, z = 0;
                boolean ubicacionValida = false;

                while (!ubicacionValida) {
                    x = ThreadLocalRandom.current().nextInt(-borde, borde + 1);
                    z = ThreadLocalRandom.current().nextInt(-borde, borde + 1);

                    Material tipoBloque = world.getHighestBlockAt(x, z).getType();

                    if (tipoBloque != Material.WATER && tipoBloque != Material.LAVA) {
                        ubicacionValida = true;
                    }
                }

                team.setLocation(new Location(world, x, world.getHighestBlockYAt(x, z), z).add(0.5, 0, 0.5));
            }
        }


        Bukkit.getScheduler().runTask(LunaticUHC.getPlugin(), this::randomTeleport);
    }

    private void randomTeleport () {
        AtomicInteger delay = new AtomicInteger(10);
        Bukkit.getScheduler().runTaskLater(LunaticUHC.getPlugin(), () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                UhcTeam team = getPlayerTeam(player);
                player.teleport(team.getLocation());
                delay.addAndGet(10);
                Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&8[&fUHC&8] &fTeltransportando a &b"+player.getName()+" &8(&b"+player.getWorld().getPlayers().size()+"/"+Bukkit.getOnlinePlayers().size()+"&8)&f."));
            }
        }, delay.get());

        for (Player player : Bukkit.getOnlinePlayers()) {
            Bukkit.getScheduler().runTaskLater(LunaticUHC.getPlugin(), () -> {
                for (PotionEffect activePotionEffect : player.getActivePotionEffects()) {
                    player.removePotionEffect(activePotionEffect.getType());
                }
                player.sendTitle(ChatColor.translateAlternateColorCodes('&', "&aStarted UHC!"), "");
                player.getInventory().addItem(new ItemStack(Material.BOOK, 2), new ItemStack(Material.COOKED_BEEF, 8));
                Bukkit.getScheduler().runTaskLater(LunaticUHC.getPlugin(), () -> player.playSound(player.getLocation(), Sound.BLOCK_WOOD_BREAK, 1, 1), 30);
                Bukkit.getScheduler().runTaskLater(LunaticUHC.getPlugin(), () -> player.playSound(player.getLocation(), Sound.BLOCK_WOOD_BREAK, 1, 1), 20);
                Bukkit.getScheduler().runTaskLater(LunaticUHC.getPlugin(), () -> player.playSound(player.getLocation(), Sound.BLOCK_WOOD_BREAK, 1, 1), 10);
            }, delay.get() + 300);

            Bukkit.getScheduler().runTaskLater(LunaticUHC.getPlugin(), () -> {
                player.sendTitle(ChatColor.translateAlternateColorCodes('&', "&a1"), "");
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_GUITAR, 1, 1);
            }, delay.get() + 280);

            Bukkit.getScheduler().runTaskLater(LunaticUHC.getPlugin(), () -> {
                player.sendTitle(ChatColor.translateAlternateColorCodes('&', "&a2"), "");
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_GUITAR, 1, 1);
            }, delay.get() + 260);

            Bukkit.getScheduler().runTaskLater(LunaticUHC.getPlugin(), () -> {
                player.sendTitle(ChatColor.translateAlternateColorCodes('&', "&a3"), "");
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_GUITAR, 1, 1);
            }, delay.get() + 240);
        }
    }

    public void iniciarPlugin () {
        registerCommands();
        registerListeners();
        Bukkit.getScheduler().runTaskLater(LunaticUHC.getPlugin(), () -> WorldManager.createWorld(config), 20);
    }

    public UhcTeam getPlayerTeam (Player player) {
        UUID uuid = player.getUniqueId();
        if (listTeams.containsKey(uuid)) {
            return listTeams.get(uuid);
        } else {
            return listTeams.put(uuid, new UhcTeam(player, listTeams.size() + 1, getColor()));
        }
    }

    private void registerCommands() {
        Objects.requireNonNull(Bukkit.getServer().getPluginCommand("start")).setExecutor(new StartCommand(this));
    }

    private void registerListeners() {
        Bukkit.getPluginManager().registerEvents(new PlayerJoinEvent(this), LunaticUHC.getPlugin());
    }

    public World getWorld() {
        return world;
    }

    public HashMap<UUID, UhcPlayer> getPlayerList() {
        return playerList;
    }

    public UhcPlayer getPlayer (Player player) {
        UUID uuid = player.getUniqueId();
        if (playerList.containsKey(uuid)) {
            return playerList.get(uuid);
        } else {
            return playerList.put(uuid, new UhcPlayer());
        }
    }

    private ChatColor getColor() {
        int random = ThreadLocalRandom.current().nextInt(10);
        if (random == 1) {
            return ChatColor.AQUA;
        } else if (random == 2) {
            return ChatColor.DARK_AQUA;
        } else if (random == 3) {
            return ChatColor.GOLD;
        }else if (random == 4) {
            return ChatColor.YELLOW;
        }else if (random == 5) {
            return ChatColor.BLUE;
        }else if (random == 6) {
            return ChatColor.RED;
        }else if (random == 7) {
            return ChatColor.GRAY;
        }else if (random == 8) {
            return ChatColor.LIGHT_PURPLE;
        } else if (random == 9) {
            return ChatColor.GREEN;
        } else {
            return ChatColor.BLUE;
        }
    }

    public void setPlayerList(HashMap<UUID, UhcPlayer> list) {
        playerList = list;
    }

    public StateUhc getState() {
        return state;
    }

    public void setState(StateUhc state) {
        this.state = state;
    }

    public HashMap<UUID, UhcTeam> getListTeams() {
        return listTeams;
    }

    public void setListTeams(HashMap<UUID, UhcTeam> listTeams) {
        this.listTeams = listTeams;
    }

    public FileConfiguration getConfig() {
        return config;
    }
}
