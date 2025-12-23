package com.traffic.app.model;

public class TrafficLight {
    public enum Color { GREEN, YELLOW, RED }

    private Color color;
    private int remainingTime; // seconds for current color

    public TrafficLight(Color initialColor) {
        this.color = initialColor;
        this.remainingTime = 0;
    }

    public synchronized Color getColor() { return color; }
    public synchronized void setColor(Color color) { this.color = color; }
    public synchronized int getRemainingTime() { return remainingTime; }
    public synchronized void setRemainingTime(int time) { this.remainingTime = time; }
}
