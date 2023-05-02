package com.example.demo.model;

import lombok.Data;

@Data
public class EventInformation {
    private int event_number;
    private String event_name;
    private String event_time;
    private String event_location;

    public EventInformation(String event_name, String event_location, String event_time) {

    }
}
