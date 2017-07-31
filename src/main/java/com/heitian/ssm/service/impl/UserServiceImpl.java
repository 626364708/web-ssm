package com.heitian.ssm.service.impl;

import com.heitian.ssm.dao.UserDao;
import com.heitian.ssm.model.User;
import com.heitian.ssm.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;


@Service
@Transactional(rollbackFor = Exception.class)
public class UserServiceImpl implements UserService {
    
    @Resource
    private UserDao userDao;


    
    public List<User> getAllUser() {
        return userDao.selectAllUser();
    }

    public List<Map<String, Object>> getall() {
        return userDao.getall();
    }



    public void add(List<Object> objects) {
        for (Object object : objects) {
                     userDao.add(object);
        }
    }
}
