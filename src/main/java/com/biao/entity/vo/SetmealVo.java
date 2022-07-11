package com.biao.entity.vo;

import com.biao.entity.Setmeal;
import com.biao.entity.SetmealDish;
import lombok.Data;

import java.util.List;


@Data
public class SetmealVo extends Setmeal {
    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
