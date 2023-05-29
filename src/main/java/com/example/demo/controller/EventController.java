package com.example.demo.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import com.example.demo.commom.AjaxResult;
import com.example.demo.model.EventInformation;
import com.example.demo.service.EventInformationService;
import com.example.demo.service.EventTeamService;
import com.example.demo.service.TeamInformationService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;



@RestController
//@CrossOrigin(origins = "http://localhost:9528")
@RequestMapping("/events")
@Api(value = "活动控制器", tags = "活动管理接口")
public class EventController {

    @Autowired
    private EventInformationService eventInformationService;

    @Autowired
    private EventTeamService eventTeamService;

    @Autowired
    private TeamInformationService teamInformationService;

    // 1. Returns information about all events currently in the database
    @RequestMapping("/list")
    @ApiOperation(value = "获取所有活动信息", notes = "返回数据库中所有活动的信息")
    public List<EventInformation> getAllEvents() {
        return eventInformationService.findAllEvents();
    }

    // 2. Look up event information for a specific event name
    @RequestMapping("/byname/{event_name}")
    @ApiOperation(value = "根据活动名称查询活动信息", notes = "根据给定的活动名称查找活动信息")
    public HashMap<String, Object> getEventByName(@PathVariable("event_name") String event_name) {
        //todo: The return value is incomplete
        if(StringUtils.isBlank(event_name)){
            return AjaxResult.fail(-1,"传输的活动名称非法");
        }
        EventInformation event = eventInformationService.findEventByName(event_name);
        if (event == null) {
            return AjaxResult.fail(-1, "活动未找到");
        }
        return AjaxResult.success(event);
    }

    // 3. Modify or add event information
    @RequestMapping("/addOrUpdate")
    @ApiOperation(value = "添加或更新活动信息", notes = "根据给定的活动信息添加新活动或更新已有活动信息")
    public HashMap<String, Object> addOrUpdateEvent(@RequestBody EventInformation eventInformation) {
        if(StringUtils.isBlank(eventInformation.getEvent_location()) || StringUtils.isBlank(eventInformation.getEvent_name()) || StringUtils.isBlank(eventInformation.getEvent_time())){
            return AjaxResult.fail(-1,"传输的活动信息不完整或含有非法信息");
        }
        EventInformation existingEvent = eventInformationService.findEventByName(eventInformation.getEvent_name());
        if (existingEvent != null) {
            // Update event information
            existingEvent.setEvent_name(existingEvent.getEvent_name());
            existingEvent.setEvent_location(eventInformation.getEvent_location());
            existingEvent.setEvent_time(eventInformation.getEvent_time());
            eventInformationService.updateEvent(existingEvent);
        } else {
            // Add new event
            EventInformation newEvent = new EventInformation(0, eventInformation.getEvent_name(), eventInformation.getEvent_time(), eventInformation.getEvent_location());
            eventInformationService.addEvent(newEvent);
        }
        return AjaxResult.success(200, "赛事信息操作成功");
    }

    // 4. Delete the event information according to the specified event number
    @RequestMapping("/delete/{event_number}")
    @ApiOperation(value = "根据活动编号删除活动信息", notes = "根据指定的活动编号删除活动信息")
    public HashMap<String, Object> deleteEvent(@PathVariable("event_number") int event_number) {
        if(event_number < 0){
            return AjaxResult.fail(-1,"传输的赛事编号非法");
        }
        eventTeamService.deleteEventTeamsByEventNumber(event_number);
        eventInformationService.deleteEvent(event_number);
        return AjaxResult.success(200,"删除赛事成功");
    }
}
