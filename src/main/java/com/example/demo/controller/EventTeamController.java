package com.example.demo.controller;

import com.example.demo.model.EventTeam;
import com.example.demo.model.TeamInformation;
import com.example.demo.service.EventTeamService;
import com.example.demo.service.TeamInformationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/event-team")
public class EventTeamController {

    @Autowired
    private EventTeamService eventTeamService;

    @Autowired
    private TeamInformationService teamInformationService;

    @GetMapping("/event/{eventNumber}")
    public List<String> getEventTeamsByEventNumber(@PathVariable("eventNumber") int eventNumber) {
        List<EventTeam> eventTeams = eventTeamService.getEventTeamsByEventNumber(eventNumber);
        List<String> teamNames = new ArrayList<>();

        for (EventTeam eventTeam : eventTeams) {
            TeamInformation teamInformation = teamInformationService.findTeamByNumber(eventTeam.getTeam_number());
            teamNames.add(teamInformation.getTeam_name());
        }

        return teamNames;
    }
}