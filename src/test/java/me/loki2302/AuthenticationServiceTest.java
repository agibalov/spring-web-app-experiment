package me.loki2302;

import me.loki2302.service.AuthenticationService;
import me.loki2302.service.exceptions.UserNotRegisteredException;

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
public class AuthenticationServiceTest {
    @Autowired
    private AuthenticationService authenticationService;
    
    @Test(expected = UserNotRegisteredException.class)
    public void canSignInIfDidNotSignUpBefore() {
        authenticationService.signIn("loki2302", "qwerty");        
    }
}