package me.loki2302.service;

import me.loki2302.dao.UserDao;
import me.loki2302.dao.rows.UserRow;
import me.loki2302.service.exceptions.IncorrectPasswordException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    @Autowired
    private UserDao userDao;
    
    public int signInOrSignUp(String userName, String password) {
        UserRow user = userDao.findUserByUserName(userName);
        if(user != null) {
            if(!user.Password.equals(password)) {
                throw new IncorrectPasswordException();
            }
            
            return user.Id;
        }
        
        user = userDao.createUser(userName, password);
        return user.Id;
    }
    
    public int signIn(String userName, String password) {
        throw new RuntimeException();
    }
    
    public int signUp(String userName, String password) {
        throw new RuntimeException();
    }
}