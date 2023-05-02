package com.example.demo.model;


import lombok.Data;

@Data
public class TeamInfoFrontEnd {
    private String team_name;
    private String team_affiliation_college;
    private String captain_name;
    private String captain_gender;
    private int captain_total_score;
    private int captain_total_assists;
    private String player_1_name;
    private String player_1_gender;
    private int player_1_total_score;
    private int player_1_total_assists;
    private String player_2_name;
    private String player_2_gender;
    private int player_2_total_score;
    private int player_2_total_assists;
}
