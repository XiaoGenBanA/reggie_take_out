package com.itheima.reggie;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.entity.DishFlavor;
import com.itheima.reggie.mapper.DishFlavorMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.Date;

@SpringBootTest
public class ReggieApplicationTest {

    @Resource
    private DishFlavorMapper dishFlavorMapper;


//    @Test
//    void ListSelect(){
//        List<DishFlavor> dishFlavors = dishFlavorMapper.selectList();
//        System.out.println(dishFlavors);
//    }

    @Test
    void testInsert(){
        DishFlavor dishFlavor = new DishFlavor();
        dishFlavor.setName("测试数据1");
        dishFlavor.setValue("测试数据222");
        dishFlavor.setDishId(1111112222L);
        Date date = new Date();
        dishFlavor.setCreateTime(date);
        dishFlavor.setUpdateTime(date);
        dishFlavorMapper.insert(dishFlavor);
    }

    @Test
    void testPage(){
        IPage dishFlavorPage = new Page(1,3);
        dishFlavorMapper.selectPage(dishFlavorPage,null);
        System.out.println("当前页：" + dishFlavorPage.getCurrent());
        System.out.println("当前条数：" + dishFlavorPage.getSize());
        System.out.println("总条数：" + dishFlavorPage.getTotal());
        System.out.println("总页数：" + dishFlavorPage.getPages());
        System.out.println("数据：" + dishFlavorPage.getRecords());

    }

}
