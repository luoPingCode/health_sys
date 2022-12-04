package com.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.health.dao.RoleDao;
import com.health.entity.PageResult;
import com.health.entity.QueryPageBean;
import com.health.pojo.Role;
import com.health.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 角色业务类
 * @author LuoPing
 * @project IntelliJ IDEA
 * @Package health_sys
 * @Date 2022/12/3 21:41
 */
@Service(interfaceClass = RoleService.class)
public class RoleServiceImpl implements RoleService {
    @Autowired
    RoleDao roleDao;
    /**
     * 分页查询角色数据
     * @param queryPageBean
     * @return
     */
    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        //使用pageHelper进行分页
        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        Page<Role> pageRole = roleDao.findPageRole(queryPageBean.getQueryString());
        return new PageResult(pageRole.getTotal(),pageRole);
    }
}
