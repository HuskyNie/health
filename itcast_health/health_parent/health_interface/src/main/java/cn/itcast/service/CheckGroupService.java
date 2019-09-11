package cn.itcast.service;


import cn.itcast.pojo.CheckGroup;

public interface CheckGroupService {
    void add(CheckGroup checkGroup , Integer[] checkitemIds);
}
