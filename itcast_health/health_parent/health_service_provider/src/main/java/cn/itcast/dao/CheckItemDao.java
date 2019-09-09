package cn.itcast.dao;

import cn.itcast.pojo.CheckItem;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface CheckItemDao {
    @Insert("insert into t_checkitem values(#{id} , #{code} , #{name} , #{sex} , #{age} , #{price} , #{type} , #{attention} , #{remark})")
    void add(CheckItem checkItem);


    Page<CheckItem> selectByCondition(String queryString);
}
