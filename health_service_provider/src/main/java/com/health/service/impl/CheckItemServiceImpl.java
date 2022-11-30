package com.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.health.dao.CheckItemDao;
import com.health.entity.PageResult;
import com.health.entity.QueryPageBean;
import com.health.exception.DeleteCheckItemException;
import com.health.pojo.CheckItem;
import com.health.service.CheckItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 检查项业务处理层
 * @author LuoPing
 * @date 2022/10/25 23:48
 */
@Service(interfaceClass = CheckItemService.class)
@Transactional
public class CheckItemServiceImpl implements CheckItemService {

    @Autowired
    CheckItemDao checkItemDao;

    /**
     * 添加检查项
     * @param checkItem
     */
    @Override
    public void add(CheckItem checkItem) {
//        System.out.println("添加进");
        checkItemDao.add(checkItem);
    }

    /**
     * 分页查询检查项
     * @param queryPageBean
     * @return
     */
    @Override
    public PageResult queryCheckItem(QueryPageBean queryPageBean) {
//        pageHelper分页 ThreadLocal:本地线程
//        在同一个线程中，数据都能拿到
//        System.out.println("进来"+queryPageBean.getCurrentPage()+queryPageBean.getPageSize());
        //使用分页插件分页查询
        PageHelper.startPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize());
        //调用持久层查询所有并返回
        Page<CheckItem> checkItemList = checkItemDao.findCheckItemList(queryPageBean.getQueryString());
        return new PageResult(checkItemList.getTotal(),checkItemList.getResult());
    }

    /**
     * 删除检查项
     * @param id
     */
    @Override
    public void deleteCheckItem(int id) {
//        先判断是否有关联的检查组
        int count = checkItemDao.queryCheckGroupById(id);
        if (count > 0){
            throw new DeleteCheckItemException("该检查项有关联的检查组");
        }
        checkItemDao.deleteCheckItem(id);
    }

    /**
     * 根据ID查询检查项
     * @param id
     * @return
     */
    @Override
    public CheckItem queryCheckItemByid(int id) {
        return checkItemDao.queryCheckItemById(id);
    }

    @Override
    public void updateCheckItemById(CheckItem checkItem) {
        checkItemDao.update(checkItem);
    }

    @Override
    public List<CheckItem> findAllCheckItem() {
        return checkItemDao.findAll();
    }
}
