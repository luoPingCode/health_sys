package com.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.health.dao.SysLogDao;
import com.health.entity.PageResult;
import com.health.entity.QueryPageBean;
import com.health.pojo.Syslog;
import com.health.service.SysLogService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

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
    /**
     * 分页查询
     * @param queryPageBean
     * @return
     */
    @Override
    public PageResult findAll(QueryPageBean queryPageBean) {
        //使用pageHelper分页
        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        //分页查询
        Page<Syslog> syslogPage = sysLogDao.findAll(queryPageBean.getQueryString());
        return new PageResult(syslogPage.getTotal(),syslogPage);
    }

    /**
     * 批量删除
     * @param ids
     */
    @Override
    public void deleteByIds(List<String> ids) {
        if (ids != null && ids.size() >0){
            for (String id : ids) {
                sysLogDao.deleteById(id);
            }
        }
    }
//    根据id删除
    @Override
    public void deleteById(String id) {
        if (id != null){
            sysLogDao.deleteById(id);
        }
    }
}
