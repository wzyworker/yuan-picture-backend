package com.wzy.yuanpicturebackend.controller;

import com.qcloud.cos.model.COSObject;
import com.qcloud.cos.model.COSObjectInputStream;
import com.qcloud.cos.utils.IOUtils;
import com.wzy.yuanpicturebackend.annotation.AuthCheck;
import com.wzy.yuanpicturebackend.common.BaseResponse;
import com.wzy.yuanpicturebackend.common.ResultUtils;
import com.wzy.yuanpicturebackend.constant.UserConstant;
import com.wzy.yuanpicturebackend.exception.BusinessException;
import com.wzy.yuanpicturebackend.exception.ErrorCode;
import com.wzy.yuanpicturebackend.manage.CosManage;
import com.wzy.yuanpicturebackend.model.dto.picture.PictureUploadRequest;
import com.wzy.yuanpicturebackend.model.entity.User;
import com.wzy.yuanpicturebackend.model.vo.PictureVO;
import com.wzy.yuanpicturebackend.service.PictureService;
import com.wzy.yuanpicturebackend.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

/**
 * @author wzy
 * @date 2025年03月31日 21:34
 */

@Slf4j
@RestController
@RequestMapping("/picture")
public class PictureController {

    @Resource
    private UserService userService;

    @Resource
    private PictureService pictureService;

    @PostMapping("/upload")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<PictureVO> uploadPicture(@RequestPart("file") MultipartFile multipartFile,
                                              PictureUploadRequest pictureUploadRequest,
                                              HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        PictureVO pictureVO = pictureService.uploadPicture(multipartFile, pictureUploadRequest, loginUser);
        return ResultUtils.success(pictureVO);
    }
}
