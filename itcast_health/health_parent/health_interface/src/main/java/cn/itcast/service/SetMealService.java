package cn.itcast.service;

import cn.itcast.entity.PageResult;
import cn.itcast.pojo.Setmeal;

import java.util.List;
import java.util.Map;

public interface SetMealService {
    void add(Setmeal setmeal, Integer[] checkGroupIds);

    PageResult findPage(Integer currentPage, Integer pageSize, String queryString);

    List<Setmeal> findAll();

    Setmeal findById(Integer id);

    List<Map<String, Object>> findSetMealCount();

    void delete(Integer id);

    List<Integer> findCheckGroupIdsBySetMealId(Integer id);

    void update(Setmeal setmeal, Integer[] checkGroupIds);
}
