package com.biao.common;

import com.baomidou.mybatisplus.extension.api.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLIntegrityConstraintViolationException;

@ControllerAdvice(annotations = {RestController.class, Controller.class})
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {
    //
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseResult exception(SQLIntegrityConstraintViolationException e){
        log.info(e.getMessage());
        if (e.getMessage().contains("Duplicate entry")){
            String[] s = e.getMessage().split(" ");
            return ResponseResult.error(s[2]+"已存在");
        }
        return ResponseResult.error("失败");
    }
}
