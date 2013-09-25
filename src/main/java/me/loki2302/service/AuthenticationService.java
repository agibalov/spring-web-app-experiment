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
        
        int userId = user.Id;
        AuthenticationResult authenticationResult = startSessionForUser(userId);
        return authenticationResult;
    }
    
    public AuthenticationResult signUp(String userName, String password) {
        UserRow user = userDao.findUserByUserName(userName);
        if(user != null) {
            throw new UserNameAlreadyUsedException();
        }
        
        user = userDao.createUser(userName, password);
        
        int userId = user.Id;
        AuthenticationResult authenticationResult = startSessionForUser(userId);
        return authenticationResult;
    }
    
    private AuthenticationResult startSessionForUser(int userId) {
        String sessionToken = UUID.randomUUID().toString();
        SessionRow session = sessionDao.createSession(userId, sessionToken);
        
        AuthenticationResult authenticationResult = new AuthenticationResult();
        authenticationResult.SessionToken = session.Token;
        authenticationResult.UserId = session.UserId;
        return authenticationResult;
    }
}