package com.health.service;


import com.health.pojo.Member;

import java.util.List;

/**
*会员操作业务接口
*/
public interface MemberService {

    Member getMember(String telephone);

    void insertMember(Member member);
}
