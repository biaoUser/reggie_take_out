package com.biao.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.biao.common.QQSendEmailService;
import com.biao.common.ResponseResult;
import com.biao.entity.User;
import com.biao.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    QQSendEmailService qqSendEmailService;

    @PostMapping("/sendMsg")
    public ResponseResult sendMsg(@RequestBody Map<String, String> map, HttpSession session) {
        String phone = map.get("phone");
        //发送邮箱
        //2799298058@qq.com
//        String code = qqSendEmailService.send_email(phone);
        session.setAttribute(phone, "1111");
        return ResponseResult.success("消息发送成功");
    }

    @PostMapping("/login")
    public ResponseResult login(@RequestBody Map<String, String> map, HttpSession session) {
        String phone = map.get("phone");
        String code = map.get("code");
        String c = (String) session.getAttribute(phone);
        if (code != null && code.equals(c)) {
            //如果数据库没有该用户则新增
            QueryWrapper<User> wrapper = new QueryWrapper<>();
            wrapper.eq("phone", phone);
            User user = userService.getOne(wrapper);
            if (user == null) {
                user = new User();
                user.setPhone(phone);
                user.setStatus(1);
                userService.save(user);
            }
            session.removeAttribute(phone);
            session.setAttribute("user",user.getId());
            return ResponseResult.success(user);
        }
        return ResponseResult.error("验证码错误");
    }

    @PostMapping("/loginout")
    public ResponseResult loginOut(HttpServletRequest request){
        request.getSession().removeAttribute("user");


        return ResponseResult.success("退出成功");
    }
}
