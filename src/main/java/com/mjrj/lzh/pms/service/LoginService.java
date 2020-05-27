package com.mjrj.lzh.pms.service;

import com.mjrj.lzh.pms.entity.UserDO;
import com.mjrj.lzh.pms.dto.StatusEnum;

/**
 * @BelongsProject: pms
 * @BelongsPackage: com.mjrj.lzh.pms.service
 * @Author: lzh
 * @CreateTime: 2020-03-17 19:49
 * @Description: ${Description}
 */
public interface LoginService {
    /**
     *
     * @param user
     */
    StatusEnum register(UserDO user);

    StatusEnum login(UserDO user);
}
