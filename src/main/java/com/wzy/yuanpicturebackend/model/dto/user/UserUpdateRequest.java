package com.wzy.yuanpicturebackend.model.dto.user;

import lombok.Data;

import java.io.Serializable;

/**
 * @author wzy
 * @date 2025年03月29日 15:51
 * 用户更新请求封装类（管理员和用户皆可使用）
 */
@Data
public class UserUpdateRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 用户昵称
     */
    private String userName;

    /**
     * 用户头像
     */
    private String userAvatar;

    /**
     * 简介
     */
    private String userProfile;

    /**
     * 用户角色：user/admin
     */
    private String userRole;

    private static final long serialVersionUID = 1L;
}
