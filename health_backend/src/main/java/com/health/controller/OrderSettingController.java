package com.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.health.constant.MessageConstant;
import com.health.entity.Result;
import com.health.pojo.OrderSetting;
import com.health.service.OrderSettingService;
import com.health.utils.POIUtils;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author LuoPing
 * @date 2022/11/1 16:37
 */
@RestController
@RequestMapping("orderSetting")
public class OrderSettingController {
    //注入service
    @Reference
    private OrderSettingService orderSettingService;

//    上传套餐预约设置
    @PostMapping("upload")
    public Result uploadExcel(@RequestParam("excelFile") MultipartFile excelFile){
        try {
            //使用POI工具获取Excel内容
            List<String[]> list = POIUtils.readExcel(excelFile);
//            System.out.println(list);
            //创建一个List
            List<OrderSetting> orderSettings = new ArrayList<>();
            for (String[] strings : list) {//遍历
                //获取日期
                String data = strings[0];
                //获取最大预约人数
                String number = strings[1];
//                System.out.println(data + " ----" + number);
                //将日期和人数封装到OrderSetting对象中
                OrderSetting orderSetting = new OrderSetting(new Date(strings[0]), Integer.parseInt(strings[1]));
                //将对象存入集合
                orderSettings.add(orderSetting);
            }
            orderSettingService.uploadExcel(orderSettings);
            return new Result(true,MessageConstant.IMPORT_ORDERSETTING_SUCCESS);
        }catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.IMPORT_ORDERSETTING_FAIL);
        }
    }

    /**
     * 查询预约情况
     * @param date
     * @return
     */
    @GetMapping("getOrderSettingByMonth")
    public Result getOrderSettingByMonth(@RequestParam("date") String date){
        try {
            //查询预约数据设置
            List<Map<String, Integer>> orderSettings = orderSettingService.findeOrderSetting(date);
            return new Result(true,MessageConstant.QUERY_ORDER_SUCCESS,orderSettings);
        }catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_ORDER_FAIL);
        }
    }

    /**
     * 根据天数修改预约设置
     * @param orderSetting
     * @return
     */
    @PutMapping
    public Result updateOrderSetting(@RequestBody OrderSetting orderSetting) {
        try {
            orderSettingService.updateOrderSetting(orderSetting);
            return new Result(true, MessageConstant.ORDERSETTING_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ORDERSETTING_FAIL);
        }
    }
}
