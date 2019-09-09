package cn.itcast.dao;

import cn.itcast.pojo.CheckItem;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface CheckItemDao {
    @Insert("insert into t_checkitem values(#{id} , #{code} , #{name} , #{sex} , #{age} , #{price} , #{type} , #{attention} , #{remark})")
    void add(CheckItem checkItem);


    Page<CheckItem> selectByCondition(String queryString);

    @Select("select count(*) from t_checkgroup_checkitem where checkitem_id = #{id}")
    long findCountByCheckItemId(Integer id);

    @Delete("delete from t_checkitem where id = #{id}")
    void deleteById(Integer id);
}
