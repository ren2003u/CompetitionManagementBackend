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

    public TeamInformation(int team_number, String team_name, String team_affiliation_college, String captain_name, String player_1_name, String player_2_name, int total_score, int total_assists) {
        this.team_number = team_number;
        this.team_name = team_name;
        this.team_affiliation_college = team_affiliation_college;
        this.captain_name = captain_name;
        this.player_1_name = player_1_name;
        this.player_2_name = player_2_name;
        this.total_score = total_score;
        this.total_assists = total_assists;
    }

//    public TeamInformation(String captain_name, String player_1_name, String player_2_name, String team_affiliation_college, String team_name, int total_assists, int total_score) {
//        this.team_name = team_name;
//        this.team_affiliation_college = team_affiliation_college;
//        this.captain_name = captain_name;
//        this.player_1_name = player_1_name;
//        this.player_2_name = player_2_name;
//        this.total_score = total_score;
//        this.total_assists = total_assists;
//    }
}