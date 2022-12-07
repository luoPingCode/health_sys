package com.health.dao;

import com.health.pojo.Syslog;

/**
 * 日志数据操作层
 * @author LuoPing
 * @project IntelliJ IDEA
 * @Package health_sys
 * @Date 2022/12/5 22:27
 */
public interface SysLogDao {
    void insert(Syslog sysLog);
}
