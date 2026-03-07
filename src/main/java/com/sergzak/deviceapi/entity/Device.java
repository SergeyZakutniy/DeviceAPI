package com.sergzak.deviceapi.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "devices")
public class Device
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private String brand;

    @Enumerated(EnumType.STRING)
    private DeviceState state = DeviceState.AVAILABLE;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime creationTime;

    public Long getId()
    {
        return id;
    }

    public void setId(final Long id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(final String name)
    {
        this.name = name;
    }

    public String getBrand()
    {
        return brand;
    }

    public void setBrand(final String brand)
    {
        this.brand = brand;
    }

    public DeviceState getState()
    {
        return state;
    }

    public void setState(final DeviceState state)
    {
        this.state = state;
    }

    public LocalDateTime getCreationTime()
    {
        return creationTime;
    }

    public void setCreationTime(final LocalDateTime creationTime)
    {
        this.creationTime = creationTime;
    }
}
