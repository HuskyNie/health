package cn.itcast.service;

import cn.itcast.entity.PageResult;
import cn.itcast.pojo.CheckItem;

public interface CheckItemService {
    void add(CheckItem checkItem);

    PageResult findPage(Integer currentPage, Integer pageSize, String queryString);

    void delete(Integer id);
}
