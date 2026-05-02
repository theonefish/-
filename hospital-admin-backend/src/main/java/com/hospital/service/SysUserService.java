package com.hospital.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hospital.entity.SysUser;

public interface SysUserService extends IService<SysUser> {

    SysUser login(String username, String password);

    boolean resetPassword(Integer userId);
}
