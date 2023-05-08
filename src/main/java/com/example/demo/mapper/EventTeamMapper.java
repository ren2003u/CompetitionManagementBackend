package com.example.demo.mapper;


import com.example.demo.model.EventTeam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface EventTeamMapper {
    List<EventTeam> findAllEventTeams();
    EventTeam findEventTeamById(int id);
    void addEventTeam(EventTeam eventTeam);
    void updateEventTeam(EventTeam eventTeam);
    void deleteEventTeam(int id);
    void deleteEventTeamsByEventNumber(int event_number);

    void deleteEventTeamsByTeamNumber(int team_number);

    List<EventTeam> getEventTeamsByEventNumber(@Param("eventNumber")int eventNumber);

    EventTeam judgeIfEventTeamExist(@Param("event_number")int event_number,@Param("team_number")int team_number);

    void deleteEventTeamsByEventNumberAndTeamNumber(@Param("event_number")int event_number,@Param("team_number")int team_number);
}