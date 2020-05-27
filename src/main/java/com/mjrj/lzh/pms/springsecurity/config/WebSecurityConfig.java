package com.mjrj.lzh.pms.springsecurity.config;

import com.mjrj.lzh.pms.springsecurity.component.*;
import com.mjrj.lzh.pms.springsecurity.jwt.JwtAuthenticationTokenFilter;
import com.mjrj.lzh.pms.springsecurity.service.UserDetailsServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.provisioning.UserDetailsManagerConfigurer;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @BelongsProject: security
 * @BelongsPackage: com.example.security.config
 * @Author: lzh
 * @CreateTime: 2020-03-18 13:57
 * @Description: ${Description}
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Slf4j
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    //    未登入，或登入过期
    @Autowired
    private CustomizeAuthenticationEntryPoint authenticationEntryPoint;
    //    登入失败
    @Autowired
    private CustomizeAuthenticationFailureHandler authenticationFailureHandler;
    //    登入成功
    @Autowired
    private CustomizeAuthenticationSuccessHandler authenticationSuccessHandler;
    //    退出成功
    @Autowired
    private CustomizeLogoutSuccessHandler logoutSuccessHandler;
    //    无权访问
    @Autowired
    private CustomizeAccessDeniedHandler accessDeniedHandler;
    //    jwt
    @Autowired
    private JwtAuthenticationTokenFilter authenticationTokenFilter;

////    单用户登入
//    @Autowired
//    private CustomizeSessionInformationExpiredStrategy sessionInformationExpiredStrategy;
////    安全数据源
//    @Autowired
//    private CustomizeFilterInvocationSecurityMetadataSource  securityMetadataSource;
////    决策中心
//    @Autowired
//    private CustomizeAccessDecisionManager accessDecisionManager;
////    权限拦截器  路径
//    @Autowired
//    private CustomizeAbstractSecurityInterceptor securityInterceptor;


    @Bean
    @Override
    public UserDetailsService userDetailsService() {
        return new UserDetailsServiceImpl();
    }


    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    /**
     * 验证用户信息
     *
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService());
    }


    /**
     * http相关的配置，包括登入登出、异常处理、会话管理等
     *
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {

//        http.authorizeRequests().anyRequest().permitAll().and().logout().permitAll();
        log.info("鉴权配置 ");
        //security 关闭跨域
        http.csrf().disable();

//        security的原理就是拦截器，拦截每条请求
//        http.addFilterBefore(securityInterceptor, FilterSecurityInterceptor.class);

        //jwt  禁用session 改用token  springsecurity 登入成功后会产生jsession到服务端每次请求校验该数据
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);


//        数据库动态匹配权限  缺点：每次路径都需要访问数据库，太耗时  优点动态匹配
//        .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
//            @Override
//            public <O extends FilterSecurityInterceptor> O postProcess(O o) {
////              拦截每条请求后 在securityMetadataSource 中获取每条请求需要的权限 在accessDecisionManager 决策器中去匹配需要的权限和用户权限
//                o.setAccessDecisionManager(accessDecisionManager);
//                o.setSecurityMetadataSource(securityMetadataSource);
//                return o;
//            }
//        })
        String[] allowPath = new String[]{"/login", "/dept/getDeptAll", "/test", "/*", "/asserts/**",
                "/favicon.ico", "/webjars/**", "/druid/**", "/uploadFiles/**", "/user/sendMessage",
                "/user/register", "/user/forgot","/user/edit","/admin/userMessage"};
// "/dashboard",
        http.authorizeRequests()
                .antMatchers(allowPath)
                .permitAll().anyRequest().authenticated();
        http.formLogin().usernameParameter("telephone")
                .successHandler(authenticationSuccessHandler)
                .failureHandler(authenticationFailureHandler)
                .and().logout().logoutSuccessHandler(logoutSuccessHandler)
                .deleteCookies("JSESSIONID")  //使用session的情况下退出需要删除JSESSIONID,不使用token的情况下,security每次校验JSESSIONID
                .and().exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler)
                .authenticationEntryPoint(authenticationEntryPoint);
//                当用户多台电脑先后登入，后面一台会顶掉前面一台的用户  需要session支持
//                .and().sessionManagement().maximumSessions(1).expiredSessionStrategy(sessionInformationExpiredStrategy);


        //        解决spring boot项目中出现不能加载iframe
        http.headers().frameOptions().disable();
        http.headers().cacheControl();

        http.addFilterBefore(authenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);


    }


}
