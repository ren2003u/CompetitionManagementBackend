package com.example.demo.model;

import lombok.Data;

@Data
public class TeamInformation {
    private int team_number;
    private String team_name;
    private String team_affiliation_college;
    private String captain_name;
    private String player_1_name;
    private String player_2_name;
    private int total_score;
    private int total_assists;

    public TeamInformation(String team_name, String team_affiliation_college, String captain_name, String player_1_name, String player_2_name, int teamTotalScore, int teamTotalAssists) {

    }
}