package com.example.demo.model;

import lombok.Data;

import java.util.List;

@Data
public class EventRequest {
    private String event_name;
    private String event_location;
    private String event_time;
    private List<String> team_names;
}
