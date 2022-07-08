package com.biao.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.biao.common.ResponseResult;
import com.biao.entity.Category;
import com.biao.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//种类
@RestController
@Slf4j
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public ResponseResult save(@RequestBody Category category) {

        categoryService.save(category);
        return ResponseResult.success("添加成功");
    }

    @GetMapping("/page")
    public ResponseResult page(@RequestParam("page") Integer page,
                               @RequestParam("pageSize") Integer pageSize) {
        QueryWrapper<Category> wrapper=new QueryWrapper<>();
        wrapper.orderByAsc("sort");
        IPage p=new Page(page,pageSize);
        IPage iPage = categoryService.page(p,wrapper);
        return ResponseResult.success(iPage);
    }
    @DeleteMapping
    public ResponseResult remove(@RequestParam("ids")Long ids){
        categoryService.remove(ids);
        return ResponseResult.success("删除成功");

    }
    //菜品管理获取分类列表管理
    @GetMapping("/list")
    public ResponseResult list(@RequestParam("type")Integer type){
        QueryWrapper<Category> wrapper=new QueryWrapper<>();
        wrapper.eq("type",type);
        wrapper.orderByAsc("sort").orderByAsc("update_time");
        List<Category> list = categoryService.list(wrapper);
        return ResponseResult.success(list);
    }
}
