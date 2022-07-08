package com.biao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.biao.common.CustoException;
import com.biao.entity.Category;
import com.biao.entity.Dish;
import com.biao.entity.Setmeal;
import com.biao.mapper.CategoryMapper;
import com.biao.service.CategoryService;
import com.biao.service.DishService;
import com.biao.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    DishService dishService;
    @Autowired
    SetmealService setmealService;


    /***
     * @return:void
     * @param:[ids]
     *判断是否关联菜品和套餐
     */

    @Override
    public void remove(Long ids) {
        QueryWrapper<Dish> wrapper = new QueryWrapper<>();
        wrapper.eq("category_id", ids);
        int count1 = dishService.count(wrapper);
        if (count1 > 0) {
           throw  new CustoException("已关联菜品无法删除");
        }
        QueryWrapper<Setmeal> wrapper1 = new QueryWrapper<>();
        wrapper.eq("category_id", ids);
        final int count = dishService.count(wrapper);
        if (count > 0) {
            throw  new CustoException("已关联套餐无法删除");
        }
        super.removeById(ids);
    }
}
