package com.traffic.app.controller;

import com.traffic.app.service.SignalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/status") // keeping base path
public class TrafficController {

    @Autowired
    private SignalService signalService;

    @GetMapping
    public Map<String, Object> getStatus() {
        Map<String, Object> response = new HashMap<>();
        response.put("lights", signalService.getLights());
        response.put("roads", signalService.getRoads());
        return response;
    }

    @PostMapping("/emergency/{roadId}")
    public String triggerEmergency(@PathVariable int roadId) {
        signalService.triggerEmergency(roadId);
        return "Emergency Triggered for Road " + roadId;
    }
}