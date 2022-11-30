package com.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.health.constant.MessageConstant;
import com.health.entity.PageResult;
import com.health.entity.QueryPageBean;
import com.health.entity.Result;
import com.health.pojo.CheckGroup;
import com.health.service.CheckGroupService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author LuoPing
 * @date 2022/10/28 16:20
 */
@RestController
@RequestMapping("checkGroup")
public class CheckGroupController {

    @Reference
    private CheckGroupService checkGroupService;

    /**
     * 新增检查组
     *
     * @param checkItemIds
     * @param checkGroup
     * @return
     */
    @PostMapping
    @PreAuthorize("hasAuthority('CHECKGROUP_ADD')")
    public Result add(Integer[] checkItemIds, @RequestBody CheckGroup checkGroup) {
        try {
            checkGroupService.addCheckGroup(checkItemIds, checkGroup);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_CHECKGROUP_FAIL);
        }
//        插入成功
        return new Result(true, MessageConstant.ADD_CHECKGROUP_SUCCESS);
    }

    /**
     * 分页查询检查组
     *
     * @param queryPageBean
     * @return
     */
    @PostMapping("queryCheckGroupList")
    public PageResult queryCheckGroupList(@RequestBody QueryPageBean queryPageBean) {
        return checkGroupService.findCheckGroupList(queryPageBean);
    }

    //    根据id查询检查组
    @GetMapping
    public Result queryCheckGroupById(int id) {
        CheckGroup checkGroup = null;
        try {
            checkGroup = checkGroupService.findCheckGroupById(id);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_CHECKGROUP_FAIL);
        }
        return new Result(true, MessageConstant.ADD_CHECKGROUP_SUCCESS, checkGroup);
    }

    //    查询检查组与检查项关联数据
    @GetMapping("findCheckItemIdsByCheckGroupId")
    public List<Integer> findCheckItemIdsByCheckGroupId(int id) {
        List<Integer> checkItemIds = null;
        try {
            checkItemIds = checkGroupService.queryCheckItemIdsByCheckGroupId(id);
            return checkItemIds;
        } catch (Exception e) {
            e.printStackTrace();
//            return new Result(false, MessageConstant.QUERY_CHECKGROUP_FAIL);
            return checkItemIds;
        }
//        return new Result(true, MessageConstant.ADD_CHECKGROUP_SUCCESS, checkItemIds);
    }

    //    修改检查组
    @PutMapping
    @PreAuthorize("hasAuthority('CHECKGROUP_EDIT')")
    public Result update(Integer[] checkItemIds, @RequestBody CheckGroup checkGroup) {
        try {
            checkGroupService.updateCheckGroup(checkItemIds, checkGroup);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.EDIT_CHECKGROUP_FAIL);
        }
        return new Result(true, MessageConstant.EDIT_CHECKGROUP_SUCCESS);
    }

    /**
     * 删除检查组
     *
     * @param id
     * @return
     */
    @DeleteMapping
    @PreAuthorize("hasAuthority('CHECKGROUP_DELETE')")//进行权限校验
    public Result delete(Integer id) {
        try {
            checkGroupService.deleteCheckGroup(id);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.DELETE_CHECKGROUP_FAIL);
        }
        return new Result(true, MessageConstant.DELETE_CHECKGROUP_SUCCESS);
    }

    /**
     * 查找所有检查组，用于套餐新增回显检查组信息
     *
     * @return
     */
    @GetMapping("findAll")
    public Result findAll() {
        List<CheckGroup> checkGroupList = null;
        try {
            checkGroupList = checkGroupService.findAll();
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_CHECKGROUP_FAIL);
        }
        return new Result(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS, checkGroupList);
    }
}
