package com.hospital.controller;

import com.hospital.entity.SysUser;
import com.hospital.entity.SysMenu;
import com.hospital.entity.WxUser;
import com.hospital.service.SysUserService;
import com.hospital.service.SysMenuService;
import com.hospital.service.WxUserService;
import com.hospital.utils.JwtUtil;
import com.hospital.utils.Md5Util;
import com.hospital.vo.ResultVo;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private WxUserService wxUserService;

    @PostMapping("/login")
    public ResultVo<Map<String, Object>> login(@RequestBody Map<String, String> params) {
        String username = params.get("username");
        String password = params.get("password");
        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            return ResultVo.error("用户名和密码不能为空");
        }

        SysUser user = sysUserService.login(username, password);
        if (user == null) {
            return ResultVo.error("用户名或密码错误");
        }

        if (user.getIsEnabled() != null && user.getIsEnabled() == 0) {
            return ResultVo.error("账号已被禁用");
        }

        String role = "1".equals(user.getIsAdmin()) ? "admin" : "doctor";
        String token = JwtUtil.generateToken(user.getUserId().longValue(), user.getUsername(), role);

        Map<String, Object> data = new HashMap<>();
        data.put("token", token);
        data.put("userId", user.getUserId());
        data.put("expires", 7200);
        return ResultVo.success(data);
    }

    @GetMapping("/info")
    public ResultVo<Map<String, Object>> info(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        String role = (String) request.getAttribute("role");

        Map<String, Object> data = new HashMap<>();

        if ("patient".equals(role)) {
            WxUser user = wxUserService.getById(userId.intValue());
            if (user == null) {
                return ResultVo.unauthorized();
            }
            data.put("id", user.getUserId());
            data.put("username", user.getUserName());
            data.put("nickname", user.getNickName());
            data.put("avatar", user.getImage());
            data.put("role", "患者");
            data.put("roleId", 3);
            data.put("permissions", new String[]{"*"});
        } else {
            SysUser user = sysUserService.getById(userId.intValue());
            if (user == null) {
                return ResultVo.unauthorized();
            }
            data.put("id", user.getUserId());
            data.put("username", user.getUsername());
            data.put("nickname", user.getNickName());
            data.put("avatar", user.getImage());
            data.put("roleId", user.getDeptId());
            data.put("role", "1".equals(user.getIsAdmin()) ? "超级管理员" : "医生");
            data.put("permissions", new String[]{"*"});
        }
        return ResultVo.success(data);
    }

    @Autowired
    private SysMenuService sysMenuService;

    @GetMapping("/menus")
    public ResultVo<?> menus() {
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

    @PostMapping("/password")
    public ResultVo<?> password(HttpServletRequest request, @RequestBody Map<String, String> params) {
        Long userId = (Long) request.getAttribute("userId");
        String oldPassword = params.get("oldPassword");
        String newPassword = params.get("newPassword");
        if (oldPassword == null || oldPassword.isEmpty() || newPassword == null || newPassword.isEmpty()) {
            return ResultVo.error("原密码和新密码不能为空");
        }

        SysUser user = sysUserService.getById(userId.intValue());
        if (user == null || !Md5Util.matches(oldPassword, user.getPassword())) {
            return ResultVo.error("原密码错误");
        }

        user.setPassword(Md5Util.encrypt(newPassword));
        sysUserService.updateById(user);
        return ResultVo.success();
    }

    // 小程序微信登录
    @PostMapping("/wx-login")
    public ResultVo<Map<String, Object>> wxLogin(@RequestBody Map<String, String> params) {
        String openId = params.get("openId");
        // 实际应通过code换取openId
        WxUser user = wxUserService.lambdaQuery().eq(WxUser::getUserName, openId).one();
        if (user == null) {
            return ResultVo.error("用户未注册");
        }

        String token = JwtUtil.generateToken(user.getUserId().longValue(), user.getUserName(), "patient");
        Map<String, Object> data = new HashMap<>();
        data.put("token", token);
        data.put("userId", user.getUserId());
        data.put("expires", 7200);
        return ResultVo.success(data);
    }

    // 小程序用户注册
    @PostMapping("/wx-register")
    public ResultVo<?> wxRegister(@RequestBody WxUser user) {
        user.setPassword(Md5Util.encrypt(user.getPassword()));
        user.setStatus(1);
        wxUserService.save(user);
        return ResultVo.success();
    }
}
