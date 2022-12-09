package com.health.service.impl;


import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.health.dao.MemberDao;
import com.health.dao.UserDao;
import com.health.entity.PageResult;
import com.health.entity.QueryPageBean;
import com.health.pojo.Member;
import com.health.service.MemberService;
import com.health.utils.DateUtils;
import com.health.utils.MD5Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
*会员业务处理
*/
@Service(interfaceClass = MemberService.class)
@Slf4j
public class MemberServiceImpl implements MemberService {
    @Autowired
    MemberDao memberDao;//注入会员数据层

    @Autowired
    UserDao userDao;
    /**
     * 根据条件获取会员信息
     * @param telephone
     * @return
     */
    @Override
    public Member getMember(String telephone) {
        return memberDao.getMemberByTelPhone(telephone);
    }

    /**
     * 新增会员
     * @param member
     */
    @Override
    public void insertMember(Member member) {
//        进行密码加密
        String password = member.getPassword();
        if (password != null){
            String md5Pwd = MD5Utils.md5(password);
            member.setPassword(md5Pwd);
        }
        memberDao.addMember(member);
    }

    /**
     * 分页查询
     * @param queryPageBean
     * @return
     */
    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        //pageHelper分页
        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        Page<Member> memberPage = memberDao.findPage(queryPageBean.getQueryString());
        //获取健康管理师
        String name = "健康管理师";
        List<String> username = userDao.getHealthManager(name);
        for (Member member : memberPage) {
            //计算年龄
            Date birthday = member.getBirthday();
            if (birthday != null){
                member.setAge(DateUtils.getAgeByBirth(birthday));
            }
            //判断健康管理师是否存在
            String healthmanager = member.getHealthmanager();
            if (healthmanager == null){
                member.setHealthmanager("此会员暂时未绑定健康管理师");
            }
            log.info("healthmanager====="+healthmanager);
            //判断健康管理师是否失效
            if (!username.contains(healthmanager)){
                //给出显示信息
                String message="管理师已失去权限,请更换";
                member.setHealthmanager(message);
            }
        }
        return new PageResult(memberPage.getTotal(),memberPage.getResult());
    }


}
