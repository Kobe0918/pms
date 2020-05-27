package com.mjrj.lzh.pms.redis;

import com.alibaba.fastjson.support.spring.GenericFastJsonRedisSerializer;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

import java.net.UnknownHostException;
import java.time.Duration;

/**
 * @BelongsProject: pms
 * @BelongsPackage: com.mjrj.lzh.pms.config
 * @Author: lzh
 * @CreateTime: 2020-03-06 15:35
 * @Description: ${Description}
 */
@Configuration
@Slf4j
public class RedisConfig {


    private Duration liveTime = Duration.ofHours(1);


    @Configuration
    public static class JedisConf {
        @Value("${spring.redis.host}")
        private String host;
        @Value("${spring.redis.port}")
        private Integer port;
        @Value("${spring.redis.password}")
        private String password;
        @Value("${spring.redis.jedis.pool.max-active}")
        private Integer maxActive;
        @Value("${spring.redis.database}")
        private Integer database;
        @Value("${spring.redis.jedis.pool.max-idle}")
        private Integer maxIdle;
        @Value("${spring.redis.jedis.pool.max-wait:}")
        private Long maxWait;
        @Value("${spring.redis.jedis.pool.min-idle}")
        private Integer minIdle;

        @Bean
        public JedisPoolConfig jedisPool() {
            JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
            jedisPoolConfig.setMaxIdle(maxIdle);
            jedisPoolConfig.setMaxWaitMillis(maxWait);
            jedisPoolConfig.setMaxTotal(maxActive);
            jedisPoolConfig.setMinIdle(minIdle);
            return jedisPoolConfig;
        }

        @Bean
        public RedisStandaloneConfiguration jedisConfig() {
            RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
            config.setHostName(host);
            config.setPort(port);
            config.setDatabase(database);
            config.setPassword(RedisPassword.of(password));
            return config;
        }
    }

    @Bean
    public RedisConnectionFactory redisConnectionFactory(JedisPoolConfig jedisPool,
                                                         RedisStandaloneConfiguration jedisConfig) {
        JedisConnectionFactory connectionFactory = new JedisConnectionFactory(jedisConfig);
        connectionFactory.setPoolConfig(jedisPool);
        return connectionFactory;
    }


    /**
     * 配置redisTemplate（手动使用redis进行缓存时，区别注解）
     * @param redisConnectionFactory
     * @return
     * @throws UnknownHostException
     */
//    @Bean(name = "redisTemplate")
//    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) throws UnknownHostException {
//        RedisTemplate <Object, Object> template = new RedisTemplate();
//        template.setConnectionFactory(redisConnectionFactory);
//        GenericFastJsonRedisSerializer redisSerializer = new GenericFastJsonRedisSerializer();
//        template.setDefaultSerializer(redisSerializer);
//
//        return template;
//    }

    @Bean("redisTemplate")
    public RedisTemplate redisTemplate(@Lazy RedisConnectionFactory connectionFactory) {
        RedisTemplate redis = new RedisTemplate();
        GenericToStringSerializer<String> keySerializer = new GenericToStringSerializer<String>(String.class);
        redis.setKeySerializer(keySerializer);
        redis.setHashKeySerializer(keySerializer);
//        GenericFastJsonRedisSerializer valueSerializer = new GenericFastJsonRedisSerializer();
        GenericJackson2JsonRedisSerializer valueSerializer = new GenericJackson2JsonRedisSerializer();
        redis.setValueSerializer(valueSerializer);
        redis.setHashValueSerializer(valueSerializer);
        redis.setConnectionFactory(connectionFactory);

        return redis;
    }


    /**
     * 注解使用redis 缓存 重写cacheManage 进行json序列化
     * @param redisConnectionFactory
     * @return
     */
    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
//GenericFastJsonRedisSerializer相对Jackson2JsonRedisSerializer更高效，空间占用比jdk大一点，但是速度快5倍
//        而且面对复杂数据反序列化时  不会出问题
        GenericFastJsonRedisSerializer redisSerializer = new GenericFastJsonRedisSerializer();

//        Jackson2JsonRedisSerializer 效率较慢，而且反序列化时面对复杂的数据类型还要解决转换异常
//        Jackson2JsonRedisSerializer<Object> redisSerializer = new Jackson2JsonRedisSerializer (Object.class);
        //解决查询缓存转换异常的问题
//        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
//        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
//        redisSerializer.setObjectMapper(objectMapper);


        // 配置序列化（解决乱码的问题）
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration
                .defaultCacheConfig()
                .entryTtl(liveTime)   //过期时间[毫秒]
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(stringRedisSerializer))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(redisSerializer))
                .disableCachingNullValues();//不缓存空值


        RedisCacheManager redisCacheManager = RedisCacheManager.builder(redisConnectionFactory).cacheDefaults(redisCacheConfiguration).build();
        return redisCacheManager;
    }
}
