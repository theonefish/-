package com.hospital.service;

import java.util.List;
import java.util.Map;

public interface StatisticsService {

    Map<String, Object> getDashboardStats();

    Map<String, Object> getAppointmentTrend(String startDate, String endDate);

    List<Map<String, Object>> getDeptRatio();

    List<Map<String, Object>> getDoctorRanking(Integer limit);
}
