package com.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.health.constant.MessageConstant;
import com.health.constant.RedisMessageConstant;
import com.health.dao.MemberDao;
import com.health.dao.OrderDao;
import com.health.dao.OrderSettingDao;
import com.health.entity.Result;
import com.health.pojo.Member;
import com.health.pojo.Order;
import com.health.pojo.OrderSetting;
import com.health.service.OrderService;
import com.health.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.JedisPool;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

/**
 * @author LuoPing
 * @date 2022/11/9 21:47
 */
@Service(interfaceClass = OrderService.class)
public class OrderServiceImpl implements OrderService {
    @Autowired
    JedisPool jedisPool;//注入Redis对象
    @Autowired
    OrderSettingDao orderSettingDao;//注入预约设置对象
    @Autowired
    MemberDao memberDao;//注入会员对象
    @Autowired
    OrderDao orderDao;//注入预约信息对象

//    预约检查
    @Override
    public Result submitOrder(Map<String, String> map) {
        //1校验验证码
        //1.1redis取出验证码
//        System.out.println(map);
        //            获取预约的套餐id
        Integer setmealId = Integer.parseInt(map.get("setmealId"));
        String telephone = map.get("telephone");//获取电话
        String code = jedisPool.getResource().get( telephone + RedisMessageConstant.SENDTYPE_ORDER);
//        System.out.println(code);
        //1.2map验证码对比
        if (code==null||!code.equals(map.get("validateCode"))){
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }
        //2对比通过service存入数据库
        //2.1检查用户选择的预约日期是否进行预约设置
        String orderDate = map.get("orderDate");//获取选择的时间
        //把string转换为date
        Date date = null;
        try {
            date = DateUtils.parseString2Date(orderDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        OrderSetting orderSetting = orderSettingDao.findByOrderDate(date);
        if (orderSetting == null){
            return new Result(false, MessageConstant.SELECTED_DATE_CANNOT_ORDER);
        }
        //用于接收预约的id
        Integer orderId = null;
//        2.2判断用户所预约的时间是否已预约满
        int number = orderSetting.getNumber();//获取可预约数
        int reservations = orderSetting.getReservations();//获取预约数
        if(number == reservations){
            return new Result(false, MessageConstant.ORDER_FULL);
        }
//        检查用户是否重复预约(同一个用户在同一天预约了同一个套餐)，如果是重复预约则无法完成再次预约
        Member member = memberDao.getMemberByTelPhone(telephone);//根据电话获取会员信息
        if(member != null){//检查当前用户是否为会员，如果是会员则直接完成预约，
//            获取会员id
            Integer memberId = member.getId();
            //根据会员id查询用户是否已预约
            Order order = orderDao.getOrderByMemberId(memberId);
            //判断是否已预约
            if(order != null){
                return new Result(false, MessageConstant.HAS_ORDERED);
            }
            //没有预约，则进行预约
            Order orderData = new Order(memberId,date,Order.ORDERTYPE_WEIXIN,Order.ORDERSTATUS_NO,setmealId);
            orderDao.addOrder(orderData);
            //预约时间的已预约人数加一
            //更新预约设置
//            int newNumber = number - 1;
            int newReservations = reservations + 1;
            orderSetting.setReservations(newReservations);
            orderSettingDao.updateOrderSettingByDate(orderSetting);
            orderId = orderData.getId();//获取预约id
        }else {//如果不是会员则自动完成会员注册并进行预约
            //进行会员注册
            String name = map.get("name");
            String sex = map.get("sex");
//            String telephone1 = map.get("telephone");
            String idCard = map.get("idCard");
            if (name != null && sex != null && idCard != null){
                member = new Member();
                member.setName(name);
                member.setSex(sex);
                member.setIdCard(idCard);
                member.setRegTime(date);
                memberDao.addMember(member);//进行注册
                //进行预约
                Integer newMemberId = member.getId();//获取会员id
                //没有预约，则进行预约
                Order orderData = new Order(newMemberId,date,Order.ORDERTYPE_WEIXIN,Order.ORDERSTATUS_NO,setmealId);
                orderDao.addOrder(orderData);
                //预约时间的已预约人数加一
                //更新预约设置
//            int newNumber = number - 1;
                int newReservations = reservations + 1;
                orderSetting.setReservations(newReservations);
                orderSettingDao.updateOrderSettingByDate(orderSetting);
                orderId = orderData.getId();//获取预约id
            }else {
                return new Result(false, MessageConstant.ADD_MEMBER_FAIL);
            }
        }
        //3预约成功，发送短信通知
        return new Result(true, MessageConstant.ORDER_SUCCESS,orderId);
    }

    /**
     * 查询预约信息
     * @param id
     * @return
     */
    @Override
    public Map<String, String> findById(Integer id) {
        Map<String, String> order = orderDao.getOrderById(id);
        return order;
    }

}
