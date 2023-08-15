package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.R;
import com.itheima.reggie.dto.DishDto;
import com.itheima.reggie.entity.Category;
import com.itheima.reggie.entity.Dish;
import com.itheima.reggie.service.CategoryService;
import com.itheima.reggie.service.DishSerivce;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("dish")
@Slf4j
public class DishController {

    @Autowired
    private DishSerivce dishSerivce;

    @Autowired
    private CategoryService categoryService;

    /**
     * 新增菜品
     * @param dishDto
     * @return
     */
    @PostMapping()
    public R<String> save(@RequestBody DishDto dishDto){
        dishSerivce.saveWithFlavor(dishDto);
        return R.success("新增菜品成功");
    }

    /**
     * 菜品分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("page")
    public R<Page> page(int page,int pageSize,String name){
        //分页构造器
        Page<Dish> pageInfo = new Page<>(page,pageSize);
        Page<DishDto> dishPage = new Page<>();
        //条件构造器
        LambdaQueryWrapper<Dish> qw = new LambdaQueryWrapper<>();
        qw.like(name != null,Dish::getName,name);
        qw.orderByDesc(Dish::getUpdateTime);
        dishSerivce.page(pageInfo,qw);

        //对象拷贝
        BeanUtils.copyProperties(pageInfo,dishPage,"records");

        //将pageInfo中的records拷贝到dishPage的records里面去并且加上菜品分类名称
        List<Dish> records = pageInfo.getRecords();
//        List<DishDto> recordsNew = new ArrayList<>();
//        for (Dish record : records) {
//            DishDto dishDto = new DishDto();
//            BeanUtils.copyProperties(record,dishDto);
//            Category result = categoryService.getById(record.getCategoryId());
//            String name1 = result.getName();
//            dishDto.setCategoryName(name1);
//            recordsNew.add(dishDto);
//        }
//        dishPage.setRecords(recordsNew);
        List<DishDto> dishDtos = records.stream().map((item) -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);
            Category category = categoryService.getById(item.getCategoryId());
            if (category != null) {
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }
            return dishDto;
        }).collect(Collectors.toList());
        dishPage.setRecords(dishDtos);
        return R.success(dishPage);
    }

    /**
     * 回显当前菜品及口味信息
     * @param id
     * @return
     */
    @GetMapping("{id}")
    public R<DishDto> dish(@PathVariable Long id){
        DishDto dishDto = dishSerivce.getDishAndFlavorById(id);
        return R.success(dishDto);

    }

    /**
     * 修改菜品
     * @param dishDto
     * @return
     */
    @PutMapping()
    public R<String> update(@RequestBody DishDto dishDto){
        dishSerivce.updateWithFlavor(dishDto);
        return R.success("修改菜品成功");
    }

}
