package com.biao.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.biao.entity.OrderDetail;
import com.biao.mapper.OrderDetailMapper;
import com.biao.service.OrderDetailService;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {

}