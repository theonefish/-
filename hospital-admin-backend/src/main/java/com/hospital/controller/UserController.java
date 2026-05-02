package com.hospital.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hospital.entity.WxUser;
import com.hospital.service.WxUserService;
import com.hospital.utils.Md5Util;
import com.hospital.vo.PageVo;
import com.hospital.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private WxUserService wxUserService;

    @GetMapping("/list")
    public ResultVo<PageVo<Map<String, Object>>> list(
            @RequestParam(defaultValue = "1") Long page,
            @RequestParam(defaultValue = "10") Long pageSize,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer status) {

        LambdaQueryWrapper<WxUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(WxUser::getCreateTime);

        if (keyword != null && !keyword.isEmpty()) {
            wrapper.and(w -> w.like(WxUser::getName, keyword).or().like(WxUser::getPhone, keyword));
        }
        if (status != null) {
            wrapper.eq(WxUser::getStatus, status);
        }

        Page<WxUser> result = wxUserService.page(new Page<>(page, pageSize), wrapper);
        List<Map<String, Object>> records = result.getRecords().stream().map(u -> {
            Map<String, Object> m = new HashMap<>();
            m.put("userId", u.getUserId());
            m.put("userName", u.getUserName());
            m.put("nickName", u.getNickName());
            m.put("phone", u.getPhone());
            m.put("sex", u.getSex());
            m.put("name", u.getName());
            m.put("image", u.getImage());
            m.put("status", u.getStatus());
            m.put("createTime", u.getCreateTime());
            // 兼容字段
            m.put("id", u.getUserId());
            m.put("gender", u.getSex() != null ? Integer.parseInt(u.getSex()) : 2);
            m.put("age", 0);
            m.put("idCard", "");
            return m;
        }).collect(Collectors.toList());

        return ResultVo.success(PageVo.of(records, result.getTotal(), page, pageSize));
    }

    @PostMapping
    public ResultVo<?> add(@RequestBody WxUser user) {
        user.setPassword(Md5Util.encrypt("123456"));
        user.setStatus(1);
        wxUserService.save(user);
        return ResultVo.success();
    }

    @PutMapping("/{id}")
    public ResultVo<?> update(@PathVariable Integer id, @RequestBody WxUser user) {
        user.setUserId(id);
        wxUserService.updateById(user);
        return ResultVo.success();
    }

    @DeleteMapping("/{id}")
    public ResultVo<?> delete(@PathVariable Integer id) {
        wxUserService.removeById(id);
        return ResultVo.success();
    }

    @PostMapping("/{id}/reset-password")
    public ResultVo<?> resetPassword(@PathVariable Integer id) {
        WxUser user = wxUserService.getById(id);
        if (user == null) return ResultVo.error("用户不存在");
        user.setPassword(Md5Util.encrypt("123456"));
        wxUserService.updateById(user);
        return ResultVo.success();
    }
}
