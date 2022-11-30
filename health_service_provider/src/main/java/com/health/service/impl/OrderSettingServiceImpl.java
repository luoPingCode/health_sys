package com.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.health.dao.OrderSettingDao;
import com.health.pojo.OrderSetting;
import com.health.service.OrderSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 预约设置业务处理
 * @author LuoPing
 * @date 2022/11/1 17:10
 */
@Service(interfaceClass = OrderSettingService.class)
@Transactional
public class OrderSettingServiceImpl implements OrderSettingService {
    @Autowired
    private OrderSettingDao orderSettingDao;
//    上传文件
    @Override
    public void uploadExcel(List<OrderSetting> orderSettings) {
//        判断是否为空
        if (orderSettings != null && orderSettings.size() > 0) {
            //遍历
            for (OrderSetting orderSetting : orderSettings) {
                //            查询是否以存在当天的数据
                int count = orderSettingDao.findCountByOrderDate(orderSetting.getOrderDate());
                if (count > 0) {//如果以设置过，则更新，没有则添加
                    orderSettingDao.update(orderSetting);
                }else {
                    orderSettingDao.save(orderSetting);
                }
            }
        }
    }

    /**
     * 查询预约情况
     * @param date
     * @return
     */
    @Override
    public List<Map<String, Integer>> findeOrderSetting(String date) {
        //拼接完整的查询条件
        //月初
        String startDate = date + "-01";
        //月末，假设每个月31天
        String endDate = date + "-31";
        List<Map<String, Integer>> orderSettings = new ArrayList<>();
        //查询预约情况
        List<OrderSetting> list = orderSettingDao.queryOrderSetting(startDate, endDate);
        Calendar calendar = Calendar.getInstance();
        if (list!= null && list.size() > 0) {
            for (OrderSetting orderSetting : list) {
                Map<String, Integer> dateHashMap = new HashMap<>();
                calendar.setTime(orderSetting.getOrderDate());//将date转换为Calendar 日历
                dateHashMap.put("date",calendar.get(Calendar.DAY_OF_MONTH));
                dateHashMap.put("number",orderSetting.getNumber());
                dateHashMap.put("reservations",orderSetting.getReservations());
                orderSettings.add(dateHashMap);
            }
        }
        return orderSettings;
    }

    /**
     * 根据天数设置可预约人数
     * @param orderSetting
     */
    @Override
    public void updateOrderSetting(OrderSetting orderSetting) {
        if (orderSetting.getNumber() >= 0) {//设置的人数要大于0
//            查询该天是否以预约设置过
            int count = orderSettingDao.findCountByOrderDate(orderSetting.getOrderDate());
            if (count > 0) {//该天已经设置过，则更新
//                System.out.println(orderSetting.getOrderDate());
                orderSettingDao.updateOrderSetting(orderSetting);
            }else {
                orderSettingDao.save(orderSetting);
            }
        }
    }
}
