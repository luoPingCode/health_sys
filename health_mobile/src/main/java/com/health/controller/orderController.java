package com.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.health.constant.MessageConstant;
import com.health.constant.RedisMessageConstant;
import com.health.entity.Result;
import com.health.pojo.Order;
import com.health.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.JedisPool;

import java.util.Map;

/**
 * 预约
 * @author LuoPing
 * @date 2022/11/9 19:11
 */
@RestController
@RequestMapping("order")
public class orderController {

    @Reference
    OrderService orderService;

    /**
     * 进行预约
     * @param map
     * @return
     */
    @PostMapping("submit")
    public Result submit(@RequestBody Map<String, String> map){
//        调用service
        try {
            return orderService.submitOrder(map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.ORDER_FAIL);
        }

//        return new Result(false,MessageConstant.ORDER_SUCCESS);
    }

    /**
     * 预约成功后，查询预约信息，用于成功页面显示
     * @param id
     * @return
     */
    @GetMapping("findById")
    public Result findById(@RequestParam("id") Integer id){
        try {
            Map<String, String> orderMap = orderService.findById(id);
            return new Result(true,MessageConstant.QUERY_ORDER_SUCCESS,orderMap);
        }catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_ORDER_FAIL);
        }
    }
}
