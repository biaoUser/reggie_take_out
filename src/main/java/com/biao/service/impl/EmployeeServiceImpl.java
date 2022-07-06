package com.biao.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.biao.entity.Employee;
import com.biao.mapper.EmployeeMapper;
import com.biao.service.EmployeeService;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {

}
