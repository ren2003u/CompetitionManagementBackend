package com.example.demo.controller;

import com.example.demo.commom.AjaxResult;
import com.example.demo.model.TeamInformation;
import com.example.demo.service.EventTeamService;
import com.example.demo.service.TeamInformationService;
import com.example.demo.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.Map.Entry;

@Api(tags = "团队控制器")
@RestController
//@CrossOrigin(origins = "http://localhost:9528")
@RequestMapping("/team")
public class TeamController {
    @Autowired
    private TeamInformationService teamInformationService;

    @Autowired
    private EventTeamService eventTeamService;

    @Autowired
    private UserService userService;
    @ApiOperation(value = "查询所有团队", notes = "返回数据库中所有团队的信息")
    @RequestMapping("/list")
    public HashMap<String, Object> findAllTeams() {
        List<TeamInformation> teams = teamInformationService.findAllTeams();
        return AjaxResult.success(teams);
    }
    @ApiOperation(value = "根据团队名称查询", notes = "返回指定团队名称的团队信息")
    @RequestMapping("/byName/{team_name}")
    public HashMap<String, Object> findTeamByName(@PathVariable("team_name") String team_name) {
        if(StringUtils.isBlank(team_name)){
            return AjaxResult.fail(-1,"传输的队伍名称非法");
        }
        TeamInformation team = teamInformationService.findTeamByName(team_name);
        if (team == null) {
            return AjaxResult.fail(-1, "该队伍未找到!");
        }
        return AjaxResult.success(team);
    }
//    @ApiOperation(value = "添加或更新团队信息", notes = "根据提供的团队信息添加新团队或更新现有团队")
//    @RequestMapping("/addOrUpdate")
//    public HashMap<String, Object> addOrUpdateTeam(@RequestBody TeamInformation teamInformation) {
//        if(StringUtils.isBlank(teamInformation.getTeam_name()) || StringUtils.isBlank(teamInformation.getTeam_affiliation_college())){
//            return AjaxResult.fail(-1,"传输的团队信息不完全或含有非法信息");
//        }
//        TeamInformation existingTeam = null;
//        if(!(findTeamByName(teamInformation.getTeam_name()).get("data") instanceof String)) {
//            existingTeam = (TeamInformation) findTeamByName(teamInformation.getTeam_name()).get("data");
//        }
//
//        // Create team information
//        TeamInformation teamInfo = new TeamInformation(0,teamInformation.getTeam_name(), teamInformation.getTeam_affiliation_college(),0);
//
//        if (existingTeam != null) {
//            teamInfo.setTeam_number(existingTeam.getTeam_number());
//            userService.updateByTeamname(existingTeam.getTeam_name(), teamInformation.getTeam_name());
//            teamInformationService.updateTeam(teamInfo);
//        } else {
//            teamInformationService.addTeam(teamInfo);
//        }
//        return AjaxResult.success(200,"操作成功");
//    }
    @ApiOperation(value = "添加团队信息", notes = "根据提供的团队信息添加新团队")
    @RequestMapping("/addTeam")
    public HashMap<String, Object> addTeam(@RequestBody TeamInformation teamInformation) {
        if(StringUtils.isBlank(teamInformation.getTeam_name()) || StringUtils.isBlank(teamInformation.getTeam_affiliation_college())){
            return AjaxResult.fail(-1,"传输的团队信息不完全或含有非法信息");
        }
        TeamInformation existingTeam = null;
        if(teamInformationService.findTeamByName(teamInformation.getTeam_name()) != null){
            return AjaxResult.fail(-1,"你想要创建的队伍已经存在");
        }
        // Create team information
        TeamInformation teamInfo = new TeamInformation(0,teamInformation.getTeam_name(), teamInformation.getTeam_affiliation_college(),0);
            teamInformationService.addTeam(teamInfo);
        return AjaxResult.success(200,"操作成功");
    }

    @ApiOperation(value = "更新团队信息", notes = "根据提供的团队信息更新现有团队")
    @RequestMapping("/updateTeam")
    public HashMap<String, Object> updateTeam(@RequestBody TeamInformation teamInformation) {
        if(teamInformationService.findTeamByNumber(teamInformation.getTeam_number()) == null){
            return AjaxResult.fail(-1,"传输的团队编号查找不到对应团队");
        }
        if(StringUtils.isBlank(teamInformation.getTeam_name()) || StringUtils.isBlank(teamInformation.getTeam_affiliation_college())){
            return AjaxResult.fail(-1,"传输的团队信息不完全或含有非法信息");
        }
        if(!Objects.equals(teamInformation.getTeam_name(), teamInformationService.findTeamByNumber(teamInformation.getTeam_number()).getTeam_name())){
            if(teamInformationService.findTeamByName(teamInformation.getTeam_name()) != null){
                return AjaxResult.fail(-1,"要更新的团队名称已经被占用");
            }
        }
        String oriTeamName = teamInformationService.findTeamByNumber(teamInformation.getTeam_number()).getTeam_name();
            userService.updateByTeamname(oriTeamName, teamInformation.getTeam_name());
            teamInformationService.updateTeam(teamInformation);
        return AjaxResult.success(200,"操作成功");
    }
    @ApiOperation(value = "删除指定团队", notes = "根据团队编号删除指定团队")
    @RequestMapping("/delete/{team_number}")
    public HashMap<String, Object> deleteTeam(@PathVariable("team_number") int team_number) {
        if(team_number < 0){
            return AjaxResult.fail(-1,"传输的队伍编号非法");
        }
        if(teamInformationService.findTeamByNumber(team_number) == null){
            return AjaxResult.fail(-1,"传输的团队编号查找不到对应团队");
        }
        userService.updateByTeamname(teamInformationService.findTeamByNumber(team_number).getTeam_name(),"");
        eventTeamService.deleteEventTeamsByTeamNumber(team_number);
        teamInformationService.deleteTeam(team_number);
        return AjaxResult.success(200,"操作成功");
    }

    @ApiOperation(value = "按学院排序", notes = "返回学院按照比分降序排序的结果")
    @RequestMapping("/getRankByTeamCollege")
    public HashMap<String, Object> getRankByTeamCollege() {
        List<TeamInformation> teams = teamInformationService.findAllTeams();

        Map<String, Integer> collegeAndScoreMap = new HashMap<>();
        for (TeamInformation teamInformation : teams) {
            String college = teamInformation.getTeam_affiliation_college();
            int totalScore = teamInformation.getTotal_score();
            collegeAndScoreMap.put(college, collegeAndScoreMap.getOrDefault(college, 0) + totalScore);
        }

        List<Entry<String, Integer>> collegeAndScoreList = new ArrayList<>(collegeAndScoreMap.entrySet());
        collegeAndScoreList.sort(Comparator.comparingInt(Entry<String, Integer>::getValue).reversed());

        return AjaxResult.success(collegeAndScoreList);
    }
}

