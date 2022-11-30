package com.health.service;

import java.util.List;
import java.util.Map;

/**
 * 统计分析接口
 * @author LuoPing
 * @project IntelliJ IDEA
 * @Package health_sys
 * @Date 2022/11/20 23:57
 */
public interface ReportService {
//    会员统计
    List<Integer> getMemberReport(List<String> months);
    //套餐预约统计
    Map<String, Object> getSetmealReport();

    Map<String, Object> getBusinessReportData();
}
