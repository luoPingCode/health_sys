package com.health.service;

import com.health.pojo.Address;

import java.util.List;

/**
 * 体检机构业务接口
 * @author LuoPing
 * @project IntelliJ IDEA
 * @Package health_sys
 * @Date 2022/12/4 18:20
 */
public interface AddressService {
    List<Address> findAll();
}
