package com.health.service.impl;


import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.health.dao.MemberDao;
import com.health.dao.UserDao;
import com.health.entity.PageResult;
import com.health.entity.QueryPageBean;
import com.health.pojo.Member;
import com.health.service.MemberService;
import com.health.utils.DateUtils;
import com.health.utils.MD5Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/**
 * 会员业务处理
 */
@Service(interfaceClass = MemberService.class)
@Slf4j
public class MemberServiceImpl implements MemberService {
    @Autowired
    MemberDao memberDao;//注入会员数据层

    @Autowired
    UserDao userDao;

    /**
     * 根据条件获取会员信息
     *
     * @param telephone
     * @return
     */
    @Override
    public Member getMember(String telephone) {
        return memberDao.getMemberByTelPhone(telephone);
    }

    /**
     * 新增会员
     *
     * @param member
     */
    @Override
    public void insertMember(Member member) {
//        进行密码加密
        String password = member.getPassword();
        if (password != null) {
            String md5Pwd = MD5Utils.md5(password);
            member.setPassword(md5Pwd);
        }
        memberDao.addMember(member);
    }

    /**
     * 分页查询
     *
     * @param queryPageBean
     * @return
     */
    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        //pageHelper分页
        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        Page<Member> memberPage = memberDao.findPage(queryPageBean.getQueryString());
        //获取健康管理师
        String name = "健康管理师";
        List<String> username = userDao.getHealthManager(name);
        for (Member member : memberPage) {
            //计算年龄
            Date birthday = member.getBirthday();
            if (birthday != null) {
                member.setAge(DateUtils.getAgeByBirth(birthday));
            }
            //判断健康管理师是否存在
            String healthmanager = member.getHealthmanager();
            if (healthmanager == null) {
                member.setHealthmanager("此会员暂时未绑定健康管理师");
            }
            log.info("healthmanager=====" + healthmanager);
            //判断健康管理师是否失效
            if (!username.contains(healthmanager)) {
                //给出显示信息
                String message = "管理师已失去权限,请更换";
                member.setHealthmanager(message);
            }
        }
        return new PageResult(memberPage.getTotal(), memberPage.getResult());
    }

    /**
     * 获取会员其他关联数据
     *
     * @param id
     * @return
     */
    @Override
    public List<String> getMemberMessage(Integer id) {
        List<Map<String, String>> memberListMap = memberDao.getMemberOtherData(id);
        log.info("memberListMap===" + memberListMap);
        Set<String> setmealSet = new HashSet<>();
        Set<String> addressSet = new HashSet<>();
        Set<String> checkgroupSet = new HashSet<>();
        Set<String> checkitemSet = new HashSet<>();
        List<String> list = new ArrayList<>();
        for (Map<String, String> stringMap : memberListMap) {
            String setmealName = stringMap.get("setmealName");
            log.info("setmealName==" + setmealName);
            String addressName = stringMap.get("addressName");
            log.info("address===" + addressName);
            String checkgroupName = stringMap.get("checkgroupName");
            log.info("checkgroupName==" + checkgroupName);
            String checkitemName = stringMap.get("checkitemName");
            log.info("checkitemName==" + checkitemName);
            //添加到各自集合
//            判断检查套餐是否有空
            if (setmealName != null && setmealName.length() > 0) {
                setmealSet.add(setmealName);
            }
//            判断地址是否有空
            if (addressName != null && addressName.length() > 0) {
                addressSet.add(addressName);
            }
//            判断检查组是否有空
            if (checkgroupName != null && checkgroupName.length() > 0) {
                checkgroupSet.add(checkgroupName);
            }
//            判断检查项是否有空
            if (checkitemName != null && checkitemName.length() > 0) {
                checkitemSet.add(checkitemName);
            }
        }
        log.info("setmealSet" + setmealSet);
        log.info("setmealSet" + setmealSet.toString());
        log.info("addressSet" + addressSet.toString());
        log.info("checkgroupSet" + checkgroupSet.toString());
        log.info("checkitemSet" + checkitemSet.toString());
//        对数据进行优化
//        if (setmealSet.toString().equals("[]") || setmealSet.toString() == null) {
//            list.add("");
//        }else {
        list.add(setmealSet.toString().substring(1, setmealSet.toString().indexOf(']')));
//        }
        //        对数据进行优化，
//        if (addressSet.toString().equals("[]") || addressSet.toString() == null) {
//            list.add("");
//        }else {
        list.add(addressSet.toString().substring(1, addressSet.toString().indexOf(']')));
//        }
        //        对数据进行优化，
//        if (checkgroupSet.toString().equals("[]") || checkgroupSet.toString() == null) {
//            list.add("");
//        }else {
        list.add(checkgroupSet.toString().substring(1, checkgroupSet.toString().indexOf(']')));
//        }
        //        对数据进行优化，
//        if (checkitemSet.toString().equals("[]") || checkitemSet.toString() == null){
//            list.add("");
//        }else {
        list.add(checkitemSet.toString().substring(1, checkitemSet.toString().indexOf(']')));
//        }
        return list;
    }


}
