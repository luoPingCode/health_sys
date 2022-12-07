package com.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.health.dao.SysLogDao;
import com.health.pojo.Syslog;
import com.health.service.SysLogService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 日志业务处理
 * @author LuoPing
 * @project IntelliJ IDEA
 * @Package health_sys
 * @Date 2022/12/5 22:09
 */
@Service(interfaceClass = SysLogService.class)
public class SysLogServiceImpl implements SysLogService {
    @Autowired
    SysLogDao sysLogDao;
    @Override
    public void insert(Syslog sysLog) {
        sysLogDao.insert(sysLog);
    }
}
