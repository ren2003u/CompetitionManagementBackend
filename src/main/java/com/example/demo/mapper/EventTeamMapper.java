package com.example.demo.mapper;


import com.example.demo.model.EventTeam;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface EventTeamMapper {
    List<EventTeam> findAllEventTeams();
    EventTeam findEventTeamById(int id);
    void addEventTeam(EventTeam eventTeam);
    void updateEventTeam(EventTeam eventTeam);
    void deleteEventTeam(int id);
    void deleteEventTeamsByEventNumber(int event_number);
}