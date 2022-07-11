package com.biao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.biao.common.CustoException;
import com.biao.entity.Setmeal;
import com.biao.entity.SetmealDish;
import com.biao.entity.vo.SetmealVo;
import com.biao.mapper.SetmealMapper;
import com.biao.service.SetmealDishService;
import com.biao.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {
    @Autowired
    SetmealDishService setmealDishService;
    @Autowired
    SetmealMapper setmealMapper;

    @Override
    public void saveWithDish(SetmealVo setmealVo) {
        //保存套餐的基本信息
        this.save(setmealVo);//MP返回id字段了
        List<SetmealDish> setmealDishes = setmealVo.getSetmealDishes();
        setmealDishes.stream().forEach(item ->
                item.setSetmealId(setmealVo.getId()));
        setmealDishService.saveBatch(setmealDishes);

    }

    @Override
    public IPage<SetmealVo> setmealVoPage(Integer page, Integer pageSize, String name) {
        Page p = new Page(page, pageSize);

        return setmealMapper.setmealVoPage(p, name);
    }

    @Transactional
    @Override
    public void deletes(String ids) {
        List<String> list = Arrays.asList(ids.split(","));
        QueryWrapper<Setmeal> wrapper = new QueryWrapper<>();
        wrapper.in("id", list);
        wrapper.eq("status", 1);
        int statusCount = this.count(wrapper);
        if (statusCount > 0) {
            throw new CustoException("存在正在售卖套餐,不可删除");
        }
        this.removeByIds(list);
        QueryWrapper<SetmealDish> wrapper1 = new QueryWrapper<>();
        wrapper1.in("setmeal_id", list);
        setmealDishService.remove(wrapper1);


    }

    @Override
    public void updateStatus(Integer status, String ids) {
        List<String> list = Arrays.asList(ids.split(","));
        UpdateWrapper<Setmeal> wrapper = new UpdateWrapper<>();
        wrapper.set("status", status);
        wrapper.in("id", list);
        this.update(wrapper);
    }

    @Override
    public SetmealVo getSetmealVo(Long id) {

        return baseMapper.getSetmealVo(id);
    }

    @Transactional
    @Override
    public void updateSetmealVo(SetmealVo setmealVo) {
        this.updateById(setmealVo);
        QueryWrapper<SetmealDish>wrapper=new QueryWrapper<>();
        wrapper.eq("setmeal_id",setmealVo.getId());
        setmealDishService.remove(wrapper);
        List<SetmealDish> setmealDishes = setmealVo.getSetmealDishes();
        setmealDishes.stream().forEach(item ->
                item.setSetmealId(setmealVo.getId())
        );
        setmealDishService.saveBatch(setmealDishes);

    }
}
