package com.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.health.dao.PermissionDao;
import com.health.entity.PageResult;
import com.health.entity.QueryPageBean;
import com.health.pojo.Permission;
import com.health.pojo.Role;
import com.health.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


/**
 * (TPermission)表服务实现类
 *
 * @author LuoPing
 * @since 2022-11-24 20:43:56
 */
@Service(interfaceClass = PermissionService.class)
public class PermissionServiceImpl implements PermissionService {
    @Autowired
    private PermissionDao permissionDao;


    /**
     * 分页查询
     *
     * @return 查询结果
     */
    @Override
    public PageResult queryByPage(QueryPageBean queryPageBean) {
        //pageHelper分页
        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        Page<Permission> permissions = permissionDao.queryByPage(queryPageBean.getQueryString());
        return new PageResult(permissions.getTotal(),permissions);
    }

    /**
     * 新增数据
     *
     * @param permission 实例对象
     * @return 实例对象
     */
    @Override
    public void insert(Permission permission) {
        permissionDao.insert(permission);
//        return tPermission;
    }

    /**
     * 修改数据
     * @param permission 实例对象
     * @return 实例对象
     */
    @Override
    public void update(Permission permission) {
        permissionDao.update(permission);
//        return this.queryById(tPermission.getId());
    }

    /**
     * 通过主键删除数据
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer id) {
        return permissionDao.deleteById(id) > 0;
    }

    /**
     * 根据权限id获取角色
     * @param id
     * @return
     */
    @Override
    public List<Role> findRoleByPmId(Integer id) {
        return permissionDao.findRoleByPmId(id);
    }
}
