package com.example.demo.mapper;

import com.example.demo.model.TeamInformation;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TeamInformationMapper {
    List<TeamInformation> findAllTeams();

    TeamInformation findTeamByNumber(int team_number);

    void addTeam(TeamInformation teamInformation);

    void updateTeam(TeamInformation teamInformation);

    void deleteTeam(int team_number);

    TeamInformation findTeamByName(String team_name);

}