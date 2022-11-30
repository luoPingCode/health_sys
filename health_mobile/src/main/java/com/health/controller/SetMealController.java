package com.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.health.constant.MessageConstant;
import com.health.entity.Result;
import com.health.pojo.Setmeal;
import com.health.service.SetmealService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author LuoPing
 * @date 2022/11/3 0:58
 */
@RestController
@RequestMapping("setMeal")
public class SetMealController {

    //注入service
    @Reference
    private SetmealService setmealService;

    /**
     * 查询所有检查套餐，用于用户展示
     * @return
     */
    @GetMapping
    public Result findAllSetMeal(){
        try{
            List<Setmeal> setMealList = setmealService.findAllSetMeal();
            return new Result(true,MessageConstant.GET_SETMEAL_LIST_SUCCESS,setMealList);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_SETMEAL_LIST_FAIL);
        }
    }
    //用户查询套餐详情
    @GetMapping("findById")
    public Result findById(@RequestParam("id") Integer id){
        try{
            Setmeal setmeal = setmealService.findById(id);
            return new Result(true,MessageConstant.QUERY_SETMEAL_SUCCESS,setmeal);
        }catch (Exception e){
        e.printStackTrace();
        return new Result(false, MessageConstant.QUERY_SETMEAL_FAIL);
        }
    }
}
