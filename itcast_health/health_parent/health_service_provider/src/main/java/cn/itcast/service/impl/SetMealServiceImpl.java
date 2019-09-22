package cn.itcast.service.impl;

import cn.itcast.constant.RedisConstant;
import cn.itcast.dao.SetMealDao;
import cn.itcast.entity.PageResult;
import cn.itcast.pojo.Setmeal;
import cn.itcast.service.SetMealService;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.JedisPool;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = SetMealService.class)
@Transactional
public class SetMealServiceImpl implements SetMealService {

    @Autowired
    private SetMealDao setMealDao;
    @Autowired
    private JedisPool jedisPool;

    //分页查询方法
    public PageResult findPage(Integer currentPage, Integer pageSize, String queryString) {
        PageHelper.startPage(currentPage , pageSize);
        Page<Setmeal> page = setMealDao.selectByCondition(queryString);
        return new PageResult(page.getTotal() , page.getResult());
    }

    //移动端根据id查询套餐方法
    public Setmeal findById(Integer id) {
        return setMealDao.findById(id);
    }

    //移动端查询所有方法
    public List<Setmeal> findAll() {
        return setMealDao.findAll();
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
                setMealDao.setSetMealAndCheckGroup(map);
            }
        }
        //将图片名称保存到Redis
        jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES,setmeal.getImg());
    }

    //后台套餐预约占比统计方法
    public List<Map<String, Object>> findSetMealCount() {
        return setMealDao.findSetMealCount();
    }

    //删除方法
    public void delete(Integer id) {
        setMealDao.deleteAssociation(id);
        setMealDao.deleteById(id);
    }

    //根据套餐id查询关联检查组
    public List<Integer> findCheckGroupIdsBySetMealId(Integer id) {
        return setMealDao.findCheckGroupIdsBySetMealId(id);
    }

    //编辑方法
    public void update(Setmeal setmeal, Integer[] checkGroupIds) {
        //更新基本信息
        setMealDao.update(setmeal);
        //清除中间表关系
        setMealDao.deleteAssociation(setmeal.getId());
        //更新中间表关系
        if (checkGroupIds != null && checkGroupIds.length > 0) {
            for (Integer checkGroupId : checkGroupIds) {
                Map<String , Integer> map = new HashMap<>();
                map.put("setmeal_id" , setmeal.getId());
                map.put("checkgroup_id" , checkGroupId);
                setMealDao.setSetMealAndCheckGroup(map);
            }
        }
    }
}
