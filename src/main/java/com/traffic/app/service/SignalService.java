package com.traffic.app.service;

import com.traffic.app.model.Road;
import com.traffic.app.model.TrafficLight;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class SignalService {

    private final List<TrafficLight> lights = new ArrayList<>();
    private final List<Road> roads = new ArrayList<>();
    private final Random random = new Random();

    // Multithreading Control Objects
    private final Object lock = new Object();
    private volatile boolean isEmergencyActive = false;
    private Thread normalCycleThread;
    private Thread emergencyThread;

    @PostConstruct
    public void init() {
        // Initialize Lights & Roads
        for (int i = 0; i < 4; i++) lights.add(new TrafficLight(TrafficLight.Color.RED));
        roads.add(new Road("North")); // 0
        roads.add(new Road("South")); // 1
        roads.add(new Road("East"));  // 2
        roads.add(new Road("West"));  // 3

        // Start Traffic Simulation Threads
        startVehicleGeneration();
        startNormalCycle();
    }

    /* ---------------- 1. THREAD CREATION & NORMAL CYCLE ---------------- */
    private void startNormalCycle() {
        normalCycleThread = new Thread(() -> {
            int[] rotationOrder = {1, 3, 0, 2}; // South -> West -> North -> East
            
            while (true) {
                for (int activeIndex : rotationOrder) {
                    // SYNCHRONIZATION: Check if we need to pause for emergency
                    synchronized (lock) {
                        while (isEmergencyActive) {
                            try {
                                System.out.println("Normal Cycle: WAITING for Emergency...");
                                lock.wait(); // Inter-Thread Communication: Wait for notify()
                            } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                            }
                        }
                    }

                    // Normal Phase Execution
                    runPhase(activeIndex, TrafficLight.Color.GREEN, 10);
                    runPhase(activeIndex, TrafficLight.Color.YELLOW, 3);
                    setLightState(activeIndex, TrafficLight.Color.RED, 0);
                }
            }
        }, "Normal-Cycle-Thread");

        normalCycleThread.setPriority(Thread.NORM_PRIORITY); // Thread Priority
        normalCycleThread.start();
    }

    private void runPhase(int index, TrafficLight.Color color, int duration) {
        setLightState(index, color, duration);
        
        // Custom Sleep Loop to allow faster interruption if needed
        for (int i = duration; i > 0; i--) {
            if (isEmergencyActive) break; // Exit immediately if emergency triggers
            lights.get(index).setTimeLeft(i);
            processTraffic(index, color);
            sleepSeconds(1);
        }
    }

    /* ---------------- 2. EMERGENCY HANDLING (Priority & Join) ---------------- */
    public synchronized void triggerEmergency(int roadIndex) {
        // Concept: isAlive() check
        if (emergencyThread != null && emergencyThread.isAlive()) {
            System.out.println("Emergency already in progress!");
            return; 
        }

        emergencyThread = new Thread(() -> {
            System.out.println("🚑 EMERGENCY STARTED on Road: " + roadIndex);
            
            synchronized (lock) {
                isEmergencyActive = true; // Suspend Normal Cycle
            }

            // Force all RED first
            resetAllLights();
            
            // Turn Green for Emergency
            setLightState(roadIndex, TrafficLight.Color.GREEN, 5); // 5 sec emergency pass
            
            // Simulate Emergency Crossing
            for (int i = 5; i > 0; i--) {
                lights.get(roadIndex).setTimeLeft(i);
                roads.get(roadIndex).removeVehicles(2); // Clear traffic fast
                sleepSeconds(1);
            }

            // Reset
            setLightState(roadIndex, TrafficLight.Color.RED, 0);

            // RESUME Normal Cycle
            synchronized (lock) {
                isEmergencyActive = false;
                lock.notifyAll(); // Inter-Thread Communication: Wake up Normal Cycle
                System.out.println("Normal Cycle Resumed.");
            }

        }, "Emergency-Thread");

        emergencyThread.setPriority(Thread.MAX_PRIORITY); // High Priority
        emergencyThread.start();

        // Concept: join() - Wait for it to ensure it started cleanly (optional demonstration)
        try {
            emergencyThread.join(100); 
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /* ---------------- HELPER METHODS ---------------- */
    private void setLightState(int index, TrafficLight.Color color, int time) {
        // If not emergency, ensure others are red
        if (!isEmergencyActive) resetAllLights();
        
        TrafficLight light = lights.get(index);
        light.setColor(color);
        light.setTimeLeft(time);
    }

    private void resetAllLights() {
        for (TrafficLight light : lights) {
            light.setColor(TrafficLight.Color.RED);
            light.setTimeLeft(0);
        }
    }

    private void processTraffic(int index, TrafficLight.Color color) {
        if (color == TrafficLight.Color.GREEN) {
             roads.get(index).removeVehicles(random.nextInt(2) + 1);
        }
    }

    private void startVehicleGeneration() {
        new Thread(() -> {
            while (true) {
                for (Road road : roads) {
                    if (road.getVehicleCount() < 20) road.addVehicles(random.nextInt(2));
                }
                sleepSeconds(2);
            }
        }).start();
    }

    private void sleepSeconds(int seconds) {
        try { Thread.sleep(seconds * 1000L); } catch (InterruptedException e) {}
    }

    public List<TrafficLight> getLights() { return lights; }
    public List<Road> getRoads() { return roads; }
}