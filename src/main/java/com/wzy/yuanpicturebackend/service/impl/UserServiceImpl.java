package com.wzy.yuanpicturebackend.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wzy.yuanpicturebackend.constant.UserConstant;
import com.wzy.yuanpicturebackend.exception.BusinessException;
import com.wzy.yuanpicturebackend.exception.ErrorCode;
import com.wzy.yuanpicturebackend.model.dto.user.UserQueryRequest;
import com.wzy.yuanpicturebackend.model.entity.User;
import com.wzy.yuanpicturebackend.model.enums.UserRoleEnum;
import com.wzy.yuanpicturebackend.model.vo.LoginUserVO;
import com.wzy.yuanpicturebackend.model.vo.UserVO;
import com.wzy.yuanpicturebackend.service.UserService;
import com.wzy.yuanpicturebackend.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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

    @Override
    public User getLoginUser(HttpServletRequest request) {
        // 判断用户是否登录
        Object userObj = request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null || currentUser.getId() == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR, "用户未登录");
        }
        // 从数据库查询（安全，防止别人修改了session）（追求性能可以注释）
        Long id = currentUser.getId();
        User user = this.getById(id);
        if (user == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR, "用户不存在");
        }
        return currentUser;
    }

    /**
     * 获取脱敏后的登录用户信息
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

    /**
     * 获取脱敏后的用户信息
     * @param user 用户
     * @return
     */
    @Override
    public UserVO getUserVO(User user) {
        if (user == null){
            return null;
        }
        UserVO userVO = new UserVO();
        BeanUtil.copyProperties(user, userVO);
        return userVO;
    }

    /**
     * 获取脱敏后的用户列表
     * @param userList 用户列表
     * @return
     */
    @Override
    public List<UserVO> getUserVOList(List<User> userList) {
        if (CollectionUtil.isEmpty(userList)){
            return null;
        }
        return userList.stream()
                .map(this::getUserVO)
                .collect(Collectors.toList());
    }

    /**
     * 用户注销（登出）
     * @param request
     * @return
     */
    @Override
    public boolean userLogout(HttpServletRequest request) {
        // 判断用户是否登录
        Object userObj = request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
        if (userObj == null) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "用户未登录");
        }

        // 移除登录态
        request.getSession().removeAttribute(UserConstant.USER_LOGIN_STATE);
        return true;
    }

    @Override
    public QueryWrapper<User> getQueryWrapper(UserQueryRequest userQueryRequest) {
        if (userQueryRequest == null){
            throw new BusinessException(ErrorCode.PARAMETER_ERROR, "参数为空");
        }
        Long id = userQueryRequest.getId();
        String userName = userQueryRequest.getUserName();
        String userAccount = userQueryRequest.getUserAccount();
        String userProfile = userQueryRequest.getUserProfile();
        String userRole = userQueryRequest.getUserRole();
        String sortField = userQueryRequest.getSortField();
        String sortOrder = userQueryRequest.getSortOrder();
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(ObjUtil.isNotNull(id), "id", id);
        queryWrapper.eq(StrUtil.isNotBlank(userRole), "userRole", userRole);
        queryWrapper.like(StrUtil.isNotBlank(userName), "userName", userName);
        queryWrapper.like(StrUtil.isNotBlank(userAccount), "userAccount", userAccount);
        queryWrapper.like(StrUtil.isNotBlank(userProfile), "userProfile", userProfile);
        queryWrapper.orderBy(StrUtil.isNotEmpty(sortField), sortOrder.equals("ascend"), sortField);
        return queryWrapper;
    }
}




