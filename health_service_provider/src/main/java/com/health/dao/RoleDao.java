package com.health.dao;

import com.github.pagehelper.Page;
import com.health.pojo.Role;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;

/**
 * @author LuoPing
 * @project IntelliJ IDEA
 * @Package health_sys
 * @Date 2022/11/17 18:09
 */
public interface RoleDao {
    List<Role> findAllRoles();

    void addUserAndRole(HashMap<String, Integer> map);

    List<Role> getRoleById(@Param("id") Integer id);

    Page<Role> findPageRole(@Param("queryString") String queryString);
}
