package com.wzy.yuanpicturebackend.model.dto.picture;

import lombok.Data;

import java.io.Serializable;

/**
 * @author wzy
 * @date 2025年03月31日 22:42
 */

@Data
public class PictureUploadRequest implements Serializable {

    private static final long serialVersionUID = 9100572845034719005L;

    /**
     * 图片ID（用于修改）
     */
    private Long id;
}
