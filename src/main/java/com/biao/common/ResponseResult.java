package com.biao.common;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class ResponseResult<T> {

    private Integer code; //编码：1成功，0和其它数字为失败

    private String msg; //错误信息

    private T data; //数据

    private Map map = new HashMap(); //动态数据

    public static <M> ResponseResult<M> success(M object) {
        ResponseResult<M> r = new ResponseResult<M>();
        r.data = object;
        r.code = 1;
        return r;
    }

    public static <M> ResponseResult<M> error(String msg) {
        ResponseResult r = new ResponseResult();
        r.msg = msg;
        r.code = 0;
        return r;
    }

    public ResponseResult<T> add(String key, Object value) {
        this.map.put(key, value);
        return this;
    }

}
