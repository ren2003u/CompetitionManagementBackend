package com.example.demo.service;

import com.example.demo.mapper.EventTeamMapper;
import com.example.demo.model.EventTeam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventTeamService {
    @Autowired
    private EventTeamMapper eventTeamMapper;

    public List<EventTeam> findAllEventTeams() {
        return eventTeamMapper.findAllEventTeams();
    }

    public EventTeam findEventTeamById(int id) {
        return eventTeamMapper.findEventTeamById(id);
    }

    public void addEventTeam(EventTeam eventTeam) {
        eventTeamMapper.addEventTeam(eventTeam);
    }

    public void updateEventTeam(EventTeam eventTeam) {
        eventTeamMapper.updateEventTeam(eventTeam);
    }

    public void deleteEventTeam(int id) {
        eventTeamMapper.deleteEventTeam(id);
    }

    public void deleteEventTeamsByEventNumber(int event_number) {
        eventTeamMapper.deleteEventTeamsByEventNumber(event_number);
    }
}