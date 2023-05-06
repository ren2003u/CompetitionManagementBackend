package com.example.demo.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import com.example.demo.commom.AjaxResult;
import com.example.demo.model.EventInformation;
import com.example.demo.service.EventInformationService;
import com.example.demo.service.EventTeamService;
import com.example.demo.service.TeamInformationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;



@RestController
@CrossOrigin(origins = "http://localhost:8080")
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
    @GetMapping("/list")
    @ApiOperation(value = "获取所有活动信息", notes = "返回数据库中所有活动的信息")
    public List<EventInformation> getAllEvents() {
        return eventInformationService.findAllEvents();
    }

    // 2. Look up event information for a specific event name
    @GetMapping("/byname/{event_name}")
    @ApiOperation(value = "根据活动名称查询活动信息", notes = "根据给定的活动名称查找活动信息")
    public HashMap getEventByName(@PathVariable String event_name) {
        //todo: The return value is incomplete
        EventInformation event = eventInformationService.findEventByName(event_name);
        if (event == null) {
            return AjaxResult.fail(-1, "Event not found!");
        }
        return AjaxResult.success(event);
    }

    // 3. Modify or add event information
    @PostMapping("/addOrUpdate")
    @ApiOperation(value = "添加或更新活动信息", notes = "根据给定的活动信息添加新活动或更新已有活动信息")
    public HashMap<String, Object> addOrUpdateEvent(@RequestBody EventInformation eventInformation) {
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
        return AjaxResult.success(200, "Event information processed successfully");
    }

    // 4. Delete the event information according to the specified event number
    @DeleteMapping("/delete/{event_number}")
    @ApiOperation(value = "根据活动编号删除活动信息", notes = "根据指定的活动编号删除活动信息")
    public String deleteEvent(@PathVariable int event_number) {
        eventTeamService.deleteEventTeamsByEventNumber(event_number);
        eventInformationService.deleteEvent(event_number);
        return "Deleted event information successfully.";
    }
}
