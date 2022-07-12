package com.biao.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.biao.entity.User;
import com.biao.mapper.UserMapper;
import com.biao.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
