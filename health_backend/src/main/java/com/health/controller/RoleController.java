package com.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.health.entity.PageResult;
import com.health.entity.QueryPageBean;
import com.health.service.RoleService;
import org.springframework.web.bind.annotation.*;

/**
 * 角色控制层
 * @author LuoPing
 * @project IntelliJ IDEA
 * @Package health_sys
 * @Date 2022/12/3 21:06
 */
@RestController
@RequestMapping("role")
public class RoleController {
    @Reference
    RoleService roleService;

    /**
     * 分页获取角色数据
     * @param queryPageBean
     * @return
     */
    @PostMapping("findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        return roleService.findPage(queryPageBean);
    }
}
