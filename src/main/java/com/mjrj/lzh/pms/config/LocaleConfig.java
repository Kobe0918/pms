package com.mjrj.lzh.pms.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

/**
 * @BelongsProject: pms
 * @BelongsPackage: com.mjrj.lzh.pms.config
 * @Author: lzh
 * @CreateTime: 2020-03-03 16:07
 * @Description: ${Description}
 */
@Configuration
public class LocaleConfig {
    //国际化 视图解析器
    @Bean
    public LocaleResolver localeResolver() {
        return new LocaleResolver() {
            @Override
            public Locale resolveLocale(HttpServletRequest httpServletRequest) {
                String result = httpServletRequest.getParameter("language");
//            不带请求头默认操作系统的语言
                Locale locale = Locale.getDefault();
                if (!StringUtils.isEmpty(result)) {
                    String[] language = result.split("_");
                    locale = new Locale(language[0], language[1]);
                }
                return locale;
            }

            @Override
            public void setLocale(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Locale locale) {
            }
        };
    }
}
