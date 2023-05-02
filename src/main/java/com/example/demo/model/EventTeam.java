package com.example.demo.model;


import lombok.Data;

@Data
public class EventTeam {
    private int id;
    private int event_number;
    private int team_number;

    public EventTeam(int event_number, int team_number) {
        this.event_number = event_number;
        this.team_number = team_number;
    }
}
