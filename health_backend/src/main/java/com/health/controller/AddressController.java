package com.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.health.constant.MessageConstant;
import com.health.entity.Result;
import com.health.pojo.Address;
import com.health.service.AddressService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 检查机构分布图控制层
 * @author LuoPing
 * @project IntelliJ IDEA
 * @Package health_sys
 * @Date 2022/12/4 18:18
 */
@RestController
@RequestMapping("address")
@Slf4j
public class AddressController {

    @Reference
    AddressService addressService;

    /**
     * 获取所有体检机构
     * @return
     */
    @GetMapping("findAll")
    public Result findAll(){
        try {
            List<Address> addressList = addressService.findAll();
            return new Result(true,MessageConstant.GET_ADDRESS_SUCCESS,addressList);
        }catch (Exception e){
            e.printStackTrace();
            log.error(e.getMessage());
            return new Result(false, MessageConstant.GET_ADDRESS_ERROR);
        }
    }
}
