package cn.itcast.service;

import cn.itcast.pojo.Setmeal;

public interface SetMealService {
    void add(Setmeal setmeal, Integer[] checkGroupIds);
}
