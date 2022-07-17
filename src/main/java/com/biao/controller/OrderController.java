package com.biao.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.biao.common.BaseContext;
import com.biao.common.ResponseResult;
import com.biao.entity.OrderDetail;
import com.biao.entity.Orders;
import com.biao.entity.ShoppingCart;
import com.biao.entity.vo.OrdersVo;
import com.biao.service.OrderDetailService;
import com.biao.service.OrderService;
import com.biao.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 订单
 */
@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderDetailService orderDetailService;

    @Autowired
    private ShoppingCartService shoppingCartService;

    //再来一单
    @PostMapping("/again")
    public ResponseResult again(@RequestBody Map<String, Long> map) {
        Long orderId = map.get("id");
        QueryWrapper<OrderDetail> wrapper = new QueryWrapper<>();
        wrapper.eq("order_id", orderId);
        Long userId = BaseContext.getCurrentId();
        List<OrderDetail> list = orderDetailService.list(wrapper);
        list.stream().forEach(item -> {
            ShoppingCart shoppingCart = new ShoppingCart();
            shoppingCart.setName(item.getName());
            shoppingCart.setImage(item.getImage());
            shoppingCart.setUserId(userId);
            shoppingCart.setDishId(item.getDishId());
            shoppingCart.setSetmealId(item.getSetmealId());
            shoppingCart.setDishFlavor(item.getDishFlavor());
            shoppingCart.setNumber(item.getNumber());
            shoppingCart.setAmount(item.getAmount());
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartService.save(shoppingCart);
        });
        return ResponseResult.success("成功");
    }

    /**
     * 最新账单
     */
    @GetMapping("/userPage")
    public ResponseResult userPage(Integer page, Integer pageSize) {
        Long userId = BaseContext.getCurrentId();
        IPage<OrdersVo> voIPage = new Page<>();
        QueryWrapper<Orders> wrapper = new QueryWrapper<>();
        //找最近最新的订单
        if (pageSize.equals(1) || pageSize == 1) {

            IPage<Orders> p = new Page<>(0, 1);
            wrapper.eq("status", 4);
            wrapper.orderByDesc("order_time");
            wrapper.eq("user_id", userId);
            IPage<Orders> ordersIPage = orderService.page(p, wrapper);
            Orders orders = ordersIPage.getRecords().get(0);
            OrdersVo vo = new OrdersVo();
            BeanUtils.copyProperties(orders, vo);
            List<OrderDetail> orderDetails = orderDetailService.list(new QueryWrapper<OrderDetail>().eq("order_id", vo.getId()));
            vo.setOrderDetails(orderDetails);
            BeanUtils.copyProperties(ordersIPage, voIPage, "records");
            ArrayList<OrdersVo> ordersVos = new ArrayList<>();
            ordersVos.add(vo);
            voIPage.setRecords(ordersVos);
            return ResponseResult.success(voIPage);
        }
        //找全部订单
//       Orders orders= orderService.getOneByNewOrder(userId);
        IPage<Orders> p = new Page<>(page, pageSize);
        wrapper.eq("user_id", userId);
        IPage<Orders> ordersIPage = orderService.page(p, wrapper);
        BeanUtils.copyProperties(ordersIPage, voIPage, "records");
        List<Orders> records = ordersIPage.getRecords();
        List<OrdersVo> voList = records.stream().map(item -> {
            Long id = item.getId();
            List<OrderDetail> orderDetails = orderDetailService.list(new QueryWrapper<OrderDetail>().eq("order_id", id));
            OrdersVo vo = new OrdersVo();
            BeanUtils.copyProperties(item, vo);
            vo.setOrderDetails(orderDetails);
            return vo;
        }).collect(Collectors.toList());
        voIPage.setRecords(voList);
        return ResponseResult.success(voIPage);

    }


    /**
     * 用户下单
     *
     * @param orders
     * @return
     */
    @PostMapping("/submit")
    public ResponseResult<String> submit(@RequestBody Orders orders) {
        log.info("订单数据：{}", orders);
        orderService.submit(orders);
        return ResponseResult.success("下单成功");
    }

    @GetMapping("/page")
    public ResponseResult page(@RequestParam("page") Integer page,
                               @RequestParam("pageSize") Integer pageSize,
                               @RequestParam(value = "number", required = false) String number,
                               @RequestParam(value = "beginTime", required = false) String beginTime,
                               @RequestParam(value = "endTime", required = false) String endTime) {
        IPage<Orders> p = new Page<>(page, pageSize);
        QueryWrapper<Orders> wrapper = new QueryWrapper<>();
        wrapper.like(StringUtils.isNotEmpty(number), "number", number);
        wrapper.between(StringUtils.isNotEmpty(beginTime) && StringUtils.isNotEmpty(endTime), "order_time", beginTime, endTime);
        IPage<Orders> ordersIPage = orderService.page(p, wrapper);
        return ResponseResult.success(ordersIPage);
    }

    @PutMapping
    public ResponseResult update(@RequestBody Orders orders) {
        orderService.updateById(orders);
        return ResponseResult.success("修改成功");
    }
}