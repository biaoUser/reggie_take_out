package com.biao.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.biao.entity.Setmeal;
import com.biao.entity.vo.SetmealVo;

public interface SetmealService extends IService<Setmeal> {
    /**
     * @return:void
     * @param:[setmealVo]
     * 新增套餐并保持和菜品的关联关系
     */

    void saveWithDish(SetmealVo setmealVo);

    IPage<SetmealVo> setmealVoPage(Integer page, Integer pageSize, String name);

    void deletes(String ids);

    void updateStatus(Integer status, String ids);

    SetmealVo getSetmealVo(Long id);

    void updateSetmealVo(SetmealVo setmealVo);
}
