package com.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.health.dao.MemberDao;
import com.health.dao.OrderDao;
import com.health.dao.SetmealDao;
import com.health.pojo.Order;
import com.health.service.OrderService;
import com.health.service.ReportService;
import com.health.utils.DateUtils;
import net.sf.jasperreports.engine.JasperCompileManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 统计分析业务处理
 * @author LuoPing
 * @project IntelliJ IDEA
 * @Package health_sys
 * @Date 2022/11/20 23:58
 */
@Service(interfaceClass = ReportService.class)
public class ReportServiceImpl implements ReportService {
    @Autowired
    private MemberDao memberDao;//注入会员数据层
    @Autowired
    private SetmealDao setmealDao;
    @Autowired
    private OrderDao orderDao;
    /**
     * 根据月份获取该月份之前的会员数，
     * @param months
     * @return
     */
    @Override
    public List<Integer> getMemberReport(List<String> months) {
//        封装会员list
        List<Integer> list = new ArrayList<>();
        if (months != null && months.size() >0){
            //循环日期
            for (String month : months) {
                month += ".01";
                int countMember = memberDao.getCountByMonth(month);
                list.add(countMember);
            }
        }
        return list;
    }
    /**
     * 获取套餐预约统计
     * @return
     */
    @Override
    public Map<String, Object> getSetmealReport() {
//        data:{
//            setmealNames:{"套餐1","套餐2"},
//            setmealCount:[
//            {name: "",value:""},
//                    ]
//        }
        HashMap<String, Object> resultMap = new HashMap<>();//封装
        List<Map<String, Object>> setmealCounts = setmealDao.getSetmealReport();
        resultMap.put("setmealCount",setmealCounts);
        ArrayList<Object> list = new ArrayList<>();
        setmealCounts.forEach(map -> {//遍历预约套餐，获取套餐名字
            String name = (String) map.get("name");
            list.add(name);
        });
        resultMap.put("setmealNames",list);
        return resultMap;
    }

    /**
     * 运营统计
     * @return
     */
    @Override
    public Map<String, Object> getBusinessReportData() {
//        reportData:{
//            reportDate:null,
//                    todayNewMember :0,
//                    totalMember :0,
//                    thisWeekNewMember :0,
//                    thisMonthNewMember :0,
//                    todayOrderNumber :0,
//                    todayVisitsNumber :0,
//                    thisWeekOrderNumber :0,
//                    thisWeekVisitsNumber :0,
//                    thisMonthOrderNumber :0,
//                    thisMonthVisitsNumber :0,
//                    hotSetmeal :[]
//        }
        Map<String, Object> map = new HashMap<>();
        try {
            //        获取当前日期
            String today = DateUtils.parseDate2String(DateUtils.getToday());
            //获取本周一日期
            String thisWeekMonday = DateUtils.parseDate2String(DateUtils.getThisWeekMonday());
            //获取本月一号日期
            String firstDayThisMonth = DateUtils.parseDate2String(DateUtils.getFirstDay4ThisMonth());
            //获取今天新增会员数
            int todayNewMember = memberDao.getCountByDay(today);
            //获取总会员数
            int totalMember = memberDao.getCountAll();
//            获取本周新增会员
            int thisWeekNewMember = memberDao.getCountByThisWeek(thisWeekMonday);
            //获取本月新增会员数
            int thisMonthNewMember = memberDao.getCountByThisWeek(firstDayThisMonth);
            //今日预约数
//            String orderStatus = null;
            int todayOrderNumber = orderDao.getCountByDay(today, null);
            //今日到诊数
            int todayVisitsNumber = orderDao.getCountByDay(today, Order.ORDERSTATUS_YES);
            //本周预约数
            int thisWeekOrderNumber = orderDao.getCountByThisWeek(thisWeekMonday,null);
            //本周到诊数
            int thisWeekVisitsNumber = orderDao.getCountByThisWeek(thisWeekMonday, Order.ORDERSTATUS_YES);
            //本月预约数
            int thisMonthOrderNumber = orderDao.getCountByThisWeek(firstDayThisMonth, null);
            //本月到诊数
            int thisMonthVisitsNumber = orderDao.getCountByThisWeek(firstDayThisMonth, Order.ORDERSTATUS_YES);
//            热门套餐
            List<Map<String, Object>> hotSetmeal = setmealDao.getHotSetmeal();
            map.put("reportDate",today);
            map.put("todayNewMember",todayNewMember);
            map.put("totalMember",totalMember);
            map.put("thisWeekNewMember",thisWeekNewMember);
            map.put("thisMonthNewMember",thisMonthNewMember);
            map.put("todayOrderNumber",todayOrderNumber);
            map.put("todayVisitsNumber",todayVisitsNumber);
            map.put("thisWeekOrderNumber",thisWeekOrderNumber);
            map.put("thisWeekVisitsNumber",thisWeekVisitsNumber);
            map.put("thisMonthOrderNumber",thisMonthOrderNumber);
            map.put("thisMonthVisitsNumber",thisMonthVisitsNumber);
            map.put("hotSetmeal",hotSetmeal);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }
}
