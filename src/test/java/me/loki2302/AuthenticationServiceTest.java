package me.loki2302;

import me.loki2302.service.AuthenticationService;
import me.loki2302.service.UserType;
import me.loki2302.service.dto.AuthenticationResult;
import me.loki2302.service.exceptions.IncorrectPasswordException;
import me.loki2302.service.exceptions.UserNameAlreadyUsedException;
import me.loki2302.service.exceptions.UserNotRegisteredException;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import static org.junit.Assert.*;

public class AuthenticationServiceTest extends AbstractServiceTest {
    @Autowired
    private AuthenticationService authenticationService;
    
    @Test(expected = UserNotRegisteredException.class)
    public void cantSignInIfNotRegistered() {
        authenticationService.signIn("loki2302", "qwerty");        
    }
    
    @Test
    public void canSignInIfRegistered() {
        AuthenticationResult authResultA = authenticationService.signUp("loki2302", "qwerty", UserType.Reader);
        assertAdequateAuthenticationResult(authResultA);
        
        AuthenticationResult authResultB = authenticationService.signIn("loki2302", "qwerty");
        assertAdequateAuthenticationResult(authResultB);
        
        assertEquals(authResultA.UserId, authResultB.UserId);
    }
    
    @Test
    public void canSignUp() {
        AuthenticationResult authenticationResult = 
                authenticationService.signUp("loki2302", "qwerty", UserType.Reader);
        assertAdequateAuthenticationResult(authenticationResult);
    }
    
    @Test(expected = UserNameAlreadyUsedException.class)
    public void cantSignUpTwice() {
        authenticationService.signUp("loki2302", "qwerty", UserType.Reader);
        authenticationService.signUp("loki2302", "qwerty", UserType.Reader);
    }
    
    @Test(expected = IncorrectPasswordException.class)
    public void cantSignInWithIncorrectPassword() {
        authenticationService.signUp("loki2302", "qwerty", UserType.Reader);
        authenticationService.signIn("loki2302", "qwert");
    }
    
    private static void assertAdequateAuthenticationResult(AuthenticationResult authenticationResult) {
        assertNotNull(authenticationResult.SessionToken);        
        assertNotNull(authenticationResult.UserName);
        assertNotNull(authenticationResult.UserType);
    }
}