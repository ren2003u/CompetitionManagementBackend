package com.example.demo.mapper;

import com.example.demo.model.PlayerInformation;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PlayerInformationMapper {
    List<PlayerInformation> findAllPlayers();

    PlayerInformation findPlayerById(int id);

    void addPlayer(PlayerInformation playerInformation);

    void updatePlayer(PlayerInformation playerInformation);

    void deletePlayer(int id);

    PlayerInformation findPlayerByName(String name);

    List<PlayerInformation> findPlayersByTeamNumber(int team_number);

    void deletePlayersByTeamNumber(int team_number);
}
