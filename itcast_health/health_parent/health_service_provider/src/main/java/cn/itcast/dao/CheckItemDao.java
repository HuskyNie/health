package cn.itcast.dao;

import cn.itcast.pojo.CheckItem;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CheckItemDao {
    //新建方法
    @Insert("insert into t_checkitem values(#{id} , #{code} , #{name} , #{sex} , #{age} , #{price} , #{type} , #{attention} , #{remark})")
    void add(CheckItem checkItem);

    //分页/条件查询方法
    Page<CheckItem> selectByCondition(String queryString);

    //查询检查项是否关联检查组方法
    @Select("select count(*) from t_checkgroup_checkitem where checkitem_id = #{id}")
    long findCountByCheckItemId(Integer id);

    //删除方法
    @Delete("delete from t_checkitem where id = #{id}")
    void deleteById(Integer id);

    //数据回显方法
    @Select("select * from t_checkitem where id = #{id}")
    CheckItem findById(Integer id);

    //更新方法
    void update(CheckItem checkItem);

    //查询所有方法
    @Select("select * from t_checkitem")
    List<CheckItem> findAll();
}
