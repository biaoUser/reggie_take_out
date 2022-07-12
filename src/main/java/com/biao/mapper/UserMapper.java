package com.biao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.biao.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
