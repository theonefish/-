package com.hospital.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hospital.entity.MakeOrder;
import com.hospital.entity.SysDepartment;
import com.hospital.entity.SysUser;
import com.hospital.entity.VisitOrder;
import com.hospital.service.MakeOrderService;
import com.hospital.service.SysDepartmentService;
import com.hospital.service.SysUserService;
import com.hospital.service.VisitOrderService;
import com.hospital.vo.PageVo;
import com.hospital.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/visit")
public class VisitController {

    @Autowired
    private VisitOrderService visitOrderService;

    @Autowired
    private MakeOrderService makeOrderService;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysDepartmentService departmentService;

    @GetMapping("/list")
    public ResultVo<PageVo<Map<String, Object>>> list(
            @RequestParam(defaultValue = "1") Long page,
            @RequestParam(defaultValue = "10") Long pageSize,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer doctorId) {

        LambdaQueryWrapper<VisitOrder> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(VisitOrder::getVisitTime);

        if (doctorId != null) {
            wrapper.eq(VisitOrder::getDoctorId, doctorId);
        }

        Page<VisitOrder> result = visitOrderService.page(new Page<>(page, pageSize), wrapper);
        List<VisitOrder> records = result.getRecords();

        // 批量查关联
        List<Integer> makeIds = records.stream().map(VisitOrder::getMakeId).filter(id -> id != null).distinct().collect(Collectors.toList());
        List<Integer> doctorIds = records.stream().map(VisitOrder::getDoctorId).filter(id -> id != null).distinct().collect(Collectors.toList());
        Map<Integer, MakeOrder> makeOrderMap = makeIds.isEmpty() ? new HashMap<>()
                : makeOrderService.listByIds(makeIds).stream()
                .collect(Collectors.toMap(MakeOrder::getMakeId, m -> m, (a, b) -> a));
        Map<Integer, String> doctorNameMap = doctorIds.isEmpty() ? new HashMap<>()
                : sysUserService.listByIds(doctorIds).stream()
                .collect(Collectors.toMap(SysUser::getUserId, u -> u.getNickName() != null ? u.getNickName() : u.getUsername(), (a, b) -> a));
        Map<Integer, String> deptNameMap = departmentService.list().stream()
                .collect(Collectors.toMap(SysDepartment::getDeptId, SysDepartment::getDeptName, (a, b) -> a));

        List<Map<String, Object>> list = records.stream().map(v -> {
            Map<String, Object> m = new HashMap<>();
            MakeOrder mo = makeOrderMap.get(v.getMakeId());
            m.put("visitId", v.getVisitId());
            m.put("makeId", v.getMakeId());
            m.put("userId", v.getUserId());
            m.put("visitUserId", v.getVisitUserId());
            m.put("doctorId", v.getDoctorId());
            m.put("times", v.getTimes());
            m.put("timesArea", v.getTimesArea());
            m.put("week", v.getWeek());
            m.put("advice", v.getAdvice());
            m.put("visitTime", v.getVisitTime());
            m.put("createTime", v.getCreateTime());
            // 兼容字段
            m.put("id", v.getVisitId());
            m.put("patientName", "");
            m.put("doctorName", doctorNameMap.getOrDefault(v.getDoctorId(), ""));
            m.put("deptName", mo != null ? deptNameMap.getOrDefault(mo.getDeptId(), "") : "");
            m.put("diagnosis", v.getAdvice() != null ? v.getAdvice() : "");
            m.put("status", "1".equals(v.getHasVisit()) ? 1 : 0);
            return m;
        }).collect(Collectors.toList());

        return ResultVo.success(PageVo.of(list, result.getTotal(), page, pageSize));
    }

    @GetMapping("/user/{userId}")
    public ResultVo<List<Map<String, Object>>> listByUser(@PathVariable Integer userId) {
        List<VisitOrder> list = visitOrderService.lambdaQuery()
                .eq(VisitOrder::getUserId, userId)
                .orderByDesc(VisitOrder::getVisitTime)
                .list();

        List<Map<String, Object>> result = list.stream().map(v -> {
            Map<String, Object> m = new HashMap<>();
            m.put("visitId", v.getVisitId());
            m.put("makeId", v.getMakeId());
            m.put("advice", v.getAdvice());
            m.put("visitTime", v.getVisitTime());
            m.put("createTime", v.getCreateTime());
            m.put("diagnosis", v.getAdvice() != null ? v.getAdvice() : "");
            m.put("prescription", "");
            return m;
        }).collect(Collectors.toList());

        return ResultVo.success(result);
    }

    @PostMapping
    public ResultVo<?> add(@RequestBody VisitOrder visit) {
        visitOrderService.save(visit);
        return ResultVo.success();
    }

    @PutMapping("/{id}")
    public ResultVo<?> update(@PathVariable Integer id, @RequestBody VisitOrder visit) {
        visit.setVisitId(id);
        visitOrderService.updateById(visit);
        return ResultVo.success();
    }

    @GetMapping("/{id}")
    public ResultVo<VisitOrder> detail(@PathVariable Integer id) {
        VisitOrder visit = visitOrderService.getById(id);
        return ResultVo.success(visit);
    }
}
