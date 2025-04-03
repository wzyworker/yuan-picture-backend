package com.wzy.yuanpicturebackend.model.vo;

import lombok.Data;

import java.util.List;

/**
 * @author wzy
 * @date 2025年04月03日 21:52
 */

@Data
public class PictureTagCategory {

    /**
     * 标签列表
     */
    private List<String> tagList;

    /**
     * 分类列表
     */
    private List<String> categoryList;
}
