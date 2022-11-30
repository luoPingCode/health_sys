package com.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.health.constant.MessageConstant;
import com.health.constant.RedisConstant;
import com.health.constant.RedisMessageConstant;
import com.health.entity.Result;
import com.health.pojo.Member;
import com.health.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

/**
 * @author LuoPing
 * @Date 2022/11/13 21:30
 */
@RestController
@RequestMapping("loginOrOut")
public class IndexController {
    @Autowired
    JedisPool jedisPool;
    @Reference
    MemberService memberService;
    /**
     * 手机验证码登陆
     * @param map
     * @param request
     * @param response
     * @return
     */
    @PostMapping("loginTelephone")
    public Result login4ValidateCode(@RequestBody Map<String, String> map, HttpServletRequest request, HttpServletResponse response){
//        获取验证码、手机号
        String validateCode = map.get("validateCode");
        String telephone = map.get("telephone");
        if (telephone == null && validateCode == null){
            return  new Result(false,MessageConstant.TELEPHONE_VALIDATECODE_NOTNULL);
        }
        //获取Redis中存储的验证码
        String loginCode = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_LOGIN);
        //        1、校验用户输入的短信验证码是否正确，如果验证码错误则登录失败
        if (loginCode == null || !loginCode.equals(validateCode)){
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }

//        2、如果验证码正确，则判断当前用户是否为会员，如果不是会员则自动完成会员注册
        Member member = memberService.getMember(telephone);//获取会员信息
        if (member == null){
            member = new Member();
            member.setPassword("1234");
            member.setPhoneNumber(telephone);
            member.setRegTime(new Date());
            memberService.insertMember(member);
        }
//        3、向客户端写入Cookie，内容为用户手机号
        Cookie cookie = new Cookie("login_telephone",telephone);
        cookie.setPath("/");//设置能够获取cookie的路径
        cookie.setMaxAge(60 * 60 * 24);
        response.addCookie(cookie);
//        4、将会员信息保存到Redis，使用手机号作为key，保存时长为30分钟
        String memberJson = JSON.toJSONString(member);
        jedisPool.getResource().setex(telephone+RedisMessageConstant.SENDTYPE_LOGIN,RedisConstant.LOGIN_COOKIE, memberJson);
        return new Result(true,MessageConstant.LOGIN_SUCCESS);
    }
    /**
     * 邮箱登陆
     */
//    @PostMapping("loginEmailAndPwd")
//    public Result loginEmailAndPwd(@RequestBody Map<String, String> map, HttpServletRequest request, HttpServletResponse response){
//        //        获取验证码、手机号
//        String email = map.get("email");
//        String password = map.get("password");
//        if (email == null && password == null){
//            return  new Result(false,MessageConstant.TELEPHONE_VALIDATECODE_NOTNULL);
//        }
//
////        2、如果验证码正确，则判断当前用户是否为会员，如果不是会员则自动完成会员注册
//        Member member = memberService.getMember(email);//获取会员信息
//
////        3、向客户端写入Cookie，内容为用户手机号
//        Cookie cookie = new Cookie("login_telephone",telephone);
//        cookie.setPath("/");//设置能够获取cookie的路径
//        cookie.setMaxAge(60 * 60 * 24);
//        response.addCookie(cookie);
////        4、将会员信息保存到Redis，使用手机号作为key，保存时长为30分钟
//        String memberJson = JSON.toJSONString(member);
//        jedisPool.getResource().setex(telephone+RedisMessageConstant.SENDTYPE_LOGIN,RedisConstant.LOGIN_COOKIE, memberJson);
//        return new Result(true,MessageConstant.LOGIN_SUCCESS);
//    }
}
