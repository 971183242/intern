package com.oocl.workshop.intern.infrastructure.common.entity;

import lombok.Data;

@Data
public class PageInfo {
    private int pageSize;
    private int pageIndex;
    private long totalSize;
    private long totalPage;
}
