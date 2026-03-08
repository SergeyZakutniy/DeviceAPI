package com.sergzak.deviceapi.repository;

import com.sergzak.deviceapi.entity.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for {@link Device} entity persistence.
 */
@Repository
public interface DeviceRepository extends JpaRepository<Device, Long>
{
}
