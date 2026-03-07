package com.sergzak.deviceapi.service;

import com.sergzak.deviceapi.v1.dto.CreateDeviceRequest;
import com.sergzak.deviceapi.v1.dto.DevicePageResponse;
import com.sergzak.deviceapi.v1.dto.DeviceResponse;
import com.sergzak.deviceapi.v1.dto.DeviceState;

public interface DeviceService
{
    DeviceResponse createNewDevice(final CreateDeviceRequest createDeviceRequest);

    DevicePageResponse findDevices(final String brand, final DeviceState state,
        final Integer pageNumber, final Integer sizeOfElementsOnPage);
}
