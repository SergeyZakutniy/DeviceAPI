package com.sergzak.deviceapi.cache;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
public class CacheInvalidator
{
    @CacheEvict(value = { "devices", "devicePages" }, allEntries = true)
    @Scheduled(fixedRate = 3600000)
    public void emptyCache()
    {
    }
}
