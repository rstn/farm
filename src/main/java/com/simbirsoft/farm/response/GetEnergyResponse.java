package com.simbirsoft.farm.response;

public class GetEnergyResponse {

    private Long energyValue;

    public static GetEnergyResponse valueOf(long energyValue) {
        return new GetEnergyResponse(energyValue);
    }
    
    public GetEnergyResponse(Long energyValue) {
        this.energyValue = energyValue;
    }

    public Long getEnergyValue() {
        return energyValue;
    }

    public void setEnergyValue(Long energyValue) {
        this.energyValue = energyValue;
    }

    @Override
    public String toString() {
        return "GetEnergyResponse [energyValue=" + energyValue + "]";
    }
}
