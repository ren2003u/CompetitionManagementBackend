package com.example.demo.controller;


import com.example.demo.model.PlayerInformation;
import com.example.demo.service.PlayerInformationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@Api(value = "队员控制器", tags = "队员管理接口")
@RestController
@RequestMapping("/playerss")
public class PlayerController {
    @Autowired
    private PlayerInformationService playerInformationService;
    @ApiOperation(value = "根据队伍编号获取对应队员", notes = "返回指定队伍编号的所有参赛团队名称")
    @GetMapping("/byteamId/{teamId}")
    public List<PlayerInformation> getPlayersByTeamId(@PathVariable("teamId") Integer teamId) {
        return playerInformationService.findPlayersByTeamNumber(teamId);
    }

}
