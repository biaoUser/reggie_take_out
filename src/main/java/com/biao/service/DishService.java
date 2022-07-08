package com.biao.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.biao.entity.Dish;
import com.biao.entity.vo.DishVo;

public interface DishService extends IService<Dish> {
    void saveWithFlavor(DishVo dishVo);

    IPage<DishVo> DishVoPage(IPage<DishVo> iPage, String name);

    Page<DishVo> DishVoPage2(Page<Dish> iPage, String name);

    DishVo getOneDishVo(Long id);

    DishVo getOneDishVo2(Long id);

    void updateWithFlavor(DishVo dishVo);

    void updateStatus(String ids, String type);
}
