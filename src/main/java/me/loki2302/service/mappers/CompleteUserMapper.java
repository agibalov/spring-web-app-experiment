package me.loki2302.service.mappers;

import me.loki2302.dao.rows.UserRow;
import me.loki2302.service.dto.user.CompleteUser;

import org.springframework.stereotype.Component;

@Component
public class CompleteUserMapper {
    public CompleteUser makeCompleteUser(UserRow userRow) {
        CompleteUser user = new CompleteUser();
        user.UserId = userRow.Id;
        user.Name = userRow.Name;
        return user;
    }
}