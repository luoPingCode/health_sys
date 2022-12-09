package com.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.health.dao.AddressDao;
import com.health.pojo.Address;
import com.health.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 体检机构业务处理
 * @author LuoPing
 * @project IntelliJ IDEA
 * @Package health_sys
 * @Date 2022/12/4 18:26
 */
@Service(interfaceClass = AddressService.class)
public class AddressServiceImpl implements AddressService {

    @Autowired
    AddressDao addressDao;
    @Override
    public List<Address> findAll() {
        return addressDao.findAll();
    }
}
