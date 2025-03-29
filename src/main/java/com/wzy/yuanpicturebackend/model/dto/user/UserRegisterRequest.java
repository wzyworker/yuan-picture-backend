package com.wzy.yuanpicturebackend.model.dto.user;

import lombok.Data;

import java.io.Serializable;

/**
 * @author wzy
 * @date 2025年03月29日 15:51
 * 用户注册请求封装类
 */
@Data
public class UserRegisterRequest implements Serializable {

    private static final long serialVersionUID = 1468878626881877018L;

    /**
     * 账号
     */
    private String userAccount;

    /**
     * 密码
     */
    private String password;

    /**
     * 确认密码
     */
    private String checkPassword;

}
