package cn.itcast.service.impl;

import cn.itcast.dao.OrderSettingDao;
import cn.itcast.pojo.OrderSetting;
import cn.itcast.service.OrderSettingService;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = OrderSettingService.class)
@Transactional
public class OrderSettingServiceImpl implements OrderSettingService {

    @Autowired
    private OrderSettingDao orderSettingDao;

    //处理用户上传预约文件
    public void add(List<OrderSetting> data) {
        if (data != null && data.size() > 0) {
            for (OrderSetting orderSetting : data) {
                //检查此数据日期是否存在
                long count = orderSettingDao.findCountByOrderDate(orderSetting.getOrderDate());
                if (count > 0) {
                    //已存在,更新
                    orderSettingDao.editNumberByOrderDate(orderSetting);
                } else {
                    //不存在,添加
                    orderSettingDao.add(orderSetting);
                }
            }
        }
    }

    //根据日期修改可预约人数
    public void editNumberByDate(OrderSetting orderSetting) {
        //查询当前日期是否已设置预约人数
        long count = orderSettingDao.findCountByOrderDate(orderSetting.getOrderDate());
        if (count > 0) {
            //已设置预约人数,更新设置
            orderSettingDao.editNumberByOrderDate(orderSetting);
        } else {
            //未设置预约人数,添加设置
            orderSettingDao.add(orderSetting);
        }
    }

    //根据月份获取预约信息
    public List<Map> getOrderSettingByMonth(String date) {
        //拼接调用Dao的Map数据
        String dateBegin = date + "-1";
        String dateEnd = date + "-31";
        Map<String , String> map = new HashMap<>();
        map.put("dateBegin" , dateBegin);
        map.put("dateEnd" , dateEnd);
        List<OrderSetting> list = orderSettingDao.getOrderSettingByMonth(map);
        //将查询到的数据转换格式
        List<Map> data = new ArrayList<>();
        for (OrderSetting orderSetting : list) {
            Map<String , Integer> orderSettingMap = new HashMap<>();
            orderSettingMap.put("date" , orderSetting.getOrderDate().getDate());
            orderSettingMap.put("number" , orderSetting.getNumber());
            orderSettingMap.put("reservations" , orderSetting.getReservations());
            data.add(orderSettingMap);
        }
        return data;
    }
}
