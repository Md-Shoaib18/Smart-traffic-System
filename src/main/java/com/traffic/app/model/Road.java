package com.traffic.app.model;

public class Road {
    private String name;
    private volatile int vehicleCount; // Current number of cars

    public Road(String name) {
        this.name = name;
        this.vehicleCount = 0;
    }

    // Synchronized: Prevents race conditions between Generator Thread and Traffic Flow Thread
    public synchronized void addVehicles(int count) {
        this.vehicleCount += count;
    }

    // Synchronized: Ensures we don't remove more cars than exist
    public synchronized void removeVehicles(int count) {
        if (this.vehicleCount >= count) {
            this.vehicleCount -= count;
        } else {
            this.vehicleCount = 0;
        }
    }

    public int getVehicleCount() {
        return vehicleCount;
    }
    
    // Optional: useful for debugging
    public String getName() {
        return name;
    }
}