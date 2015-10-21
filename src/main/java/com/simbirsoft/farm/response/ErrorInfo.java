package com.simbirsoft.farm.response;

import java.util.Collections;
import java.util.List;

public class ErrorInfo {
    
    private final Code code;
    
    private final String description;
    
    public static ErrorInfo validationError(String description) {
        return new ErrorInfo(Code.validationError, description);
    }
    
    public static Errors errors(List<ErrorInfo> errorInfoList) {
        return new Errors(errorInfoList);
    }
    
    public ErrorInfo(Code code, String description) {
        this.code = code;
        this.description = description;
    }

    public Code getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "Error [code=" + code + ", description=" + description + "]";
    }

    public static enum Code {
        validationError;
    }
    
    public static class Errors {
        
        private final List<ErrorInfo> errors;

        public Errors(List<ErrorInfo> errors) {
            this.errors = (errors == null) ? Collections.emptyList() : errors;
        }

        public List<ErrorInfo> getErrors() {
            return errors;
        }
        
        public boolean hasErrors() {
            return !errors.isEmpty();
        }
    }
}
