package com.itheima.reggie.mapper;

import ch.qos.logback.classic.spi.EventArgUtil;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itheima.reggie.entity.Employee;
import com.itheima.reggie.entity.User;
import com.itheima.reggie.filter.LoginCheckFilter;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
}
