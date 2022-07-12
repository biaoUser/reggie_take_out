package com.biao.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.biao.common.ResponseResult;
import com.biao.entity.Dish;
import com.biao.entity.DishFlavor;
import com.biao.entity.vo.DishVo;
import com.biao.service.DishFlavorService;
import com.biao.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Slf4j
@RestController
@RequestMapping("/dish")
public class DishController {
    @Autowired
    private DishService dishService;
    @Autowired
    DishFlavorService dishFlavorService;

    @PostMapping
    public ResponseResult addDish(@RequestBody DishVo dishVo) {
        dishService.saveWithFlavor(dishVo);
        return ResponseResult.success("添加成功");
    }

    @GetMapping("/page")
    public ResponseResult page(@RequestParam("page") Integer page,
                               @RequestParam("pageSize") Integer pageSize,
                               @RequestParam(value = "name", required = false) String name) {

        //方式一
//        IPage<DishVo> iPage = new Page<>(page, pageSize);
//        IPage<DishVo> p = dishService.DishVoPage(iPage, name);
        //方式二
        Page<Dish> iPage = new Page<>(page, pageSize);
        Page<DishVo> p = dishService.DishVoPage2(iPage, name);

        return ResponseResult.success(p);
    }

    @GetMapping("/{id}")
    public ResponseResult getOne(@PathVariable("id") Long id) {
        //方式一利用mybatis-plus框架
//        DishVo dishVo=dishService.getOneDishVo(id);
        //方式二手写sql
        DishVo dishVo = dishService.getOneDishVo2(id);
        return ResponseResult.success(dishVo);
    }

    @PutMapping
    public ResponseResult update(@RequestBody DishVo dishVo) {

        dishService.updateWithFlavor(dishVo);

        return ResponseResult.success("修改成功");
    }

    @DeleteMapping
    public ResponseResult delete(@RequestParam("ids") String ids) {
        String[] split = ids.split(",");
        dishService.removeByIds(Arrays.asList(split));

        return ResponseResult.success("删除成功");
    }

    /**
     * @param:[ids, type]
     * type需要修改的状态
     */

    @PostMapping("/status/{type}")
    public ResponseResult updateStatus(@RequestParam("ids") String ids, @PathVariable("type") String type) {
        //方式1手写sql
        dishService.updateStatus(ids, type);
        return ResponseResult.success("修改成功");
    }

    @GetMapping("/list")
    public ResponseResult list(Dish dish) {
//        log.info("dish={}",dish.toString());
        QueryWrapper<Dish> wrapper = new QueryWrapper<>();
        wrapper.eq(dish.getCategoryId() != null, "category_id", dish.getCategoryId());
        wrapper.eq("status", 1);
        wrapper.orderByAsc("sort").orderByDesc("update_time");
        List<Dish> list = dishService.list(wrapper);
        List<DishVo> l = new ArrayList<>();
        list.stream().forEach(item -> {
            QueryWrapper<DishFlavor> w=new QueryWrapper<>();
            w.eq("dish_id",item.getId());
            List<DishFlavor> list1 = dishFlavorService.list(w);
            DishVo dishVo=new DishVo();
            BeanUtils.copyProperties(item,dishVo);
            dishVo.setFlavors(list1);
            l.add(dishVo);
        });
        return ResponseResult.success(l);
    }

}
