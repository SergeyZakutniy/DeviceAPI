package com.sergzak.deviceapi.entity;

public enum DeviceState
{
    AVAILABLE("AVAILABLE"),

    IN_USE("IN_USE"),

    INACTIVE("INACTIVE");

    private final String value;

    DeviceState(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    public static DeviceState fromValue(String value) {
        for (DeviceState state : DeviceState.values()) {
            if (state.value.equals(value)) {
                return state;
            }
        }
        throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
}
