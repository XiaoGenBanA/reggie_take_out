package com.itheima.reggie.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DishFlavor {
    private Long id;
    private Long dishId;
    private String name;
    private String value;
    private Date createTime;
    private Date updateTime;
    private Long createUser;
    private Long updateUser;
    private Integer isDeleted;


}
