package com.hospital.controller;

import com.hospital.entity.WxUser;
import com.hospital.service.WxUserService;
import com.hospital.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/wx")
public class WxUserController {

    @Autowired
    private WxUserService wxUserService;

    @GetMapping("/user/info/{id}")
    public ResultVo<Map<String, Object>> info(@PathVariable Integer id) {
        WxUser user = wxUserService.getById(id);
        if (user == null) {
            return ResultVo.error("用户不存在");
        }
        Map<String, Object> m = new HashMap<>();
        m.put("userId", user.getUserId());
        m.put("userName", user.getUserName());
        m.put("nickName", user.getNickName());
        m.put("phone", user.getPhone());
        m.put("sex", user.getSex());
        m.put("name", user.getName());
        m.put("image", user.getImage());
        m.put("status", user.getStatus());
        m.put("createTime", user.getCreateTime());
        // 兼容字段
        m.put("id", user.getUserId());
        m.put("avatarUrl", user.getImage());
        return ResultVo.success(m);
    }

    @PutMapping("/user/{id}")
    public ResultVo<?> update(@PathVariable Integer id, @RequestBody WxUser user) {
        user.setUserId(id);
        wxUserService.updateById(user);
        return ResultVo.success();
    }
}
