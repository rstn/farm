package com.simbirsoft.farm.request;

import com.simbirsoft.farm.model.FieldType;

public class EatForm extends PeriodForm {

    private FieldType fieldType;

    public FieldType getFieldType() {
        return fieldType;
    }

    public void setFieldType(FieldType fieldType) {
        this.fieldType = fieldType;
    }
    
    @Override
    public boolean isValid() {
        return (fieldType != null) && super.isValid();
    }

    @Override
    public String toString() {
        return "EatParam [fieldType=" + fieldType + ", " + super.toString() + "]";
    }
}
