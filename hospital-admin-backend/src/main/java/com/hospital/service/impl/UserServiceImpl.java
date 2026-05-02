package com.hospital.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hospital.entity.User;
import com.hospital.mapper.UserMapper;
import com.hospital.service.UserService;
import com.hospital.utils.Md5Util;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    public User login(String username, String password) {
        User user = baseMapper.selectByUsername(username);
        if (user != null && Md5Util.matches(password, user.getPassword())) {
            return user;
        }
        return null;
    }

    @Override
    public User wxLogin(String openId) {
        return baseMapper.selectByWxOpenId(openId);
    }

    @Override
    public boolean resetPassword(Long userId) {
        User user = getById(userId);
        if (user == null) return false;
        user.setPassword(Md5Util.encrypt("666666"));
        return updateById(user);
    }
}
