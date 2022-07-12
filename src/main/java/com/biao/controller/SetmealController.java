package com.biao.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.biao.common.ResponseResult;
import com.biao.entity.Setmeal;
import com.biao.entity.vo.SetmealVo;
import com.biao.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/setmeal")
public class SetmealController {
    @Autowired
    private SetmealService setmealService;

    @PostMapping
    public ResponseResult save(@RequestBody SetmealVo setmealVo) {
        log.info("vo={}", setmealVo);
        setmealService.saveWithDish(setmealVo);
        return ResponseResult.success("保存成功");
    }

    @GetMapping("/page")
    public ResponseResult page(@RequestParam("page") Integer page,
                               @RequestParam("pageSize") Integer pageSize,
                               @RequestParam(value = "name", required = false) String name) {
        IPage<SetmealVo> iPage = setmealService.setmealVoPage(page, pageSize, name);

        return ResponseResult.success(iPage);
    }

    @DeleteMapping
    public ResponseResult delete(String ids) {
        setmealService.deletes(ids);
        return ResponseResult.success("删除成功");
    }

    @PostMapping("/status/{status}")
    public ResponseResult updateStatus(@PathVariable("status") Integer status, String ids) {

        setmealService.updateStatus(status, ids);
        return ResponseResult.success("修改成功");
    }

    @GetMapping("/{id}")
    public ResponseResult getSetmealVo(@PathVariable("id") Long id) {
        SetmealVo setmealVo = setmealService.getSetmealVo(id);
        return ResponseResult.success(setmealVo);
    }

    @PutMapping
    public ResponseResult update(@RequestBody SetmealVo setmealVo) {
        setmealService.updateSetmealVo(setmealVo);

        return ResponseResult.success("修改成功");
    }

    @GetMapping("/list")
    public ResponseResult list(Long categoryId, Integer status) {

        QueryWrapper<Setmeal> wrapper=new QueryWrapper<>();
        wrapper.eq(categoryId!=null,"category_id",categoryId);
        wrapper.eq(status!=null,"status",status);
        wrapper.orderByDesc("update_time");

        List<Setmeal> list = setmealService.list(wrapper);

        return ResponseResult.success(list);
    }
}
