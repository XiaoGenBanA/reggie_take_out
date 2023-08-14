package com.itheima.reggie;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

public class Test {
    public static void main(String[] args) {
        Date date = new Date();
        System.out.println(date);
        System.out.println(LocalDate.now().toString());
    }
}
