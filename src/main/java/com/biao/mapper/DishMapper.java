package com.biao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.biao.entity.Dish;
import com.biao.entity.vo.DishVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DishMapper extends BaseMapper<Dish> {
    IPage<DishVo> page(@Param("iPage") IPage<DishVo> iPage, @Param("name") String name);

    DishVo getOneDishVo(@Param("id") Long id);

    void updateStatus(@Param("id") String id, @Param("status") int status);
}
