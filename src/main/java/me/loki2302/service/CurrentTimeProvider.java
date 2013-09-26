package me.loki2302.service;

import java.util.Date;

import org.springframework.stereotype.Component;

@Component
public class CurrentTimeProvider {
    private Date currentTimeOverride;
    
    public Date getCurrentTime() {
        if(currentTimeOverride != null) {
            return currentTimeOverride;
        }
        
        return new Date();
    }
    
    public void overrideCurrentTime(Date currentTimeOverride) {
        this.currentTimeOverride = currentTimeOverride;
    }
}