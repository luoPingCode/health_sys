package com.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.health.constant.MessageConstant;
import com.health.dao.MenuDao;
import com.health.dao.RoleDao;
import com.health.entity.PageResult;
import com.health.entity.QueryPageBean;
import com.health.entity.Result;
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
        return new PageResult(menuPage.getTotal(),menuPage.getResult());
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
    public List<Menu> getChildren(Integer id) {
        return menuDao.getChildren(id);
    }

    /**
     * 根据id获取菜单
     * @param id
     * @return
     */
    @Override
    public Menu getMenuById(Integer id) {
        return menuDao.queryById(id);
    }

    /**
     * 编辑菜单
     * @param menu
     * @return
     */
    @Override
    public Result edit(Menu menu) {
        //获取根据id修改的菜单
        Menu oldMenu = menuDao.queryById(menu.getId());
        boolean flagL;
        boolean flagN = oldMenu.getName().equals(menu.getName());
        if (oldMenu.getLinkUrl() != null && oldMenu.getLinkUrl().length() > 0){//判断是一级还是二级
            //判断修改的菜单名字和路径是否已修改
            flagL = oldMenu.getLinkUrl().equals(menu.getLinkUrl());
        }else {
            if (menu.getLinkUrl() != null && menu.getLinkUrl().length() > 0){
                flagL = menu.getLinkUrl().equals(oldMenu.getLinkUrl());
            }else {
                flagL = true;
            }
        }
        return goodEdit(flagL,flagN,menu);
    }

    /**
     *根据不同情况提取的修改菜单方法
     * @param flagL 路径是否相同
     * @param flagN 名字是否相同
     * @param menu 修改的菜单内容
     * @return result
     */
    private Result goodEdit(boolean flagL, boolean flagN, Menu menu) {
        Result result = null;
        //判断名字和访问路径是否修改
        if (flagL && flagN) { //没有修改
            menuDao.update(menu);
            return new Result(true,MessageConstant.EDIT_MENU_SUCCESS);
        }else if (!flagL && !flagN){//名字和访问路径都修改了
            result = checkEditByNameAndLinkUrl(menu);
        }else {//名字和路径其中一个修改了
            result = checkEditByNameAndLinkUrl(menu);
        }
        return result;
    }
    /**
     * 提取的名字或路径是否修改方法
     * @param menu
     * @return
     */
    private Result checkEditByNameAndLinkUrl(Menu menu) {
        //查找是否有重复的菜单数据
        List<Menu> menus = menuDao.getMenuByNamePath(menu.getName(), menu.getLinkUrl());
        if (menus != null && menus.size() >= 1) {
            return new Result(false,MessageConstant.EDIT_MENU_FAIL + ",菜单已存在");
        }
        menuDao.update(menu);
        return new Result(true,MessageConstant.EDIT_MENU_SUCCESS);
    }
}
