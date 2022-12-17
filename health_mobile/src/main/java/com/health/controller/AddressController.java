package com.health.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.health.constant.MessageConstant;
import com.health.entity.Result;
import com.health.pojo.Address;
import com.health.service.AddressService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/address")
public class AddressController {

    @Reference
    private AddressService addressService;


    //查询所有体检机构信息,用于用户预约页面展示
    @RequestMapping("/findAllAddress")
    public Result findAllAddress(){
        try {
            List<Address> addressList = addressService.findAll();
            return new Result(true, MessageConstant.GET_ADDRESS_SUCCESS,addressList);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_ADDRESS_ERROR);
        }
    }


    @RequestMapping("/findByAddressId")
    public Result findByAddressId(Integer addressId){
//        Address address = addressService.findById(addressId);
        return new Result(true, MessageConstant.GET_ADDRESS_SUCCESS);
    }

}
