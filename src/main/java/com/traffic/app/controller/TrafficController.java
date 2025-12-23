package com.traffic.app.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.traffic.app.service.SignalService;
import com.traffic.app.model.TrafficStatus;

import java.util.List;

@RestController
public class TrafficController {

    private final SignalService signalService;

    public TrafficController(SignalService signalService) {
        this.signalService = signalService;
    }

    @GetMapping("/status")
    public TrafficStatus getStatus() {
        return new TrafficStatus(signalService.getLights(), signalService.getRoads());
    }
}
