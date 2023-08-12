package com.itheima.reggie.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MybatisPlusConfig {

//    @Bean
//    public MybatisPlusInterceptor mybatisPlusInterceptor(){
//        MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();
//        PaginationInnerInterceptor paginationInnerInterceptor = new PaginationInnerInterceptor();
//        paginationInnerInterceptor.setOverflow(true);
//        paginationInnerInterceptor.setDbType(DbType.MYSQL);
//        mybatisPlusInterceptor.addInnerInterceptor(paginationInnerInterceptor);
//        return mybatisPlusInterceptor;
//    }
    @Bean
    public PaginationInnerInterceptor paginationInnerInterceptor(){
    return new PaginationInnerInterceptor();
    }
}
