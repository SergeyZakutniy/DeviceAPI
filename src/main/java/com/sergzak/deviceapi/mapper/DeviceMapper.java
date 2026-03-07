package com.sergzak.deviceapi.mapper;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import com.sergzak.deviceapi.entity.Device;
import com.sergzak.deviceapi.entity.DeviceState;
import com.sergzak.deviceapi.v1.dto.CreateDeviceRequest;
import com.sergzak.deviceapi.v1.dto.DeviceResponse;

public class DeviceMapper
{
    public static Device fromCreateDeviceRequest(final CreateDeviceRequest createDeviceRequest) {
        final Device device = new Device();
        if (createDeviceRequest != null)
        {
            device.setBrand(createDeviceRequest.getBrand());
            device.setName(createDeviceRequest.getName());
            device.setState(createDeviceRequest.getState() != null
                ? DeviceState.fromValue(createDeviceRequest.getState().getValue()) : null);
        }
        return device;
    }

    public static DeviceResponse fromDevice(Device device) {
        final DeviceResponse deviceResponse = new DeviceResponse();
        if (device != null) {
            deviceResponse.setId(String.valueOf(device.getId()));
            deviceResponse.setName(device.getName());
            deviceResponse.setBrand(device.getBrand());
            if (device.getState() != null)
            {
                deviceResponse.setState(com.sergzak.deviceapi.v1.dto.DeviceState.fromValue(device.getState().toString()));
            }
            deviceResponse.setCreationTime(toOffsetDateTime(device.getCreationTime()));
        }
        return deviceResponse;
    }

    private static OffsetDateTime toOffsetDateTime(LocalDateTime localDateTime) {
        return localDateTime == null ? null : localDateTime.atOffset(ZoneOffset.UTC);
    }
}
