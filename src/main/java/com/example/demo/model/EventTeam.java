package com.example.demo.model;


import lombok.Data;

@Data
public class EventTeam {
    private int id;
    private int event_number;
    private int team_number;

    public EventTeam(int id,int event_number, int team_number) {
        this.id = id;
        this.event_number = event_number;
        this.team_number = team_number;
    }
}
