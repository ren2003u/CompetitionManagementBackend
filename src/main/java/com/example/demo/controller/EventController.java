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
import org.springframework.http.ResponseEntity;
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
    public String addOrUpdateEvent(@RequestBody EventRequest eventRequest) {
        EventInformation existingEvent = eventInformationService.findEventByName(eventRequest.getEvent_name());

        if (existingEvent != null) {
            // Update event information
            existingEvent.setEvent_name(eventRequest.getEvent_name());
            existingEvent.setEvent_location(eventRequest.getEvent_location());
            existingEvent.setEvent_time(eventRequest.getEvent_time());
            eventInformationService.updateEvent(existingEvent);

            // Update event teams
            eventTeamService.deleteEventTeamsByEventNumber(existingEvent.getEvent_number());
            for (String teamName : eventRequest.getTeam_names()) {
                TeamInformation team = teamInformationService.findTeamByName(teamName);
                if (team != null) {
                    eventTeamService.addEventTeam(new EventTeam(existingEvent.getEvent_number(), team.getTeam_number()));
                } else {
                    return "Team not found: " + teamName;
                }
            }
        } else {
            // Add new event
            EventInformation newEvent = new EventInformation(0,eventRequest.getEvent_name(), eventRequest.getEvent_time(),eventRequest.getEvent_location());
            eventInformationService.addEvent(newEvent);
            int eventNumber = eventInformationService.findEventByName(eventRequest.getEvent_name()).getEvent_number();

            // Add event teams
            for (String teamName : eventRequest.getTeam_names()) {
                TeamInformation team = teamInformationService.findTeamByName(teamName);
                if (team != null) {
                    eventTeamService.addEventTeam(new EventTeam(eventNumber, team.getTeam_number()));
                } else {
                    return "Team not found: " + teamName;
                }
            }
        }
        return "Event information processed successfully";
    }

    // 4. Delete the event information according to the specified event number
    @DeleteMapping("/delete/{event_number}")
    public String deleteEvent(@PathVariable int event_number) {
        eventTeamService.deleteEventTeamsByEventNumber(event_number);
        eventInformationService.deleteEvent(event_number);
        return "Event deleted successfully";
    }
}