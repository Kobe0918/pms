package com.mjrj.lzh.pms.config;

import org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @BelongsProject: pms
 * @BelongsPackage: com.mjrj.lzh.pms.config
 * @Author: lzh
 * @CreateTime: 2020-03-03 16:08
 * @Description: ${Description}
 */
@Configuration
public class MybatisConfig {
    //    mybatis自定义的一些配置 开启驼峰，insert时自动往bean注入id值
    @Bean
    public ConfigurationCustomizer mybatisCustomizer() {
        return new ConfigurationCustomizer() {
            @Override
            public void customize(org.apache.ibatis.session.Configuration configuration) {
                configuration.setUseGeneratedKeys(true);
                configuration.setMapUnderscoreToCamelCase(true);
                configuration.setLazyLoadingEnabled(true);
                configuration.setAggressiveLazyLoading(false);
            }
        };
    }
}
