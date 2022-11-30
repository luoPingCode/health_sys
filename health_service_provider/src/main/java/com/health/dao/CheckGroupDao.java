package com.health.dao;

import com.github.pagehelper.Page;
import com.health.pojo.CheckGroup;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 检查组数据操作接口
 * @author LuoPing
 * @date 2022/10/28 16:52
 */
public interface CheckGroupDao {
    void addCheckGroup(CheckGroup checkGroup);

    void addCheckGroupIdAndItemId(Map<String, Integer> map);

//    Long queryCheckGroupCount();
    //分页查询
    Page<CheckGroup> findCheckGroupList(@Param("queryString") String queryString);

    CheckGroup findCheckGroupById(int id);
//    检查组与检查项关联关系
    List<Integer> findCheckItemIdsByCheckGroupId(@Param("id") int id);

    void updateCheckGroup(CheckGroup checkGroup);

    void clear(@Param("id") Integer id);

    void delete(@Param("id") Integer id);

    List<CheckGroup> findAll();
}
