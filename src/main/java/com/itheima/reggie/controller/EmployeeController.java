package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.Employee;
import com.itheima.reggie.service.EmployeeSerivce;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@RestController
@RequestMapping("employee")
@Slf4j
public class EmployeeController {
    @Autowired
    private EmployeeSerivce employeeSerivce;

    /**
     * 员工登录
     * @param servletRequest
     * @param employee
     * @return
     */
    @PostMapping("login")
    public R<Employee> login(HttpServletRequest servletRequest, @RequestBody Employee employee){
        //1.密码进行MD5加密
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        //2.根据页面提交的用户名username查询数据库
        LambdaQueryWrapper<Employee> qw = new LambdaQueryWrapper<>();
        qw.eq(Employee::getUsername,employee.getUsername());
        Employee em = employeeSerivce.getOne(qw);

        if(em ==null){
            return R.error("登陆失败，该账号未注册");
        }

        if(!em.getPassword().equals(password)){
            return R.error("密码错误，请重试");
        }

        if(em.getStatus() == 0){
            return R.error("该账号已被禁用");
        }

        servletRequest.getSession().setAttribute("employee",em.getId());
        return R.success(em);

    }

    /**
     * 退出
     * @param servletRequest
     * @return
     */
    @PostMapping("logout")
    public R logout(HttpServletRequest servletRequest){
        servletRequest.getSession().removeAttribute("employee");
        return R.success("退出成功");
    }

    /**
     * 新增员工
     * @param servletRequest
     * @param employee
     * @return
     */
    @PostMapping
    public R saveEmployee(HttpServletRequest servletRequest,@RequestBody Employee employee){
        log.info("新增员工，信息: {}",employee);
        employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());
        Long id = (Long) servletRequest.getSession().getAttribute("employee");
        String password = DigestUtils.md5DigestAsHex("123456".getBytes());
        employee.setPassword(password);
        employee.setCreateUser(id);
        employee.setUpdateUser(id);
        employeeSerivce.save(employee);
        return R.success("添加成功");
    }


}
