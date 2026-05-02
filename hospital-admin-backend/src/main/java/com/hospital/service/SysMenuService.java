package com.hospital.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hospital.entity.SysMenu;

import java.util.List;

public interface SysMenuService extends IService<SysMenu> {

    List<SysMenu> getMenusByRoleId(Integer roleId);

    List<SysMenu> getParentMenus();

    List<SysMenu> buildMenuTree(List<SysMenu> menus);
}
