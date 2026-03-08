package com.sergzak.deviceapi.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.sergzak.deviceapi.entity.Device;
import com.sergzak.deviceapi.entity.DeviceStatus;
import com.sergzak.deviceapi.exception.DeviceInUseException;
import com.sergzak.deviceapi.exception.DeviceNotFoundException;
import com.sergzak.deviceapi.repository.DeviceRepository;
import com.sergzak.deviceapi.v1.dto.CreateDeviceRequest;
import com.sergzak.deviceapi.v1.dto.DevicePageResponse;
import com.sergzak.deviceapi.v1.dto.DeviceResponse;
import com.sergzak.deviceapi.v1.dto.DeviceState;
import com.sergzak.deviceapi.v1.dto.PatchDeviceRequest;
import com.sergzak.deviceapi.v1.dto.PutDeviceRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DeviceServiceImplTest
{
    private static final Long DEVICE_ID = 1L;
    private static final Integer PAGE_NUMBER = 0;
    private static final Integer PAGE_SIZE = 20;
    private static final String DEVICE_NAME = "iPhone 15 Pro";
    private static final String DEVICE_BRAND = "Apple";
    private static final String UPDATED_BRAND = "Android";
    private static final String UPDATED_NAME = "Google Pixel 10 Pro";

    private DeviceServiceImpl deviceService;

    @Mock
    private DeviceRepository deviceRepository;

    private Device device;
    private CreateDeviceRequest createDeviceRequest;
    private PutDeviceRequest putDeviceRequest;
    private PatchDeviceRequest patchDeviceRequest;
    private Page<Device> devicePage;


    @BeforeEach
    public void setup() {
        deviceService = new DeviceServiceImpl(deviceRepository, PAGE_NUMBER, PAGE_SIZE);

        createDeviceRequest = new CreateDeviceRequest();
        createDeviceRequest.setName(DEVICE_NAME);
        createDeviceRequest.setBrand(DEVICE_BRAND);
        createDeviceRequest.setState(DeviceState.AVAILABLE);

        device = new Device();
        device.setId(DEVICE_ID);
        device.setBrand(DEVICE_BRAND);
        device.setName(DEVICE_NAME);
        device.setStatus(DeviceStatus.AVAILABLE);
        device.setCreationTime(LocalDateTime.now());

        devicePage = new PageImpl<>(List.of(device));

        putDeviceRequest = new PutDeviceRequest();
        putDeviceRequest.setBrand(UPDATED_BRAND);
        putDeviceRequest.setName(UPDATED_NAME);
        putDeviceRequest.setState(DeviceState.IN_USE);

        patchDeviceRequest = new PatchDeviceRequest();
        patchDeviceRequest.setBrand(UPDATED_BRAND);
        patchDeviceRequest.setName(UPDATED_NAME);
    }

    @Test
    public void shouldCreateNewDevice() {
        //given
        when(deviceRepository.save(any(Device.class))).thenReturn(device);

        //when
        DeviceResponse deviceResponse = deviceService.createNewDevice(createDeviceRequest);

        //then
        assertNotNull(deviceResponse);
        assertEquals(String.valueOf(DEVICE_ID), deviceResponse.getId());
        assertEquals(DEVICE_BRAND, deviceResponse.getBrand());
        assertEquals(DEVICE_NAME, deviceResponse.getName());
        assertEquals(DeviceState.AVAILABLE.getValue(), deviceResponse.getState().getValue());
        ArgumentCaptor<Device> deviceCaptor = ArgumentCaptor.forClass(Device.class);
        verify(deviceRepository).save(deviceCaptor.capture());

        Device capturedDevice = deviceCaptor.getValue();
        assertEquals(DEVICE_NAME, capturedDevice.getName());
        assertEquals(DEVICE_BRAND, capturedDevice.getBrand());
    }

    @Test
    public void shouldFindDevices() {
        //given
        when(deviceRepository.findAll(any(Example.class), any(Pageable.class))).thenReturn(devicePage);

        //when
        DevicePageResponse devicePageResponse = deviceService.findDevices(DEVICE_BRAND, DeviceState.AVAILABLE, PAGE_NUMBER, PAGE_SIZE);

        //then
        assertNotNull(devicePageResponse);
        assertEquals(1, devicePageResponse.getContent().size());
        assertEquals(DEVICE_BRAND, devicePageResponse.getContent().get(0).getBrand());

        ArgumentCaptor<Example<Device>> exampleCaptor = ArgumentCaptor.forClass(Example.class);
        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);

        verify(deviceRepository).findAll(exampleCaptor.capture(), pageableCaptor.capture());

        Pageable capturedPageable = pageableCaptor.getValue();
        assertEquals(PAGE_NUMBER, capturedPageable.getPageNumber());
        assertEquals(PAGE_SIZE, capturedPageable.getPageSize());

        Device capturedExample = exampleCaptor.getValue().getProbe();
        assertEquals(DEVICE_BRAND, capturedExample.getBrand());
        assertEquals(DeviceStatus.AVAILABLE, capturedExample.getStatus());
    }

    @Test
    public void shouldFindDeviceById() {
        //given
        when(deviceRepository.findById(DEVICE_ID)).thenReturn(Optional.of(device));

        //when
        DeviceResponse deviceResponse = deviceService.findDeviceById(DEVICE_ID);

        //then
        assertNotNull(deviceResponse);
        verify(deviceRepository).findById(DEVICE_ID);
        assertEquals(String.valueOf(DEVICE_ID), deviceResponse.getId());
        assertEquals(DEVICE_BRAND, deviceResponse.getBrand());
        assertEquals(DEVICE_NAME, deviceResponse.getName());
        assertEquals(DeviceState.AVAILABLE.getValue(), deviceResponse.getState().getValue());
    }

    @Test
    public void shouldThrowAnExceptionWhenDeviceIsNotFound() {
        //given
        when(deviceRepository.findById(DEVICE_ID)).thenReturn(Optional.empty());

        //when
        DeviceNotFoundException exception = assertThrows(DeviceNotFoundException.class,
            () -> deviceService.findDeviceById(DEVICE_ID));

        //then
        verify(deviceRepository).findById(DEVICE_ID);
        assertEquals("Device with id: 1 doesn't exist.", exception.getMessage());
    }

    @Test
    public void shouldRemoveDeviceById() {
        //given
        when(deviceRepository.findById(DEVICE_ID)).thenReturn(Optional.of(device));

        //when
        deviceService.removeDeviceById(DEVICE_ID);

        //then
        verify(deviceRepository).delete(device);
    }

    @Test
    public void shouldThrowDeviceInUseExceptionWhenRemovingDevice() {
        //given
        device.setStatus(DeviceStatus.IN_USE);
        when(deviceRepository.findById(DEVICE_ID)).thenReturn(Optional.of(device));

        //when
        DeviceInUseException exception = assertThrows(DeviceInUseException.class,
            () -> deviceService.removeDeviceById(DEVICE_ID));

        //then
        assertEquals("Current device has status: IN_USE and cannot be deleted.", exception.getMessage());
        verify(deviceRepository, never()).delete(device);
    }

    @Test
    public void shouldReplaceDeviceById() {
        //given
        when(deviceRepository.findById(DEVICE_ID)).thenReturn(Optional.of(device));

        //when
        DeviceResponse response = deviceService.replaceDeviceById(DEVICE_ID, putDeviceRequest);

        //then
        verify(deviceRepository).save(device);
        assertNotNull(response);
        assertEquals(DeviceState.IN_USE, response.getState());
        assertEquals(String.valueOf(DEVICE_ID), response.getId());
        assertEquals(UPDATED_NAME, response.getName());
        assertEquals(UPDATED_BRAND, response.getBrand());
    }

    @Test
    public void shouldThrowDeviceInUseExceptionWhenReplacingDevice() {
        //given
        device.setStatus(DeviceStatus.IN_USE);
        when(deviceRepository.findById(DEVICE_ID)).thenReturn(Optional.of(device));

        //when
        DeviceInUseException exception = assertThrows(DeviceInUseException.class,
            () -> deviceService.replaceDeviceById(DEVICE_ID, putDeviceRequest));

        //then
        assertEquals("Device with id: 1 cannot be updated because the status is: IN_USE", exception.getMessage());
        verify(deviceRepository, never()).save(device);
    }

    @Test
    public void shouldUpdateDeviceById() {
        //given
        when(deviceRepository.findById(DEVICE_ID)).thenReturn(Optional.of(device));

        //when
        DeviceResponse response = deviceService.updateDeviceById(DEVICE_ID, patchDeviceRequest);

        //then
        verify(deviceRepository).save(device);
        assertNotNull(response);
        assertEquals(DeviceState.AVAILABLE, response.getState());
        assertEquals(String.valueOf(DEVICE_ID), response.getId());
        assertEquals(UPDATED_NAME, response.getName());
        assertEquals(UPDATED_BRAND, response.getBrand());
    }

    @Test
    public void shouldThrowDeviceInUseExceptionWhenUpdatingDeviceNameOrBrand() {
        //given
        device.setStatus(DeviceStatus.IN_USE);
        when(deviceRepository.findById(DEVICE_ID)).thenReturn(Optional.of(device));

        //when
        DeviceInUseException exception = assertThrows(DeviceInUseException.class,
            () -> deviceService.updateDeviceById(DEVICE_ID, patchDeviceRequest));

        //then
        assertEquals("Device with id: 1 cannot be updated because the status is: IN_USE", exception.getMessage());
        verify(deviceRepository, never()).save(device);
    }
}
