package cn.itcast.service;


import cn.itcast.entity.PageResult;
import cn.itcast.pojo.CheckGroup;

import java.util.List;

public interface CheckGroupService {
    void add(CheckGroup checkGroup , Integer[] checkitemIds);

    PageResult findPage(Integer currentPage, Integer pageSize, String queryString);

    CheckGroup findById(Integer id);

    List<Integer> findCheckItemIdsByCheckGroupId(Integer id);

    void update(CheckGroup checkGroup, Integer[] checkItemIds);

    void deleteById(Integer id);

    List<CheckGroup> findAll();
}
