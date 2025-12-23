package com.traffic.app.model;

public class Vehicle {
    private String id;
    private String type; // e.g., "Car", "Truck", "Emergency"

    public Vehicle(String id, String type) {
        this.id = id;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }
}