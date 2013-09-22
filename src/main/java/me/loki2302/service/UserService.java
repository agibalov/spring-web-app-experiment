package me.loki2302.service;

import me.loki2302.dao.UserDao;
import me.loki2302.dao.rows.UserRow;
import me.loki2302.service.dto.user.BriefUser;
import me.loki2302.service.dto.user.CompleteUser;
import me.loki2302.service.exceptions.UserNotFoundException;
import me.loki2302.service.mappers.BriefUserMapper;
import me.loki2302.service.mappers.CompleteUserMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserDao userDao;
    
    @Autowired
    private BriefUserMapper briefUserMapper;
    
    @Autowired
    private CompleteUserMapper completeUserMapper;
    
    public BriefUser getBriefUser(int userId) {
        UserRow userRow = userDao.getUser(userId);
        if(userRow == null) {
            throw new UserNotFoundException();
        }
        
        return briefUserMapper.makeBriefUser(userRow);
    }
    
    public CompleteUser getCompleteUser(int userId) {
        UserRow userRow = userDao.getUser(userId);
        if(userRow == null) {
            throw new UserNotFoundException();
        }
        
        return completeUserMapper.makeCompleteUser(userRow);
    }
}