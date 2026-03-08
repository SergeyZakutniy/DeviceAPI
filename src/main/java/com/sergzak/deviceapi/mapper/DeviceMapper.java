package com.sergzak.deviceapi.mapper;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

import com.sergzak.deviceapi.entity.Device;
import com.sergzak.deviceapi.entity.DeviceStatus;
import com.sergzak.deviceapi.v1.dto.CreateDeviceRequest;
import com.sergzak.deviceapi.v1.dto.DevicePageResponse;
import com.sergzak.deviceapi.v1.dto.DeviceResponse;
import com.sergzak.deviceapi.v1.dto.DeviceState;
import org.springframework.data.domain.Page;

public class DeviceMapper
{
    public static Device fromCreateDeviceRequest(final CreateDeviceRequest createDeviceRequest) {
        final Device device = new Device();
        if (createDeviceRequest != null)
        {
            device.setBrand(createDeviceRequest.getBrand());
            device.setName(createDeviceRequest.getName());
            device.setStatus(createDeviceRequest.getState() != null
                ? DeviceStatus.fromValue(createDeviceRequest.getState().getValue()) : null);
        }
        return device;
    }

    public static DeviceResponse fromDevice(Device device) {
        final DeviceResponse deviceResponse = new DeviceResponse();
        if (device != null) {
            deviceResponse.setId(String.valueOf(device.getId()));
            deviceResponse.setName(device.getName());
            deviceResponse.setBrand(device.getBrand());
            if (device.getStatus() != null)
            {
                deviceResponse.setState(DeviceState.fromValue(device.getStatus().toString()));
            }
            deviceResponse.setCreationTime(toOffsetDateTime(device.getCreationTime()));
        }
        return deviceResponse;
    }
    public static DevicePageResponse fromDevicePage(Page<Device> devicePage) {
        final DevicePageResponse devicePageResponse = new DevicePageResponse();
        devicePageResponse.setTotalPages(devicePage.getTotalPages());
        devicePageResponse.setTotalElements(devicePage.getTotalElements());
        devicePageResponse.setNumber(devicePage.getNumber());
        if (!devicePage.isEmpty()) {
            final List<DeviceResponse> devices = devicePage.stream().map(DeviceMapper::fromDevice).toList();
            devicePageResponse.setContent(devices);
        }
        return devicePageResponse;
    }

    private static OffsetDateTime toOffsetDateTime(LocalDateTime localDateTime) {
        return localDateTime == null ? null : localDateTime.atOffset(ZoneOffset.UTC);
    }
}
