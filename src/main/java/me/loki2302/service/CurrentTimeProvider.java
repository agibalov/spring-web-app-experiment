package me.loki2302.service;

import java.util.Date;

import org.springframework.stereotype.Component;

@Component
public class CurrentTimeProvider {
    public Date getCurrentTime() {
        return new Date();
    }
}