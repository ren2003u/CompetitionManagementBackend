package com.example.demo.controller;

import com.example.demo.commom.AjaxResult;
import com.example.demo.model.EventTeam;
import com.example.demo.model.TeamInformation;
import com.example.demo.service.EventTeamService;
import com.example.demo.service.TeamInformationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
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

    @PostMapping("/joinEvent")
    public HashMap<String, Object> captainJoinEvent(int event_number, String team_name)
    {
        int team_number = teamInformationService.findTeamByName(team_name).getTeam_number();
        if(eventTeamService.judgeIfEventTeamExist(event_number,team_number) != null){
            return AjaxResult.fail(-1,"您已加入了这场比赛");
        }
        EventTeam eventTeam = new EventTeam(0,event_number,team_number);
        eventTeamService.addEventTeam(eventTeam);
        return AjaxResult.success(200,"加入成功");
    }

    @PostMapping("/withdrawFromEvent")
    public HashMap<String, Object> captainWithdrawEvent(int event_number, String team_name)
    {
        int team_number = teamInformationService.findTeamByName(team_name).getTeam_number();
        if(eventTeamService.judgeIfEventTeamExist(event_number,team_number) == null){
            return AjaxResult.fail(-1,"您尚未加入该比赛.");
        }
        eventTeamService.deleteEventTeamsByEventNumberAndTeamNumber(event_number,team_number);

        return AjaxResult.success(200,"退出成功.");
    }
}