package com.biao.controller;

import com.biao.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class SetmealController {
    @Autowired
    private SetmealService setmealService;
}
