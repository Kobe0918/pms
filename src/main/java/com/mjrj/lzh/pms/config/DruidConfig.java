package com.mjrj.lzh.pms.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * @BelongsProject: pms
 * @BelongsPackage: com.mjrj.lzh.pms.config
 * @Author: lzh
 * @CreateTime: 2020-03-03 16:07
 * @Description: ${Description}
 */
@Configuration
public class DruidConfig {
    //    druid数据源
    @ConfigurationProperties(prefix = "spring.datasource")
    @Bean
    public DataSource druidDataSource() {
        return new DruidDataSource();
    }

    //    访问druid的后台servlet
    @Bean
    public ServletRegistrationBean statViewServlet() {
        ServletRegistrationBean bean = new ServletRegistrationBean(new StatViewServlet());
        bean.addUrlMappings("/druid/*");
        bean.addInitParameter("loginUsername", "admin");
        bean.addInitParameter("loginPassword", "123");
        return bean;
    }

    //    druid资源访问的 过滤器
    @Bean
    public FilterRegistrationBean webStatFilter() {
        FilterRegistrationBean bean = new FilterRegistrationBean(new WebStatFilter());
        bean.addUrlPatterns("/**");
        bean.addInitParameter("exclusions", "/druid/*,*.js,*.css");
        return bean;
    }
}
