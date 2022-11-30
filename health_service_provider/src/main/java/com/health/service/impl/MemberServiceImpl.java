package com.health.service.impl;


import com.alibaba.dubbo.config.annotation.Service;
import com.health.dao.MemberDao;
import com.health.pojo.Member;
import com.health.service.MemberService;
import com.health.utils.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
*会员业务处理
*/
@Service(interfaceClass = MemberService.class)
public class MemberServiceImpl implements MemberService {
    @Autowired
    MemberDao memberDao;//注入会员数据层
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


}
