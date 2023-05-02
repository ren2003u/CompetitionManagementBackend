package com.example.demo.controller;

import com.example.demo.model.PlayerInformation;
import com.example.demo.model.TeamInfoFrontEnd;
import com.example.demo.model.TeamInformation;
import com.example.demo.service.PlayerInformationService;
import com.example.demo.service.TeamInformationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;



@RestController
@RequestMapping("/team")
public class TeamController {
    @Autowired
    private TeamInformationService teamInformationService;

    @Autowired
    private PlayerInformationService playerInformationService;

    @GetMapping("/list")
    public List<TeamInformation> findAllTeams() {
        return teamInformationService.findAllTeams();
    }

    @GetMapping("/byname")
    public TeamInformation findTeamByName(@RequestParam("team_name") String team_name) {
        return teamInformationService.findTeamByName(team_name);
    }

    @PostMapping("/addOrUpdate")
    public String addOrUpdateTeam(@RequestBody TeamInfoFrontEnd teamInfoFrontEnd) {
        TeamInformation existingTeam = findTeamByName(teamInfoFrontEnd.getTeam_name());

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
            existingTeam = findTeamByName(teamInfo.getTeam_name());
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

