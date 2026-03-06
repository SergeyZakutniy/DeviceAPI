package com.sergzak.deviceapi.controller;

import java.util.UUID;

import com.sergzak.deviceapi.v1.DevicesApi;
import com.sergzak.deviceapi.v1.dto.CreateDeviceRequest;
import com.sergzak.deviceapi.v1.dto.DeviceResponse;
import com.sergzak.deviceapi.v1.dto.DeviceState;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DevicesApiController implements DevicesApi
{
    @Override
    public ResponseEntity<Void> createDevice(final CreateDeviceRequest createDeviceRequest) throws Exception
    {
        return null;
    }

    @Override
    public ResponseEntity<Void> findDevices(final String brand, final DeviceState state) throws Exception
    {
        return null;
    }

    @Override
    public ResponseEntity<DeviceResponse> getDeviceById(final UUID id) throws Exception
    {
        return null;
    }

    @Override
    public ResponseEntity<DeviceResponse> removeDeviceById(final UUID id) throws Exception
    {
        return null;
    }

    @Override
    public ResponseEntity<Void> replaceDeviceById(final UUID id) throws Exception
    {
        return null;
    }

    @Override
    public ResponseEntity<Void> updateDeviceById(final UUID id) throws Exception
    {
        return null;
    }
}
