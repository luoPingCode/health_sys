package com.health.security;


import com.alibaba.dubbo.config.annotation.Reference;
import com.health.pojo.Permission;
import com.health.pojo.Role;
import com.health.pojo.User;
import com.health.service.UserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author LuoPing
 * @Date 2022/11/14 22:01
 */
@Component
public class SpringSecurityUserService implements UserDetailsService {
    @Reference
    UserService userService;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //获取用户信息
        User user = userService.findUserInfo(username);
        //判断用户是否存在
        if (user == null) {
            return null;
        }
        List<GrantedAuthority> list = new ArrayList<>();
        //获取用户角色
        Set<Role> roles = user.getRoles();
        if (roles != null && roles.size() > 0) {
            //循环角色信息 给与角色权限GrantedAuthority
            for (Role role : roles) {
                list.add(new SimpleGrantedAuthority(role.getKeyword()));
//                获取权限
                Set<Permission> permissions = role.getPermissions();
//                System.out.println("per"+permissions);
                if (permissions != null && permissions.size() > 0) {
                    for (Permission permission : permissions) {
                        list.add(new SimpleGrantedAuthority(permission.getKeyword()));
                    }
                }
            }
        }
        return new org.springframework.security.core.userdetails.User(username,user.getPassword(),list);
    }
}
