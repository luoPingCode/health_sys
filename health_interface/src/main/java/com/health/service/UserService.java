package com.health.service;

import com.health.entity.PageResult;
import com.health.entity.QueryPageBean;
import com.health.entity.Result;
import com.health.pojo.Menu;
import com.health.pojo.Role;
import com.health.pojo.User;

import java.util.LinkedHashSet;
import java.util.List;

/**
 * @author LuoPing
 * @Date 2022/11/15 23:42
 */
public interface UserService {
    User findUserInfo(String username);

    List<Role> findAllRoles();

    Result addUser(User user, Integer[] roleIds);

    PageResult findPageUser(QueryPageBean queryPageBean);

    void update(User userInfo);

    void updatePassword(User userInfo);
//    根据id查询用户
    User findUserByid(Integer id);

    List<Integer> findRoleIdByUid(Integer id);

    void updateUser(User user, Integer[] roleId);

    Boolean deleteUser(Integer id, String username);

    LinkedHashSet<Menu> getAllMenus(Integer id);
}
