package cn.itcast.dao;

import cn.itcast.pojo.OrderSetting;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Mapper
public interface OrderSettingDao {

    //查询此日期是否存在
    @Select("select count(*) from t_ordersetting where orderDate = #{orderDate}")
    long findCountByOrderDate(Date orderDate);

    //添加新预约
    @Insert("insert into t_ordersetting (orderDate,number,reservations) values (#{orderDate},#{number},#{reservations})")
    void add(OrderSetting orderSetting);

    //更新预约人数
    @Update("update t_ordersetting set number = #{number} where orderDate = #{orderDate}")
    void editNumberByOrderDate(OrderSetting orderSetting);

    //根据月份查询预约信息
    @Select("select * from t_ordersetting where orderDate between #{dateBegin} and #{dateEnd}")
    List<OrderSetting> getOrderSettingByMonth(Map<String, String> map);

    //根据日期查询预约信息
    @Select("select * from t_ordersetting where orderDate = #{orderDate}")
    OrderSetting findByOrderDate(Date orderDate);

    //根据日期设置已预约人数
    @Update("update t_ordersetting set reservations = #{reservations} where orderDate = #{orderDate}")
    void editReservationsByOrderDate(OrderSetting orderSetting);
}
