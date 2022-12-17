package com.health.service;

import com.health.entity.Conditions;
import com.health.entity.PageResult;
import com.health.entity.Result;
import com.health.pojo.Order;

import java.util.Map;

/**
 * 预约套餐业务接口
 * @author LuoPing
 * @date 2022/11/9 21:47
 */
public interface OrderService {
    Result submitOrder(Map<String, String> map);

    Map<String, String> findById(Integer id);

    PageResult findByPageAndCondition(Conditions conditions);
}
