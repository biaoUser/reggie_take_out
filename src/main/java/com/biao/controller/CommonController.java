package com.biao.controller;

import com.biao.common.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

@RestController
@Slf4j
@RequestMapping("/common")
public class CommonController {

    @Value("${takeout.path}")
    private String basePath;
    //文件上传
    @PostMapping("/upload")
    public ResponseResult upload(MultipartFile file) {
        //前端上传文件的源文件名称
        String originalFilename = file.getOriginalFilename();
        //防止相同文件上传造成覆盖,后端生成独一无二的照片名称
        //①先获取文件的后缀名
        String substring = originalFilename.substring(originalFilename.lastIndexOf("."));
        //②生成文件名
        String fileName= UUID.randomUUID().toString()+substring;
        //③存储文件可能不存在,如果不存在我们要手动创建
        File file1=new File(basePath);
        if (!file1.exists()){
            file1.mkdirs();
        }
        //存储在指定磁盘位置
        try {
            file.transferTo(new File(basePath+fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseResult.success(fileName);
    }


    //文件下载
    @GetMapping("/download")
    public void download(String name, HttpServletResponse response){

        //输入流读取
        response.setContentType("image/jpeg");
        try {
            FileInputStream fileInputStream=new FileInputStream(basePath+name);
            //输出流将文件写到浏览器页面显示
            ServletOutputStream outputStream = response.getOutputStream();
            byte[] bytes=new byte[1024];
            int len=0;
            while ((len=fileInputStream.read(bytes))!=-1){
                outputStream.write(bytes,0,len);
                outputStream.flush();
            }
            outputStream.close();
            fileInputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
