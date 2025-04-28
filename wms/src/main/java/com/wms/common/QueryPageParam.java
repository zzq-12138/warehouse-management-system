package com.wms.common;

import lombok.Data;

import java.util.HashMap;

@Data
public class QueryPageParam {
    private static int PAGE_SIZE = 10; // 每页显示的条数
    private static int PAGE_NUM = 1; // 当前页码

    private int pageSize = PAGE_SIZE; // 每页显示的条数
    private int pageNum = PAGE_NUM; // 当前页码

    private HashMap param = new HashMap(); // 查询参数
}
