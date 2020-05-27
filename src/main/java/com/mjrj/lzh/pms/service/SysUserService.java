package com.mjrj.lzh.pms.service;

import com.mjrj.lzh.pms.dto.UserDTO;
import com.mjrj.lzh.pms.dto.pagedto.UserPageDTO;
import com.mjrj.lzh.pms.dto.response.ResponseResult;
import com.mjrj.lzh.pms.entity.SysUserDO;
import com.mjrj.lzh.pms.springsecurity.response.JsonResult;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @BelongsProject: pms
 * @BelongsPackage: com.mjrj.lzh.pms.service
 * @Author: lzh
 * @CreateTime: 2020-03-27 20:08
 * @Description: ${Description}
 */
public interface SysUserService<T> extends BaseService <T> {
    JsonResult validateUser();

    void loginSuccessProcedure(Integer userId);

    ResponseResult getUserSelect2(String userName);

    ResponseResult register(SysUserDO user);

    ResponseResult forgotPassword(SysUserDO user);

    ResponseResult updatePassword(SysUserDO user);

    ResponseResult selectBossForLeave(Integer userId);

    ResponseResult selectPersonnelLeave();

    ResponseResult selectCaiGoCheckUserId();

    ResponseResult updateUser(MultipartFile file, UserDTO dto, HttpServletRequest request);

    UserPageDTO <SysUserDO> selectWithPage(UserPageDTO pageDTO);

    ResponseResult addUser(MultipartFile file, UserDTO user, HttpServletRequest request);
//select b.id  from
//(select count(*) num ,user_id id  from attendance where take_time like '2020-04-07%'  group by user_id)b where  b.num<2
}
