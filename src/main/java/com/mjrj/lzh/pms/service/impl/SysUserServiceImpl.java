package com.mjrj.lzh.pms.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mjrj.lzh.pms.dao.SysDeptPositionRelationDOMapper;
import com.mjrj.lzh.pms.dao.SysUserDOMapper;
import com.mjrj.lzh.pms.dto.CurrentUserDTO;
import com.mjrj.lzh.pms.dto.UserDTO;
import com.mjrj.lzh.pms.dto.pagedto.UserPageDTO;
import com.mjrj.lzh.pms.dto.response.ResponseResult;
import com.mjrj.lzh.pms.dto.response.ResponseTool;
import com.mjrj.lzh.pms.dto.response.Select2DTO;
import com.mjrj.lzh.pms.entity.SysDeptPositionRelationDO;
import com.mjrj.lzh.pms.entity.SysLogsDO;
import com.mjrj.lzh.pms.entity.SysUserDO;
import com.mjrj.lzh.pms.redis.RedisComponent;
import com.mjrj.lzh.pms.service.SysLogsService;
import com.mjrj.lzh.pms.service.SysUserService;
import com.mjrj.lzh.pms.springsecurity.jwt.JwtTokenUtils;
import com.mjrj.lzh.pms.springsecurity.response.JsonResult;
import com.mjrj.lzh.pms.springsecurity.response.ResultTool;
import com.mjrj.lzh.pms.util.CommonUtil;
import com.mjrj.lzh.pms.util.UploadFileUtil;
import com.mjrj.lzh.pms.util.UserUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @BelongsProject: pms
 * @BelongsPackage: com.mjrj.lzh.pms.service.impl
 * @Author: lzh
 * @CreateTime: 2020-03-27 20:07
 * @Description: ${Description}
 */
@Service
@Slf4j
public class SysUserServiceImpl extends BaseServiceImpl <SysUserDOMapper, SysUserDO> implements SysUserService <SysUserDO> {
    @Autowired
    private RedisComponent redisComponent;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private SysLogsService sysLogsService;
    @Autowired
    private JwtTokenUtils tokenUtils;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private UploadFileUtil uploadFileUtil;
    @Autowired
    private SysDeptPositionRelationDOMapper deptPositionRelationDOMapper;


    @Override
    public JsonResult validateUser() {
        JsonResult fail = ResultTool.fail();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            if (authentication instanceof AnonymousAuthenticationToken) {
                return fail;
            }
            if (authentication instanceof UsernamePasswordAuthenticationToken) {
                System.out.println(((CurrentUserDTO) authentication.getPrincipal()).getUsername() + ":username");
                fail = ResultTool.success();
                return fail;
            }
        }

