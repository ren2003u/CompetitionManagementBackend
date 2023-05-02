package com.example.demo.service;

import com.example.demo.mapper.TeamInformationMapper;
import com.example.demo.model.TeamInformation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeamInformationService {
    @Autowired
    private TeamInformationMapper teamInformationMapper;

    public List<TeamInformation> findAllTeams() {
        return teamInformationMapper.findAllTeams();
    }

    public TeamInformation findTeamByNumber(int team_number) {
        return teamInformationMapper.findTeamByNumber(team_number);
    }

    public void addTeam(TeamInformation teamInformation) {
        teamInformationMapper.addTeam(teamInformation);
    }

    public void updateTeam(TeamInformation teamInformation) {
        teamInformationMapper.updateTeam(teamInformation);
    }

    public void deleteTeam(int team_number) {
        teamInformationMapper.deleteTeam(team_number);
    }

    public TeamInformation findTeamByName(String team_name) {
        return teamInformationMapper.findTeamByName(team_name);
    }

}
