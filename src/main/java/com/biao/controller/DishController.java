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
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


@Slf4j
@RestController
@RequestMapping("/dish")
public class DishController {
    @Autowired
    private DishService dishService;
    @Autowired
    DishFlavorService dishFlavorService;

    @Autowired
    RedisTemplate redisTemplate;

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
        //更新菜品后删除redis缓存
        //获取所有以dish_开头的键
        Set keys = redisTemplate.keys("dish_*");
        //删除所有
        redisTemplate.delete(keys);

        //删除指定的redis缓存
        String key="dish_"+dishVo.getCategoryId()+"_1";
        redisTemplate.delete(key);
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
        List<DishVo> l = null;
        //准备redis中的key
        String key = "dish_" + dish.getCategoryId() + "_" + dish.getStatus();
        //先查询redis中是否缓存
        l = (List<DishVo>) redisTemplate.opsForValue().get(key);
        if (l != null) {
            return ResponseResult.success(l);
        }
//        log.info("dish={}",dish.toString());
        QueryWrapper<Dish> wrapper = new QueryWrapper<>();
        wrapper.eq(dish.getCategoryId() != null, "category_id", dish.getCategoryId());
        wrapper.eq("status", 1);
        wrapper.orderByAsc("sort").orderByDesc("update_time");
        List<Dish> list = dishService.list(wrapper);

        l = list.stream().map(item -> {
            QueryWrapper<DishFlavor> w = new QueryWrapper<>();
            w.eq("dish_id", item.getId());
            List<DishFlavor> list1 = dishFlavorService.list(w);
            DishVo dishVo = new DishVo();
            BeanUtils.copyProperties(item, dishVo);
            dishVo.setFlavors(list1);
            return dishVo;
        }).collect(Collectors.toList());
        redisTemplate.opsForValue().set(key,l,60, TimeUnit.MINUTES);
        return ResponseResult.success(l);
    }

}
