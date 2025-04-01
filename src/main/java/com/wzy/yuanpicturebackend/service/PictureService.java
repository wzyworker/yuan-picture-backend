package com.wzy.yuanpicturebackend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wzy.yuanpicturebackend.model.dto.picture.PictureUploadRequest;
import com.wzy.yuanpicturebackend.model.entity.Picture;
import com.wzy.yuanpicturebackend.model.entity.User;
import com.wzy.yuanpicturebackend.model.vo.PictureVO;
import org.springframework.web.multipart.MultipartFile;

/**
* @author wzy
* @description 针对表【picture(图片)】的数据库操作Service
* @createDate 2025-03-31 22:15:52
*/
public interface PictureService extends IService<Picture> {

    /**
     * 上传图片
     *
     * @param multipartFile
     * @param request
     * @param loginUser
     * @return
     */
    PictureVO uploadPicture(MultipartFile multipartFile,
                            PictureUploadRequest pictureUploadRequest,
                            User loginUser);
}
