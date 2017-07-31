package com.heitian.ssm.service;

import com.heitian.ssm.model.User;

import java.util.List;
import java.util.Map;


public interface UserService {

    List<User> getAllUser();
    List<Map<String ,Object>> getall();

    void add(List<Object> objects);
}
