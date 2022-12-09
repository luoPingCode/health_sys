package com.health.dao;

import com.github.pagehelper.Page;
import com.health.entity.Conditions;
import com.health.pojo.Order;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.Map;

/**
 * @author LuoPing
 * @date 2022/11/9 23:22
 */
public interface OrderDao {
    //根据会员id查询患者是否进行预约
    Order getOrderByMemberId(@Param("memberId") Integer memberId);
    //添加预约信息
    void addOrder(Order orderData);

    Map<String, String> getOrderById(@Param("id") Integer id);

    int getCountByDay(@Param("today") String today, @Param("orderStatus")String orderStatus);

    int getCountByThisWeek(@Param("thisWeekMonday") String thisWeekMonday, @Param("orderStatus")String orderStatus);

    Page<Order> findByPageAndCondition(Map<String, Object> map);
}
