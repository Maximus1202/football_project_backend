package com.dev.objects;
import javax.persistence.*;

@Entity
@Table(name = "live_games")
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    int id;
    @Column
    String team1;
    @Column
    int team1Goals;
    @Column
    String team2;
    @Column
    int team2Goals;
    @Column
    boolean isLive;


    public Match(String team1, String team2){
        this.team1 = team1;
        this.team2 = team2;
        this.isLive = true;

    }

    public Match() {

    }

    public boolean isLive() {
        return isLive;
    }

    public void setLive(boolean live) {
        isLive = live;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTeam1() {
        return team1;
    }

    public void setTeam1(String team1) {
        this.team1 = team1;
    }

    public int getTeam1Goals() {
        return team1Goals;
    }

    public void setTeam1Goals(int team1Goals) {
        this.team1Goals = team1Goals;
    }

    public String getTeam2() {
        return team2;
    }

    public void setTeam2(String team2) {
        this.team2 = team2;
    }

    public int getTeam2Goals() {
        return team2Goals;
    }

    public void setTeam2Goals(int team2Goals) {
        this.team2Goals = team2Goals;
    }
}
