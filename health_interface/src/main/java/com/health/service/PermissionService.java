package com.health.service;

import com.health.entity.PageResult;
import com.health.entity.QueryPageBean;
import com.health.pojo.Permission;
import com.health.pojo.Role;

import java.util.List;

/**
 * (TPermission)表服务接口
 *
 * @author LuoPing
 * @since 2022-11-24 20:40:13
 */
public interface PermissionService {

    /**
     * 分页查询
     *
     * @param queryPageBean 筛选条件
     * @return 查询结果
     */
    PageResult queryByPage(QueryPageBean queryPageBean);

    /**
     * 新增数据
     *
     * @param permission 实例对象
     * @return 实例对象
     */
    void insert(Permission permission);

    /**
     * 修改数据
     *
     * @param permission 实例对象
     * @return 实例对象
     */
    void update(Permission permission);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Integer id);

    List<Role> findRoleByPmId(Integer id);
}
