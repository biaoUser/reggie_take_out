package com.biao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.biao.entity.Category;
import com.biao.entity.Dish;
import com.biao.entity.DishFlavor;
import com.biao.entity.vo.DishVo;
import com.biao.mapper.DishMapper;
import com.biao.service.CategoryService;
import com.biao.service.DishFlavorService;
import com.biao.service.DishService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
    @Autowired
    DishFlavorService dishFlavorService;
    @Autowired
    CategoryService categoryService;

    @Transactional
    @Override
    public void saveWithFlavor(DishVo dishVo) {
        this.save(dishVo);
        Long dishVoId = dishVo.getId();
        List<DishFlavor> flavors = dishVo.getFlavors();
//        flavors = flavors.stream().map(dishFlavor -> {
////            dishFlavor.setDishId(dishVoId);
////            return dishFlavor;
////        }).collect(Collectors.toList());
        flavors.stream().forEach(dishFlavor -> dishFlavor.setDishId(dishVoId));
        dishFlavorService.saveBatch(flavors);
    }

    @Override
    public IPage<DishVo> DishVoPage(IPage<DishVo> iPage, String name) {
        return baseMapper.page(iPage, name);
    }

    @Override
    public Page<DishVo> DishVoPage2(Page<Dish> iPage, String name) {

        QueryWrapper<Dish> wrapper = new QueryWrapper<>();
        wrapper.like(StringUtils.isNotEmpty(name), "name", name);
        wrapper.orderByDesc("update_time");
        Page<Dish> page = this.page(iPage, wrapper);
        Page<DishVo> dishVoPage = new Page<>();
        BeanUtils.copyProperties(page, dishVoPage, "records");
        //真实数据
        List<Dish> records = page.getRecords();
        List<DishVo> dishVos = records.stream().map(item -> {
            DishVo dishVo = new DishVo();
            Long categoryId = item.getCategoryId();
            if (categoryId != null) {
                Category byId = categoryService.getById(categoryId);
                dishVo.setCategoryName(byId.getName());
                BeanUtils.copyProperties(item, dishVo);
            }
            return dishVo;
        }).collect(Collectors.toList());

        dishVoPage.setRecords(dishVos);

        return dishVoPage;
    }

    @Transactional
    @Override
    public DishVo getOneDishVo(Long id) {
        Dish dish = this.getById(id);
        List<DishFlavor> flavors = dishFlavorService.list(new QueryWrapper<DishFlavor>().eq("dish_id", id));
        DishVo dishVo = new DishVo();
        dishVo.setFlavors(flavors);
        BeanUtils.copyProperties(dish, dishVo);
        return dishVo;
    }

    @Override
    public DishVo getOneDishVo2(Long id) {
        return baseMapper.getOneDishVo(id);
    }
    @Transactional
    @Override
    public void updateWithFlavor(DishVo dishVo) {
        this.updateById(dishVo);
        Long id = dishVo.getId();
        QueryWrapper<DishFlavor> wrapper = new QueryWrapper<>();
        wrapper.eq("dish_id",id );
        dishFlavorService.remove(wrapper);
        List<DishFlavor> flavors = dishVo.getFlavors();
        flavors.stream().forEach(item -> item.setDishId(id));
        dishFlavorService.saveBatch(flavors);
    }

    @Override
    public void updateStatus(String ids, String type) {
        int status = Integer.parseInt(type);
        String[] id = ids.split(",");
        for (int i = 0; i < id.length; i++) {
            baseMapper.updateStatus(id[i],status);
        }

    }
}
