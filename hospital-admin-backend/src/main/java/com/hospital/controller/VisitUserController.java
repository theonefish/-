package com.hospital.controller;

import com.hospital.entity.VisitUser;
import com.hospital.service.VisitUserService;
import com.hospital.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/visit-user")
public class VisitUserController {

    @Autowired
    private VisitUserService visitUserService;

    @GetMapping("/list/{userId}")
    public ResultVo<List<Map<String, Object>>> list(@PathVariable Integer userId) {
        List<VisitUser> list = visitUserService.lambdaQuery()
                .eq(VisitUser::getUserId, userId)
                .list();

        List<Map<String, Object>> result = list.stream().map(v -> {
            Map<String, Object> m = new HashMap<>();
            m.put("visitId", v.getVisitId());
            m.put("userId", v.getUserId());
            m.put("visitname", v.getVisitname());
            m.put("sex", v.getSex());
            m.put("birthday", v.getBirthday());
            m.put("phone", v.getPhone());
            m.put("idCard", v.getIdCard());
            // 兼容字段
            m.put("name", v.getVisitname());
            m.put("cardNo", v.getIdCard());
            m.put("age", v.getBirthday() != null ? calculateAge(v.getBirthday()) : null);
            m.put("relation", "本人");
            m.put("isDefault", "1");
            return m;
        }).collect(Collectors.toList());

        return ResultVo.success(result);
    }

    @PostMapping
    public ResultVo<?> add(@RequestBody VisitUser visitUser) {
        visitUserService.save(visitUser);
        return ResultVo.success();
    }

    @PutMapping("/{id}")
    public ResultVo<?> update(@PathVariable Integer id, @RequestBody VisitUser visitUser) {
        visitUser.setVisitId(id);
        visitUserService.updateById(visitUser);
        return ResultVo.success();
    }

    @DeleteMapping("/{id}")
    public ResultVo<?> delete(@PathVariable Integer id) {
        visitUserService.removeById(id);
        return ResultVo.success();
    }

    private Integer calculateAge(String birthday) {
        try {
            int year = Integer.parseInt(birthday.substring(0, 4));
            return java.time.Year.now().getValue() - year;
        } catch (Exception e) {
            return null;
        }
    }
}
