package com.biao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.biao.entity.Setmeal;
import com.biao.entity.vo.SetmealVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SetmealMapper extends BaseMapper<Setmeal> {
    IPage<SetmealVo> setmealVoPage(@Param("page") Page p,@Param("name") String name);

    SetmealVo getSetmealVo(@Param("id") Long id);
}
