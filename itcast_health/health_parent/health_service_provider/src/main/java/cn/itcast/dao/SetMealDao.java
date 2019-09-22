package cn.itcast.dao;

import cn.itcast.pojo.Setmeal;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

@Mapper
public interface SetMealDao {
    //新增套餐基本信息
    @SelectKey(statement = "select last_insert_id()" , keyProperty = "id" , before = false , resultType = Integer.class)
    @Insert("insert into t_setmeal (code,name,sex,age,helpCode,price,remark,attention,img) values (#{code},#{name},#{sex},#{age},#{helpCode},#{price},#{remark},#{attention},#{img})")
    void add(Setmeal setmeal);

    //设置套餐与检查组关联关系
    @Insert("insert into t_setmeal_checkgroup (setmeal_id,checkgroup_id) values (#{setmeal_id},#{checkgroup_id})")
    void setSetMealAndCheckGroup(Map<String, Integer> map);

    //设置分页查询方法
    Page<Setmeal> selectByCondition(String queryString);

    //移动端查询所有方法
    @Select("select * from t_setmeal")
    List<Setmeal> findAll();

    //移动端根据id查询套餐所有信息方法
    @Select("select * from t_setmeal where id = #{id}")
    @Results({
            @Result(id = true , column = "id" , property = "id"),
            @Result(column = "name" , property = "name"),
            @Result(column = "code" , property = "code"),
            @Result(column = "helpCode" , property = "helpCode"),
            @Result(column = "sex" , property = "sex"),
            @Result(column = "age" , property = "age"),
            @Result(column = "price" , property = "price"),
            @Result(column = "remark" , property = "remark"),
            @Result(column = "attention" , property = "attention"),
            @Result(column = "img" , property = "img"),
            @Result(
                    property = "checkGroups",
                    javaType = List.class,
                    column = "id",
                    many = @Many(select = "cn.itcast.dao.CheckGroupDao.findCheckGroupById")
            )
    })
    Setmeal findById(Integer id);

    //后台套餐预约占比统计方法
    @Select("select s.name , count(s.id) value from t_setmeal s ,t_order o where s.id = o.setmeal_id group by s.id")
    List<Map<String, Object>> findSetMealCount();

    //清除中间关系表
    @Delete("delete from t_setmeal_checkgroup where setmeal_id = #{id}")
    void deleteAssociation(Integer id);

    //根据id删除套餐基本信息
    @Delete("delete from t_setmeal where id = #{id}")
    void deleteById(Integer id);

    //根据套餐id查询关联检查组
    @Select("select checkgroup_id from t_setmeal_checkgroup where setmeal_id = #{id}")
    List<Integer> findCheckGroupIdsBySetMealId(Integer id);

    //根据id更新套餐基本信息方法
    void update(Setmeal setmeal);
}
