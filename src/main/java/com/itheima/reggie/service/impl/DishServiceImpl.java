package com.itheima.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.dto.DishDto;
import com.itheima.reggie.entity.Dish;
import com.itheima.reggie.entity.DishFlavor;
import com.itheima.reggie.mapper.DishMapper;
import com.itheima.reggie.service.DishFlavorService;
import com.itheima.reggie.service.DishSerivce;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishSerivce {

    @Autowired
    private DishFlavorService dishFlavorService;

    /**
     * 新增菜品
     * @param dishDto
     */
    @Transactional
    public void saveWithFlavor(DishDto dishDto) {
        //保存菜品信息
        this.save(dishDto);

        Long dishId = dishDto.getId();
        //获取菜品口味信息
        List<DishFlavor> flavors = dishDto.getFlavors();
        for (DishFlavor flavor : flavors) {
            flavor.setDishId(dishId);

        }
        dishFlavorService.saveBatch(flavors);
    }

    /**
     * 根据id查询对应的菜品信息和口味信息
     * @param id
     * @return
     */
    public DishDto getDishAndFlavorById(Long id) {
        Dish dish = this.getById(id);
        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish,dishDto);
        LambdaQueryWrapper<DishFlavor> qw = new LambdaQueryWrapper<>();
        qw.eq(DishFlavor::getDishId,id);
        List<DishFlavor> dishFlavors = dishFlavorService.list(qw);
        dishDto.setFlavors(dishFlavors);
        return dishDto;
    }

    /**
     * 根据id修改对应菜品信息和口味信息
     * @param dishDto
     */
    public void updateWithFlavor(DishDto dishDto) {
        this.updateById(dishDto);

        //清理当前口味信息并添加提交口味信息
        LambdaQueryWrapper<DishFlavor> QW = new LambdaQueryWrapper<>();
        QW.eq(DishFlavor::getDishId,dishDto.getId());
        dishFlavorService.remove(QW);

        List<DishFlavor> flavors = dishDto.getFlavors();
        //需要补充当前对应菜品ID 即dishId
        flavors = flavors.stream().map((item) -> {
            item.setDishId(dishDto.getId());
            return item;
        }).collect(Collectors.toList());
        dishFlavorService.saveBatch(flavors);
    }

}
