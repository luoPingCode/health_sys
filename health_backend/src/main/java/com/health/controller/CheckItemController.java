package com.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.health.constant.MessageConstant;
import com.health.entity.PageResult;
import com.health.entity.QueryPageBean;
import com.health.entity.Result;
import com.health.exception.DeleteCheckItemException;
import com.health.pojo.CheckItem;
import com.health.service.CheckItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author LuoPing
 * @date 2022/10/25 21:59
 */
@RestController
@RequestMapping("/checkItem")
public class CheckItemController {
    @Reference
    private CheckItemService checkItemService;

    /**
     * 分页查询
     * @param queryPageBean
     */
    @PostMapping("queryCheckItemList")
    public PageResult queryCheckItemList(@RequestBody QueryPageBean queryPageBean){
//        System.out.println("进来了"+queryPageBean.getPageSize()+queryPageBean.getCurrentPage());
        return checkItemService.queryCheckItem(queryPageBean);
    }

    /**
     * 添加检查项
     * @param checkItem
     */
    @PostMapping
    @PreAuthorize("hasAuthority('CHECKITEM_ADD')")
    public Result addCheckItem(@RequestBody CheckItem checkItem){
//        System.out.println("add进来了");
        try{
            checkItemService.add(checkItem);
            return new Result(true, MessageConstant.ADD_CHECKITEM_SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.ADD_CHECKITEM_FAIL);
        }
    }

    /**
     * 根据ID查询检查项
     * @param id
     * @return
     */
    @GetMapping
    public Result queryCheckItemById(int id){
        CheckItem checkItem = checkItemService.queryCheckItemByid(id);
        return new Result(true,MessageConstant.QUERY_CHECKITEM_SUCCESS,checkItem);
    }
    /**
     * 编辑检查项
     * @param checkItem
     * @return
     */
    @PutMapping
    @PreAuthorize("hasAuthority('CHECKITEM_EDIT')")
    public Result updateCheckItem(@RequestBody CheckItem checkItem){
        try {
            checkItemService.updateCheckItemById(checkItem);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.EDIT_CHECKITEM_FAIL);
        }
        return new Result(true, MessageConstant.EDIT_CHECKITEM_SUCCESS);
    }

    /**
     * 删除检查项
     * @param id
     * @return
     */
    @DeleteMapping
    @PreAuthorize("hasAuthority('CHECKITEM_DELETE')")
    public Result deleteCheckItem(int id){
        try {
            checkItemService.deleteCheckItem(id);
        }catch (Exception e){
            e.printStackTrace();
            if (e instanceof DeleteCheckItemException){
                //判断异常类型
                return new Result(false,"已经有检查组关联此检查项，请先删除相应的检查组");
            }
            return new Result(false,MessageConstant.DELETE_CHECKITEM_FAIL);
        }
        return new Result(true, MessageConstant.DELETE_CHECKITEM_SUCCESS);
    }

    /**
     * 查询所有检查项，用于检查组中显示
     * @return
     */
    @GetMapping("findAll")
    public Result queryAllCheckItem(){
        List<CheckItem> checkItem = null;
        try {
            checkItem = checkItemService.findAllCheckItem();//不分页查询
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_CHECKITEM_FAIL);
        }
        return new Result(true,MessageConstant.QUERY_CHECKITEM_SUCCESS,checkItem);
    }
}
