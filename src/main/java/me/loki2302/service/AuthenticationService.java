package me.loki2302.service;

import java.util.UUID;

import me.loki2302.dao.SessionDao;
import me.loki2302.dao.UserDao;
import me.loki2302.dao.rows.SessionRow;
import me.loki2302.dao.rows.UserRow;
import me.loki2302.service.dto.AuthenticationResult;
import me.loki2302.service.exceptions.IncorrectPasswordException;
import me.loki2302.service.exceptions.UserNameAlreadyUsedException;
import me.loki2302.service.exceptions.UserNotRegisteredException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    @Autowired
    private UserDao userDao;
    
    @Autowired
    private SessionDao sessionDao;
            
    public AuthenticationResult signIn(String userName, String password) {
        UserRow user = userDao.findUserByUserName(userName);
        if(user == null) {
            throw new UserNotRegisteredException();
        }
        
        if(!user.Password.equals(password)) {
            throw new IncorrectPasswordException();
        }
        
        AuthenticationResult authenticationResult = startSessionForUser(user);
        return authenticationResult;
    }
    
    public AuthenticationResult signUp(String userName, String password) {
        UserRow user = userDao.findUserByUserName(userName);
        if(user != null) {
            throw new UserNameAlreadyUsedException();
        }
        
        user = userDao.createUser(userName, password);
        
        AuthenticationResult authenticationResult = startSessionForUser(user);
        return authenticationResult;
    }
    
    public AuthenticationResult getSessionInfo(String sessionToken) {
        SessionRow session = sessionDao.getSession(sessionToken);
        if(session == null) {
            throw new RuntimeException("no such session");
        }
        
        int userId = session.UserId;
        UserRow user = userDao.getUser(userId);
        AuthenticationResult authenticationResult = makeAuthenticationResult(sessionToken, user);
        return authenticationResult;
    }
    
    private AuthenticationResult startSessionForUser(UserRow userRow) {
        String sessionToken = UUID.randomUUID().toString();
        SessionRow session = sessionDao.createSession(userRow.Id, sessionToken);
        
        AuthenticationResult authenticationResult = makeAuthenticationResult(session.Token, userRow);
        return authenticationResult;
    }
    
    private AuthenticationResult makeAuthenticationResult(String sessionToken, UserRow userRow) {
        AuthenticationResult authenticationResult = new AuthenticationResult();
        authenticationResult.SessionToken = sessionToken;
        authenticationResult.UserId = userRow.Id;
        authenticationResult.UserName = userRow.Name;
        return authenticationResult;
    }
}