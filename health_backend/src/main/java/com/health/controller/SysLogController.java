package com.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.health.constant.MessageConstant;
import com.health.entity.PageResult;
import com.health.entity.QueryPageBean;
import com.health.entity.Result;
import com.health.service.SysLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author LuoPing
 * @project IntelliJ IDEA
 * @Package health_sys
 * @Date 2022/12/7 19:29
 */
@RestController
@RequestMapping("sysLog")
@Slf4j
public class SysLogController {

    @Reference
    SysLogService sysLogService;

    /**
     * 分页查询日志
     * @param queryPageBean
     * @return
     */
    @PostMapping("findAll")
    public PageResult findAll(@RequestBody QueryPageBean queryPageBean){
        return sysLogService.findAll(queryPageBean);
    }

    /**
     * 批量删除
     * @param ids
     * @return
     */
    @PostMapping("deleteByIds")
    public Result deleteByIds(@RequestBody List<String> ids){
        try {
            sysLogService.deleteByIds(ids);
            return new Result(true,MessageConstant.DELETE_SYSLOG_SUCCESS);
        }catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return new Result(false, MessageConstant.DELETE_SYSLOG_ERROR);
        }
    }
    /**
     * 根据id删除
     * @param id
     * @return
     */
    @DeleteMapping("deleteById")
    public Result deleteById(String id){
        try {
            sysLogService.deleteById(id);
            return new Result(true,MessageConstant.DELETE_SYSLOG_SUCCESS);
        }catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return new Result(false, MessageConstant.DELETE_SYSLOG_ERROR);
        }
    }
}
