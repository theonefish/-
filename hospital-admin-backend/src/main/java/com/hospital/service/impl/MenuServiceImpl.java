package com.hospital.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hospital.entity.Menu;
import com.hospital.mapper.MenuMapper;
import com.hospital.service.MenuService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    @Override
    public List<Menu> getMenusByRoleId(Long roleId) {
        return baseMapper.selectMenusByRoleId(roleId);
    }

    @Override
    public List<Menu> getParentMenus() {
        return baseMapper.selectParentMenus();
    }

    @Override
    public List<Menu> buildMenuTree(List<Menu> menus) {
        List<Menu> tree = new ArrayList<>();
        for (Menu menu : menus) {
            if (menu.getParentId() == null) {
                tree.add(findChildren(menu, menus));
            }
        }
        return tree;
    }

    private Menu findChildren(Menu menu, List<Menu> menus) {
        List<Menu> children = menus.stream()
                .filter(m -> menu.getId().equals(m.getParentId()))
                .collect(Collectors.toList());
        if (!children.isEmpty()) {
            for (Menu child : children) {
                findChildren(child, menus);
            }
        }
        return menu;
    }
}
