package com.biao.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.biao.common.ResponseResult;
import com.biao.entity.Employee;
import com.biao.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/login")
    public ResponseResult login(HttpServletRequest request, @RequestBody Employee employee) {
        QueryWrapper<Employee> wrapper = new QueryWrapper<>();
        Employee e = employeeService.getOne(wrapper.eq("username", employee.getUsername()));
        if (e == null) {
            return ResponseResult.error("登陆失败");
        }
        String password = DigestUtils.md5DigestAsHex(employee.getPassword().getBytes());
        if (!password.equals(e.getPassword())) {
            return ResponseResult.error("登陆失败");
        }
        if (e.getStatus() == 0) {
            return ResponseResult.error("账号异常");
        }
        HttpSession session = request.getSession();
        session.setAttribute("employee", e.getId());
        return ResponseResult.success(e);
    }
    @PostMapping("/logout")
    public ResponseResult logout(HttpServletRequest request){
        request.getSession().removeAttribute("employee");

        return ResponseResult.success("退出成功");
    }
}
