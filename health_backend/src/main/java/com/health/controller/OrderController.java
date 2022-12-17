package com.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.health.entity.Conditions;
import com.health.entity.PageResult;
import com.health.service.OrderService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author LuoPing
 * @project IntelliJ IDEA
 * @Package health_sys
 * @Date 2022/12/9 16:22
 */
@RestController
@RequestMapping("order")
public class OrderController {

    @Reference
    OrderService orderService;

    /**
     * 后台根据条件分页查询订单
     * @param conditions
     * @return
     */
    @PostMapping("findByPageAndCondition")
    public PageResult findByPageAndCondition(@RequestBody Conditions conditions){
        return orderService.findByPageAndCondition(conditions);
    }
}
