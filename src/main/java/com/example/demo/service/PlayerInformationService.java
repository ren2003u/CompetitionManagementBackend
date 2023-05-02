package com.example.demo.service;

import com.example.demo.mapper.PlayerInformationMapper;
import com.example.demo.model.PlayerInformation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlayerInformationService {
    @Autowired
    private PlayerInformationMapper playerInformationMapper;

    public List<PlayerInformation> findAllPlayers() {
        return playerInformationMapper.findAllPlayers();
    }

    public PlayerInformation findPlayerById(int id) {
        return playerInformationMapper.findPlayerById(id);
    }

    public void addPlayer(PlayerInformation playerInformation) {
        playerInformationMapper.addPlayer(playerInformation);
    }

    public void updatePlayer(PlayerInformation playerInformation) {
        playerInformationMapper.updatePlayer(playerInformation);
    }

    public void deletePlayer(int id) {
        playerInformationMapper.deletePlayer(id);
    }

    public PlayerInformation findPlayerByName(String name) {
        return playerInformationMapper.findPlayerByName(name);
    }

    public List<PlayerInformation> findPlayersByTeamNumber(int team_number) {
        return playerInformationMapper.findPlayersByTeamNumber(team_number);
    }

    public void deletePlayersByTeamNumber(int team_number) {
        playerInformationMapper.deletePlayersByTeamNumber(team_number);
    }
}
