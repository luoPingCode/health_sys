package com.lp;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author LuoPing
 * @date 2022/10/31 16:11
 */
public class Application {
    public static void main(String[] args) {
//        System.out.println("args=="+args[0]);
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext-jobs.xml");
        context.start();
    }
}
