package com.example.demo.mapper;

import com.example.demo.model.EventInformation;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface EventInformationMapper {
    List<EventInformation> findAllEvents();

    EventInformation findEventByNumber(int event_number);

    void addEvent(EventInformation eventInformation);

    void updateEvent(EventInformation eventInformation);

    void deleteEvent(int event_number);
    EventInformation findEventByName(String event_name);
}