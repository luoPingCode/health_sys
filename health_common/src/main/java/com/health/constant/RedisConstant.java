package com.health.constant;

public class RedisConstant {
    //套餐图片所有图片名称
    public static final String SETMEAL_PIC_RESOURCES = "setmealPicResources";
    //套餐图片保存在数据库中的图片名称
    public static final String SETMEAL_PIC_DB_RESOURCES = "setmealPicDbResources";
    //redis 中的失效时间
    public static final int TOKEN_TIME = 10*60;
    //登陆的cookie失效时间
    public static final int LOGIN_COOKIE = 30 * 60 * 24;
}
