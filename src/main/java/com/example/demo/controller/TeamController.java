package com.example.demo.controller;

import com.example.demo.commom.AjaxResult;
import com.example.demo.model.TeamInformation;
import com.example.demo.service.EventTeamService;
import com.example.demo.service.TeamInformationService;
import com.example.demo.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
        if (teams == null || teams.isEmpty()) {
            return AjaxResult.fail(-1, "当前没有队伍!");
        }
        return AjaxResult.success(teams);
    }
    @ApiOperation(value = "根据团队名称查询", notes = "返回指定团队名称的团队信息")
    @RequestMapping("/byName/{team_name}")
    public HashMap<String, Object> findTeamByName(@PathVariable("team_name") String team_name) {
        TeamInformation team = teamInformationService.findTeamByName(team_name);
        if (team == null) {
            return AjaxResult.fail(-1, "该队伍未找到!");
        }
        return AjaxResult.success(team);
    }
    @ApiOperation(value = "添加或更新团队信息", notes = "根据提供的团队信息添加新团队或更新现有团队")
    @RequestMapping("/addOrUpdate")
    public String addOrUpdateTeam(@RequestBody TeamInformation teamInformation) {
        TeamInformation existingTeam = null;
        if(!(findTeamByName(teamInformation.getTeam_name()).get("data") instanceof String)) {
            existingTeam = (TeamInformation) findTeamByName(teamInformation.getTeam_name()).get("data");
        }

        // Create team information
        TeamInformation teamInfo = new TeamInformation(0,teamInformation.getTeam_name(), teamInformation.getTeam_affiliation_college(),0);

        if (existingTeam != null) {
            teamInfo.setTeam_number(existingTeam.getTeam_number());
            userService.updateByTeamname(existingTeam.getTeam_name(), teamInformation.getTeam_name());
            teamInformationService.updateTeam(teamInfo);
        } else {
            teamInformationService.addTeam(teamInfo);
        }
        return "操作成功";
    }
    @ApiOperation(value = "删除指定团队", notes = "根据团队编号删除指定团队")
    @RequestMapping("/delete/{team_number}")
    public String deleteTeam(@PathVariable("team_number") int team_number) {
        userService.updateByTeamname(teamInformationService.findTeamByNumber(team_number).getTeam_name(),"");
        eventTeamService.deleteEventTeamsByTeamNumber(team_number);
        teamInformationService.deleteTeam(team_number);
        return "删除队伍成功.";
    }

    @ApiOperation(value = "按学院排序", notes = "返回学院按照比分降序排序的结果")
    @RequestMapping("/getRankByTeamCollege")
    public HashMap<String, Object> getRankByTeamCollege() {
        List<TeamInformation> teams = teamInformationService.findAllTeams();
        if (teams == null || teams.isEmpty()) {
            return AjaxResult.fail(-1, "当前没有队伍!");
        }

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

