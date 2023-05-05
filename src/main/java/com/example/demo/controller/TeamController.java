package com.example.demo.controller;

import com.example.demo.commom.AjaxResult;
import com.example.demo.model.PlayerInformation;
import com.example.demo.model.TeamInfoFrontEnd;
import com.example.demo.model.TeamInformation;
import com.example.demo.service.EventTeamService;
import com.example.demo.service.PlayerInformationService;
import com.example.demo.service.TeamInformationService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


@RestController
@RequestMapping("/team")
public class TeamController {
    @Autowired
    private TeamInformationService teamInformationService;

    @Autowired
    private PlayerInformationService playerInformationService;

    @Autowired
    private EventTeamService eventTeamService;

    @Autowired
    private UserService userService;

    @GetMapping("/list")
    public HashMap findAllTeams() {
        List<TeamInformation> teams = teamInformationService.findAllTeams();
        if (teams == null || teams.isEmpty()) {
            return AjaxResult.fail(-1, "No teams found!");
        }
        return AjaxResult.success(teams);
    }

    @GetMapping("/byname/{team_name}")
    public HashMap findTeamByName(@PathVariable String team_name) {
        TeamInformation team = teamInformationService.findTeamByName(team_name);
        if (team == null) {
            return AjaxResult.fail(-1, "Team not found!");
        }
        return AjaxResult.success(team);
    }

    @PostMapping("/addOrUpdate")
    public String addOrUpdateTeam(@RequestBody TeamInformation teamInformation) {
        TeamInformation existingTeam = null;
        if(!(findTeamByName(teamInformation.getTeam_name()).get("data") instanceof String)) {
            existingTeam = (TeamInformation) findTeamByName(teamInformation.getTeam_name()).get("data");
        }

        // Create team information
        TeamInformation teamInfo = new TeamInformation(0,teamInformation.getTeam_name(), teamInformation.getTeam_affiliation_college(),0);

        if (existingTeam != null) {
            teamInfo.setTeam_number(existingTeam.getTeam_number());
            userService.updateByTeamname(existingTeam.getTeam_name(), teamInformation.getTeam_name());
            teamInformationService.updateTeam(teamInfo);
        } else {
            teamInformationService.addTeam(teamInfo);
        }
        return "操作成功";
    }

    @DeleteMapping("/delete/{team_number}")
    public String deleteTeam(@PathVariable("team_number") int team_number) {
        userService.updateByTeamname(teamInformationService.findTeamByNumber(team_number).getTeam_name(),"");
        eventTeamService.deleteEventTeamsByTeamNumber(team_number);
        teamInformationService.deleteTeam(team_number);
        return "删除队伍成功.";
    }
}

