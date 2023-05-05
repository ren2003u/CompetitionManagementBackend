package com.example.demo.model;

import lombok.Data;

@Data
public class User {
    private int id;
    private String team_name;
    private String username;
    private String password;
    private String status;
    private int score;

    public User(int id, String team_name, String username, String password, String status, int score) {
        this.id = id;
        this.team_name = team_name;
        this.username = username;
        this.password = password;
        this.status = status;
        this.score = score;
    }
}
