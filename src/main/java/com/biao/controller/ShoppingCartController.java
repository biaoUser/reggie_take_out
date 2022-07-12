package com.biao.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.biao.common.BaseContext;
import com.biao.common.ResponseResult;
import com.biao.entity.ShoppingCart;
import com.biao.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {
    @Autowired
    private ShoppingCartService shoppingCartService;

    @PostMapping("/add")
    public ResponseResult add(@RequestBody ShoppingCart shoppingCart) {
        Long userId = BaseContext.getCurrentId();
        shoppingCart.setUserId(userId);
        QueryWrapper<ShoppingCart> wrapper = new QueryWrapper<>();
        //如果添加多份相同的只需修改数量即可
        Long dishId = shoppingCart.getDishId();
        ShoppingCart shop = null;
        wrapper.eq("user_id", userId);
        if (dishId != null) {
            wrapper.eq("dish_id", dishId);

            shop = shoppingCartService.getOne(wrapper);
            if (shop == null) {
                shoppingCart.setNumber(1);
                shoppingCart.setCreateTime(LocalDateTime.now());
                shoppingCartService.save(shoppingCart);
                shop=shoppingCart;
                return ResponseResult.success(shop);
            }
            shop.setCreateTime(LocalDateTime.now());
            shop.setNumber(shop.getNumber() + 1);
            shoppingCartService.updateById(shop);
            return ResponseResult.success(shop);
        } else {
            Long setmealId = shoppingCart.getSetmealId();
            wrapper.eq("setmeal_id", setmealId);
            shop = shoppingCartService.getOne(wrapper);
            if (shop == null) {
                shoppingCart.setNumber(1);
                shoppingCartService.save(shoppingCart);
                shop=shoppingCart;
                return ResponseResult.success(shop);
            }
            shop.setNumber(shop.getNumber() + 1);
            shop.setCreateTime(LocalDateTime.now());
            shoppingCartService.updateById(shop);
            return ResponseResult.success(shop);
        }
    }

    /**
     * 查看购物车
     * @return
     */
    @GetMapping("/list")
    public ResponseResult<List<ShoppingCart>> list(){
        log.info("查看购物车...");

        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,BaseContext.getCurrentId());
        queryWrapper.orderByAsc(ShoppingCart::getCreateTime);

        List<ShoppingCart> list = shoppingCartService.list(queryWrapper);

        return ResponseResult.success(list);
    }

    /**
     * 清空购物车
     * @return
     */
    @DeleteMapping("/clean")
    public ResponseResult<String> clean(){
        //SQL:delete from shopping_cart where user_id = ?

        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,BaseContext.getCurrentId());

        shoppingCartService.remove(queryWrapper);

        return ResponseResult.success("清空购物车成功");
    }
    /**
     * 添加购物车
     * @param shoppingCart
     * @return
     */
//    @PostMapping("/add")
//    public ResponseResult<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart){
//        log.info("购物车数据:{}",shoppingCart);
//
//        //设置用户id，指定当前是哪个用户的购物车数据
//        Long currentId = BaseContext.getCurrentId();
//        shoppingCart.setUserId(currentId);
//
//        Long dishId = shoppingCart.getDishId();
//
//        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.eq(ShoppingCart::getUserId,currentId);
//
//        if(dishId != null){
//            //添加到购物车的是菜品
//            queryWrapper.eq(ShoppingCart::getDishId,dishId);
//
//        }else{
//            //添加到购物车的是套餐
//            queryWrapper.eq(ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
//        }
//
//        //查询当前菜品或者套餐是否在购物车中
//        //SQL:select * from shopping_cart where user_id = ? and dish_id/setmeal_id = ?
//        ShoppingCart cartServiceOne = shoppingCartService.getOne(queryWrapper);
//
//        if(cartServiceOne != null){
//            //如果已经存在，就在原来数量基础上加一
//            Integer number = cartServiceOne.getNumber();
//            cartServiceOne.setNumber(number + 1);
//            shoppingCartService.updateById(cartServiceOne);
//        }else{
//            //如果不存在，则添加到购物车，数量默认就是一
//            shoppingCart.setNumber(1);
//            shoppingCart.setCreateTime(LocalDateTime.now());
//            shoppingCartService.save(shoppingCart);
//            cartServiceOne = shoppingCart;
//        }
//
//        return ResponseResult.success(cartServiceOne);
//    }
}
