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
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

/**
 * @author wzy
 * @date 2025年03月31日 21:34
 */

@Slf4j
@RestController
@RequestMapping("/file")
public class FileController {

    @Resource
    private CosManage cosManage;

    /**
     * 测试文件上传
     * @param multipartFile
     * @return
     */
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    @PostMapping("/test/upload")
    public BaseResponse<String> testUploadFile(@RequestPart("file") MultipartFile multipartFile ) {
        // 指定文件目录
        String fileName = multipartFile.getOriginalFilename();
        String filePath = String.format("/test/%s", fileName);

        File file = null;
        try {
            // 创建临时文件
            file = File.createTempFile(filePath, null);
            // 保存文件
            multipartFile.transferTo(file);
            // 上传文件
            cosManage.putObject(filePath, file);

            // 返回可访问的地址
            return ResultUtils.success(filePath);
        } catch (Exception e) {
            log.error("文件上传失败,filepath = " + filePath, e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "文件上传失败");
        } finally {
            if (file != null) {
                // 删除临时文件
                boolean delete = file.delete();
                if (!delete) {
                    log.error("临时文件删除失败,filepath = {}", filePath);
                }
            }
        }
    }

    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    @GetMapping("/test/download")
    public void testDownloadFile(String filePath, HttpServletResponse response) throws IOException {
        COSObjectInputStream cosObjectInputStream = null;
        try {
            COSObject cosObject = cosManage.getObject(filePath);
            cosObjectInputStream = cosObject.getObjectContent();
            byte[] bytes = IOUtils.toByteArray(cosObjectInputStream);
            // 设置响应头，告诉浏览器需要下载文件
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment;filename=" + filePath);
            // 写入响应中
            response.getOutputStream().write(bytes);
            response.getOutputStream().flush();
        } catch (Exception e) {
            log.error("文件下载失败,filepath = " + filePath, e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "文件下载失败");
        }finally {
            // 关闭输入流
            if (cosObjectInputStream != null) {
                cosObjectInputStream.close();
            }
        }
    }
}
