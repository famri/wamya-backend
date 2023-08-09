package com.excentria_it.wamya.common.exception;

public class VehicleNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 5111261903976786067L;

    public VehicleNotFoundException(String message) {
        super(message);
    }

}