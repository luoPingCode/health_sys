package com.health.dao;


import com.github.pagehelper.Page;
import com.health.pojo.Member;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
* @Entity com.health.domain.Member
*/
public interface MemberDao {

    Member getMemberByTelPhone(@Param("telephone") String telephone);

    void addMember(Member member);
    //根据月份查询每月之前的会员数
    int getCountByMonth(@Param("month") String month);

    int getCountByDay(@Param("today") String today);

    int getCountAll();

    int getCountByThisWeek(@Param("thisWeekMonday") String thisWeekMonday);

    Member getMemberByCondition(@Param("healthmanager") String healthmanager);

    Page<Member> findPage(@Param("queryString") String queryString);

    List<Map<String, String>> getMemberOtherData(Integer id);
}
