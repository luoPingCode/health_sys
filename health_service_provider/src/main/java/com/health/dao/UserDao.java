package com.health.dao;

import com.github.pagehelper.Page;
import com.health.pojo.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author LuoPing
 * @project IntelliJ IDEA
 * @Package
 * @Date 2022/11/16 0:39
 */
public interface UserDao {
    User findByUsername(@Param("username") String username);

    void addUser(User user);

    Page<User> findPageUser(@Param("queryString") String queryString);

    void updateUser(User user);

    void updatePassword(User user);

    User findUserById(@Param("id") Integer id);

    List<Integer> findRoleById(@Param("id") Integer id);
    //清空用户角色关联信息接口
    void clear(@Param("id") Integer id);

    void deleteUser(Integer id);
}
