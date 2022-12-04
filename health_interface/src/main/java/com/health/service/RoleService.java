package com.health.service;

import com.health.entity.PageResult;
import com.health.entity.QueryPageBean;

/**
 * 角色业务接口
 * @author LuoPing
 * @project IntelliJ IDEA
 * @Package health_sys
 * @Date 2022/12/3 21:39
 */
public interface RoleService {

    PageResult findPage(QueryPageBean queryPageBean);
}
