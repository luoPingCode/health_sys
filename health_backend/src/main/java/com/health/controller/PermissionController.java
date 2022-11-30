package com.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.health.constant.MessageConstant;
import com.health.entity.PageResult;
import com.health.entity.QueryPageBean;
import com.health.entity.Result;
import com.health.pojo.Permission;
import com.health.pojo.Role;
import com.health.service.PermissionService;
import com.health.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * @author LuoPing
 * @project IntelliJ IDEA
 * @Package health_sys
 * @Date 2022/11/17 21:44
 */
@RestController
@RequestMapping("permission")
@Slf4j
public class PermissionController {
    @Reference
    PermissionService permissionService;
    @Reference
    UserService userService;
    /**
     * 查询登陆用户所拥有权限
     * @return
     */
    @GetMapping("findAllPermission")
    public Map<String, Object> findAllPermission(){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        System.out.println(user.toString());
        Collection<GrantedAuthority> authorities = user.getAuthorities();
//        System.out.println(authorities);
        log.info("authorities="+authorities);
        //遍历用户拥有的权限，并按照规则把权限封装进map集合
        Map<String, Object> map = new HashMap<>();
        for (GrantedAuthority authority : authorities) {
            map.put(authority.getAuthority(),"have");
        }
        return map;
    }

    /**
     * 分页查询角色权限
     * @param queryPageBean
     * @return
     */
    @PostMapping("findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        PageResult pageResult = permissionService.queryByPage(queryPageBean);
        log.info("findPage={}",pageResult);
        return pageResult;
    }

    /**
     * 新增权限数据
     * @param permission
     * @return
     */
    @PostMapping("add")
    public Result add(@RequestBody Permission permission){
        try {
            permissionService.insert(permission);
            log.info("新增成功");
            return new Result(true, MessageConstant.ADD_PERMISSION_SUCCESS);
        }catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_PERMISSION_FAIL);
        }
    }
    /**
     * 修改权限数据
     * @param permission
     * @return
     */
    @PutMapping("edit")
    public Result editPermission(@RequestBody Permission permission){
        try {
            permissionService.update(permission);
            log.info("修改成功");
            return new Result(true, MessageConstant.EDIT_PERMISSION_SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, MessageConstant.EDIT_PERMISSION_FAIL);
        }
    }

    /**
     * 通过权限id查找是否有角色关联该权限
     * @param id
     * @return
     */
    @GetMapping("findRoleByPmId")
    @PreAuthorize("hasAuthority('PERMISSION_DELETE')")
    public Result findRoleByPmId(Integer id){
        try {
            //判断当前角色是否绑定了该权限
            //获取当前登陆用户
            org.springframework.security.core.userdetails.User loginUser = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            //获取角色权限
            com.health.pojo.User userInfo = userService.findUserInfo(loginUser.getUsername());
            //判断是否存在关联角色
            Set<Role> roles = userInfo.getRoles();
            if (roles != null && roles.size() > 0){
                for (Role role : roles){//循环角色
                    Set<Permission> permissions = role.getPermissions();
                    if (permissions != null && permissions.size() > 0){
                        for (Permission permission : permissions){//循环权限
                            if (permission.getId().equals(id)){
                                return new Result(false, "当前角色绑定了该权限无法删除");
                            }
                        }
                    }
                }
            }
//            根据权限id获取角色
            List<Role> roleList = permissionService.findRoleByPmId(id);
            if (roleList == null){
                return new Result(false, "没有关联角色");
            }
            StringBuilder roleNames = new StringBuilder();//用于拼接并返回前端
            for (Role role : roleList) {
                roleNames.append(role.getName()).append(";");
            }
            log.info("roleNames={}",roleNames);
            return new Result(true, roleNames.toString());
        }catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_ROLE_ERROR);
        }
    }
    /**
     * 删除权限数据
     * @param id
     * @return
     */
    @DeleteMapping("delete")
    public Result delete(Integer id){
        try {
            permissionService.deleteById(id);
            return new Result(true, MessageConstant.DELETE_PERMISSION_SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, MessageConstant.DELETE_PERMISSION_FAIL);
        }
    }
}
