package com.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.health.constant.MessageConstant;
import com.health.constant.RedisConstant;
import com.health.entity.PageResult;
import com.health.entity.QueryPageBean;
import com.health.entity.Result;
import com.health.pojo.Setmeal;
import com.health.service.SetmealService;
import com.health.utils.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author LuoPing
 * @date 2022/10/29 23:37
 */
@RestController
@RequestMapping("setmeal")
public class SetmealController {
    @Reference
    private SetmealService setmealService;
    @Autowired
    private JedisPool jedisPool; //Redis缓存对象

    /**
     * 新增检查套餐
     *
     * @param checkGroupIds
     * @param setmeal
     * @return
     */
    @PostMapping("add")
    public Result add(Integer[] checkGroupIds, @RequestBody Setmeal setmeal) {
        try {//调用业务层添加套餐
            setmealService.add(checkGroupIds, setmeal);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_SETMEAL_FAIL);
        }
        return new Result(true, MessageConstant.ADD_SETMEAL_SUCCESS);
    }

    /**
     * 上传图片
     *
     * @param imgFile
     * @return
     */
    @PostMapping("upload")
    public Result uploadFile(@RequestParam("imgFile") MultipartFile imgFile) {
//        获取文件名字
        String originalFilename = imgFile.getOriginalFilename();

        String substring = null;
        if (originalFilename != null) {
            //截取后缀名
            substring = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
//        使用UUID重命名文件+源文件后缀名
        String fileName = UUID.randomUUID().toString() + substring;
        //使用七牛云上传云存储
        try {
            QiniuUtils.upload2Qiniu(imgFile.getBytes(), fileName);
            //存入Redis中
            jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_RESOURCES, fileName);
        } catch (IOException e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.PIC_UPLOAD_FAIL);
        }
        return new Result(true, MessageConstant.PIC_UPLOAD_SUCCESS, fileName);
    }

    /**
     * 分页查询套餐
     *
     * @return
     */
    @PostMapping("findPage")
    public PageResult querySetmeal(@RequestBody QueryPageBean queryPageBean) {
        return setmealService.querySetmeal(queryPageBean);
    }

    /**
     * 修改套餐
     *
     * @param setmeal
     * @return
     */
    @PutMapping("edit")
    public Result update(@RequestBody Setmeal setmeal, Integer[] checkGroupIds,String tempImgId) {
        try {
            setmealService.updateSetmeal(setmeal, checkGroupIds,tempImgId);
            return new Result(true, MessageConstant.EDIT_SETMEAL_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.EDIT_SETMEAL_FAIL);
        }

    }

    /**
     * 根据id查询套餐，用于套餐回显修改
     *
     * @param id
     * @return
     */
    @GetMapping("findById")
    public Result setMealFindById(@RequestParam("id") int id) {
        try {
            Setmeal setmeal = setmealService.setMealfindById(id);
            return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS, setmeal);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_SETMEAL_FAIL);
        }
    }

    /**
     * 根据查询套餐与检查组关联信息
     *
     * @param id
     * @return
     */
    @GetMapping("findCheckGroupIdsBySetMealId")
    public List<Integer> findCheckGroupIdsBySetMealId(@RequestParam("id") int id) {
        try {
            List<Integer> checkGroupIds = setmealService.findCheckGroupIdsById(id);
            return checkGroupIds;
        } catch (Exception e) {
            e.printStackTrace();
            ArrayList<Integer> list = new ArrayList<>();
            return list;
        }
    }
}
