package com.health.dao;

import com.health.pojo.OrderSetting;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 预约设置数据操作
 * @author LuoPing
 * @date 2022/11/1 17:13
 */
public interface OrderSettingDao {

    int findCountByOrderDate(@Param("orderDate") Date orderDate);

    void update(OrderSetting orderSetting);

    void save(OrderSetting orderSetting);

    List<OrderSetting> queryOrderSetting(@Param("startDate") String startDate, @Param("endDate") String endDate);
//<!--    根据日期设置可预约人数-->
    void updateOrderSetting(OrderSetting orderSetting);
    //查询是可以进行预约
    OrderSetting findByOrderDate(@Param("orderDate") Date orderDate);

    void updateOrderSettingByDate(OrderSetting orderSetting);
}
