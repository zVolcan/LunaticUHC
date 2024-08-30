package lunatic.xyz.lunaticuhc.players;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.List;

public class UhcTeam {

    private int teamKills;
    private Location location;
    private List<Player> miembros;
    private final Team score;

    public UhcTeam (Player player, int teamNumber, ChatColor color) {
        teamKills = 0;
        location = null;
        this.miembros = new ArrayList<>();
        miembros.add(player);
        this.score = Bukkit.getScoreboardManager().getMainScoreboard().registerNewTeam(String.valueOf(teamNumber));
        score.addEntry(player.getName());
        score.setColor(color);
    }

    public int getTeamKills() {
        return teamKills;
    }

    public void addKill() {
        teamKills++;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public List<Player> getMiembros() {
        return miembros;
    }
}
