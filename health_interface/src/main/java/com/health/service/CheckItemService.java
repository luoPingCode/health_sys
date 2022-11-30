package com.health.service;

import com.health.entity.PageResult;
import com.health.entity.QueryPageBean;
import com.health.pojo.CheckItem;

import java.util.List;

/**
 * 业务接口
 * @author LuoPing
 * @date 2022/10/25 23:30
 */
public interface CheckItemService {
//    新增
    void add(CheckItem checkItem);
//分页查询
    PageResult queryCheckItem(QueryPageBean queryPageBean);
//删除
    void deleteCheckItem(int id);

    CheckItem queryCheckItemByid(int id);

    void updateCheckItemById(CheckItem checkItem);

    List<CheckItem> findAllCheckItem();
}
