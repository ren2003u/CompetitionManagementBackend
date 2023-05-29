package com.example.demo.service;

import com.example.demo.mapper.EventInformationMapper;
import com.example.demo.model.EventInformation;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventInformationService {
    @Autowired
    private EventInformationMapper eventInformationMapper;

    public List<EventInformation> findAllEvents() {
        return eventInformationMapper.findAllEvents();
    }

    public EventInformation findEventByNumber(int event_number) {
        return eventInformationMapper.findEventByEventNumber(event_number);
    }

    public void addEvent(EventInformation eventInformation) {
        eventInformationMapper.addEvent(eventInformation);
    }

    public void updateEvent(EventInformation eventInformation) {
        eventInformationMapper.updateEvent(eventInformation);
    }

    public void deleteEvent(int event_number) {
        eventInformationMapper.deleteEvent(event_number);
    }

    public EventInformation findEventByName(String event_name) {
        return eventInformationMapper.findEventByName(event_name);
    }
    public List<EventInformation> fuzzyQueryEligibleEventByEventName(String fuzzyEventName){
        return eventInformationMapper.fuzzyQueryEligibleEventByEventName(fuzzyEventName);
    }
}
