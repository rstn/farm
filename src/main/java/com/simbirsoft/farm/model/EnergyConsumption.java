package com.simbirsoft.farm.model;

import java.time.LocalDateTime;

public class EnergyConsumption {
    
    private final long value;
    
    private final LocalDateTime startTime;
    
    private final LocalDateTime stopTime;

    public EnergyConsumption(long value, LocalDateTime startTime, LocalDateTime stopTime) {
        this.value = value;
        this.startTime = startTime;
        this.stopTime = stopTime;
    }

    public long getValue() {
        return value;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getStopTime() {
        return stopTime;
    }

    @Override
    public String toString() {
        return "EnergyConsumption [value=" + value + ", startTime=" + startTime + ", stopTime=" + stopTime + "]";
    }

}
