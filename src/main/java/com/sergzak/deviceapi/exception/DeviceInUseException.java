package com.sergzak.deviceapi.exception;

public class DeviceInUseException extends RuntimeException
{
    public DeviceInUseException(String message) {
        super(message);
    }
}
