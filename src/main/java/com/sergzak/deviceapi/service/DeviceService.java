package com.sergzak.deviceapi.service;

import com.sergzak.deviceapi.v1.dto.CreateDeviceRequest;
import com.sergzak.deviceapi.v1.dto.DevicePageResponse;
import com.sergzak.deviceapi.v1.dto.DeviceResponse;
import com.sergzak.deviceapi.v1.dto.DeviceState;
import com.sergzak.deviceapi.v1.dto.PatchDeviceRequest;
import com.sergzak.deviceapi.v1.dto.PutDeviceRequest;

/**
 * Service for managing devices
 */
public interface DeviceService
{
    /**
     * A method which creates a new device based on provided request.
     *
     * @param createDeviceRequest client request
     * @return a device response
     */
    DeviceResponse createNewDevice(final CreateDeviceRequest createDeviceRequest);

    /**
     * A method which retrieves paginated data about all devices according to provided search criteria.
     *
     * @param brand brand name
     * @param state state of device (can be AVAILABLE, IN_USE, INACTIVE)
     * @param pageNumber number of the returned page
     * @param sizeOfElementsOnPage size of devices per a single page
     * @return paginated response with devices
     */
    DevicePageResponse findDevices(final String brand, final DeviceState state,
        final Integer pageNumber, final Integer sizeOfElementsOnPage);

    /**
     * A method which should find a device based on provided id.
     *
     * @param id id of a device to search
     * @return a response with found device, or DeviceNotFoundException
     */
    DeviceResponse findDeviceById(final Long id);

    /**
     * A method which removes a device from the database.
     * Device which have state='IN_USE' cannot be deleted.
     * DeviceNotFoundException will be thrown if there is no such a device.
     *
     * @param id id of a device to remove
     */
    void removeDeviceById(final Long id);

    /**
     * Fully replaces a device based on a client's request.
     * Device which have state='IN_USE' cannot be updated.
     *
     * @param id id of a device to replace
     * @param putDeviceRequest client's replace request
     * @return response with replaced device
     */
    DeviceResponse replaceDeviceById(final Long id, final PutDeviceRequest putDeviceRequest);

    /**
     * Partially updates a device based on a client's request.
     * It's not possible to update 'name' and 'brand' properties if device have state='IN_USE'.
     *
     * @param id id of a device to update
     * @param patchDeviceRequest client's update request
     * @return response with updated device
     */
    DeviceResponse updateDeviceById(final Long id, final PatchDeviceRequest patchDeviceRequest);
}
