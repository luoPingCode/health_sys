package com.health.service;

import com.health.entity.PageResult;
import com.health.entity.QueryPageBean;
import com.health.pojo.Syslog;

import java.util.List;

/**
 * 日志业务接口
 * @author LuoPing
 * @project IntelliJ IDEA
 * @Package health_sys
 * @Date 2022/12/5 22:09
 */
public interface SysLogService {
    void insert(Syslog sysLog);

    PageResult findAll(QueryPageBean queryPageBean);

    void deleteByIds(List<String> ids);

    void deleteById(String id);
}
