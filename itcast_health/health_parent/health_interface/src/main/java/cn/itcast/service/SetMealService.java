package cn.itcast.service;

import cn.itcast.entity.PageResult;
import cn.itcast.pojo.Setmeal;

import java.util.List;

public interface SetMealService {
    void add(Setmeal setmeal, Integer[] checkGroupIds);

    PageResult findPage(Integer currentPage, Integer pageSize, String queryString);

    List<Setmeal> findAll();

    Setmeal findById(Integer id);
}
