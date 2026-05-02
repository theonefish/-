package com.hospital.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hospital.entity.*;
import com.hospital.service.*;
import com.hospital.vo.PageVo;
import com.hospital.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/system")
public class SystemController {

    @Autowired
    private SysMenuService sysMenuService;

    @Autowired
    private SysRoleService sysRoleService;

    @Autowired
    private SysNoticeService sysNoticeService;

    @Autowired
    private SuggestService suggestService;

    // ==================== 菜单管理 ====================
    @GetMapping("/menu/list")
    public ResultVo<List<Map<String, Object>>> menuList() {
        List<SysMenu> menus = sysMenuService.list();
        List<Map<String, Object>> tree = sysMenuService.buildMenuTree(menus).stream().map(this::convertMenu).collect(Collectors.toList());
        return ResultVo.success(tree);
    }

    private Map<String, Object> convertMenu(SysMenu menu) {
        Map<String, Object> m = new HashMap<>();
        m.put("menuId", menu.getMenuId());
        m.put("parentId", menu.getParentId());
        m.put("title", menu.getTitle());
        m.put("code", menu.getCode());
        m.put("name", menu.getName());
        m.put("path", menu.getPath());
        m.put("url", menu.getUrl());
        m.put("type", menu.getType());
        m.put("icon", menu.getIcon());
        m.put("orderNum", menu.getOrderNum());
        // 兼容字段
        m.put("id", menu.getMenuId());
        m.put("component", menu.getUrl());
        m.put("sort", menu.getOrderNum());
        m.put("hidden", false);
        m.put("permission", menu.getCode());
        if (menu.getChildren() != null) {
            m.put("children", menu.getChildren().stream().map(this::convertMenu).collect(Collectors.toList()));
        }
        return m;
    }

    @PostMapping("/menu")
    public ResultVo<?> addMenu(@RequestBody SysMenu menu) {
        sysMenuService.save(menu);
        return ResultVo.success();
    }

    @PutMapping("/menu/{id}")
    public ResultVo<?> updateMenu(@PathVariable Integer id, @RequestBody SysMenu menu) {
        menu.setMenuId(id);
        sysMenuService.updateById(menu);
        return ResultVo.success();
    }

    @DeleteMapping("/menu/{id}")
    public ResultVo<?> deleteMenu(@PathVariable Integer id) {
        sysMenuService.removeById(id);
        return ResultVo.success();
    }

    @GetMapping("/menu/parent")
    public ResultVo<List<SysMenu>> parentMenus() {
        return ResultVo.success(sysMenuService.getParentMenus());
    }

