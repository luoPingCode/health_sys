package com.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.health.constant.MessageConstant;
import com.health.dao.MemberDao;
import com.health.dao.RoleDao;
import com.health.dao.UserDao;
import com.health.entity.PageResult;
import com.health.entity.QueryPageBean;
import com.health.entity.Result;
import com.health.pojo.Member;
import com.health.pojo.Role;
import com.health.pojo.User;
import com.health.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

/**
 * 用户业务逻辑操作
 *
 * @author LuoPing
 * @Date 2022/11/16 0:01
 */
@Service(interfaceClass = UserService.class)
@Transactional
@Slf4j
public class UserServiceImpl implements UserService {
    @Autowired
    UserDao userDao;
    @Autowired
    RoleDao roleDao;
    @Autowired
    MemberDao memberDao;//注入会员



    /*
    根据名字获取用户信息,进行用户权限查询
     */
    @Override
    public User findUserInfo(String username) {
        User user = userDao.findByUsername(username);
        if (user == null) {
            throw new RuntimeException("没有该用户");
        }
        log.info("User :" + user.toString());
        return user;
    }

    /**
     * 获取用户角色信息
     *
     * @return
     */
    @Override
    public List<Role> findAllRoles() {
        return roleDao.findAllRoles();
    }

    /**
     * 新增用户
     *
     * @param user
     * @param roleIds
     */
    @Override
    public Result addUser(User user, Integer[] roleIds) {
        if (user == null) {
            return new Result(false, MessageConstant.ADD_USER_FAIL);
        }
        //查询是否存在重复名字
        User byUsername = userDao.findByUsername(user.getUsername());
        if (byUsername != null) {
            return new Result(false, MessageConstant.ADD_USER_HAS);
        }
        //添加
        userDao.addUser(user);
        //获取用户id
        Integer id = user.getId();
        //执行添加用户角色关联信息
        if (roleIds.length > 0) {
            addOrEditUserAndRole(roleIds, id);
        }
        return new Result(true, MessageConstant.ADD_USER_SUCCESS);
    }

    /**
     * 分页查询用户
     *
     * @param queryPageBean
     */
    @Override
    public PageResult findPageUser(QueryPageBean queryPageBean) {
        //使用pageHelper进行分页
        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        //执行dao方法,返回分页数
        Page<User> pageUser = userDao.findPageUser(queryPageBean.getQueryString());
        return new PageResult(pageUser.getTotal(), pageUser);
    }

    /**
     * 修改用户信息
     *
     * @param userInfo
     */
    @Override
    public void update(User userInfo) {
        if (userInfo != null) {
            userDao.updateUser(userInfo);
        }
    }

    /**
     * 修改用户密码
     *
     * @param userInfo
     */
    @Override
    public void updatePassword(User userInfo) {
        if (userInfo != null) {
            userDao.updateUser(userInfo);
        }
    }

    /**
     * 根据id查询用户
     *
     * @param id
     * @return
     */
    @Override
    public User findUserByid(Integer id) {
        return userDao.findUserById(id);
    }

    /**
     * 根据用户id查询用户角色关联信息
     * @param id
     * @return
     */
    @Override
    public List<Integer> findRoleIdByUid(Integer id) {
        List<Integer> roleIds = userDao.findRoleById(id);
        return roleIds;
    }

    /**
     *修改用户信息
     * @param user
     * @param roleId
     */
    @Override
    public void updateUser(User user, Integer[] roleId) {
        userDao.updateUser(user);
        //清空用户角色关联表
        userDao.clear(user.getId());
//        Integer[] oldRoleId = userDao.findRoleById(user.getId());
        //关联的角色id是否不为空
        if (roleId != null && roleId.length > 0){
            this.addOrEditUserAndRole(roleId,user.getId());
        }
    }

    /**
     * 删除用户
     * @param id
     * @param username
     */
    @Override
    public Boolean deleteUser(Integer id, String username) {
        //删除关联表数据
        userDao.clear(id);
        //查询删除的用户是否有管理会员
        Member member = memberDao.getMemberByCondition(username);
        if (member != null){
           return false;
        }
        //删除用户
        userDao.deleteUser(id);
        return true;
    }
    /**
     * 提取的新增用户或编辑用户插入用户角色关联数据
     *
     * @param roleIds
     * @param id
     */
    public void addOrEditUserAndRole(Integer[] roleIds, Integer id) {
        HashMap<String, Integer> map = new HashMap<>();
//        循环roleid
        for (Integer roleId : roleIds) {
            map.put("userId", id);
            map.put("roleId", roleId);
            //执行新增用户角色关联方法
            roleDao.addUserAndRole(map);
        }
    }
//    @Test
//    public void test() {
//        log.info("1111");
//        log.debug("2222");
//        log.error("3333");
//        log.warn("4444");
//    }
}
