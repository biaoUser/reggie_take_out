package com.biao.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.biao.entity.AddressBook;
import com.biao.mapper.AddressBookMapper;
import com.biao.service.AddressBookService;
import org.springframework.stereotype.Service;

@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {

}