    // ==================== 角色管理 ====================
    @GetMapping("/role/list")
    public ResultVo<PageVo<Map<String, Object>>> roleList(
            @RequestParam(defaultValue = "1") Long page,
            @RequestParam(defaultValue = "10") Long pageSize,
            @RequestParam(required = false) String keyword) {
        LambdaQueryWrapper<SysRole> wrapper = new LambdaQueryWrapper<>();
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.like(SysRole::getRoleName, keyword);
        }
        wrapper.orderByDesc(SysRole::getCreateTime);
        Page<SysRole> result = sysRoleService.page(new Page<>(page, pageSize), wrapper);
        List<Map<String, Object>> records = result.getRecords().stream().map(r -> {
            Map<String, Object> m = new HashMap<>();
            m.put("roleId", r.getRoleId());
            m.put("roleName", r.getRoleName());
            m.put("type", r.getType());
            m.put("remark", r.getRemark());
            m.put("createTime", r.getCreateTime());
            // 兼容字段
            m.put("id", r.getRoleId());
            m.put("name", r.getRoleName());
            m.put("code", r.getType());
            m.put("description", r.getRemark());
            m.put("menuIds", new Integer[]{});
            m.put("status", 1);
            return m;
        }).collect(Collectors.toList());
        return ResultVo.success(PageVo.of(records, result.getTotal(), page, pageSize));
    }

    @PostMapping("/role")
    public ResultVo<?> addRole(@RequestBody SysRole role) {
        sysRoleService.save(role);
        return ResultVo.success();
    }

    @PutMapping("/role/{id}")
    public ResultVo<?> updateRole(@PathVariable Integer id, @RequestBody SysRole role) {
        role.setRoleId(id);
        sysRoleService.updateById(role);
        return ResultVo.success();
    }

    @DeleteMapping("/role/{id}")
    public ResultVo<?> deleteRole(@PathVariable Integer id) {
        sysRoleService.removeById(id);
        return ResultVo.success();
    }

    // ==================== 公告管理 ====================
    @GetMapping("/notice/list")
    public ResultVo<PageVo<Map<String, Object>>> noticeList(
            @RequestParam(defaultValue = "1") Long page,
            @RequestParam(defaultValue = "10") Long pageSize) {
        LambdaQueryWrapper<SysNotice> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(SysNotice::getCreateTime);
        Page<SysNotice> result = sysNoticeService.page(new Page<>(page, pageSize), wrapper);
        List<Map<String, Object>> records = result.getRecords().stream().map(n -> {
            Map<String, Object> m = new HashMap<>();
            m.put("noticeId", n.getNoticeId());
            m.put("noticeTitle", n.getNoticeTitle());
            m.put("noticeText", n.getNoticeText());
            m.put("createTime", n.getCreateTime());
            // 兼容字段
            m.put("id", n.getNoticeId());
            m.put("title", n.getNoticeTitle());
            m.put("content", n.getNoticeText());
            m.put("showHome", false);
            m.put("status", 1);
            return m;
        }).collect(Collectors.toList());
        return ResultVo.success(PageVo.of(records, result.getTotal(), page, pageSize));
    }

    @PostMapping("/notice")
    public ResultVo<?> addNotice(@RequestBody SysNotice notice) {
        sysNoticeService.save(notice);
        return ResultVo.success();
    }

    @PutMapping("/notice/{id}")
    public ResultVo<?> updateNotice(@PathVariable Integer id, @RequestBody SysNotice notice) {
        notice.setNoticeId(id);
        sysNoticeService.updateById(notice);
        return ResultVo.success();
    }

    @DeleteMapping("/notice/{id}")
    public ResultVo<?> deleteNotice(@PathVariable Integer id) {
        sysNoticeService.removeById(id);
        return ResultVo.success();
    }

    // ==================== 反馈管理 ====================
    @GetMapping("/feedback/list")
    public ResultVo<PageVo<Map<String, Object>>> feedbackList(
            @RequestParam(defaultValue = "1") Long page,
            @RequestParam(defaultValue = "10") Long pageSize) {
        LambdaQueryWrapper<Suggest> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(Suggest::getCreateTime);
        Page<Suggest> result = suggestService.page(new Page<>(page, pageSize), wrapper);
        List<Map<String, Object>> records = result.getRecords().stream().map(s -> {
            Map<String, Object> m = new HashMap<>();
            m.put("id", s.getId());
            m.put("userId", s.getUserId());
            m.put("title", s.getTitle());
            m.put("content", s.getContent());
            m.put("createTime", s.getCreateTime());
            // 兼容字段
            m.put("userName", "");
            m.put("contact", "");
            m.put("status", 0);
            return m;
        }).collect(Collectors.toList());
        return ResultVo.success(PageVo.of(records, result.getTotal(), page, pageSize));
    }

    @GetMapping("/feedback/{id}")
    public ResultVo<Map<String, Object>> feedbackDetail(@PathVariable Integer id) {
        Suggest s = suggestService.getById(id);
        if (s == null) {
            return ResultVo.error("反馈不存在");
        }
        Map<String, Object> m = new HashMap<>();
        m.put("id", s.getId());
        m.put("userId", s.getUserId());
        m.put("title", s.getTitle());
        m.put("content", s.getContent());
        m.put("createTime", s.getCreateTime());
        m.put("userName", "");
        m.put("contact", "");
        m.put("status", 0);
        return ResultVo.success(m);
    }

    @DeleteMapping("/feedback/{id}")
    public ResultVo<?> deleteFeedback(@PathVariable Integer id) {
        suggestService.removeById(id);
        return ResultVo.success();
    }
}