        return fail;
    }

    @Override
    public void loginSuccessProcedure(Integer userId) {
        mapper.updateLoginTimeAndStatus(userId);
    }


    @Override
    public ResponseResult getUserSelect2(String userName) {
        List <SysUserDO> userDOS = mapper.selectUserSelect2(userName);
        List <Select2DTO> select2DTOS = new ArrayList <>();
        userDOS.forEach(u -> {
            select2DTOS.add(new Select2DTO(u.getId(), u.getUserName()));
        });
        return ResponseTool.success(select2DTOS);
    }


    @Override
    @Transactional(rollbackFor = {Exception.class}, propagation = Propagation.REQUIRED)
    public ResponseResult register(SysUserDO user) {

        String code = redisComponent.getString(CommonUtil.PHONE_PRE + user.getTelephone());
        log.info(code + ": yzm");
        if (user.getVerify().equals(code)) {
            redisComponent.deleteString(CommonUtil.PHONE_PRE + user.getTelephone());
            int result = mapper.getCountByPhone(user.getTelephone());
            if (result == 0) {
                user.setCreateTime(new Date());
                user.setPassword(passwordEncoder.encode(user.getPassword()));
                Integer positionId= mapper.selectInitPositionId();
//                改与 4-19  设置读取配置表中的默认注册用户的职位权限
                user.setPositionId(positionId);
                user.setLastLoginTime(new Date());
                mapper.insertSelective(user);
                CurrentUserDTO userDTO = (CurrentUserDTO) userDetailsService.loadUserByUsername(user.getTelephone());
                userDTO.setToken(UUID.randomUUID().toString());
                final String token = tokenUtils.generateToken(userDTO);
                redisComponent.cacheLoginUser(userDTO.getToken(), userDTO);
                sysLogsService.insert(new SysLogsDO(user.getId(), "注册", (byte) 1, new Date(), null,null));

                return ResponseTool.success(token);
            } else {
                return new ResponseResult(false, 300, "已存在该用户");
            }
        } else {
            return new ResponseResult(false, 300, "验证码不正确");
        }
    }

    @Override
    @Transactional(rollbackFor = {Exception.class}, propagation = Propagation.REQUIRED)
    public ResponseResult forgotPassword(SysUserDO user) {
        if (!user.getPassword().equals(user.getCheckPassword())) {
            return new ResponseResult(false, 300, "两次密码不一致。");
        }
        String code = redisComponent.getString(CommonUtil.PHONE_PRE + user.getTelephone());
        if (user.getVerify().equals(code)) {
            redisComponent.deleteString(CommonUtil.PHONE_PRE + user.getTelephone());
            SysUserDO userByPhone = mapper.getUserByPhone(user.getTelephone());
            if (userByPhone != null && userByPhone.getId() > 0) {
                user.setUpdateTime(new Date());
                user.setPassword(passwordEncoder.encode(user.getPassword()));
                user.setLastLoginTime(new Date());
                user.setId(userByPhone.getId());
                mapper.updateByPrimaryKeySelective(user);
                CurrentUserDTO userDTO = (CurrentUserDTO) userDetailsService.loadUserByUsername(user.getTelephone());
                userDTO.setToken(UUID.randomUUID().toString());
                final String token = tokenUtils.generateToken(userDTO);
                redisComponent.cacheLoginUser(userDTO.getToken(), userDTO);
                sysLogsService.insert(new SysLogsDO(user.getId(), "修改密码", (byte) 1, new Date(), null,null));
                return ResponseTool.success(token);
            }
            return new ResponseResult(false, 300, "不存在该用户，请先注册。");
        }
        return new ResponseResult(false, 300, "验证码不正确");
    }

    @Override
    @Transactional(rollbackFor = {Exception.class}, propagation = Propagation.REQUIRED)
    public ResponseResult updatePassword(SysUserDO user) {
        if (!user.getPassword().equals(user.getCheckPassword())) {
            return new ResponseResult(false, 300, "两次密码不一致。");
        }
        String code = redisComponent.getString(CommonUtil.PHONE_PRE + user.getTelephone());
        if (user.getVerify().equals(code)) {
            redisComponent.deleteString(CommonUtil.PHONE_PRE + user.getTelephone());
            user.setUpdateTime(new Date());
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setUpdateTime(new Date());
            Integer id = UserUtil.getLoginUser().getId();
            user.setUpdateUser(id);
            if(user.getId() == null){
                user.setId(id);
            }
            mapper.updateByPrimaryKeySelective(user);
            return ResponseTool.success();
        }
        return new ResponseResult(false, 300, "验证码不正确");
    }

    @Override
    public ResponseResult selectBossForLeave(Integer userId) {
        SysUserDO user;
        if (StringUtils.isEmpty(userId)) {
            user = (SysUserDO) UserUtil.getLoginUser();
            log.info("使用自己id" + user.getDeptId());
        } else {
            user = mapper.selectByPrimaryKey(userId);
            log.info("使用前台传来id" +
                    "" + user.getDeptId());
        }
        if ( user.getId() > 0) {
            log.info("不为空");
            user = mapper.selectBossForLeave(user.getDeptId());
        }

        return ResponseTool.success(user);
    }

    @Override
    public ResponseResult selectPersonnelLeave() {
        List <SysUserDO> sysUserDOS = mapper.selectPersonnelLeave();
        return ResponseTool.success(sysUserDOS);
    }

    @Override
    public ResponseResult selectCaiGoCheckUserId() {
        SysUserDO sysUserDO = mapper.selectCaiGoCheckUserId();
        Select2DTO select2DTO = new Select2DTO(sysUserDO.getId(), sysUserDO.getUserName());
        return ResponseTool.success(select2DTO);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class}, propagation = Propagation.REQUIRED)
    public ResponseResult updateUser(MultipartFile file, UserDTO dto, HttpServletRequest request) {
log.info(dto.toString());
        try {
            String oldImgl = dto.getImgUrl();
            ResponseResult responseResult = uploadFileUtil.uploadFile(file, request);
            if (responseResult.getSuccess()) {
                String path = responseResult.getData().toString();
                dto.setImgUrl(path);
                if(!StringUtils.isEmpty(oldImgl)){
                    uploadFileUtil.deleteFileByPath(oldImgl,request);
                }
            }
            if(!file.isEmpty() && !responseResult.getSuccess()){
                return new ResponseResult(false, 300, "图片上传出了问题");
            }
            if(!StringUtils.isEmpty(dto.getDeptId()) && !StringUtils.isEmpty(dto.getPositionId())){
                int count = deptPositionRelationDOMapper.selectCuntByDeptPosition(dto.getDeptId(),dto.getPositionId());
                if(count == 0 ){
                    SysDeptPositionRelationDO aDo = new SysDeptPositionRelationDO();
                    aDo.setDeptId(dto.getDeptId());
                    aDo.setPositionId(dto.getPositionId());
                    deptPositionRelationDOMapper.insertSelective(aDo);
                }
            }
            StringBuilder str = new StringBuilder();
            if (dto.getProvince() != null) {
                str.append(dto.getProvince());
            }
            if (dto.getCity() != null) {
                str.append("," + dto.getCity());
            }
            if (dto.getCountry() != null) {
                str.append("," + dto.getCountry());
            }
            dto.setWorkAddress(str.toString());
            dto.setUpdateUser(UserUtil.getLoginUser().getId());
            dto.setUpdateTime(new Date());
            mapper.updateByUpdateUserDtoKeySelective(dto);
            return ResponseTool.success();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseTool.fail();
    }


    @Override
    public UserPageDTO<SysUserDO> selectWithPage(UserPageDTO dto) {

        int pageNo = dto.getStart() / dto.getLength() + 1;
        log.info(dto.getUserName() + ": pageNo");
        PageHelper.startPage(pageNo, dto.getLength());
        List <SysUserDO> list = mapper.selectByPage(dto);
        PageInfo <SysUserDO> pageInfo = new PageInfo<>(list);

        dto.setData(list);
        dto.setRecordsFiltered((int) pageInfo.getTotal());
        dto.setRecordsTotal((int) pageInfo.getTotal());
        dto.setDraw(dto.getDraw());
        return dto;

    }


    @Override
    @Transactional(rollbackFor = {Exception.class}, propagation = Propagation.REQUIRED)
    public ResponseResult addUser(MultipartFile file, UserDTO user, HttpServletRequest request)  {
//        判断是否已经存在该手机号的用户了
        if(user.getPassword().equals(user.getCheckPassword())){
            if(user.getTelephone() != null &&  !"".equals(user.getTelephone())){
                SysUserDO sysUserDO = mapper.selectUserByPhone(user.getTelephone());
                if(sysUserDO != null && sysUserDO.getId() > 0){
                    return new ResponseResult(false,300,"该电话号码已经注册，请重新填写其他的联系方式！");
                }
            }
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            try {
                ResponseResult result = uploadFileUtil.uploadFile(file, request);
                if(result.getSuccess()){
                    user.setImgUrl(result.getData().toString());
                }
                StringBuilder str = new StringBuilder();
                if (user.getProvince() != null) {
                    str.append(user.getProvince());
                }
                if (user.getCity() != null) {
                    str.append("," + user.getCity());
                }
                if (user.getCountry() != null) {
                    str.append("," + user.getCountry());
                }
                user.setWorkAddress(str.toString());
                user.setCreateTime(new Date());
                user.setCreateUser(UserUtil.getLoginUser().getId());
                if(!StringUtils.isEmpty(user.getDeptId()) && !StringUtils.isEmpty(user.getPositionId())){
                    int count = deptPositionRelationDOMapper.selectCuntByDeptPosition(user.getDeptId(),user.getPositionId());
                    if(count == 0 ){
                        SysDeptPositionRelationDO aDo = new SysDeptPositionRelationDO();
                        aDo.setDeptId(user.getDeptId());
                        aDo.setPositionId(user.getPositionId());
                        deptPositionRelationDOMapper.insertSelective(aDo);
                    }
                }
                mapper.insertByUserDTO(user);
                return ResponseTool.success();
            } catch (IOException e) {
                e.printStackTrace();
                return new ResponseResult(false,300,"图片上传出错");
            }
        }
        return new ResponseResult(false,300,"两次密码不一致");

    }
}
