package com.sergzak.deviceapi.entity;

public enum DeviceStatus
{
    AVAILABLE("AVAILABLE"),

    IN_USE("IN_USE"),

    INACTIVE("INACTIVE");

    private final String value;

    DeviceStatus(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    public static DeviceStatus fromValue(String value) {
        for (DeviceStatus state : DeviceStatus.values()) {
            if (state.value.equals(value)) {
                return state;
            }
        }
        throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
}
