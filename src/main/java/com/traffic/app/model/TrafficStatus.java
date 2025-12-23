package com.traffic.app.model;

import java.util.List;

public class TrafficStatus {
    private List<TrafficLight> lights;
    private List<Road> roads;

    public TrafficStatus(List<TrafficLight> lights, List<Road> roads) {
        this.lights = lights;
        this.roads = roads;
    }

    public List<TrafficLight> getLights() { return lights; }
    public List<Road> getRoads() { return roads; }
}
