package com.traffic.app.service;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import com.traffic.app.model.TrafficLight;
import com.traffic.app.model.Road;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class SignalService {

    private final List<TrafficLight> lights = new ArrayList<>();
    private final List<Road> roads = new ArrayList<>();
    private final Random random = new Random();

    @PostConstruct
    public void init() {
        // 4 traffic lights
        for (int i = 0; i < 4; i++) {
            lights.add(new TrafficLight(TrafficLight.Color.RED));
        }

        roads.add(new Road("North"));
        roads.add(new Road("South"));
        roads.add(new Road("East"));
        roads.add(new Road("West"));

        startSignalCycle();
        startVehicleGeneration();
    }

    /* ---------------- SIGNAL CYCLE ---------------- */

    private void startSignalCycle() {
        Thread signalThread = new Thread(() -> {
            while (true) {
                for (int i = 0; i < lights.size(); i++) {
                    setGreen(i);
                    processGreenTraffic(i);
                    sleepSeconds(10);

                    setYellow(i);
                    sleepSeconds(2);

                    setRed(i);
                }
            }
        });
        signalThread.setDaemon(true);
        signalThread.start();
    }

    private void setGreen(int index) {
        resetAllLights();
        lights.get(index).setColor(TrafficLight.Color.GREEN);
    }

    private void setYellow(int index) {
        lights.get(index).setColor(TrafficLight.Color.YELLOW);
    }

    private void setRed(int index) {
        lights.get(index).setColor(TrafficLight.Color.RED);
    }

    private void resetAllLights() {
        for (TrafficLight light : lights) {
            light.setColor(TrafficLight.Color.RED);
        }
    }

    /* ---------------- VEHICLE LOGIC ---------------- */

    // Generates vehicles on ALL roads
    private void startVehicleGeneration() {
        Thread vehicleThread = new Thread(() -> {
            while (true) {
                for (Road road : roads) {
                    int incoming = random.nextInt(2); // 0–2 vehicles
                    road.addVehicles(incoming);
                }
                sleepSeconds(2);
            }
        });
        vehicleThread.setDaemon(true);
        vehicleThread.start();
    }

    // Removes vehicles when signal is GREEN
    private void processGreenTraffic(int index) {
        Thread greenFlowThread = new Thread(() -> {
            for (int i = 0; i < 10; i++) { // GREEN duration
                int passing = 1 + random.nextInt(3); // 1–3 vehicles pass
                roads.get(index).removeVehicles(passing);
                sleepSeconds(1);
            }
        });
        greenFlowThread.setDaemon(true);
        greenFlowThread.start();
    }

    private void sleepSeconds(int seconds) {
        try {
            Thread.sleep(seconds * 1000L);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /* ---------------- GETTERS ---------------- */

    public List<TrafficLight> getLights() {
        return lights;
    }

    public List<Road> getRoads() {
        return roads;
    }
}
