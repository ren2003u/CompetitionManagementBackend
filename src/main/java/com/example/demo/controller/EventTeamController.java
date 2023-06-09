package com.example.demo.controller;

import com.example.demo.commom.AjaxResult;
import com.example.demo.model.CaptainJoinEventRequest;
import com.example.demo.model.EventTeam;
import com.example.demo.model.TeamInformation;
import com.example.demo.service.EventTeamService;
import com.example.demo.service.TeamInformationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
@Api(tags = "赛事团队控制器")
@RestController
//@CrossOrigin(origins = "http://localhost:9528")
@RequestMapping("/event-team")
public class EventTeamController {

    @Autowired
    private EventTeamService eventTeamService;

    @Autowired
    private TeamInformationService teamInformationService;
    @ApiOperation(value = "根据赛事编号获取参赛团队", notes = "返回指定赛事编号的所有参赛团队名称")
    @RequestMapping("/event/{eventNumber}")
    public HashMap<String, Object> getEventTeamsByEventNumber(@PathVariable("eventNumber") int eventNumber) {
        if(eventNumber < 0){
            return AjaxResult.fail(-1,"传输的赛事编号非法");
        }
        List<EventTeam> eventTeams = eventTeamService.getEventTeamsByEventNumber(eventNumber);
        List<String> teamNames = new ArrayList<>();

        for (EventTeam eventTeam : eventTeams) {
            TeamInformation teamInformation = teamInformationService.findTeamByNumber(eventTeam.getTeam_number());
            teamNames.add(teamInformation.getTeam_name());
        }

        return AjaxResult.success(teamNames);
    }
    @ApiOperation(value = "团队加入赛事", notes = "队长将团队加入指定赛事")
    @RequestMapping("/joinEvent")
    public HashMap<String, Object> captainJoinEvent(@RequestBody CaptainJoinEventRequest request)
    {
        if(request.getEvent_number()<0){
            return AjaxResult.fail(-1,"传输的赛事编号非法");
        }
        if(StringUtils.isBlank(request.getTeam_name())){
            return AjaxResult.fail(-1,"传输的队伍名称非法");
        }
        int event_number = request.getEvent_number();
        String team_name = request.getTeam_name();
        if(teamInformationService.findTeamByName(team_name)==null){
            return AjaxResult.fail(-1,"传输的队伍信息没有队伍与其对应");
        }
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
    public HashMap<String, Object> captainWithdrawEvent(@RequestParam("event_number") int event_number, @RequestParam("team_name")String team_name)
    {
        if(event_number < 0){
            return AjaxResult.fail(-1,"传输的赛事编号非法");
        }
        if(StringUtils.isBlank(team_name)){
            return AjaxResult.fail(-1,"传输的队伍名称非法");
        }
        if(teamInformationService.findTeamByName(team_name)==null){
            return AjaxResult.fail(-1,"传输的队伍信息没有队伍与其对应");
        }
        int team_number = teamInformationService.findTeamByName(team_name).getTeam_number();
        if(eventTeamService.judgeIfEventTeamExist(event_number,team_number) == null){
            return AjaxResult.fail(-1,"您尚未加入该比赛.");
        }
        eventTeamService.deleteEventTeamsByEventNumberAndTeamNumber(event_number,team_number);

        return AjaxResult.success(200,"退出成功.");
    }
}