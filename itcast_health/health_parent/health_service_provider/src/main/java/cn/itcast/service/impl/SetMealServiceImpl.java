package cn.itcast.service.impl;

import cn.itcast.dao.SetMealDao;
import cn.itcast.entity.PageResult;
import cn.itcast.pojo.Setmeal;
import cn.itcast.service.SetMealService;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;

@Service(interfaceClass = SetMealService.class)
@Transactional
public class SetMealServiceImpl implements SetMealService {

    @Autowired
    private SetMealDao setMealDao;

    //分页查询方法
    public PageResult findPage(Integer currentPage, Integer pageSize, String queryString) {
        PageHelper.startPage(currentPage , pageSize);
        Page<Setmeal> page = setMealDao.selectByCondition(queryString);
        return new PageResult(page.getTotal() , page.getResult());
    }

    //新增方法
    public void add(Setmeal setmeal, Integer[] checkGroupIds) {
        //添加套餐基本信息
        setMealDao.add(setmeal);
        //添加套餐与检查组关联信息
        if (checkGroupIds != null && checkGroupIds.length > 0){
            for (Integer checkGroupId : checkGroupIds) {
                HashMap<String, Integer> map = new HashMap<>();
                map.put("setmeal_id" , setmeal.getId());
                map.put("checkgroup_id" , checkGroupId);
                setMealDao.setSetMeanAndCheckGroup(map);
            }
        }
    }
}
