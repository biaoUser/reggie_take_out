package com.biao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.biao.entity.Orders;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface OrderMapper extends BaseMapper<Orders> {


    Orders getOneByNewOrder(@Param("userId") Long userId);
}