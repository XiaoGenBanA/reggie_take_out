package com.itheima.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.common.CustomException;
import com.itheima.reggie.entity.Category;
import com.itheima.reggie.entity.Dish;
import com.itheima.reggie.entity.Setmeal;
import com.itheima.reggie.mapper.CategoryMapper;
import com.itheima.reggie.service.CategoryService;
import com.itheima.reggie.service.DishSerivce;
import com.itheima.reggie.service.SetmealSerivce;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Autowired
    private DishSerivce dishSerivce;
    @Autowired
    private SetmealSerivce setmealSerivce;

    @Override
    public void remove(Long id) {
        //通过菜品分类和套餐分类查询是否存在有关联的菜品和分类，如有则不允许被删除
        LambdaQueryWrapper<Dish> qw1 = new LambdaQueryWrapper<>();
        qw1.eq(Dish::getCategoryId,id);
        int count1 = dishSerivce.count(qw1);
        if (count1 > 0){
            throw  new CustomException("该分类下已关联菜品，不允许被删除");
        }

        LambdaQueryWrapper<Setmeal> qw2 = new LambdaQueryWrapper<>();
        qw2.eq(Setmeal::getCategoryId,id);
        int count2 = setmealSerivce.count(qw2);
        if (count2 > 0){
            throw new CustomException("该分类下已关联套餐，不允许被删除");
        }

        super.removeById(id);
    }
}
