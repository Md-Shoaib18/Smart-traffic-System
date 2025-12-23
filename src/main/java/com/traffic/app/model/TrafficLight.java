package com.traffic.app.model;

public class TrafficLight {
    
    // Enum for type safety
    public enum Color { RED, YELLOW, GREEN }

    // volatile ensures visibility across threads (Controller reading vs SignalService writing)
    private volatile Color color;
    private volatile int timeLeft;

    public TrafficLight(Color color) {
        this.color = color;
        this.timeLeft = 0;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public int getTimeLeft() {
        return timeLeft;
    }

    public void setTimeLeft(int timeLeft) {
        this.timeLeft = timeLeft;
    }
}