package com.health.service;

import com.health.entity.PageResult;
import com.health.entity.QueryPageBean;
import com.health.pojo.CheckGroup;

import java.util.List;

/**
 * 检查组业务接口
 * @author LuoPing
 * @date 2022/10/28 16:52
 */
public interface CheckGroupService {
    void addCheckGroup(Integer[] checkItemIds, CheckGroup checkGroup);

    PageResult findCheckGroupList(QueryPageBean queryPageBean);

    CheckGroup findCheckGroupById(int id);

    List<Integer> queryCheckItemIdsByCheckGroupId(int id);

    void updateCheckGroup(Integer[] checkItemIds, CheckGroup checkGroup);

    void deleteCheckGroup(Integer id);

    List<CheckGroup> findAll();
}
