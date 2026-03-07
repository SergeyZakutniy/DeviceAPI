package com.sergzak.deviceapi.service.impl;

import java.util.List;

import com.sergzak.deviceapi.entity.Device;
import com.sergzak.deviceapi.mapper.DeviceMapper;
import com.sergzak.deviceapi.repository.DeviceRepository;
import com.sergzak.deviceapi.service.DeviceService;
import com.sergzak.deviceapi.v1.dto.CreateDeviceRequest;
import com.sergzak.deviceapi.v1.dto.DevicePageResponse;
import com.sergzak.deviceapi.v1.dto.DeviceResponse;
import com.sergzak.deviceapi.v1.dto.DeviceState;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DeviceServiceImpl implements DeviceService
{
    public final DeviceRepository deviceRepository;
    private final Integer defaultPageSize;
    private final Integer maxPageSize;

    public DeviceServiceImpl(final DeviceRepository deviceRepository,
        @Value("${default.page.size}") Integer defaultPageSize, @Value("${max.page.size}") Integer maxPageSize) {
        this.deviceRepository = deviceRepository;
        this.defaultPageSize = defaultPageSize;
        this.maxPageSize = maxPageSize;
    }

    @Override
    @Transactional
    public DeviceResponse createNewDevice(final CreateDeviceRequest createDeviceRequest)
    {
        final Device device = deviceRepository.save(DeviceMapper.fromCreateDeviceRequest(createDeviceRequest));
        return DeviceMapper.fromDevice(device);
    }

    @Override
    @Transactional(readOnly = true)
    public DevicePageResponse findDevices(final String brand, final DeviceState state,
        final Integer pageNumber, final Integer pageSize) {
        final Pageable pageable = PageRequest.of(pageNumber != null ? pageNumber : 0, getPageSize(pageSize));
        final Device example = createDeviceExample(brand, state);
        final Page<Device> page = deviceRepository.findAll(Example.of(example,
            ExampleMatcher.matchingAll().withIgnoreNullValues()), pageable);
        return mapToDevicePageResponse(page);
    }

    private Integer getPageSize(final Integer pageSize) {
        if (pageSize != null) {
            return pageSize > maxPageSize ? maxPageSize : pageSize;
        }
        return defaultPageSize;
    }

    private Device createDeviceExample(final String brand, final DeviceState state) {
        final Device example = new Device();
        example.setBrand(brand);
        if (state != null)
        {
            example.setState(com.sergzak.deviceapi.entity.DeviceState.valueOf(state.getValue()));
        }
        return example;
    }

    private DevicePageResponse mapToDevicePageResponse(Page<Device> page) {
        final DevicePageResponse devicePageResponse = new DevicePageResponse();
        devicePageResponse.setTotalPages(page.getTotalPages());
        devicePageResponse.setTotalElements(page.getTotalElements());
        devicePageResponse.setNumber(page.getNumber());
        if (!page.isEmpty()) {
            final List<DeviceResponse> devices = page.stream().map(DeviceMapper::fromDevice).toList();
            devicePageResponse.setContent(devices);
        }
        return devicePageResponse;
    }
}
