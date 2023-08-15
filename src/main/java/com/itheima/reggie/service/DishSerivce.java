package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.dto.DishDto;
import com.itheima.reggie.entity.Dish;


public interface DishSerivce extends IService<Dish> {
    //保存菜品
    public void saveWithFlavor(DishDto dishDto);
    //根据id查询对应的菜品信息和口味信息
    public DishDto getDishAndFlavorById(Long id);
    //根据id修改菜品信息及口味信息
    public void updateWithFlavor(DishDto dishDto);
}
