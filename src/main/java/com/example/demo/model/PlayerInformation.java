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

    public PlayerInformation(int id, String name, String gender, int team, int total_points_scored, int total_assists) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.team = team;
        this.total_points_scored = total_points_scored;
        this.total_assists = total_assists;
    }

//    public PlayerInformation(String name, String gender, int team, int total_points_scored, int total_assists) {
//        this.name = name;
//        this.gender = gender;
//        this.team = team;
//        this.total_points_scored = total_points_scored;
//        this.total_assists = total_assists;
//    }




}