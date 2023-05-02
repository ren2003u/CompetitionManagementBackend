package com.example.demo.model;

import lombok.Data;

@Data
public class PlayerInformation {
    private int id;
    private String name;
    private String gender;
    private int team;
    private int total_points_scored;
    private int total_assists;

    public PlayerInformation(String captain_name, String captain_gender, int team_number, int captain_total_score, int captain_total_assists) {
    }
}