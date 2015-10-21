package com.simbirsoft.farm.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Rabbit extends RabbitBase {

    private byte[] photo;

    private final List<EnergyConsumption> energyConsumption = new ArrayList<>();

    public Rabbit() {
        super();
    }
    
    public Rabbit(Integer id, String name, String color, int age) {
        super(id, name, color, age);
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }
    
    public String getPhotoFileName() {
        return String.format("photo-%d.png", getId());
    }
    
    public RabbitBase baseClone() {
        return new RabbitBase(getId(), getName(), getColor(), getAge());
    }

    public void addEnergyConsumption(EnergyConsumption value) {
        if (value == null) {
            throw new NullPointerException("value"); 
        }
        energyConsumption.add(value);
    }
    
    public long getCharge() {
        final LocalDateTime now = LocalDateTime.now();
        return energyConsumption.stream()
                .filter(e -> now.isAfter(e.getStopTime()))
                .mapToLong(EnergyConsumption::getValue)
                .sum();
    }
    
    public long getEnergyConsumption(LocalDateTime startTime, LocalDateTime stopTime) {
        if (startTime == null) {
            throw new NullPointerException("startTime"); 
        }
        if (stopTime == null) {
            throw new NullPointerException("stopTime"); 
        }
        return energyConsumption.stream()
                .filter(e -> startTime.isBefore(e.getStopTime()) 
                                && stopTime.isAfter(e.getStartTime()) 
                                && (e.getValue() > 0))
                .mapToLong(EnergyConsumption::getValue)
                .sum();
    }
}
