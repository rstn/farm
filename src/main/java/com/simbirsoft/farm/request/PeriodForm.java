package com.simbirsoft.farm.request;

import java.time.LocalDateTime;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.simbirsoft.farm.databind.JsonDateTimeDeserializer;

public class PeriodForm {
    
    @JsonDeserialize(using = JsonDateTimeDeserializer.class)
    private LocalDateTime startTime; 
    
    @JsonDeserialize(using = JsonDateTimeDeserializer.class)
    private LocalDateTime stopTime;

    public PeriodForm() {
    }
    
    public PeriodForm(LocalDateTime startTime, LocalDateTime stopTime) {
        this.startTime = startTime;
        this.stopTime = stopTime;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getStopTime() {
        return stopTime;
    }

    public void setStopTime(LocalDateTime stopTime) {
        this.stopTime = stopTime;
    }
    
    public boolean isValid() {
        return (startTime != null) && (stopTime != null) && startTime.isBefore(stopTime);
    }

    @Override
    public String toString() {
        return "PeriodParam [startTime=" + startTime + ", stopTime=" + stopTime + "]";
    }

}
