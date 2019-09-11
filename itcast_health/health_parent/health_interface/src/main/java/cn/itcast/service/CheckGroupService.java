package cn.itcast.service;


import cn.itcast.entity.PageResult;
import cn.itcast.pojo.CheckGroup;

public interface CheckGroupService {
    void add(CheckGroup checkGroup , Integer[] checkitemIds);

    PageResult findPage(Integer currentPage, Integer pageSize, String queryString);
}
