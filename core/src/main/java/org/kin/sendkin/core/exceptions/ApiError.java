package org.kin.sendkin.core.exceptions;

public class ApiError extends Exception {
    public ApiError(String error){
        super(error);
    }
}
