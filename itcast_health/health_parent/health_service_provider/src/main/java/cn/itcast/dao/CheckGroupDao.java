package cn.itcast.dao;

import cn.itcast.pojo.CheckGroup;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;

import java.util.HashMap;
import java.util.List;

@Mapper
public interface CheckGroupDao {
    //添加方法,添加检查组基本信息,获取到自增id返回
    @SelectKey(statement = "select last_insert_id()" , keyProperty = "id" , before = false , resultType = Integer.class)
    @Insert("insert into t_checkgroup (code,name,sex,helpCode,remark,attention) values (#{code},#{name},#{sex},#{helpCode},#{remark},#{attention})")
    void add(CheckGroup checkGroup);

    //设置检查项与检查组关联关系
    @Insert("insert into t_checkgroup_checkitem (checkgroup_id , checkitem_id) values (#{checkgroup_id} , #{checkitem_id})")
    void setCheckGroupAssociationWithCheckItem(HashMap<String, Integer> map);

    //分页查询方法
    Page<CheckGroup> selectByCondition(String queryString);
}
