package com.mjrj.lzh.pms.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @BelongsProject: pms
 * @BelongsPackage: com.mjrj.lzh.pms.config
 * @Author: lzh
 * @CreateTime: 2020-04-19 16:07
 * @Description: ${Description}
 */
@EnableAsync(proxyTargetClass = true)
@Configuration
public class AsycTaskExecutorConfig {

    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //配置核心线程数  线程池创建时候初始化的线程数
        executor.setCorePoolSize(20);
        //配置最大线程数，只有当缓冲队列满了，才会申请超过核心线程数上的线程
        executor.setMaxPoolSize(50);
        //配置队列大小
        executor.setQueueCapacity(100);
        //配置线程池中的线程的名称前缀
        executor.setThreadNamePrefix("async_service");
        //线程空闲时间60s 超过销毁
        executor.setKeepAliveSeconds(20);
        // rejection-policy：当pool已经达到max size的时候，如何处理新任务
        // CALLER_RUNS：不在新线程中执行任务，而是有调用者所在的线程来执行
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        //执行初始化
        executor.initialize();
        return executor;
    }
}
