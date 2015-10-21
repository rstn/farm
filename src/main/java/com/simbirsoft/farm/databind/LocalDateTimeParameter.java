package com.simbirsoft.farm.databind;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class LocalDateTimeParameter implements Serializable {
    
    private static final long serialVersionUID = 1L;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private final LocalDateTime value;

    public static LocalDateTimeParameter valueOf(String dateStr) {
        try {
            return new LocalDateTimeParameter(LocalDateTime.parse(dateStr, DATE_FORMATTER));
        } catch (DateTimeParseException ignore) {
            return new LocalDateTimeParameter(null);
        }
    }
    
    public LocalDateTimeParameter(LocalDateTime value) {
        this.value = value;
    }

    public LocalDateTime getValue() {
        return value;
    }
}
