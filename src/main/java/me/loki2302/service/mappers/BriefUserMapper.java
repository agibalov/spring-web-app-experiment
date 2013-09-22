package me.loki2302.service.mappers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.loki2302.dao.rows.UserRow;
import me.loki2302.service.dto.user.BriefUser;

import org.springframework.stereotype.Component;

@Component
public class BriefUserMapper {
    public Map<Integer, BriefUser> makeBriefUsersMap(List<UserRow> userRows) {
        Map<Integer, BriefUser> usersMap = new HashMap<Integer, BriefUser>();
        for(UserRow userRow : userRows) {
            BriefUser briefUser = makeBriefUser(userRow);
            usersMap.put(briefUser.UserId, briefUser);
        }
        return usersMap;
    }   
    
    public BriefUser makeBriefUser(UserRow userRow) {
        BriefUser user = new BriefUser();
        user.UserId = userRow.Id;
        user.Name = userRow.Name;
        return user;
    }
}