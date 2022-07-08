package com.biao.common;
//自定义数据异常
public class CustoException extends RuntimeException{
    public CustoException(String message) {
        super(message);
    }
}
