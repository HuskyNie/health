package cn.itcast.dao;

import cn.itcast.pojo.Setmeal;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectKey;

import java.util.HashMap;

@Mapper
public interface SetMealDao {
    //新增套餐基本信息
    @SelectKey(statement = "select last_insert_id()" , keyProperty = "id" , before = false , resultType = Integer.class)
    @Insert("insert into t_setmeal (code,name,sex,age,helpCode,price,remark,attention,img) values (#{code},#{name},#{sex},#{age},#{helpCode},#{price},#{remark},#{attention},#{img})")
    void add(Setmeal setmeal);

    //设置套餐与检查组关联关系
    @Insert("insert into t_setmeal_checkgroup (setmeal_id,checkgroup_id) values (#{setmeal_id},#{checkgroup_id})")
    void setSetMeanAndCheckGroup(HashMap<String, Integer> map);

    //设置分页查询方法
    Page<Setmeal> selectByCondition(String queryString);
}
