package com.hospital.service.impl;

import com.hospital.mapper.*;
import com.hospital.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class StatisticsServiceImpl implements StatisticsService {

    @Autowired
    private DoctorMapper doctorMapper;

    @Autowired
    private DepartmentMapper departmentMapper;

    @Autowired
    private AppointmentMapper appointmentMapper;

    @Autowired
    private VisitMapper visitMapper;

    @Override
    public Map<String, Object> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("doctorCount", doctorMapper.selectCount(null));
        stats.put("deptCount", departmentMapper.selectCount(null));
        stats.put("appointmentCount", appointmentMapper.selectCount(null));
        stats.put("visitCount", visitMapper.selectCount(null));
        // 今日数据需要按日期查询，这里简化处理
        stats.put("todayAppointment", 0);
        stats.put("todayVisit", 0);
        return stats;
    }

    @Override
    public Map<String, Object> getAppointmentTrend(String startDate, String endDate) {
        // 简化实现，实际应从数据库按日期聚合查询
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
        return result;
    }

    @Override
    public List<Map<String, Object>> getDeptRatio() {
        // 简化实现
        List<Map<String, Object>> list = new ArrayList<>();
        String[] depts = {"内科", "外科", "儿科", "妇科", "骨科"};
        for (String dept : depts) {
            Map<String, Object> map = new HashMap<>();
            map.put("name", dept);
            map.put("value", (int) (Math.random() * 300 + 100));
            list.add(map);
        }
        return list;
    }

    @Override
    public List<Map<String, Object>> getDoctorRanking(Integer limit) {
        // 简化实现
        List<Map<String, Object>> list = new ArrayList<>();
        String[] doctors = {"张医生", "李医生", "王医生", "刘医生", "陈医生"};
        for (String doctor : doctors) {
            Map<String, Object> map = new HashMap<>();
            map.put("name", doctor);
            map.put("count", (int) (Math.random() * 100 + 50));
            list.add(map);
        }
        return list;
    }
}
