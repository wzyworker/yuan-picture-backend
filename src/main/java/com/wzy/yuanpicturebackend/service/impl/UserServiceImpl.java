package com.wzy.yuanpicturebackend.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wzy.yuanpicturebackend.constant.UserConstant;
import com.wzy.yuanpicturebackend.exception.BusinessException;
import com.wzy.yuanpicturebackend.exception.ErrorCode;
import com.wzy.yuanpicturebackend.model.entity.User;
import com.wzy.yuanpicturebackend.model.enums.UserRoleEnum;
import com.wzy.yuanpicturebackend.model.vo.LoginUserVO;
import com.wzy.yuanpicturebackend.service.UserService;
import com.wzy.yuanpicturebackend.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;

/**
* @author wzy
* @description 针对表【user(用户)】的数据库操作Service实现
* @createDate 2025-03-29 15:20:37
*/
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService {

    /**
     *  用户注册
     * @param userAccount 用户账号
     * @param password 用户密码
     * @param checkPassword 确认密码
     * @return 新用户id
     */
    @Override
    public long userRegister(String userAccount, String password, String checkPassword) {

        // 1. 校验参数
        if (StrUtil.hasBlank(userAccount, password, checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMETER_ERROR, "请求参数为空");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMETER_ERROR, "用户账号过短");
        }

        if (password.length() < 8 || checkPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMETER_ERROR, "用户密码过短");
        }

        if (!password.equals(checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMETER_ERROR, "两次密码不一致");
        }
        // 2. 检查数据是否已有重复
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        Long count = this.baseMapper.selectCount(queryWrapper);
        if (count > 0) {
            throw new BusinessException(ErrorCode.PARAMETER_ERROR, "用户账号已存在");
        }
        // 3. 密码加密
        String encryptPassword = getEncryptPassword(password);
        // 4. 插入数据
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassword);
        user.setUserName("无名");
        user.setUserRole(UserRoleEnum.USER.getValue());
        boolean saveResult = this.save(user);
        if (!saveResult) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "用户注册失败，数据库异常");
        }
        // save方法 主键回填
        return user.getId();
    }

    @Override
    public LoginUserVO userLogin(String userAccount, String password, HttpServletRequest request) {

        // 1. 校验
        if (StrUtil.hasBlank(userAccount, password)) {
            throw new BusinessException(ErrorCode.PARAMETER_ERROR, "用户账号或密码不能为空");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMETER_ERROR, "用户账号过短");
        }
        if (password.length() < 8 ) {
            throw new BusinessException(ErrorCode.PARAMETER_ERROR, "用户密码过短");
        }
        // 2. 密码加密
        String encryptPassword = getEncryptPassword(password);
        // 3. 查询用户是否存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        queryWrapper.eq("userPassword", encryptPassword);
        User user = this.baseMapper.selectOne(queryWrapper);
        //     不存在抛异常
        if (user == null) {
            log.info("user login failed, userAccount cannot match password");
            throw new BusinessException(ErrorCode.PARAMETER_ERROR, "用户不存在或密码错误");
        }
        // 4. 保存用户登录态
        request.getSession().setAttribute(UserConstant.USER_LOGIN_STATE, user);
        return this.getLoginUserVO(user);
    }

    /**
     * 加密
     * @param password 密码
     * @return 加密后的密码
     */
    @Override
    public String getEncryptPassword(String password) {
        // 加盐
        final String SALT = "yuan";
        return DigestUtils.md5DigestAsHex((SALT + password).getBytes());
    }

    /**
     * 获取脱敏类的用户信息
     * @param user 用户
     * @return 脱敏用户信息
     */
    @Override
    public LoginUserVO getLoginUserVO(User user) {
        if (user == null){
            return null;
        }
        LoginUserVO loginUserVO = new LoginUserVO();
        BeanUtil.copyProperties(user, loginUserVO);
        return loginUserVO;
    }
}




