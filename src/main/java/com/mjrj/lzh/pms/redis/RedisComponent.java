package com.mjrj.lzh.pms.redis;

import com.mjrj.lzh.pms.dao.SysLogsDOMapper;
import com.mjrj.lzh.pms.dto.CurrentUserDTO;
import com.mjrj.lzh.pms.entity.SysLogsDO;
import com.mjrj.lzh.pms.util.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @BelongsProject: pms
 * @BelongsPackage: com.mjrj.lzh.pms.component
 * @Author: lzh
 * @CreateTime: 2020-03-15 21:19
 * @Description: ${Description}
 */
@Component
@Slf4j
public class RedisComponent {

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private SysLogsDOMapper logsDOMapper;

    @Value("${jwt.token.expiration}")
    private Integer expiration;

    //    判断是否存在为key的map
    public boolean exitKeyInMap(String k) {
        Map map = redisTemplate.opsForHash().entries(k);
        if (map.isEmpty() || map == null) {
            return false;
        } else {
            return true;
        }
    }

    //    存map
    public void addMap(String k, Map map) {
        redisTemplate.opsForHash().putAll(k, map);
//        new HashMap<String,Object>(){{put("phone",phoneNumber);
//            put("code",code);
//            put("event",event);
//        }}
    }

    //    设置key存活时间
    public void setLifeTime(String k, Integer t) {
        redisTemplate.expire(k, t, TimeUnit.SECONDS);
    }


    public void setStringLifeTime(String k, Integer t) {
        stringRedisTemplate.expire(k, t, TimeUnit.SECONDS);
    }


    public void addString(String k, String v) {
        stringRedisTemplate.opsForValue().set(k, v);
    }

    public String getString(String k) {
        return stringRedisTemplate.opsForValue().get(k);
    }


    public void deleteString(String k) {
        stringRedisTemplate.delete(k);
    }

    public void addStringAndExpire(String k, String v, Integer t) {
        stringRedisTemplate.opsForValue().set(k, v);
        stringRedisTemplate.expire(k, t, TimeUnit.SECONDS);
    }


    public boolean exitKeyString(String key) {
        return stringRedisTemplate.hasKey(key);
    }


    public List getStaticPathByRedisT(String k) {
        return redisTemplate.opsForList().range(k, 0, -1);
    }

    public void pushListByRedisT(List <?> list, String k) {
        redisTemplate.opsForList().rightPushAll(k, list);
    }



    public String getTokenKey(String k) {
        return "token:" + k;
    }

    public String getPhoneKey(String k){return "phone:"+k;}



    public void cacheLoginUser(String k, CurrentUserDTO user) {
        redisTemplate.boundValueOps(getTokenKey(k)).set(user, expiration, TimeUnit.MINUTES);
    }

    public CurrentUserDTO getLoginUser(String k) {
        return (CurrentUserDTO) redisTemplate.boundValueOps(getTokenKey(k)).get();
    }

    public void deleteUser(String token) {
        token = getTokenKey(token);
        CurrentUserDTO loginUser = (CurrentUserDTO) redisTemplate.opsForValue().get(token);
        if (loginUser != null) {
            System.out.println("redis 有数据");
            redisTemplate.delete(token);
            SysLogsDO logsDO = new SysLogsDO();
            logsDO.setUserId(loginUser.getId());
            logsDO.setCreatetime(new Date());
            logsDO.setFlag((byte) 1);
            logsDO.setModule("退出");
            logsDOMapper.insertSelective(logsDO);
        }
    }


}
