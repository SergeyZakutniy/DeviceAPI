package com.sergzak.deviceapi.controller;

import com.sergzak.deviceapi.service.DeviceService;
import com.sergzak.deviceapi.v1.DevicesApi;
import com.sergzak.deviceapi.v1.dto.CreateDeviceRequest;
import com.sergzak.deviceapi.v1.dto.DevicePageResponse;
import com.sergzak.deviceapi.v1.dto.DeviceResponse;
import com.sergzak.deviceapi.v1.dto.DeviceState;
import com.sergzak.deviceapi.v1.dto.UpdateDeviceRequest;
import jakarta.validation.Valid;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
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
    public ResponseEntity<DeviceResponse> createDevice(@Valid @RequestBody final CreateDeviceRequest createDeviceRequest) throws Exception
    {
        LOG.debug("Creating a new device");
        final DeviceResponse deviceResponse = deviceService.createNewDevice(createDeviceRequest);
        LOG.debug("Device with id: {} was created successfully", deviceResponse.getId());
        return ResponseEntity.ok().body(deviceResponse);
    }

    @Override
    public ResponseEntity<DevicePageResponse> findDevices(final String brand, final DeviceState state, final Integer page,
        final Integer size) throws Exception
    {
        LOG.debug("Looking for devices with brand: {} and state: {}", brand, state != null ? state.getValue() : StringUtils.EMPTY);
        final DevicePageResponse devicePageResponse = deviceService.findDevices(brand, state, page, size);
        return ResponseEntity.ok().body(devicePageResponse);
    }

    @Override
    public ResponseEntity<DeviceResponse> getDeviceById(final String id) throws Exception
    {
        return null;
    }

    @Override
    public ResponseEntity<Void> removeDeviceById(final String id) throws Exception
    {
        return null;
    }

    @Override
    public ResponseEntity<DeviceResponse> replaceDeviceById(final String id, final UpdateDeviceRequest updateDeviceRequest) throws Exception
    {
        return null;
    }

    @Override
    public ResponseEntity<DeviceResponse> updateDeviceById(final String id, final UpdateDeviceRequest updateDeviceRequest) throws Exception
    {
        return null;
    }
}
