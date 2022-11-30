package com.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.health.constant.MessageConstant;
import com.health.entity.PageResult;
import com.health.entity.QueryPageBean;
import com.health.entity.Result;
import com.health.pojo.Menu;
import com.health.pojo.Role;
import com.health.service.MenuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author LuoPing
 * @project IntelliJ IDEA
 * @Package health_sys
 * @Date 2022/11/23 23:12
 */
@RestController
@RequestMapping("menu")
@Slf4j
public class MenuController {
    @Reference
    MenuService menuService;
    /**
     * 分页查询菜单权限
     * @param queryPageBean
     * @return
     */
    @PostMapping("findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        return menuService.findPage(queryPageBean);
    }

    /**
     * 获取所有菜单
     * @return
     */
    @GetMapping("findAllMenu")
    public Result findAllMenu(){
        try {
            List<Map<String, String>> allMenu = menuService.findAllMenu();
            return new Result(true,MessageConstant.GET_MENU_SUCCESS,allMenu);
        }catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_MENU_FAIL);
        }
    }

    /**
     * 新增权限菜单
     * @param menu
     * @return
     */
    @PostMapping("add")
    public Result add(@RequestBody Menu menu){
        try {
//            判断是否为空
            if (menu == null && menu.getName() == null && menu.getPath() == null &&
                    menu.getPriority() == null && menu.getLevel() == null) {
                return new Result(false,MessageConstant.ADD_MENU_FAIL+"，路径级别/菜单名称/优先级/菜单路径不能为空");
            }
            //通过菜单名与访问路径查找菜单,判断菜单是否存在，存在添加失败
            List<Menu> menuList = menuService.getMenuByNamePath(menu.getName(), menu.getLinkUrl());
            if (menuList != null && menuList.size() > 0) {
                return new Result(false,MessageConstant.ADD_MENU_FAIL + "，菜单数据已存在");
            }
            menuService.addMenu(menu);
            log.info("id="+menu.getId());
            return new Result(true,MessageConstant.ADD_MENU_SUCCESS);
        }catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_MENU_FAIL);
        }
    }

    /**
     * 修改
     * @param menu
     * @return
     */
    @PutMapping("edit")
    public Result edit(@RequestBody Menu menu){
        try {
//            判断是否有值
            if (menu == null && menu.getName() == null && menu.getPath() == null &&
                    menu.getPriority() == null && menu.getLevel() == null) {
                return new Result(false,MessageConstant.EDIT_MENU_FAIL+"路径级别/菜单名称/优先级/菜单路径不能为空");
            }

            return new Result(true,MessageConstant.EDIT_MENU_SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.EDIT_MENU_FAIL);
        }
    }
    /**
     * 根据id获取是否关联角色
     * @param id 菜单id
     * @return result
     */
    @GetMapping("findRoleById")
    public Result findRoleById(Integer id){
        try {
            List<Role> roleList = menuService.getRoleById(id);
            if (roleList != null && roleList.size() > 0) {
                return new Result(true, roleList.size() + "角色关联此菜单");
            } else {
                return new Result(false, MessageConstant.GET_ROLE_ON);
            }
        }catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_ROLE_ERROR);
        }
    }

    /**
     * 删除菜单
     * @param id
     * @return
     */
    @DeleteMapping("deleteMenu")
    public Result deleteMenu(Integer id){
        try {
            //查询是否存在子菜单
            int count = menuService.getChildren(id);
            if (count > 0){
                return new Result(false, MessageConstant.DELETE_MENU_FAIL+",该菜单包含子菜单");
            }
            menuService.deleteMenu(id);
            return new Result(true,MessageConstant.DELETE_MENU_SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, MessageConstant.DELETE_MENU_FAIL);
        }
    }
}
