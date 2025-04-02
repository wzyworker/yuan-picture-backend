package com.wzy.yuanpicturebackend.service;

import cn.hutool.http.server.HttpServerRequest;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wzy.yuanpicturebackend.model.dto.user.UserQueryRequest;
import com.wzy.yuanpicturebackend.model.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wzy.yuanpicturebackend.model.vo.LoginUserVO;
import com.wzy.yuanpicturebackend.model.vo.UserVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
* @author wzy
* @description 针对表【user(用户)】的数据库操作Service
* @createDate 2025-03-29 15:20:37
*/
public interface UserService extends IService<User> {

    /**
     * 用户注册
     *
     * @param userAccount 用户账号
     * @param password 用户密码
     * @param checkPassword 确认密码
     * @return 新用户id
     */
    long userRegister(String userAccount, String password, String checkPassword);

    /**
     * 用户登录
     * @param userAccount 用户账户
     * @param password 用户密码
     * @param request
     * @return 脱敏用户信息（不是所有信息都需要返回）
     */
    LoginUserVO userLogin(String userAccount, String password, HttpServletRequest request);

    /**
     * 密码加密
     *
     * @param password 密码
     * @return 加密后的密码
     */
    String getEncryptPassword(String password);

    /**
     * 获取当前登录用户
     * @param request 请求
     * @return 当前登录用户
     */
    User getLoginUser(HttpServletRequest request);

    /**
     * 获取登录脱敏用户信息
     * @param user 用户
     * @return 脱敏登录用户信息
     */
    LoginUserVO getLoginUserVO(User user);

    /**
     * 获取脱敏用户信息
     * @param user 用户
     * @return 脱敏用户信息
     */
    UserVO getUserVO(User user);

    /**
     * 获取脱敏用户信息列表
     * @param userList 用户列表
     * @return 脱敏用户信息列表
     */
    List<UserVO> getUserVOList(List<User> userList);

    /**
     * 用户注销（登出）
     * @param request
     * @return
     */
    boolean userLogout(HttpServletRequest request);

    /**
     * 获取查询条件
     * @param userQueryRequest
     * @return
     */
    QueryWrapper<User> getQueryWrapper(UserQueryRequest userQueryRequest);

    /**
     * 判断用户是否是管理员
     * @param user
     * @return
     */
    boolean isAdmin(User user);
}
