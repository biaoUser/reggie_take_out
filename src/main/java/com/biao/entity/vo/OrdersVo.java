package com.biao.entity.vo;

import com.biao.entity.OrderDetail;
import com.biao.entity.Orders;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class OrdersVo extends Orders {
    private List<OrderDetail> orderDetails = new ArrayList<>();

}
