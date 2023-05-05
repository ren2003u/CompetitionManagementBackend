package com.example.demo.controller;


import com.example.demo.commom.AjaxResult;
import com.example.demo.model.EventInformation;
import com.example.demo.model.EventRequest;
import com.example.demo.model.EventTeam;
import com.example.demo.model.TeamInformation;
import com.example.demo.service.EventInformationService;
import com.example.demo.service.EventTeamService;
import com.example.demo.service.TeamInformationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/events")
public class EventController {

    @Autowired
    private EventInformationService eventInformationService;

    @Autowired
    private EventTeamService eventTeamService;

    @Autowired
    private TeamInformationService teamInformationService;

    // 1. Returns information about all events currently in the database
    @GetMapping("/list")
    public List<EventInformation> getAllEvents() {
        return eventInformationService.findAllEvents();
    }

    // 2. Look up event information for a specific event name
    @GetMapping("/byname/{event_name}")
    public HashMap getEventByName(@PathVariable String event_name) {
        //todo:返回值不完整
        EventInformation event = eventInformationService.findEventByName(event_name);
        if (event == null) {
            return AjaxResult.fail(-1, "Event not found!");
        }
        return AjaxResult.success(event);
    }

    // 3. Modify or add event information
    @PostMapping("/addOrUpdate")
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
            EventInformation newEvent = new EventInformation(0,eventInformation.getEvent_name(), eventInformation.getEvent_time(),eventInformation.getEvent_location());
            eventInformationService.addEvent(newEvent);
        }
        return AjaxResult.success(200,"Event information processed successfully");
    }

    // 4. Delete the event information according to the specified event number
    @DeleteMapping("/delete/{event_number}")
    public String deleteEvent(@PathVariable int event_number) {
        eventTeamService.deleteEventTeamsByEventNumber(event_number);
        eventInformationService.deleteEvent(event_number);
        return "删除赛事信息成功.";
    }


}