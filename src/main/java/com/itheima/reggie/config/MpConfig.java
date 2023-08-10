package com.itheima.reggie.config;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MpConfig {
    @Bean
    public MybatisPlusInterceptor PageInterceptor(){
        MybatisPlusInterceptor MpInterceptor = new MybatisPlusInterceptor();
        MpInterceptor.addInnerInterceptor(new PaginationInnerInterceptor());
        MpInterceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
        return MpInterceptor;
    }

}
