package com.example.demo.controller;

import com.example.demo.commom.AjaxResult;
import com.example.demo.model.PlayerInformation;
import com.example.demo.model.TeamInfoFrontEnd;
import com.example.demo.model.TeamInformation;
import com.example.demo.service.PlayerInformationService;
import com.example.demo.service.TeamInformationService;
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

    @GetMapping("/list")
    public HashMap findAllTeams() {
        List<TeamInformation> teams = teamInformationService.findAllTeams();
        if (teams == null || teams.isEmpty()) {
            return AjaxResult.fail(-1, "No teams found!");
        }
        return AjaxResult.success(teams);
    }

    @GetMapping("/byname")
    public HashMap findTeamByName(@RequestParam("team_name") String team_name) {
        TeamInformation team = teamInformationService.findTeamByName(team_name);
        if (team == null) {
            return AjaxResult.fail(-1, "Team not found!");
        }
        return AjaxResult.success(team);
    }

    @PostMapping("/addOrUpdate")
    public String addOrUpdateTeam(@RequestBody TeamInfoFrontEnd teamInfoFrontEnd) {
        TeamInformation existingTeam = (TeamInformation) findTeamByName(teamInfoFrontEnd.getTeam_name()).get("data");

        // Calculate team's total score and assists
        int teamTotalScore = teamInfoFrontEnd.getCaptain_total_score() + teamInfoFrontEnd.getPlayer_1_total_score() + teamInfoFrontEnd.getPlayer_2_total_score();
        int teamTotalAssists = teamInfoFrontEnd.getCaptain_total_assists() + teamInfoFrontEnd.getPlayer_1_total_assists() + teamInfoFrontEnd.getPlayer_2_total_assists();

        // Create team information
        TeamInformation teamInfo = new TeamInformation(teamInfoFrontEnd.getTeam_name(), teamInfoFrontEnd.getTeam_affiliation_college(), teamInfoFrontEnd.getCaptain_name(), teamInfoFrontEnd.getPlayer_1_name(), teamInfoFrontEnd.getPlayer_2_name(), teamTotalScore, teamTotalAssists);

        if (existingTeam != null) {
            teamInfo.setTeam_number(existingTeam.getTeam_number());
            teamInformationService.updateTeam(teamInfo);
        } else {
            teamInformationService.addTeam(teamInfo);
            existingTeam = (TeamInformation) findTeamByName(teamInfo.getTeam_name()).get("data");
        }

        List<PlayerInformation> players = new ArrayList<>();
        players.add(new PlayerInformation(teamInfoFrontEnd.getCaptain_name(), teamInfoFrontEnd.getCaptain_gender(), existingTeam.getTeam_number(), teamInfoFrontEnd.getCaptain_total_score(), teamInfoFrontEnd.getCaptain_total_assists()));
        players.add(new PlayerInformation(teamInfoFrontEnd.getPlayer_1_name(), teamInfoFrontEnd.getPlayer_1_gender(), existingTeam.getTeam_number(), teamInfoFrontEnd.getPlayer_1_total_score(), teamInfoFrontEnd.getPlayer_1_total_assists()));
        players.add(new PlayerInformation(teamInfoFrontEnd.getPlayer_2_name(), teamInfoFrontEnd.getPlayer_2_gender(), existingTeam.getTeam_number(), teamInfoFrontEnd.getPlayer_2_total_score(), teamInfoFrontEnd.getPlayer_2_total_assists()));

        for (PlayerInformation player : players) {
            PlayerInformation existingPlayer = playerInformationService.findPlayerByName(player.getName());
            if (existingPlayer != null) {
                player.setId(existingPlayer.getId());
                playerInformationService.updatePlayer(player);
            } else {
                playerInformationService.addPlayer(player);
            }
        }

        return "Success";
    }

    @DeleteMapping("/delete/{team_number}")
    public String deleteTeam(@PathVariable("team_number") int team_number) {
        playerInformationService.deletePlayersByTeamNumber(team_number);
        teamInformationService.deleteTeam(team_number);
        return "Deleted";
    }
}

