package com.hospital.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hospital.entity.MakeOrder;
import com.hospital.entity.SysDepartment;
import com.hospital.entity.SysUser;
import com.hospital.entity.VisitUser;
import com.hospital.service.MakeOrderService;
import com.hospital.service.SysDepartmentService;
import com.hospital.service.SysUserService;
import com.hospital.service.VisitUserService;
import com.hospital.vo.PageVo;
import com.hospital.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/appointment")
public class AppointmentController {

    @Autowired
    private MakeOrderService makeOrderService;

    @Autowired
    private VisitUserService visitUserService;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysDepartmentService departmentService;

    @GetMapping("/list")
    public ResultVo<PageVo<Map<String, Object>>> list(
            @RequestParam(defaultValue = "1") Long page,
            @RequestParam(defaultValue = "10") Long pageSize,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer doctorId,
            @RequestParam(required = false) Integer deptId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {

        LambdaQueryWrapper<MakeOrder> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(MakeOrder::getCreateTime);

        if (doctorId != null) {
            wrapper.eq(MakeOrder::getDoctorId, doctorId);
        }
        if (deptId != null) {
            wrapper.eq(MakeOrder::getDeptId, deptId);
        }
        if (status != null && !status.isEmpty()) {
            wrapper.eq(MakeOrder::getStatus, status);
        }
        if (startDate != null && !startDate.isEmpty()) {
            wrapper.ge(MakeOrder::getTimes, startDate);
        }
        if (endDate != null && !endDate.isEmpty()) {
            wrapper.le(MakeOrder::getTimes, endDate);
        }

        Page<MakeOrder> result = makeOrderService.page(new Page<>(page, pageSize), wrapper);
        List<MakeOrder> records = result.getRecords();

        // 批量查关联名称
        List<Integer> visitUserIds = records.stream().map(MakeOrder::getVisitUserId).filter(id -> id != null).distinct().collect(Collectors.toList());
        List<Integer> doctorIds = records.stream().map(MakeOrder::getDoctorId).filter(id -> id != null).distinct().collect(Collectors.toList());
        List<Integer> deptIds = records.stream().map(MakeOrder::getDeptId).filter(id -> id != null).distinct().collect(Collectors.toList());

        Map<Integer, VisitUser> visitUserMap = visitUserIds.isEmpty() ? new HashMap<>()
                : visitUserService.listByIds(visitUserIds).stream()
                .collect(Collectors.toMap(VisitUser::getVisitId, v -> v, (a, b) -> a));
        Map<Integer, String> doctorNameMap = doctorIds.isEmpty() ? new HashMap<>()
                : sysUserService.listByIds(doctorIds).stream()
                .collect(Collectors.toMap(SysUser::getUserId, u -> u.getNickName() != null ? u.getNickName() : u.getUsername(), (a, b) -> a));
        Map<Integer, String> deptNameMap = deptIds.isEmpty() ? new HashMap<>()
                : departmentService.listByIds(deptIds).stream()
                .collect(Collectors.toMap(SysDepartment::getDeptId, SysDepartment::getDeptName, (a, b) -> a));

        List<Map<String, Object>> list = records.stream().map(o -> {
            Map<String, Object> m = new HashMap<>();
            VisitUser vu = visitUserMap.get(o.getVisitUserId());
            m.put("makeId", o.getMakeId());
            m.put("userId", o.getUserId());
            m.put("scheduleId", o.getScheduleId());
            m.put("visitUserId", o.getVisitUserId());
            m.put("doctorId", o.getDoctorId());
            m.put("deptId", o.getDeptId());
            m.put("times", o.getTimes());
            m.put("timesArea", o.getTimesArea());
            m.put("week", o.getWeek());
            m.put("createTime", o.getCreateTime());
            m.put("price", o.getPrice());
            m.put("address", o.getAddress());
            m.put("status", o.getStatus());
            m.put("hasCall", o.getHasCall());
            m.put("hasVisit", o.getHasVisit());
            // 管理端兼容字段
            m.put("id", o.getMakeId());
            m.put("patientName", vu != null ? vu.getVisitname() : "");
            m.put("patientPhone", vu != null ? vu.getPhone() : "");
            m.put("doctorName", doctorNameMap.getOrDefault(o.getDoctorId(), ""));
            m.put("deptName", deptNameMap.getOrDefault(o.getDeptId(), ""));
            m.put("date", o.getTimes());
            m.put("period", o.getTimesArea());
            return m;
        }).collect(Collectors.toList());

        return ResultVo.success(PageVo.of(list, result.getTotal(), page, pageSize));
    }

    @PostMapping
    public ResultVo<?> add(@RequestBody MakeOrder order) {
        makeOrderService.save(order);
        return ResultVo.success();
    }

    @PutMapping("/{id}")
    public ResultVo<?> update(@PathVariable Integer id, @RequestBody MakeOrder order) {
        order.setMakeId(id);
        makeOrderService.updateById(order);
        return ResultVo.success();
    }

    @DeleteMapping("/{id}")
    public ResultVo<?> delete(@PathVariable Integer id) {
        makeOrderService.removeById(id);
        return ResultVo.success();
    }

    @PostMapping("/{id}/call")
    public ResultVo<?> callNumber(@PathVariable Integer id) {
        boolean success = makeOrderService.callNumber(id);
        if (!success) {
            return ResultVo.error("叫号失败");
        }
        return ResultVo.success();
    }
}
