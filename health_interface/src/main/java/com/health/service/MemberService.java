package com.health.service;


import com.health.entity.PageResult;
import com.health.entity.QueryPageBean;
import com.health.pojo.Member;

import java.util.List;
import java.util.Map;

/**
*会员操作业务接口
*/
public interface MemberService {

    Member getMember(String telephone);

    void insertMember(Member member);

    PageResult findPage(QueryPageBean queryPageBean);

    List<String> getMemberMessage(Integer id);
}
