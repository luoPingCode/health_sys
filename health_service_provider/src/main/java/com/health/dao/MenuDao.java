package com.health.dao;

import com.github.pagehelper.Page;
import com.health.pojo.Menu;
import com.health.pojo.Role;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

/**
 * 菜单数据层接口
 * @author LuoPing
 * @project IntelliJ IDEA
 * @Package health_sys
 * @Date 2022/11/23 23:59
 */
public interface MenuDao {
    Page<Menu> findPage(@Param("queryString") String queryString);

    List<Map<String, String>> findAllMenu();

    List<Menu> getMenuByNamePath(@Param("name") String name, @Param("linkUrl") String linkUrl);

    void insert(Menu menu);

    void deleteRoleMenuById(@Param("id") Integer id);

    void deleteById(Integer id);

    List<Menu> getChildren(@Param("id") Integer id);

    Menu queryById(Integer id);
//    修改菜单
    void update(Menu menu);

    LinkedHashSet<Menu> getAllMenus(@Param("id") Integer id);
}
