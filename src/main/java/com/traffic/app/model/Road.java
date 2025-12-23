package com.traffic.app.model;

public class Road {
    private String name;
    private int vehicleCount;

    public Road(String name) {
        this.name = name;
        this.vehicleCount = 0;
    }

    public String getName() {
        return name;
    }

    public synchronized int getVehicleCount() {
        return vehicleCount;
    }

    public synchronized void addVehicles(int count) {
        vehicleCount = Math.min(10, vehicleCount + count);
    }

    public synchronized void removeVehicles(int count) {
        vehicleCount = Math.max(0, vehicleCount - count);
    }
}
