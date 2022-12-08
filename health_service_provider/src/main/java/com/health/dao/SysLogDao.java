package com.health.dao;

import com.github.pagehelper.Page;
import com.health.pojo.Syslog;
import org.apache.ibatis.annotations.Param;

/**
 * 日志数据操作层
 * @author LuoPing
 * @project IntelliJ IDEA
 * @Package health_sys
 * @Date 2022/12/5 22:27
 */
public interface SysLogDao {
    void insert(Syslog sysLog);

    Page<Syslog> findAll(@Param("queryString") String queryString);

    void deleteById(@Param("id") String id);
}
