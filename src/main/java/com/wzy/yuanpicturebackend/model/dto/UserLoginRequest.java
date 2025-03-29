package com.wzy.yuanpicturebackend.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author wzy
 * @date 2025年03月29日 15:51
 * 用户注册请求封装类
 */
@Data
public class UserLoginRequest implements Serializable {

    private static final long serialVersionUID = -1108003209003850589L;
    /**
     * 账号
     */
    private String userAccount;

    /**
     * 密码
     */
    private String password;


}
