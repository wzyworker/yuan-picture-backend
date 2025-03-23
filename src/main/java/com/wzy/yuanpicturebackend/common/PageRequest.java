package com.wzy.yuanpicturebackend.common;

import lombok.Data;

/**
 * @author wzy
 * @date 2025年03月23日 20:02
 * 分页请求类
 */
@Data
public class PageRequest {

    /**
     * 当前页码
     */
    private int current = 1;

    /**
     * 页面条数
     */
    private int pageSize = 20;

    /**
     * 排序字段
     */
    private String sortField;

    /**
     * 排序顺序(默认升序)
     */
    private String sortOrder = "descend";
}
