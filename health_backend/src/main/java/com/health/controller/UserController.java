package com.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.health.constant.MessageConstant;
import com.health.entity.PageResult;
import com.health.entity.QueryPageBean;
import com.health.entity.Result;
import com.health.pojo.Menu;
import com.health.pojo.Role;
import com.health.pojo.User;
import com.health.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * @author LuoPing
 * @project IntelliJ IDEA
 * @Package health_sys
 * @Date 2022/11/16 21:58
 */
@RestController
@RequestMapping("user")
@Slf4j
public class UserController {
    @Reference
    UserService userService;
    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    /**
     * 分页查询
     * @param queryPageBean
     * @return
     */
    @PostMapping("findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        PageResult pageUser = userService.findPageUser(queryPageBean);
        log.info("查询成功，user: {}" , pageUser);
        return pageUser;
    }
    /**
     * 查询所有角色信息用户显示
     * @return
     */
    @GetMapping("findAllRoles")
    public Result findAllRoles(){
        try {
            List<Role> roles = userService.findAllRoles();
            return new Result(true,MessageConstant.GET_ROLE_SUCCESS,roles);
        }catch(Exception e){
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_ROLE_ERROR);
        }
    }

    /**
     * 根据id查询用户
     * @param id
     * @return
     */
    @GetMapping("findUserById")
    public Result findUserById(@RequestParam("id") Integer id){
        try {
            User user = userService.findUserByid(id);
            return new Result(true, MessageConstant.GET_USER_FAIL,user);
        }catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return new Result(false, MessageConstant.GET_USER_FAIL);
        }
    }
    /**
     * 根据用户id查询角色id
     * @param id
     * @return
     */
    @GetMapping("findRoleIdByUid")
    public Result findRoleIdByUid(@RequestParam("id") Integer id){
        try {
            List<Integer> roleIds = userService.findRoleIdByUid(id);
            return new Result(true, MessageConstant.GET_ROLE_SUCCESS,roleIds);
        }catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return new Result(false, MessageConstant.GET_ROLE_ERROR);
        }
    }
    /**
     * 获取登陆用户名
     * @return
     */
    @GetMapping("getUsername")
    public Result getUsername(){
        try {
            org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            log.info("user:{}",user.getUsername());
            return new Result(true, MessageConstant.GET_USERNAME_SUCCESS,user.getUsername());
        }catch (Exception e){
            e.printStackTrace();
            log.error(e.getMessage());
            return new Result(false,MessageConstant.GET_USERNAME_FAIL);
        }
    }
    /**
     * 新增用户
     * @param user
     * @param roleIds
     */
    @PostMapping("add")
    @PreAuthorize("hasAuthority('USER_ADD')")
    public Result add(@RequestBody User user,Integer[] roleIds) {
        try {
            //获取密码
            String password = user.getPassword();
            //密码加密
            String encode = passwordEncoder.encode(password);
            log.info("encode: {}" , encode);
            user.setPassword(encode);
            return userService.addUser(user,roleIds);
        }catch(Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.ADD_USER_FAIL);
        }
    }
    /**
     * 修改用户信息
     * @param user
     * @param roleIds
     * @return
     */
    @PostMapping("edit")
    @PreAuthorize("hasAuthority('USER_EDIT')")
    public Result edit(@RequestBody User user,Integer[] roleIds){
        try {
//            判断用户是否为空
            if(user == null || user.getUsername() == null || user.getPassword() == null){
                return new Result(false,MessageConstant.EDIT_USER_FAIL);
            }
            //获取修改的用户
            User oldUser = userService.findUserByid(user.getId());
            //判断用户名是否修改过，用户名不可修改
            if (!oldUser.getUsername().equals(user.getUsername())){
                return new Result(false,MessageConstant.EDIT_USER_FAIL,"用户名不可修改");
            }
//            判断密码是否修改过
            if (!oldUser.getPassword().equals(user.getPassword())){
                String encode = passwordEncoder.encode(user.getPassword());
                user.setPassword(encode);
            }
            userService.updateUser(user,roleIds);
            return new Result(true,MessageConstant.EDIT_USER_SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, MessageConstant.EDIT_USER_FAIL);
        }
    }

    /**
     * 删除用户
     * @param user
     */
    @PostMapping("delete")
    @PreAuthorize("hasAuthority('USER_DELETE')")
    public Result delete(@RequestBody User user){
        try {
             //获取当前登陆用户
            org.springframework.security.core.userdetails.User loginUser = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            //判断删除用户是否是当前登陆用户
            if (loginUser.getUsername().equals(user.getUsername())){
                return new Result(false,MessageConstant.EDIT_USER_FAIL+",所删除的用户是当前登陆用户，不可删除");
            }
            Boolean deleteUser = userService.deleteUser(user.getId(), user.getUsername());
            if (!deleteUser){
                return new Result(false,MessageConstant.EDIT_USER_FAIL+"该用户有管理会员，删除失败");
            }
            return new Result(true,MessageConstant.EDIT_USER_SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            log.error(e.getMessage());
            return new Result(false,MessageConstant.DELETE_USER_FAIL);
        }
    }
    /**
     * 修改密码
     * @param map
     * @return
     */
    @PostMapping("changePassword")
    public Result changePassword(@RequestBody Map<String,String> map){
        try {
            String password = map.get("password");
            String senPassword = map.get("senPassword");
            if (password != null && password.equals(senPassword)){
                //获取当前登陆用户
                org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//            根据用户名查询用户
                User userInfo = userService.findUserInfo(user.getUsername());
//            获取用户原始密码
                String oldPassword = userInfo.getPassword();
//            新密码加密
                String newPassword = passwordEncoder.encode(password);
//            判断新旧密码是否一样
                if (!oldPassword.equals(newPassword) && newPassword != null){
                    userInfo.setPassword(newPassword);
                }
                userService.updatePassword(userInfo);
            }
            return new Result(true,MessageConstant.EDIT_PASSWORD_SUCCESS);
        }catch(Exception e){
            e.printStackTrace();
            log.error(e.getMessage());
            return new Result(false,MessageConstant.EDIT_PASSWORD_FAIL);
        }
    }

    /**
     * 获取登陆者的菜单权限
     * @return
     */
    @GetMapping("getAllMenus")
    public Result getAllMenus(){
        try {
            //获取当前登陆用户
            org.springframework.security.core.userdetails.User loginUser = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (loginUser != null){
                String loginUsername = loginUser.getUsername();
                //根据用户名查询用户信息
                User user = userService.findUserInfo(loginUsername);
                user.setStation("1");
                userService.update(user);//更新user的登陆状态
                LinkedHashSet<Menu> menus = userService.getAllMenus(user.getId());
                return new Result(true,MessageConstant.GET_MENU_SUCCESS,menus);
            }
            return new Result(false,MessageConstant.GET_USERNAME_FAIL);
        }catch(Exception e){
            e.printStackTrace();
            log.error(e.getMessage());
            return new Result(false,MessageConstant.GET_MENU_FAIL);
        }
    }

    public static void main(String[] args) {
        LinkedList<String> linkedList = new LinkedList<>();
        linkedList.add("hello");
        linkedList.add("java");
        linkedList.add("word");
        linkedList.add("I");
        linkedList.add("I");
        linkedList.add("am");
        System.out.println("linkedList="+linkedList);
        LinkedHashSet<String> linkedHashSet = new LinkedHashSet<>();
        linkedHashSet.add("hello");
        linkedHashSet.add("java");
        linkedHashSet.add("word");
        linkedHashSet.add("I");
        linkedHashSet.add("I");
        linkedHashSet.add("am");
        System.out.println("linkedHashSet="+linkedHashSet);
        HashSet<String> hashSet = new HashSet<>();
        hashSet.add("hello");
        hashSet.add("java");
        hashSet.add("word");
        hashSet.add("I");
        hashSet.add("I");
        hashSet.add("am");
        System.out.println("hashSet="+hashSet);
        List<String> list = new ArrayList<>();
        list.add("hello");
        list.add("java");
        list.add("word");
        list.add("I");
        list.add("I");
        list.add("am");
        System.out.println("list="+list);
    }
}
