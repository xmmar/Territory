package com.invictus.territory.model;

import com.invictus.territory.model.enums.TerritorySize;

public class RaidWindow {
    private int startHour;
    private int endHour;
    private final TerritorySize size;

    private static final int MIN_HOUR = 18;
    private static final int MAX_HOUR = 24;

    public RaidWindow(int startHour, int endHour, TerritorySize size) {
        this.startHour = Math.max(startHour, MIN_HOUR);
        this.endHour = Math.min(endHour, MAX_HOUR);
        this.size = size;
    }

    public void extendBy(int minutes) {
        int hoursToAdd = minutes / 60;
        this.endHour += hoursToAdd;
        if (this.endHour > MAX_HOUR) {
            this.endHour = MAX_HOUR;
        }
    }

    public int getDurationMinutes() {
        return (endHour - startHour) * 60;
    }

    public boolean isValid() {
        return startHour >= MIN_HOUR && endHour <= MAX_HOUR && startHour < endHour;
    }

    public int getStartHour() {
        return startHour;
    }

    public void setStartHour(int hour) {
        this.startHour = hour;
    }

    public int getEndHour() {
        return endHour;
    }

    public void setEndHour(int hour) {
        this.endHour = hour;
    }

    public TerritorySize getSize() {
        return size;
    }
}