package com.sergzak.deviceapi.service.impl;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

import com.sergzak.deviceapi.entity.Device;
import com.sergzak.deviceapi.entity.DeviceStatus;
import com.sergzak.deviceapi.exception.DeviceInUseException;
import com.sergzak.deviceapi.exception.DeviceNotFoundException;
import com.sergzak.deviceapi.mapper.DeviceMapper;
import com.sergzak.deviceapi.repository.DeviceRepository;
import com.sergzak.deviceapi.service.DeviceService;
import com.sergzak.deviceapi.v1.dto.CreateDeviceRequest;
import com.sergzak.deviceapi.v1.dto.DevicePageResponse;
import com.sergzak.deviceapi.v1.dto.DeviceResponse;
import com.sergzak.deviceapi.v1.dto.DeviceState;
import com.sergzak.deviceapi.v1.dto.PatchDeviceRequest;
import com.sergzak.deviceapi.v1.dto.PutDeviceRequest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Default implementation of DeviceService interface
 */
@Service
public class DeviceServiceImpl implements DeviceService
{
    private static final Logger LOG = LoggerFactory.getLogger(DeviceServiceImpl.class);

    public final DeviceRepository deviceRepository;

    private final Integer defaultPageSize;

    private final Integer maxPageSize;

    public DeviceServiceImpl(final DeviceRepository deviceRepository,
        @Value("${default.page.size}") Integer defaultPageSize, @Value("${max.page.size}") Integer maxPageSize)
    {
        this.deviceRepository = deviceRepository;
        this.defaultPageSize = defaultPageSize;
        this.maxPageSize = maxPageSize;
    }

    @Override
    @Transactional
    public DeviceResponse createNewDevice(final CreateDeviceRequest createDeviceRequest)
    {
        final Device device = deviceRepository.save(DeviceMapper.fromCreateDeviceRequest(createDeviceRequest));
        LOG.debug("Device with id: {} was created successfully", device.getId());
        return DeviceMapper.fromDevice(device);
    }

    @Override
    @Transactional(readOnly = true)
    public DevicePageResponse findDevices(final String brand, final DeviceState state,
        final Integer pageNumber, final Integer pageSize)
    {
        LOG.debug("Searching for devices with brand: {} and state: {}", brand, state != null ? state.getValue() : StringUtils.EMPTY);
        final Pageable pageable = PageRequest.of(pageNumber != null ? pageNumber : 0, getPageSize(pageSize));
        final Device example = createDeviceExample(brand, state);
        final Page<Device> page = deviceRepository.findAll(Example.of(example,
            ExampleMatcher.matchingAll().withIgnoreNullValues()), pageable);
        return DeviceMapper.fromDevicePage(page);
    }

    @Override
    @Transactional(readOnly = true)
    public DeviceResponse findDeviceById(final Long id)
    {
        return DeviceMapper.fromDevice(getDeviceById(id));
    }

    @Override
    @Transactional
    public void removeDeviceById(final Long id)
    {
        final Device device = getDeviceById(id);
        if (isDeviceInUse(device))
        {
            LOG.error("Device with id {}  cannot be removed because it is in use.", id);
            throw new DeviceInUseException(
                String.format("Current device has status: %s and cannot be deleted.", device.getStatus().toString()));
        }

        deviceRepository.delete(device);
        LOG.debug("Device with id: {} was removed successfully. ", id);
    }

    @Override
    @Transactional
    public DeviceResponse replaceDeviceById(final Long id, final PutDeviceRequest putDeviceRequest)
    {
        final Device device = getDeviceById(id);
        if (isDeviceInUse(device))
        {
            throw new DeviceInUseException(String.format("Device with id: %s cannot be updated because the status is: IN_USE", id));
        }

        device.setName(putDeviceRequest.getName());
        device.setStatus(DeviceStatus.valueOf(putDeviceRequest.getState().getValue()));
        device.setBrand(putDeviceRequest.getBrand());
        deviceRepository.save(device);
        LOG.debug("Device with id: {} was successfully replaced.", id);

        return DeviceMapper.fromDevice(device);
    }

    @Override
    @Transactional
    public DeviceResponse updateDeviceById(final Long id, final PatchDeviceRequest patchDeviceRequest)
    {
        final Device device = getDeviceById(id);
        if (!canBeUpdated(device, patchDeviceRequest))
        {
            LOG.error("Device with id {} cannot be updated because it is in use.", id);
            throw new DeviceInUseException(String.format("Device with id: %s cannot be updated because the status is: IN_USE", id));
        }

        updateIfChanged(patchDeviceRequest.getName(), device::getName, device::setName);
        updateIfChanged(patchDeviceRequest.getBrand(), device::getBrand, device::setBrand);
        final String updatedStatus = patchDeviceRequest.getState() != null ? patchDeviceRequest.getState().getValue() : null;
        if (updatedStatus != null)
        {
            updateIfChanged(DeviceStatus.fromValue(updatedStatus), device::getStatus, device::setStatus);
        }

        deviceRepository.save(device);
        LOG.debug("Device with id: {} was successfully updated.", id);
        return DeviceMapper.fromDevice(device);
    }

    private boolean canBeUpdated(final Device device, final PatchDeviceRequest patchDeviceRequest) {
        if (isDeviceInUse(device)) {
            return patchDeviceRequest.getBrand() == null && patchDeviceRequest.getName() == null;
        }
        return true;
    }

    private <T> void updateIfChanged(T newValue, Supplier<T> getter, Consumer<T> setter) {
        if (newValue != null && !Objects.equals(newValue, getter.get())) {
            setter.accept(newValue);
        }
    }

    private boolean isDeviceInUse(final Device device)
    {
        return DeviceStatus.IN_USE.equals(device.getStatus());
    }

    private Device getDeviceById(final Long id)
    {
        LOG.debug("Searching for a device with id: {}", id);
        final Optional<Device> device = deviceRepository.findById(id);
        if (device.isEmpty())
        {
            LOG.error("Device with id: {} is not found.", id);
            throw new DeviceNotFoundException(String.format("Device with id: %s doesn't exist.", id));
        }
        return device.get();
    }

    private Integer getPageSize(final Integer pageSize)
    {
        if (pageSize != null)
        {
            return pageSize > maxPageSize ? maxPageSize : pageSize;
        }
        return defaultPageSize;
    }

    private Device createDeviceExample(final String brand, final DeviceState state)
    {
        final Device example = new Device();
        example.setBrand(brand);
        if (state != null)
        {
            example.setStatus(DeviceStatus.valueOf(state.getValue()));
        }
        return example;
    }
}
