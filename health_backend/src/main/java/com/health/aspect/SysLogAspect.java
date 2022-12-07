package com.health.aspect;

import com.alibaba.dubbo.config.annotation.Reference;
import com.health.pojo.Syslog;
import com.health.service.SysLogService;
import com.health.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

/**
 * 日志切面
 * @author LuoPing
 * @project IntelliJ IDEA
 * @Package health_sys
 * @Date 2022/12/6 17:48
 */
@Aspect
@Component
@Slf4j
public class SysLogAspect {
    @Reference
    SysLogService sysLogService;//注入日志业务
    //开始执行时间
    private Date startTime;
    /**
     * 前置通知 主要获取开始时间,
     * //执行的类是哪一个,执行了哪个方法
     * @param joinPoint
     */
    @Before("execution(* com.health.controller.*.*(..))")
    public void beforeAdvice(JoinPoint joinPoint){
        startTime = new Date();
        //获取执行的类
//        String className = joinPoint.getTarget().getClass().getName();
        //获取执行的方法
//        String methodName = joinPoint.getSignature().getName();

    }

    /**
     * 方法执行结束通知
     * @param joinPoint
     */
    @After("execution(* com.health.controller.*.*(..))")
    public void afterAdvice(JoinPoint joinPoint) throws Exception {
        //获取执行时长
        String executionTime = String.valueOf(new Date().getTime() - startTime.getTime());
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        Syslog sysLog = createSysLog(joinPoint, request, executionTime);//获取封装的日志
        //调用日志业务插入日志
        sysLogService.insert(sysLog);
        log.info("{},执行了【{}】,操作",sysLog.getUsername(),sysLog.getUrl());
    }

    public Syslog createSysLog(JoinPoint joinPoint, HttpServletRequest request, String executionTime) throws Exception {
        //获取执行的类
        String className = joinPoint.getTarget().getClass().getName();
        //获取执行的方法
        String methodName = joinPoint.getSignature().getName();
        String method = "[类]" + className + "[方法]" + methodName;
        //获取当前登陆用户
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        //获取id
        String uuid = UUID.randomUUID().toString();
        String visitTimestr = DateUtils.parseDateString(startTime);
        Syslog syslog = new Syslog();
        syslog.setId(uuid);
        syslog.setUsername(user.getUsername()); //后台操作人
        syslog.setVisitTime(startTime);//开始时间
        syslog.setVisitTimestr(visitTimestr);//String 时间
        syslog.setIp(request.getRemoteAddr());//ip
        syslog.setUrl(request.getRequestURI());
        syslog.setExecutionTime(executionTime);//执行时间
        syslog.setMethod(method);//执行方法
        return syslog;
    }
}
