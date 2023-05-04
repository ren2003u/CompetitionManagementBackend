package com.example.demo.controller;


import com.example.demo.model.PlayerInformation;
import com.example.demo.service.PlayerInformationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/player")
public class PlayerController {
    @Autowired
    private PlayerInformationService playerInformationService;

    @GetMapping("/byteamId/{teamId}")
    public List<PlayerInformation> getPlayersByTeamId(int teamId){
        return playerInformationService.findPlayersByTeamNumber(teamId);
    }
}
