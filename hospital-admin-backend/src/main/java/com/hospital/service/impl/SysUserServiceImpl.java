package com.hospital.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hospital.entity.SysUser;
import com.hospital.mapper.SysUserMapper;
import com.hospital.service.SysUserService;
import com.hospital.utils.Md5Util;
import org.springframework.stereotype.Service;

@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    @Override
    public SysUser login(String username, String password) {
        SysUser user = baseMapper.selectByUsername(username);
        if (user != null && Md5Util.matches(password, user.getPassword())) {
            return user;
        }
        return null;
    }

    @Override
    public boolean resetPassword(Integer userId) {
        SysUser user = getById(userId);
        if (user == null) return false;
        user.setPassword(Md5Util.encrypt("666666"));
        return updateById(user);
    }
}
