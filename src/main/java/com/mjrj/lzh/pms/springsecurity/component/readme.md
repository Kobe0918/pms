springsecurity  执行过程
首先
    加载 websecurityConfig 类（两个重写方法先加载进去）
       限制一个用户登入平台处理
           CustomizeSessionInformationExpiredStrategy
       用户没登入访问路径处理
           CustomizeAuthenticationEntryPoint
       用户登入验证 （登入以后会返回一个jsession 该值security默认会保存跟后续操作比对来确定用户是否登入或登入过期
                    http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                     如果禁用session ，security不会保存jsession因此会一直提示还未登入）
           /login 路径是不会有拦截器去拦截的
           到 websecurityConfig 中找到校验用户的方法  configure(AuthenticationManagerBuilder auth)
           springsecurity通过UserDetailsService 中的loadUserByUsername方法来验证用户
           所以我们重写方法获取数据库用户信息，返回一个UserDetails类（包括账号密码权限等的一个类）
           根据登入成功与否配置了CustomizeAuthenticationFailureHandler（登入失败处理类）
           CustomizeAuthenticationSuccessHandler（登入成功处理类）
        用户访问路径处理
           先经过CustomizeAbstractSecurityInterceptor 拦截器 拦截每条请求
           再调用 CustomizeFilterInvocationSecurityMetadataSource 通过拦截的请求获取访问该路径需要的权限列表
           如果访问没有配置的路径放行
           再调用CustomizeAccessDecisionManager 访问决策管理器
           如果用户权限中含有该路径放行 否则无权
        用户访问无权限路径处理
           CustomizeAccessDeniedHandler
        用户退出处理 logout
           CustomizeLogoutSuccessHandler 只在退出的时候执行


