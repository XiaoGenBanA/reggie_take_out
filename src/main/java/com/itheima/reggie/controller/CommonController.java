package com.itheima.reggie.controller;

import com.fasterxml.jackson.databind.ser.Serializers;
import com.itheima.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequestMapping("common")
@Slf4j
public class CommonController {

    @Value("${reggie.path}")
    private String BasePath ;



    /**
     * 文件上传
     * @param file
     * @return
     */
    @PostMapping("upload")
    public R<String> upload(MultipartFile file){
        String originalFilename = file.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf('.'));
        String fileName = UUID.randomUUID().toString();
        String fileNameNew = fileName + suffix;

        File dir = new File(BasePath + LocalDate.now().toString() + "/");
        if (!dir.exists()){
            dir.mkdirs();
        }
        try {
            file.transferTo(new File(BasePath + LocalDate.now().toString() + "/" + fileNameNew));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return R.success(fileNameNew);
    }

    /**
     * 文件下载
     * @param name
     * @param response
     */
    @GetMapping("download")
    public void download(String name, HttpServletResponse response){
        //输入流读取文件
        try {
            FileInputStream inputStream = new FileInputStream(new File(BasePath + LocalDate.now().toString() + "/" + name));
            ServletOutputStream outputStream = response.getOutputStream();
            response.setContentType("image/jepg");
            byte[] bytes = new byte[1024];
            int len = 0;
            //输出流显将文件写回浏览器，回显图片内容
            while ((len = inputStream.read(bytes)) != -1){
                outputStream.write(bytes,0,len);
                outputStream.flush();
            }
            //关闭资源
            outputStream.close();
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
