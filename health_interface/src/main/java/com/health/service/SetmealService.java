package com.health.service;


import com.health.entity.PageResult;
import com.health.entity.QueryPageBean;
import com.health.pojo.Setmeal;

import java.util.List;
import java.util.Map;

/**
*业务
 * 套餐管理接口
*/
public interface SetmealService {
//    添加
    void add(Integer[] checkGroupIds, Setmeal setmeal);
    //分页查询
    PageResult querySetmeal(QueryPageBean queryPageBean);
    //查询所有套餐数据，用于用户展示查看
    List<Setmeal> findAllSetMeal();
    //用户查询套餐详情
    Setmeal findById(Integer id);
    //查询套餐根据id，用于套餐修改
    Setmeal setMealfindById(int id);
    //查询套餐与检查组关联信息
    List<Integer> findCheckGroupIdsById(int id);

    void updateSetmeal(Setmeal setmeal, Integer[] checkGroupIds,String tempImgId);
}
