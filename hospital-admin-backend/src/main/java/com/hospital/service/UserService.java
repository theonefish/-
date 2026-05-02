package com.hospital.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hospital.entity.User;

public interface UserService extends IService<User> {

    User login(String username, String password);

    User wxLogin(String openId);

    boolean resetPassword(Long userId);
}
