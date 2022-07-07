package com.biao.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.biao.entity.Category;
import com.biao.mapper.CategoryMapper;
import com.biao.service.CategoryService;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
}
