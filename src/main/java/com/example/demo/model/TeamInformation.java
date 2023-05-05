package com.example.demo.model;

import lombok.Data;
@Data
public class TeamInformation {
    private int team_number;
    private String team_name;
    private String team_affiliation_college;
    private int total_score;

    public TeamInformation(int team_number, String team_name, String team_affiliation_college, int total_score) {
        this.team_number = team_number;
        this.team_name = team_name;
        this.team_affiliation_college = team_affiliation_college;
        this.total_score = total_score;
    }
}