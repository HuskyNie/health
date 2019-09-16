package cn.itcast.service;

import cn.itcast.pojo.OrderSetting;

import java.util.List;
import java.util.Map;

public interface OrderSettingService {
    void add(List<OrderSetting> data);

    List<Map> getOrderSettingByMonth(String date);
}
