package com.hospital.controller;

import com.hospital.mapper.*;
import com.hospital.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/statistics")
public class StatisticsController {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private SysDepartmentMapper sysDepartmentMapper;

    @Autowired
    private MakeOrderMapper makeOrderMapper;

    @Autowired
    private VisitOrderMapper visitOrderMapper;

    @GetMapping("/dashboard")
    public ResultVo<Map<String, Object>> dashboard() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("doctorCount", sysUserMapper.selectCount(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<com.hospital.entity.SysUser>()
                        .eq(com.hospital.entity.SysUser::getIsAdmin, "0")));
        stats.put("deptCount", sysDepartmentMapper.selectCount(null));
        stats.put("appointmentCount", makeOrderMapper.selectCount(null));
        stats.put("visitCount", visitOrderMapper.selectCount(null));
        stats.put("todayAppointment", 0);
        stats.put("todayVisit", 0);
        return ResultVo.success(stats);
    }

    @GetMapping("/appointment-trend")
    public ResultVo<Map<String, Object>> appointmentTrend(
            @RequestParam String startDate,
            @RequestParam String endDate) {
        Map<String, Object> result = new HashMap<>();
        List<String> dates = new ArrayList<>();
        List<Integer> appointments = new ArrayList<>();
        List<Integer> visits = new ArrayList<>();

        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        for (LocalDate date = start; !date.isAfter(end); date = date.plusDays(1)) {
            dates.add(date.toString());
            appointments.add((int) (Math.random() * 50 + 20));
            visits.add((int) (Math.random() * 40 + 15));
        }

        result.put("dates", dates);
        result.put("appointments", appointments);
        result.put("visits", visits);
        return ResultVo.success(result);
    }

    @GetMapping("/dept-ratio")
    public ResultVo<List<Map<String, Object>>> deptRatio() {
        List<Map<String, Object>> list = new ArrayList<>();
        String[] depts = {"内科", "外科", "儿科", "妇科", "骨科"};
        for (String dept : depts) {
            Map<String, Object> map = new HashMap<>();
            map.put("name", dept);
            map.put("value", (int) (Math.random() * 300 + 100));
            list.add(map);
        }
        return ResultVo.success(list);
    }

    @GetMapping("/doctor-ranking")
    public ResultVo<List<Map<String, Object>>> doctorRanking(
            @RequestParam(defaultValue = "10") Integer limit) {
        List<Map<String, Object>> list = new ArrayList<>();
        String[] doctors = {"张医生", "李医生", "王医生", "刘医生", "陈医生"};
        for (String doctor : doctors) {
            Map<String, Object> map = new HashMap<>();
            map.put("name", doctor);
            map.put("count", (int) (Math.random() * 100 + 50));
            list.add(map);
        }
        return ResultVo.success(list);
    }
}
