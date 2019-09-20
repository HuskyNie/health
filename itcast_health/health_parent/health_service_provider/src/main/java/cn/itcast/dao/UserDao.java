package cn.itcast.dao;

import cn.itcast.pojo.User;
import org.apache.ibatis.annotations.Select;

public interface UserDao {
    @Select("select * from t_user where username = #{username}")
    User findByUsername(String username);
}
