package com.wzy.yuanpicturebackend.controller;

import com.wzy.yuanpicturebackend.common.BaseResponse;
import com.wzy.yuanpicturebackend.common.ResultUtils;
import com.wzy.yuanpicturebackend.exception.ErrorCode;
import com.wzy.yuanpicturebackend.exception.ThrowUtils;
import com.wzy.yuanpicturebackend.model.dto.UserRegisterRequest;
import com.wzy.yuanpicturebackend.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author wzy
 * @date 2025年03月23日 20:21
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    /**
     * 用户注册
     * @return
     */
    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        ThrowUtils.throwIf(userRegisterRequest == null, ErrorCode.PARAMETER_ERROR);
        String userAccount = userRegisterRequest.getUserAccount();
        String password = userRegisterRequest.getPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();

        long result = userService.userRegister(userAccount, password, checkPassword);

        return ResultUtils.success(result);
    }

    /**
     * 用户登录
     * @param userRegisterRequest
     * @return
     */
    @PostMapping("/login")
    public BaseResponse<Long> userLogin(@RequestBody UserRegisterRequest userRegisterRequest) {
        ThrowUtils.throwIf(userRegisterRequest == null, ErrorCode.PARAMETER_ERROR);
        String userAccount = userRegisterRequest.getUserAccount();
        String password = userRegisterRequest.getPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();

        long result = userService.userRegister(userAccount, password, checkPassword);

        return ResultUtils.success(result);
    }
}
