package com.mjrj.lzh.pms.dao;

import com.mjrj.lzh.pms.dto.UserDTO;
import com.mjrj.lzh.pms.entity.SysUserDO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SysUserDOMapper extends BaseMapper<SysUserDO>{

    SysUserDO selectUserByPhone(String phone);

    void updateLoginTimeAndStatus(Integer userId);

    List<SysUserDO>  selectUserSelect2(@Param("userName") String userName);

    @Select(value = "select count(*) from sys_user where telephone = #{phone}")
    int getCountByPhone(@Param("phone") String phone);

    @Select(value="select * from sys_user where telephone = #{phone}")
    SysUserDO getUserByPhone(@Param("phone") String phone);


    SysUserDO selectBossForLeave(@Param("deptId")Integer deptId);

    List<SysUserDO> selectPersonnelLeave();

    SysUserDO selectCaiGoCheckUserId();

    Integer selectLeaveDeptId();

    Integer updateByUpdateUserDtoKeySelective(UserDTO dto);

    void updateEnabledByPrimaryKey(@Param("id")Integer id, @Param("enabled")Boolean enabled);

    @Select(value = "select init_position_id from pms_config limit 1")
    Integer selectInitPositionId();


    void insertByUserDTO(UserDTO user);
}