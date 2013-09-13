package me.loki2302;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SomethingService {
    @Autowired
    private SomethingDao somethingDao;
    
    public int getSomething() {
        return somethingDao.getSomething();
    }
}