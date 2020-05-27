package com.mjrj.lzh.pms.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @BelongsProject: pms
 * @BelongsPackage: com.mjrj.lzh.pms.config
 * @Author: lzh
 * @CreateTime: 2020-03-03 16:08
 * @Description: ${Description}
 */
@Configuration
public class WebMvcConfig {
    //自定义 webmvc 的视图控制器 ，拦截器

    @Value("${uploadFile.resourceHandler}")
    private String resourceHandler;//请求 url 中的资源映射，不推荐写死在代码中，最好提供可配置，如 /uploadFiles/**

    @Value("${uploadFile.location}")
    private String location;//上传文件保存的本地目录，使用@Value获取全局配置文件中配置的属性值，如 E:/wmx/uploadFiles/


    @Bean
    public WebMvcConfigurer webMvcConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addViewControllers(ViewControllerRegistry registry) {
                registry.addViewController("/dashboard").setViewName("dashboard");
                registry.addViewController("/login").setViewName("login");
//                registry.addViewController("/don").setViewName("testdonhua");
                registry.addViewController("/index").setViewName("index");
                registry.addViewController("/leave").setViewName("leave/index");
                registry.addViewController("/dept").setViewName("dept/index");
                registry.addViewController("/position").setViewName("position/index");
                registry.addViewController("/menu").setViewName("menu/index");
                registry.addViewController("/caigo-user").setViewName("caigo/user/index");
                registry.addViewController("/caigo-admin").setViewName("caigo/admin/index");
                registry.addViewController("/user/edit").setViewName("users/user/edit");
                registry.addViewController("/admin/userMessage").setViewName("users/admin/index");
                registry.addViewController("/logs").setViewName("logs/index");
                registry.addViewController("/roles").setViewName("roles/index");
                registry.addViewController("/config").setViewName("config/pmsconfig");

            }

//            配置本地下载路径
            @Override
            public void addResourceHandlers(ResourceHandlerRegistry registry) {
                registry.addResourceHandler(resourceHandler).addResourceLocations("file:///" + location);
            }

            @Override
            public void addInterceptors(InterceptorRegistry registry) {
//                registry.addInterceptor(new LoginInterceptor()).addPathPatterns("/**")
//                       .excludePathPatterns("/","/login","/user/login","/asserts/**","/druid/*");
            }

        };
    }
}
