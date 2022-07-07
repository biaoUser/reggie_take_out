package com.biao.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.biao.common.ResponseResult;
import com.biao.entity.Employee;
import com.biao.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;

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
    public ResponseResult logout(HttpServletRequest request) {
        request.getSession().removeAttribute("employee");

        return ResponseResult.success("退出成功");
    }

    @PostMapping
    public ResponseResult save(@RequestBody Employee employee, HttpSession session) {
        log.info("新增员工");
        log.info("线程id:{}",Thread.currentThread().getId());
        employee.setPassword(DigestUtils.md5DigestAsHex("123".getBytes()));
//        employee.setCreateTime(LocalDateTime.now());
//        employee.setCreateUser((Long) session.getAttribute("employee"));
//        employee.setUpdateTime(LocalDateTime.now());
//        employee.setUpdateUser((Long) session.getAttribute("employee"));
        employeeService.save(employee);
        return ResponseResult.success("新增员工成功");
    }

    @GetMapping("/page")
    public ResponseResult page1(@RequestParam("page") Integer page,
                                @RequestParam("pageSize") Integer pageSize,
                                @RequestParam(value = "name", required = false) String name


    ) {
        log.info("page={}", page);
        log.info("pageSize={}", pageSize);
        log.info("name={}", name);
        IPage p = new Page<Employee>(page, pageSize);
        QueryWrapper<Employee> wrapper = new QueryWrapper<>();
        wrapper.like(StringUtils.isNotEmpty(name), "username", name);
        wrapper.orderByDesc("update_time");
        IPage iPage = employeeService.page(p, wrapper);
        return ResponseResult.success(iPage);

    }

    //更改状态和更改基本信息
    @PutMapping
    public ResponseResult updateStatusByEmployeeId(@RequestBody Employee employee, HttpSession session) {
        //管理员不能被禁用
        if (employee.getId() == 1) {
            return ResponseResult.error("管理员账号不可以禁用");
        }
        employee.setUpdateTime(LocalDateTime.now());
        employee.setUpdateUser((Long) session.getAttribute("employee"));
        employeeService.updateById(employee);
        return ResponseResult.success("操作成功");

    }

    /**
     * 前端接口
     * function queryEmployeeById (id) {
     * return $axios({
     * url: `/employee/${id}`,
     * method: 'get'
     * })
     * }
     */
    @GetMapping("/{id}")
    public ResponseResult queryEmployeeById(@PathVariable("id") Long id) {
        Employee employee = employeeService.getById(id);
        if (employee != null)
            return ResponseResult.success(employee);

        return ResponseResult.error("没找到对应员工");
    }


}
