package com.hospital.vo;

import lombok.Data;
import java.util.List;

@Data
public class PageVo<T> {
    private List<T> list;
    private Long total;
    private Long page;
    private Long pageSize;

    public static <T> PageVo<T> of(List<T> list, Long total, Long page, Long pageSize) {
        PageVo<T> pageVo = new PageVo<>();
        pageVo.setList(list);
        pageVo.setTotal(total);
        pageVo.setPage(page);
        pageVo.setPageSize(pageSize);
        return pageVo;
    }
}
