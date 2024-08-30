package lunatic.xyz.lunaticuhc.players;

import lunatic.xyz.lunaticuhc.status.PlayerState;

public class UhcPlayer {

    private int kills;
    private PlayerState state;

    public UhcPlayer () {
        kills = 0;
    }

    public int getKills() {
        return kills;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public PlayerState getState() {
        return state;
    }

    public void setState(PlayerState state) {
        this.state = state;
    }
}
