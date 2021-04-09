package com.mjrj.lzh.pms;

import com.alibaba.druid.support.http.StatViewServlet;
import okhttp3.OkHttpClient;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.management.MXBean;
import javax.sql.DataSource;


@SpringBootApplication
//启动activemq 消息队列
@EnableJms
//开启缓存
@EnableCaching
@EnableTransactionManagement
@MapperScan(basePackages = "com.mjrj.lzh.pms.dao")
public class PmsApplication {

    public static void main(String[] args) {
        SpringApplication.run(PmsApplication.class, args);
    }

    @Bean
    public OkHttpClient okHttpClient(){
        return new OkHttpClient.Builder().build();
    }

}
