package com.example.demo.controller;

import com.example.demo.commom.AjaxResult;
import com.example.demo.model.EventTeam;
import com.example.demo.model.TeamInformation;
import com.example.demo.service.EventTeamService;
import com.example.demo.service.TeamInformationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
@Api(tags = "赛事团队控制器")
@RestController
@CrossOrigin(origins = "http://localhost:9528")
@RequestMapping("/event-team")
public class EventTeamController {

    @Autowired
    private EventTeamService eventTeamService;

    @Autowired
    private TeamInformationService teamInformationService;
    @ApiOperation(value = "根据赛事编号获取参赛团队", notes = "返回指定赛事编号的所有参赛团队名称")
    @RequestMapping("/event/{eventNumber}")
    public List<String> getEventTeamsByEventNumber(@PathVariable("eventNumber") int eventNumber) {
        List<EventTeam> eventTeams = eventTeamService.getEventTeamsByEventNumber(eventNumber);
        List<String> teamNames = new ArrayList<>();

        for (EventTeam eventTeam : eventTeams) {
            TeamInformation teamInformation = teamInformationService.findTeamByNumber(eventTeam.getTeam_number());
            teamNames.add(teamInformation.getTeam_name());
        }

        return teamNames;
    }
    @ApiOperation(value = "团队加入赛事", notes = "队长将团队加入指定赛事")
    @RequestMapping("/joinEvent")
    public HashMap<String, Object> captainJoinEvent(@RequestBody int event_number, String team_name)
    {
        int team_number = teamInformationService.findTeamByName(team_name).getTeam_number();
        if(eventTeamService.judgeIfEventTeamExist(event_number,team_number) != null){
            return AjaxResult.fail(-1,"您已加入了这场比赛");
        }
        EventTeam eventTeam = new EventTeam(0,event_number,team_number);
        eventTeamService.addEventTeam(eventTeam);
        return AjaxResult.success(200,"加入成功");
    }
    @ApiOperation(value = "团队退出赛事", notes = "队长将团队从指定赛事中退出")
    @RequestMapping("/withdrawFromEvent")
    public HashMap<String, Object> captainWithdrawEvent(@RequestBody int event_number, String team_name)
    {
        int team_number = teamInformationService.findTeamByName(team_name).getTeam_number();
        if(eventTeamService.judgeIfEventTeamExist(event_number,team_number) == null){
            return AjaxResult.fail(-1,"您尚未加入该比赛.");
        }
        eventTeamService.deleteEventTeamsByEventNumberAndTeamNumber(event_number,team_number);

        return AjaxResult.success(200,"退出成功.");
    }
}