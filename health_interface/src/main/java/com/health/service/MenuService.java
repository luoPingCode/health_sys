package com.health.service;

import com.health.entity.PageResult;
import com.health.entity.QueryPageBean;
import com.health.pojo.Menu;
import com.health.pojo.Role;

import java.util.List;
import java.util.Map;

/**
 * 菜单权限业务接口
 * @author LuoPing
 * @project IntelliJ IDEA
 * @Package health_sys
 * @Date 2022/11/23 23:57
 */
public interface MenuService {
    PageResult findPage(QueryPageBean queryPageBean);

    List<Map<String, String>> findAllMenu();

    List<Menu> getMenuByNamePath(String name, String linkUrl);

    void addMenu(Menu menu);

    List<Role> getRoleById(Integer id);

    void deleteMenu(Integer id);

    int getChildren(Integer id);
}
