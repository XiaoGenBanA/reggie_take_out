package com.itheima.reggie.common;

import ch.qos.logback.classic.spi.EventArgUtil;
import org.springframework.stereotype.Component;

/**
 * 基于ThreadLocal实现的存储获取当前用户id
 */
public class BaseContext {
    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    public static void setId(Long id){
        threadLocal.set(id);
    }

    public static Long getId(){
        return threadLocal.get();
    }
}
