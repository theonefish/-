package com.hospital.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hospital.entity.ScheduleDetail;
import com.hospital.service.ScheduleDetailService;
import com.hospital.vo.PageVo;
import com.hospital.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/schedule")
public class ScheduleController {

    @Autowired
    private ScheduleDetailService scheduleDetailService;

    @GetMapping("/list")
    public ResultVo<PageVo<Map<String, Object>>> list(
            @RequestParam(defaultValue = "1") Long page,
            @RequestParam(defaultValue = "10") Long pageSize,
            @RequestParam(required = false) Integer doctorId,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {

        LambdaQueryWrapper<ScheduleDetail> wrapper = new LambdaQueryWrapper<>();
        if (doctorId != null) {
            wrapper.eq(ScheduleDetail::getDoctorId, doctorId);
        }
        if (startDate != null && !startDate.isEmpty()) {
            wrapper.ge(ScheduleDetail::getTimes, startDate);
        }
        if (endDate != null && !endDate.isEmpty()) {
            wrapper.le(ScheduleDetail::getTimes, endDate);
        }
        wrapper.orderByDesc(ScheduleDetail::getTimes);

        Page<ScheduleDetail> result = scheduleDetailService.page(new Page<>(page, pageSize), wrapper);
        List<Map<String, Object>> records = result.getRecords().stream().map(s -> {
            Map<String, Object> m = new HashMap<>();
            m.put("scheduleId", s.getScheduleId());
            m.put("doctorId", s.getDoctorId());
            m.put("doctorName", s.getDoctorName());
            m.put("times", s.getTimes());
            m.put("week", s.getWeek());
            m.put("witchWeek", s.getWitchWeek());
            m.put("amount", s.getAmount());
            m.put("type", s.getType());
            m.put("lastAmount", s.getLastAmount());
            // 兼容字段
            m.put("id", s.getScheduleId());
            m.put("date", s.getTimes());
            m.put("period", s.getType());
            m.put("quota", s.getAmount());
            m.put("booked", (s.getAmount() != null && s.getLastAmount() != null) ? s.getAmount() - s.getLastAmount() : 0);
            m.put("status", "1".equals(s.getType()) ? 1 : 0);
            m.put("timeType", s.getType());
            return m;
        }).collect(Collectors.toList());

        return ResultVo.success(PageVo.of(records, result.getTotal(), page, pageSize));
    }

    @PostMapping
    public ResultVo<?> add(@RequestBody ScheduleDetail schedule) {
        scheduleDetailService.save(schedule);
        return ResultVo.success();
    }

    @PutMapping("/{id}")
    public ResultVo<?> update(@PathVariable Integer id, @RequestBody ScheduleDetail schedule) {
        schedule.setScheduleId(id);
        scheduleDetailService.updateById(schedule);
        return ResultVo.success();
    }

    @DeleteMapping("/{id}")
    public ResultVo<?> delete(@PathVariable Integer id) {
        scheduleDetailService.removeById(id);
        return ResultVo.success();
    }

    @PostMapping("/batch")
    public ResultVo<?> batchAdd(@RequestBody List<ScheduleDetail> schedules) {
        scheduleDetailService.saveBatch(schedules);
        return ResultVo.success();
    }

    @PostMapping("/batch-delete")
    public ResultVo<?> batchDelete(@RequestBody Map<String, List<Integer>> params) {
        List<Integer> ids = params.get("ids");
        scheduleDetailService.removeByIds(ids);
        return ResultVo.success();
    }
}
