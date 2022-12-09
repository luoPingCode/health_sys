package com.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.health.dao.CheckGroupDao;
import com.health.entity.PageResult;
import com.health.entity.QueryPageBean;
import com.health.pojo.CheckGroup;
import com.health.pojo.CheckItem;
import com.health.service.CheckGroupService;
import com.health.service.CheckItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 检查组业务处理。
 *
 * @author LuoPing
 * @date 2022/10/28 16:52
 */
@Service(interfaceClass = CheckGroupService.class)
@Transactional
public class CheckGroupServiceImpl implements CheckGroupService {
    @Autowired
    private CheckGroupDao checkGroupDao;

    /**
     * 添加检查组
     */
    @Override
    public void addCheckGroup(Integer[] checkItemIds, CheckGroup checkGroup) {
//        添加检查组
        checkGroupDao.addCheckGroup(checkGroup);
//        通过LAST_INSERT_ID()对checkGroup中id赋值而获取检查组id
        Integer groupId = checkGroup.getId();
        //提取的检查组与检查项关联变插入方法
        this.addOrEditCheckGroup(checkItemIds, groupId);
    }

    /**
     * 分页查询检查组
     * @param queryPageBean
     * @return
     */
    @Override
    public PageResult findCheckGroupList(QueryPageBean queryPageBean) {
//        查询总数
//        Long total = checkGroupDao.queryCheckGroupCount();
        //使用pageHelper进行分页
        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        Page<CheckGroup> checkGroupList = checkGroupDao.findCheckGroupList(queryPageBean.getQueryString());
        return new PageResult(checkGroupList.getTotal(),checkGroupList.getResult());
    }

    /**
     * 根据id查询检查组
     * @param id
     * @return
     */
    @Override
    public CheckGroup findCheckGroupById(int id) {
        return checkGroupDao.findCheckGroupById(id);
    }

    /**
     * 查询检查组与检查项关联
     * @param id
     * @return
     */
    @Override
    public List<Integer> queryCheckItemIdsByCheckGroupId(int id) {
        return checkGroupDao.findCheckItemIdsByCheckGroupId(id);
    }

    /**
     * 编辑检查组
     * @param checkItemIds
     * @param checkGroup
     */
    @Override
    public void updateCheckGroup(Integer[] checkItemIds, CheckGroup checkGroup) {
//        编辑检查组
        checkGroupDao.updateCheckGroup(checkGroup);
        //清空检查组与检查项关联关系
        checkGroupDao.clear(checkGroup.getId());
//        编辑检查组与检查项关联数据
        this.addOrEditCheckGroup(checkItemIds, checkGroup.getId());
    }

    /**
     * 删除检查组
     * @param id
     */
    @Override
    public void deleteCheckGroup(Integer id) {
        //        删除关联检查项
        checkGroupDao.clear(id);
//        删除检查组
        checkGroupDao.delete(id);
    }

    /**
     * 查询所有检查组，用于套餐检查组回显
     * @return
     */
    @Override
    public List<CheckGroup> findAll() {
        return checkGroupDao.findAll();
    }

    /**
     * 提取的检查组关联表新增和修改方法
     * @param checkItemIds
     * @param groupId
     */
    private void addOrEditCheckGroup(Integer[] checkItemIds, Integer groupId) {
        //        判断是检查项itemid否不为空
        if (checkItemIds != null && checkItemIds.length > 0) {
//        遍历检查项id,put进map集合
            for (Integer checkItemId : checkItemIds) {
                Map<String, Integer> map = new HashMap<>();
                map.put("checkGroupId", groupId);
                map.put("checkItemId", checkItemId);
                //插入关联表中
                checkGroupDao.addCheckGroupIdAndItemId(map);
            }
        }
    }
}
