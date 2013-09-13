package me.loki2302;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfiguration.class)
@Transactional
@TransactionConfiguration(defaultRollback = true)
public class SomethingServiceTest {
    @Autowired
    private SomethingService somethingService;
    
    @Test
    public void test() {
        int something = somethingService.getSomething();
        assertEquals(123, something);
    }
}