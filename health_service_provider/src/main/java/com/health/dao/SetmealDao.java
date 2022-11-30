package com.health.dao;

import com.github.pagehelper.Page;
import com.health.pojo.Setmeal;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* @Entity
*/
public interface SetmealDao {
    void addSetmeal(Setmeal setmeal);

    void addSetmealAndCheckGroup(HashMap<String, Integer> map);

    Page<Setmeal> findSerMeal(@Param("queryString") String queryString);
//    查询套餐总数
    long findCount();

    List<Setmeal> queryAll();

    Setmeal findById(@Param("id") Integer id);

    Setmeal setMealfindById(int id);

    List<Integer> findCheckGroupIdsById(int id);
//修改套餐
    void updateSetmeal(Setmeal setmeal);
//    删除关联信息
    void clear(Integer id);

    List<Map<String, Object>> getSetmealReport();

    List<Map<String, Object>> getHotSetmeal();
}
