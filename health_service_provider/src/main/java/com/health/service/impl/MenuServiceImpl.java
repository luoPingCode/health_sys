package com.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.health.dao.MenuDao;
import com.health.dao.RoleDao;
import com.health.entity.PageResult;
import com.health.entity.QueryPageBean;
import com.health.pojo.Menu;
import com.health.pojo.Role;
import com.health.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;


/**
 * 菜单权限接口实现类
 * @author LuoPing
 * @project IntelliJ IDEA
 * @Package health_sys
 * @Date 2022/11/23 23:58
 */
@Service(interfaceClass = MenuService.class)
public class MenuServiceImpl implements MenuService {

    @Autowired
    MenuDao menuDao;
    @Autowired
    RoleDao roleDao;
    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
//        if (queryPageBean == null) {
//
//        }
        //pageHelper分页
        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        Page<Menu> menuPage = menuDao.findPage(queryPageBean.getQueryString());
        return new PageResult(menuPage.getTotal(),menuPage);
    }

    /**
     * 获取所有菜单的name，id
     * @return
     */
    @Override
    public List<Map<String, String>> findAllMenu() {
        return menuDao.findAllMenu();
    }

    /**
     * 根据名字和路径获取菜单
     * @param name
     * @param linkUrl
     * @return
     */
    @Override
    public List<Menu> getMenuByNamePath(String name, String linkUrl) {
        return menuDao.getMenuByNamePath(name,linkUrl);
    }

    /**
     * 新增菜单数据
     * @param menu
     */
    @Override
    public void addMenu(Menu menu) {
        menuDao.insert(menu);
    }

    /**
     * 根据id获取菜单
     * @param id
     * @return
     */
    @Override
    public List<Role> getRoleById(Integer id) {
        return roleDao.getRoleById(id);
    }
    /**
     * 删除菜单
     * @param id
     */
    @Override
    public void deleteMenu(Integer id) {
        //删除角色菜单关联信息
        menuDao.deleteRoleMenuById(id);
        //删除菜单
        menuDao.deleteById(id);
    }

    /**
     * 根据id查询是否存在子菜单
     * @param id
     * @return
     */
    @Override
    public int getChildren(Integer id) {
        return menuDao.getChildren(id);
    }
}
