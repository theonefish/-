package com.hospital.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hospital.entity.Menu;

import java.util.List;

public interface MenuService extends IService<Menu> {

    List<Menu> getMenusByRoleId(Long roleId);

    List<Menu> getParentMenus();

    List<Menu> buildMenuTree(List<Menu> menus);
}
