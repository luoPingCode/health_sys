package com.health.dao;

import com.github.pagehelper.Page;
import com.health.entity.QueryPageBean;
import com.health.pojo.CheckItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 检查项数据层
 * @author LuoPing
 * @date 2022/10/26 0:02
 *
 */
public interface CheckItemDao {
    void add(CheckItem checkItem);

    Page<CheckItem> findCheckItemList(@Param("queryString") String queryString);

    void deleteCheckItem(@Param("id") int id);

    int queryCheckGroupById(@Param("id") int id);

    CheckItem queryCheckItemById(@Param("id") int id);

    void update(CheckItem checkItem);

    List<CheckItem> findAll();
}
