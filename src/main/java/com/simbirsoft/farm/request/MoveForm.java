package com.simbirsoft.farm.request;

public class MoveForm extends PeriodForm {

    private static final int MAX_DISTANCE = 100;

    private Integer distance; 

    public Integer getDistance() {
        return distance;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }
    
    @Override
    public boolean isValid() {
        return (distance != null) && (distance < MAX_DISTANCE)
                && super.isValid();
    }

    @Override
    public String toString() {
        return "MoveParam [distance=" + distance + ", " + super.toString() + "]";
    }
}
