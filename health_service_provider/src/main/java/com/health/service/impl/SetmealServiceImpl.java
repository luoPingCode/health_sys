package com.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.health.constant.RedisConstant;
import com.health.dao.SetmealDao;
import com.health.entity.PageResult;
import com.health.entity.QueryPageBean;
import com.health.pojo.CheckGroup;
import com.health.pojo.Setmeal;
import com.health.service.SetmealService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;
import redis.clients.jedis.JedisPool;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
*
*/
@Service(interfaceClass = SetmealService.class)
@Transactional
public class SetmealServiceImpl implements SetmealService {
    @Autowired
    private SetmealDao setmealDao;
    @Autowired
    private JedisPool jedisPool;
//    注入freemarker对象
    @Autowired
    FreeMarkerConfig freemarkerConfig;
    //spring获取配置文件的内容
    @Value("${out_put_path}")
    String outPutPath;
    /**
     * 新增套餐
     * @param checkGroupIds
     * @param setmeal
     */
    @Override
    public void add(Integer[] checkGroupIds, Setmeal setmeal) {
        //插入套餐
        setmealDao.addSetmeal(setmeal);
        //获取套餐id
        Integer setmealId = setmeal.getId();
        //        判断是否为空
        if(checkGroupIds != null && checkGroupIds.length > 0) {
            //插入套餐与检查关联表
            this.addOrEditSetmeal(checkGroupIds, setmealId);
        }
        //获取套餐图片
        String setmealImg = setmeal.getImg();
//        判断是否存在图片
        if(setmealImg!=null && setmealImg.trim().length()>0) {
            jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES, setmealImg);
        }
//        生成所有静态化页面放发
        generateMobileStaticHtml();
    }
//    生成套餐静态化页面和套餐详情静态化页面
    private void generateMobileStaticHtml() {
//        获取数据
        List<Setmeal> list = setmealDao.queryAll();
//        1、调用生成套餐静态化页面方法
        generateSetMealStaticList(list);
//        2、生成套餐详情静态化页面
        generateSetMealDetailStaticList(list);
    }
    //生成套餐静态化页面方法
    public void generateSetMealStaticList(List<Setmeal> list){
//        获取静态化模板名字
        String templateName = "mobile_setmeal.ftl";
//        获取生成的静态化页面名字
        String htmlPageName = "setmealStatic.html";
        //获取需要生成的数据
        Map<String, Object> map = new HashMap<>();
        map.put("setmealList",list);
//        调用生成静态化页面方法
        generateHtml(templateName,htmlPageName,map);
    }
//    生成套餐详情静态化页面
    public void generateSetMealDetailStaticList(List<Setmeal> list){
        //        获取静态化模板名字
        String templateName = "mobile_setmeal_detail.ftl";
        //        获取生成的静态化页面名字
        String htmlPageName = "setmeal_detail_";
        for (Setmeal setmeal : list) {
            //获取需要生成的数据
            HashMap<String, Object> map = new HashMap<>();
            map.put("setmeal",setmealDao.findById(setmeal.getId()));
            generateHtml(templateName,htmlPageName+setmeal.getId()+".html",map);
        }
    }
    /**
     * 生成静态页面通用方法
     * @param templateName
     * @param htmlPageName
     * @param map
     */
    public void generateHtml(String templateName, String htmlPageName, Map<String,Object> map){
        try {
            //        获取配置信息
            Configuration configuration = freemarkerConfig.getConfiguration();
            Template template = configuration.getTemplate(templateName);//获取模板
//            生成文本
            System.out.println(outPutPath);
            FileWriter fileWriter = new FileWriter(outPutPath+htmlPageName);
            template.process(map,fileWriter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 分页查询套餐
     * @param queryPageBean
     * @return
     */
    @Override
    public PageResult querySetmeal(QueryPageBean queryPageBean) {
//        使用pageHelper进行分页
        PageHelper.startPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize());
        //查询套餐总数
        long total = setmealDao.findCount();
//        查询分页套餐数据
        Page<Setmeal> serMealList = setmealDao.findSerMeal(queryPageBean.getQueryString());
        //返回分页查询套餐
        return new PageResult(total,serMealList);
    }

    /**
     * 查询全部套餐，用户页面展示
     */
    @Override
    public List<Setmeal> findAllSetMeal() {
        return setmealDao.queryAll();
    }

    /**
     * 用户查询套餐详情
     * @param id
     * @return
     */
    @Override
    public Setmeal findById(Integer id) {
//        Setmeal setmeal =
//        System.out.println("----"+setmealDao.findById(id).toString());
//        for (CheckGroup checkGroup : setmeal.getCheckGroups()) {
//
//        }
        return setmealDao.findById(id);
    }

    /**
     * 查询套餐内容，修改套餐
     * @param id
     * @return
     */
    @Override
    public Setmeal setMealfindById(int id) {
        return setmealDao.setMealfindById(id);
    }

    /**
     * 查询套餐与检查组关联信息
     * @param id
     * @return
     */
    @Override
    public List<Integer> findCheckGroupIdsById(int id) {
        return setmealDao.findCheckGroupIdsById(id);
    }

    /**
     * 修改套餐
     * @param setmeal
     * @param checkGroupIds
     */
    @Override
    public void updateSetmeal(Setmeal setmeal, Integer[] checkGroupIds,String tempImgId) {
//        原有图片判断是否为空
        if(tempImgId != null && tempImgId.length() > 0) {
            //删除Redis中原有的图片
            jedisPool.getResource().srem(RedisConstant.SETMEAL_PIC_DB_RESOURCES,tempImgId);
            //存入Redis
            jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES, setmeal.getImg());
        }
        //修改套餐
        setmealDao.updateSetmeal(setmeal);
        //获取套餐id
        Integer id = setmeal.getId();
        //清空套餐与检查组关联信息
        setmealDao.clear(id);
        //判断id是否有值
        if (checkGroupIds != null && checkGroupIds.length > 0) {
            //修改套餐与检查关联表
            this.addOrEditSetmeal(checkGroupIds,id);
        }
        //        生成所有静态化页面放发
        generateMobileStaticHtml();
    }

    /**
     * 套餐和检查组关联表插入
     * @param checkGroupIds
     * @param setmealId
     */
    public void addOrEditSetmeal(Integer[] checkGroupIds,Integer setmealId){

//            循环关联检查组
            for(int checkGroupId : checkGroupIds){
//                插入map
                HashMap<String, Integer> map = new HashMap<>();
                map.put("setmeal_id",setmealId);
                map.put("checkgroup_id", checkGroupId);
                //插入套餐检查组关联表
                setmealDao.addSetmealAndCheckGroup(map);
            }
        }

}
