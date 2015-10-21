package com.simbirsoft.farm.service;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;

import com.simbirsoft.farm.model.EnergyConsumption;
import com.simbirsoft.farm.model.FieldType;
import com.simbirsoft.farm.model.Rabbit;
import com.simbirsoft.farm.model.RabbitBase;
import com.simbirsoft.farm.request.EatForm;
import com.simbirsoft.farm.request.MoveForm;
import com.simbirsoft.farm.request.PeriodForm;
import com.simbirsoft.farm.response.ErrorInfo;
import com.simbirsoft.farm.response.GetEnergyResponse;

@Singleton /* простое решение для хранения списка кроликов в памяти между вызовами */
public class RabbitService {
    
    private static final int ENERGY_PER_MINUTE_ON_BAD_FIELD = 1;

    private static final int ENERGY_PER_MINUTE_ON_GOOD_FIELD = 10;

    private static final int ENERGY_PER_KM = 1000;

    private final Map<Integer, Rabbit> rabbits = new HashMap<>();
    
    private int rabbitSeq = 0;
    
    @Lock(LockType.READ)
    public Rabbit find(Integer id) {
        return rabbits.get(id);
    }
    
    @Lock(LockType.READ)
    public List<RabbitBase> findAll() {
        return rabbits.values().stream().map(Rabbit::baseClone).collect(Collectors.toList());
    }
    
    @Lock(LockType.WRITE)
    public Rabbit create(Rabbit rabbit) {
        if (rabbit == null) {
            throw new NullPointerException("rabbit");
        }
        rabbit.setId(Integer.valueOf(nextRabbitSeq()));
        rabbits.put(rabbit.getId(), rabbit);
        return rabbit;
    }
    
    @Lock(LockType.WRITE)
    public Rabbit update(Rabbit rabbit) {
        if (rabbit == null) {
            throw new NullPointerException("rabbit");
        }
        if ((rabbit.getId() == null) || !rabbits.containsKey(rabbit.getId())) {
            return null;
        } else {
            rabbits.put(rabbit.getId(), rabbit);
            return rabbit;
        }
    }
    
    @Lock(LockType.WRITE)
    public void delete(Integer id) {
        rabbits.remove(id);
    }
    
    @Lock(LockType.WRITE)
    public Rabbit move(Integer id, MoveForm param) {
        if (id == null) {
            throw new NullPointerException("id");
        }
        if (param == null) {
            throw new NullPointerException("param");
        }
        Rabbit rabbit = rabbits.get(id);
        if (rabbit != null) {
            EnergyConsumption energyConsumption = new EnergyConsumption(
                    - ENERGY_PER_KM * param.getDistance(), param.getStartTime(), param.getStopTime());
            rabbit.addEnergyConsumption(energyConsumption);
        }
        return rabbit;
    }
    
    @Lock(LockType.WRITE)
    public Rabbit eat(Integer id, EatForm param) {
        if (id == null) {
            throw new NullPointerException("id");
        }
        if (param == null) {
            throw new NullPointerException("param");
        }
        Rabbit rabbit = rabbits.get(id);
        if (rabbit != null) {
            long periodInMinutes = ChronoUnit.MINUTES.between(param.getStartTime(), param.getStopTime());
            long energyPerMinute = 0;
            if (FieldType.good == param.getFieldType()) {
                energyPerMinute = ENERGY_PER_MINUTE_ON_GOOD_FIELD;
            } else if (FieldType.bad == param.getFieldType()){
                energyPerMinute = ENERGY_PER_MINUTE_ON_BAD_FIELD;
            }
            EnergyConsumption energyConsumption = new EnergyConsumption(
                    energyPerMinute * periodInMinutes, param.getStartTime(), param.getStopTime());
            rabbit.addEnergyConsumption(energyConsumption);
        }
        return rabbit;
    }
    
    @Lock(LockType.READ)
    public GetEnergyResponse getEnergyConsumption(Integer id, PeriodForm param) {
        if (id == null) {
            throw new NullPointerException("id");
        }
        if (param == null) {
            throw new NullPointerException("param");
        }
        long result = -1;
        Rabbit rabbit = rabbits.get(id);
        if (rabbit != null) {
            result = rabbit.getEnergyConsumption(param.getStartTime(), param.getStopTime());
        }
        return GetEnergyResponse.valueOf(result);
    }

    @Lock(LockType.WRITE)
    public Rabbit updatePhoto(Integer id, byte[] photo) {
        if (id == null) {
            throw new NullPointerException("id");
        }
        Rabbit rabbit = rabbits.get(id);
        if (rabbit != null) {
            rabbit.setPhoto(photo);
        }
        return rabbit;
    }

    @Lock(LockType.WRITE)
    public void deletePhoto(Integer id) {
        Rabbit rabbit = rabbits.get(id);
        if (rabbit != null) {
            rabbit.setPhoto(null);
        }
    }
    
    public boolean isValid(Rabbit rabbit) {
        return (rabbit != null) && (rabbit.getAge() > 0) 
                && (rabbit.getName() != null) && !rabbit.getName().isEmpty()
                && (rabbit.getColor() != null) && !rabbit.getColor().isEmpty()
                ;
    }
    
    public ErrorInfo.Errors validate(Rabbit rabbit) {
        List<ErrorInfo> result = new ArrayList<>();
        if (rabbit == null) {
            result.add(ErrorInfo.validationError("Not Found"));
        } else {
            if ((rabbit.getName() == null) || rabbit.getName().isEmpty()) {
                result.add(ErrorInfo.validationError("Name is empty"));
            }
            if ((rabbit.getColor() == null) || rabbit.getColor().isEmpty()) {
                result.add(ErrorInfo.validationError("Color is empty"));
            }
        }
        return ErrorInfo.errors(result);
    }
    
    private int nextRabbitSeq() {
        rabbitSeq = rabbitSeq + 1;
        return rabbitSeq;
    }

}
