package com.health.dao;

import com.health.pojo.Address;

import java.util.List;

/**
 * 体检机构数据交互
 * @author LuoPing
 * @project IntelliJ IDEA
 * @Package health_sys
 * @Date 2022/12/4 18:34
 */
public interface AddressDao {
    List<Address> findAll();
}
