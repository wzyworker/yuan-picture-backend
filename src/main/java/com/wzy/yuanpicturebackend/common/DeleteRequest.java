package com.wzy.yuanpicturebackend.common;

import lombok.Data;

import java.io.Serializable;

/**
 * @author wzy
 * @date 2025年03月23日 20:06
 * 删除请求类
 */

@Data
public class DeleteRequest implements Serializable {
    private Long id;

    private static final long serialVersionUID = 1L;
}
