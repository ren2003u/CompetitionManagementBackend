package com.example.demo.controller;


import com.example.demo.commom.AjaxResult;
import com.example.demo.commom.SessionUtil;
import com.example.demo.model.User;
import com.example.demo.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
@Api(tags = "队员控制器")
@RestController
@CrossOrigin(origins = "http://localhost:9528")
@RequestMapping("/player")
public class TeamUserController {
    @Autowired
    private UserService userService;
    @ApiOperation(value = "队员加入团队", notes = "根据提供的团队名称和用户名，将队员加入到团队中")
    @RequestMapping("/joinIntoTeam")
    public HashMap<String, Object> UserJoinIntoTeam(String team_name, String username, HttpServletRequest httpServletRequest){
        if(!Objects.equals(userService.findByUsername(username).getTeam_name(), "")){
            return AjaxResult.fail(-1,"您已加入其他队伍，无法加入新队伍.");
        }
        List<User> currentUsers =  userService.findByTeamname(team_name);
        User user = SessionUtil.getLoginUser(httpServletRequest);
        if (user == null) {
            return AjaxResult.fail(-1, "用户未登录");
        }
        User dbUser = userService.findByUsername(user.getUsername());
        if (dbUser == null) {
            return AjaxResult.fail(-1, "用户非法登录");
        }
        String currentStatus = dbUser.getStatus();
        int teamMemberCount = 0;
        for (User currentUser : currentUsers) {
            if (Objects.equals(currentUser.getStatus(), "player")) {
                teamMemberCount++;
            }
            if (Objects.equals(currentStatus, currentUser.getStatus()) && Objects.equals(currentStatus, "captain")) {
                return AjaxResult.fail(-1, "该队伍已经有队长了.");
            }
        }
        if(teamMemberCount < 2){
            userService.updateByTeamname("",team_name);
        }else {
            return AjaxResult.fail(-1,"该队伍已经满人.");
        }
        return AjaxResult.success(200,"加入成功！");
    }
    @ApiOperation(value = "队员退出团队", notes = "根据提供的团队名称和用户名，队员退出团队")
    @RequestMapping("/withdrawFromTeam")
    public HashMap<String, Object> userWithdrawFormTeam(String team_name, String username, HttpServletRequest httpServletRequest){
        User user = SessionUtil.getLoginUser(httpServletRequest);
        if (user == null) {
            return AjaxResult.fail(-1, "用户未登录");
        }
        User dbUser = userService.findByUsername(user.getUsername());
        if (dbUser == null) {
            return AjaxResult.fail(-1, "用户非法登录");
        }
        if(Objects.equals(userService.findByUsername(username).getTeam_name(), team_name)){
            return AjaxResult.fail(-1,"您尚未加入该队伍.");
        }
        userService.updateByTeamname(userService.findByUsername(username).getTeam_name(),"");
        return AjaxResult.success(200,"退出队伍成功！");
    }
    @ApiOperation(value = "队长移除队员", notes = "根据提供的团队名称和队员名称，队长从团队中移除队员")
    @RequestMapping("/captainDeletePlayer")
    public HashMap<String, Object> captainDeletePlayer(String team_name, String playerName, HttpServletRequest httpServletRequest){
        User user = SessionUtil.getLoginUser(httpServletRequest);
        if (user == null) {
            return AjaxResult.fail(-1, "用户未登录");
        }
        User dbUser = userService.findByUsername(user.getUsername());
        if (dbUser == null) {
            return AjaxResult.fail(-1, "用户非法登录");
        }

        String currentUserStatus = dbUser.getStatus();
        if(!Objects.equals(currentUserStatus, "captain")){
            return AjaxResult.fail(-1,"您没有权限删除队员.");
        }
        String currentUserTeamName = dbUser.getTeam_name();
        if(!Objects.equals(currentUserTeamName, userService.findByUsername(playerName).getTeam_name())){
            return AjaxResult.fail(-1,"您没有权限删除其他队的队员.");
        }
        userService.updateByTeamname(userService.findByUsername(playerName).getTeam_name(),"");
        return AjaxResult.success(200,"删除成功");
    }
}
