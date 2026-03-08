package com.sergzak.deviceapi.controller;

import com.sergzak.deviceapi.service.DeviceService;
import com.sergzak.deviceapi.v1.DevicesApi;
import com.sergzak.deviceapi.v1.dto.CreateDeviceRequest;
import com.sergzak.deviceapi.v1.dto.DevicePageResponse;
import com.sergzak.deviceapi.v1.dto.DeviceResponse;
import com.sergzak.deviceapi.v1.dto.DeviceState;
import com.sergzak.deviceapi.v1.dto.PatchDeviceRequest;
import com.sergzak.deviceapi.v1.dto.PutDeviceRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DevicesApiController implements DevicesApi
{
    public static final Logger LOG = LoggerFactory.getLogger(DevicesApiController.class);

    private final DeviceService deviceService;

    public DevicesApiController(final DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @Override
    public ResponseEntity<DeviceResponse> createDevice(final CreateDeviceRequest createDeviceRequest) throws Exception
    {
        return ResponseEntity.ok().body(deviceService.createNewDevice(createDeviceRequest));
    }

    @Override
    public ResponseEntity<DevicePageResponse> findDevices(final String brand, final DeviceState state, final Integer page,
        final Integer size) throws Exception
    {
        return ResponseEntity.ok().body(deviceService.findDevices(brand, state, page, size));
    }

    @Override
    public ResponseEntity<DeviceResponse> getDeviceById(final Long id) throws Exception
    {
        return ResponseEntity.ok().body(deviceService.findDeviceById(id));
    }

    @Override
    public ResponseEntity<Void> removeDeviceById(final Long id) throws Exception
    {
        deviceService.removeDeviceById(id);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<DeviceResponse> replaceDeviceById(final Long id, final PutDeviceRequest putDeviceRequest) throws Exception
    {
        return ResponseEntity.ok().body(deviceService.replaceDeviceById(id, putDeviceRequest));
    }

    @Override
    public ResponseEntity<DeviceResponse> updateDeviceById(final Long id, final PatchDeviceRequest patchDeviceRequest) throws Exception
    {
        return ResponseEntity.ok().body(deviceService.updateDeviceById(id, patchDeviceRequest));
    }
}
