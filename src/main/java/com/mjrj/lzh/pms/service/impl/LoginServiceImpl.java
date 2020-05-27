package com.mjrj.lzh.pms.service.impl;

import com.mjrj.lzh.pms.redis.RedisComponent;
import com.mjrj.lzh.pms.dao.UserDOMapper;
import com.mjrj.lzh.pms.entity.UserDO;
import com.mjrj.lzh.pms.service.LoginService;
import com.mjrj.lzh.pms.util.CommonUtil;
import com.mjrj.lzh.pms.util.MD5Util;
import com.mjrj.lzh.pms.dto.StatusEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;

/**
 * @BelongsProject: pms
 * @BelongsPackage: com.mjrj.lzh.pms.service.impl
 * @Author: lzh
 * @CreateTime: 2020-03-17 19:49
 * @Description: ${Description}
 */
@Service
@Slf4j
public class LoginServiceImpl implements LoginService {

    @Autowired
    private RedisComponent redisComponent;
    @Autowired
    private UserDOMapper userDOMapper;


    @Override
    public StatusEnum register(UserDO user) {
        //1.校验短信验证码  校验成功删除
        //2.是否存在该电话号码用户
        //3.密码进行加密
        //
        String code = redisComponent.getString(CommonUtil.PHONE_PRE +user.getTelephone());
        log.info(code+": yzm");
        if(user.getVerify().equals(code)){
            redisComponent.deleteString(CommonUtil.PHONE_PRE+user.getTelephone());
            String exitPhone = userDOMapper.getIdByPhone(user.getTelephone());
            if(StringUtils.isEmpty(exitPhone)){
                Date createTime = new Date();
                user.setCreateTime(createTime);
                user.setPassword(MD5Util.md5(user.getPassword(),createTime));
                userDOMapper.insertSelective(user);
                return StatusEnum.REGISTER_SUCCESS;
            }else{
               return  StatusEnum.EXIT_USER;
            }
        }else{
          return  StatusEnum.VERIFY_FAIL;
        }
    }


    @Override
    public StatusEnum login(UserDO user) {
        UserDO dbUser = userDOMapper.getUserByPhone(user.getTelephone());
        if(dbUser == null){
            return StatusEnum.USER_NOTEXIT;
        }else{
//            校验密码
            if(MD5Util.verfied(dbUser.getPassword(),user.getPassword(),dbUser.getCreateTime())){
                return StatusEnum.LOGIN_SUCCESS;
            }else{
                return  StatusEnum.WRONG_PWD;
            }
        }

    }
}
