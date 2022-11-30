package com.health.controller;

import com.health.constant.MessageConstant;
import com.health.constant.RedisConstant;
import com.health.constant.RedisMessageConstant;
import com.health.entity.Result;
import com.health.utils.SMSUtils;
import com.health.utils.ValidateCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

/**
 * @author LuoPing
 * @date 2022/11/9 18:14
 */
@RestController
@RequestMapping("validateCode")
public class ValidateCodeController {
    @Autowired
    private JedisPool jedisPool;

    /**
     * 发送预约验证码
     * @param telephone
     * @return
     */
    @PostMapping("send4Order")
    public Result validateCode(@RequestParam("telephone") String telephone) {
        try {
            //获取要发送的验证码
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            //调用阿里云发送短信
            SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE, telephone, code);
            //把验证码存到Redis
            jedisPool.getResource().setex(telephone+ RedisMessageConstant.SENDTYPE_ORDER, RedisConstant.TOKEN_TIME,code);
        }catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }
        return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
    }

    /**
     * 发送登陆验证码
     * @param telephone
     * @return
     */
    @PostMapping("sendLogin")
    public Result send4Login(@RequestParam("telephone") String telephone) {
        try {
            //获取要发送的验证码
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            //调用阿里云发送短信
            SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE, telephone, code);
            //把验证码存到Redis
            jedisPool.getResource().setex(telephone+ RedisMessageConstant.SENDTYPE_LOGIN, RedisConstant.TOKEN_TIME,code);
        }catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }
        return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
    }
}
