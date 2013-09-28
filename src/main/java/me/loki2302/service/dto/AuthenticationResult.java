package me.loki2302.service.dto;

import me.loki2302.service.UserType;

public class AuthenticationResult {
    public String SessionToken;
    public int UserId;
    public String UserName;
    public UserType UserType;
}