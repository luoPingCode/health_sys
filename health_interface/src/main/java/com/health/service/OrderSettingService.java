package com.health.service;

import com.health.pojo.OrderSetting;
import java.util.List;
import java.util.Map;

/**
 * @author LuoPing
 * @date 2022/11/1 17:07
 */
public interface OrderSettingService {
    void uploadExcel(List<OrderSetting> orderSettings);

    List<Map<String, Integer>> findeOrderSetting(String date);
    //根据天数设置可预约人数
    void updateOrderSetting(OrderSetting orderSetting);
//    上传文件
    
}
