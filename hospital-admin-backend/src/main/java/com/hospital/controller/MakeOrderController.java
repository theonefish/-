package com.hospital.controller;

import com.hospital.entity.MakeOrder;
import com.hospital.entity.ScheduleDetail;
import com.hospital.entity.SysUser;
import com.hospital.entity.VisitUser;
import com.hospital.service.MakeOrderService;
import com.hospital.service.ScheduleDetailService;
import com.hospital.service.SysUserService;
import com.hospital.service.VisitUserService;
import com.hospital.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/make-order")
public class MakeOrderController {

    @Autowired
    private MakeOrderService makeOrderService;

    @Autowired
    private VisitUserService visitUserService;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private ScheduleDetailService scheduleDetailService;

    @GetMapping("/user/{userId}")
    public ResultVo<List<Map<String, Object>>> userOrders(@PathVariable Integer userId) {
        List<MakeOrder> list = makeOrderService.lambdaQuery()
                .eq(MakeOrder::getUserId, userId)
                .orderByDesc(MakeOrder::getCreateTime)
                .list();

        // 批量查询关联名称
        List<Integer> doctorIds = list.stream().map(MakeOrder::getDoctorId).filter(id -> id != null).distinct().collect(Collectors.toList());
        Map<Integer, String> doctorNameMap = doctorIds.isEmpty() ? new HashMap<>()
                : sysUserService.listByIds(doctorIds).stream()
                .collect(Collectors.toMap(SysUser::getUserId, u -> u.getNickName() != null ? u.getNickName() : u.getUsername(), (a, b) -> a));

        List<Map<String, Object>> result = list.stream().map(o -> {
            Map<String, Object> m = new HashMap<>();
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
            m.put("status", o.getStatus() != null ? Integer.parseInt(o.getStatus()) : 0);
            m.put("hasCall", o.getHasCall());
            m.put("hasVisit", o.getHasVisit());
            // 兼容字段
            m.put("id", o.getMakeId());
            m.put("orderNo", String.valueOf(o.getMakeId()));
            m.put("doctorName", doctorNameMap.getOrDefault(o.getDoctorId(), ""));
            m.put("date", o.getTimes());
            m.put("period", o.getTimesArea());
            return m;
        }).collect(Collectors.toList());

        return ResultVo.success(result);
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

    @PostMapping("/{id}/cancel")
    public ResultVo<?> cancel(@PathVariable Integer id) {
        MakeOrder order = makeOrderService.getById(id);
        if (order == null) {
            return ResultVo.error("订单不存在");
        }
        if (!"0".equals(order.getStatus())) {
            return ResultVo.error("只能取消待就诊的预约");
        }
        // 回滚号源
        if (order.getScheduleId() != null) {
            ScheduleDetail schedule = scheduleDetailService.getById(order.getScheduleId());
            if (schedule != null && schedule.getLastAmount() != null) {
                schedule.setLastAmount(schedule.getLastAmount() + 1);
                scheduleDetailService.updateById(schedule);
            }
        }
        order.setStatus("2");
        makeOrderService.updateById(order);
        return ResultVo.success();
    }
}
