package com.biao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Slf4j
@ServletComponentScan//开启WebFilter支持
@SpringBootApplication
@EnableTransactionManagement//开启事务支持
public class TakeOutApplication {
    public static void main(String[] args) {

        SpringApplication.run(TakeOutApplication.class,args);
    }
}
