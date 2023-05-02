package com.example.demo.model;

import lombok.Data;

@Data
public class EventInformation {
    private int event_number;
    private String event_name;
    private String event_time;
    private String event_location;

    public EventInformation(int event_number, String event_name, String event_time, String event_location) {
        this.event_number = event_number;
        this.event_name = event_name;
        this.event_time = event_time;
        this.event_location = event_location;
    }

//    public EventInformation(String event_name, String event_time, String event_location) {
//        this.event_name = event_name;
//        this.event_time = event_time;
//        this.event_location = event_location;
//    }


}
