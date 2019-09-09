package cn.itcast.dao;

import cn.itcast.pojo.CheckItem;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CheckItemDao {
    @Insert("insert into t_checkitem values(#{id} , #{code} , #{name} , #{sex} , #{age} , #{price} , #{type} , #{attention} , #{remark})")
    void add(CheckItem checkItem);
}
