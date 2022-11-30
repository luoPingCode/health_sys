package com.health.jobs;


import com.health.constant.RedisConstant;
import com.health.utils.QiniuUtils;
import redis.clients.jedis.JedisPool;

import java.util.Set;

public class ClearImgJob {
//    注入jedis
    private JedisPool jedisPool;
//    清理图片
    public void ClearImg() {
        //获取Redis中的差集图片
        Set<String> imgs = jedisPool.getResource().sdiff(RedisConstant.SETMEAL_PIC_RESOURCES, RedisConstant.SETMEAL_PIC_DB_RESOURCES);
        if (imgs.size()>0){//判断是否不为空
            for (String img : imgs){
//                清理七牛云上的垃圾图片
                QiniuUtils.deleteFileFromQiniu(img);
//                清理Redis中垃圾图片
//                Long srem = jedisPool.getResource().srem(RedisConstant.SETMEAL_PIC_RESOURCES, img);
                System.out.println("清理成功");
            }
//            直接删除set
//            好处：节约内存空间，节省删除 删除Redis逻辑 坏处：编辑的时候要多判断一下
            jedisPool.getResource().del(RedisConstant.SETMEAL_PIC_RESOURCES);
            jedisPool.getResource().del(RedisConstant.SETMEAL_PIC_DB_RESOURCES);
        }
    }
}
