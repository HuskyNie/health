package cn.itcast.dao;

import cn.itcast.pojo.CheckGroup;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

@Mapper
public interface CheckGroupDao {
    //添加方法,添加检查组基本信息,获取到自增id返回
    @SelectKey(statement = "select last_insert_id()" , keyProperty = "id" , before = false , resultType = Integer.class)
    @Insert("insert into t_checkgroup (code,name,sex,helpCode,remark,attention) values (#{code},#{name},#{sex},#{helpCode},#{remark},#{attention})")
    void add(CheckGroup checkGroup);

    //设置检查项与检查组关联关系
    @Insert("insert into t_checkgroup_checkitem (checkgroup_id , checkitem_id) values (#{checkgroup_id} , #{checkitem_id})")
    void setCheckGroupAssociationWithCheckItem(Map<String, Integer> map);

    //分页查询方法
    Page<CheckGroup> selectByCondition(String queryString);

    //编辑页面数据回显方法
    @Select("select * from t_checkgroup where id = #{id}")
    CheckGroup findById(Integer id);

    //编辑页面检查项关联关系回显方法
    @Select("select checkitem_id from t_checkgroup_checkitem where checkgroup_id = #{id}")
    List<Integer> findCheckItemIdsByCheckGroupId(Integer id);

    //更新检查组基本信息
    void updateCheckGroup(CheckGroup checkGroup);

    //清空当前检查组的关联关系
    @Delete("delete from t_checkgroup_checkitem where checkgroup_id = #{id}")
    void deleteAssociation(Integer id);

    //删除检查组基本信息
    @Delete("delete from t_checkgroup where id = #{id}")
    void deleteById(Integer id);

    //套餐查询所有方法
    @Select("select * from t_checkgroup")
    List<CheckGroup> findAll();

    //移动端根据id查询方法
    @Select("select * from t_checkgroup where id in (select checkgroup_id from t_setmeal_checkgroup where setmeal_id=#{id})")
    @Results({
            @Result(id = true , column = "id" , property = "id"),
            @Result(column = "name" , property = "name"),
            @Result(column = "code" , property = "code"),
            @Result(column = "helpCode" , property = "helpCode"),
            @Result(column = "sex" , property = "sex"),
            @Result(column = "remark" , property = "remark"),
            @Result(column = "attention" , property = "attention"),
            @Result(
                    property = "checkItems",
                    javaType = List.class,
                    column = "id",
                    many = @Many(select = "cn.itcast.dao.CheckItemDao.findCheckItemById")
            )
    })
    List<CheckGroup> findCheckGroupById(Integer id);
}
