package com.health.dao;

import com.github.pagehelper.Page;
import com.health.pojo.Permission;
import com.health.pojo.Role;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 数据操作
 * @author LuoPing
 * @project IntelliJ IDEA
 * @Package health_sys
 * @Date 2022/11/17 22:41
 */
public interface PermissionDao {
    //删除
    int deleteById(Integer id);
    //分页查询
    Page<Permission> queryByPage(@Param("queryString") String queryString);
    //修改
    void update(Permission permission);
    //新增
    void insert(Permission permission);
    //获取角色信息
    List<Role> findRoleByPmId(Integer id);
}
