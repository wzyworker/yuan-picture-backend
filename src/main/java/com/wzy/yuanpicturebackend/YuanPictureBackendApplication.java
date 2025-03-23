package com.wzy.yuanpicturebackend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@MapperScan("com.wzy.yuanpicturebackend.mapper")
@EnableAspectJAutoProxy(exposeProxy = true)
public class YuanPictureBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(YuanPictureBackendApplication.class, args);
    }

}
