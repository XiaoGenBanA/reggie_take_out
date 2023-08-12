package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.Employee;
import com.itheima.reggie.filter.LoginCheckFilter;
import com.itheima.reggie.service.EmployeeSerivce;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

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

    /**
     * 员工列表分页查询
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("page")
    public R<Page> listEmployee(int page,int pageSize,String name){
        log.info("传的当前页是:{} 每页大小为:{} name:{}",page,pageSize,name);
        //分页构造器
        Page pageInfo = new Page(page,pageSize);
        //条件构造器
        LambdaQueryWrapper<Employee> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (null != name) {
            lambdaQueryWrapper.like(Employee::getName, name);
        }
        lambdaQueryWrapper.orderByDesc(Employee::getUpdateTime);
        employeeSerivce.page(pageInfo,lambdaQueryWrapper);
        System.out.println(pageInfo.toString());
        return R.success(pageInfo);
    }

    /**
     * 通过ID修改员工信息
     * @param employee
     * @return
     */
    @PutMapping
    public R updateEmployeeById(HttpServletRequest request,@RequestBody Employee employee){
        Long empId = (Long) request.getSession().getAttribute("employee");
        employee.setUpdateTime(LocalDateTime.now());
        employee.setUpdateUser(empId);
        employeeSerivce.updateById(employee);
        return R.success("修改成功");
    }

    /**
     * 通过ID回显员工信息
     * @param id
     * @return
     */
    @GetMapping("{id}")
    public R<Employee> getEmployeeById(@PathVariable Long id){
        Employee employee = employeeSerivce.getById(id);
        if (null != employee){
            return R.success(employee);
        }
        return R.error("不存在此员工信息");
    }

}
