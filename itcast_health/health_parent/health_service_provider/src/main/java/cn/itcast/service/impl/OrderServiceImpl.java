package cn.itcast.service.impl;

import cn.itcast.constant.MessageConstant;
import cn.itcast.dao.MemberDao;
import cn.itcast.dao.OrderDao;
import cn.itcast.dao.OrderSettingDao;
import cn.itcast.entity.Result;
import cn.itcast.pojo.Member;
import cn.itcast.pojo.Order;
import cn.itcast.pojo.OrderSetting;
import cn.itcast.service.OrderService;
import cn.itcast.utils.DateUtils;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = OrderService.class)
@Transactional
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderSettingDao orderSettingDao;
    @Autowired
    private MemberDao memberDao;
    @Autowired
    private OrderDao orderDao;

    //体检预约
    public Result order(Map map) throws Exception {
        //1、检查用户所选择的预约日期是否已经提前进行了预约设置，如果没有设置则无法进行预约
        Date orderDate = DateUtils.parseString2Date((String) map.get("orderDate"));
        OrderSetting orderSetting = orderSettingDao.findByOrderDate(orderDate);
        if (orderSetting == null) {
            return new Result(false, MessageConstant.SELECTED_DATE_CANNOT_ORDER);
        }

        //2、检查用户所选择的预约日期是否已经约满，如果已经约满则无法预约
        int number = orderSetting.getNumber(); //可预约人数
        int reservations = orderSetting.getReservations(); //已预约人数
        if (reservations > number) {
            //已约满,无法预约
            return new Result(false, MessageConstant.ORDER_FULL);
        }

        //4、检查当前用户是否为会员，如果是会员则直接完成预约，如果不是会员则自动完成注册并进行预约
        Member member = memberDao.findByTelephone((String) map.get("telephone"));
        if (member != null) {
            //3、检查用户是否重复预约（同一个用户在同一天预约了同一个套餐），如果是重复预约则无法完成再次预约
            Integer id = member.getId();
            int setmealId = Integer.parseInt((String) map.get("setmealId"));
            List<Order> orders = orderDao.findByCondition(new Order(id, orderDate, null, null, setmealId));
            if (orders != null && orders.size() > 0) {
                //是重复预约
                return new Result(false, MessageConstant.HAS_ORDERED);
            }
        }
        //当前用户不是会员,注册为会员
        Member newMember = new Member();
        newMember.setName((String) map.get("name"));
        newMember.setPhoneNumber((String) map.get("telephone"));
        newMember.setIdCard((String) map.get("idCard"));
        newMember.setSex((String) map.get("sex"));
        newMember.setRegTime(new Date());
        memberDao.add(newMember);
        //保存预约信息到预约表
        Order order = new Order(newMember.getId(),
                orderDate,
                (String) map.get("orderType"),
                Order.ORDERSTATUS_NO,
                Integer.parseInt((String) map.get("setmealId")));
        orderDao.add(order);
        //预约设置表预约人数加1
        orderSetting.setReservations(orderSetting.getReservations() + 1);
        orderSettingDao.editReservationsByOrderDate(orderSetting);
        return new Result(true, MessageConstant.ORDER_SUCCESS, order.getId());
    }

    //根据id查询预约信息方法
    public Map findById(Integer id) throws Exception {
        Map map = orderDao.findById4Detail(id);
        if (map != null) {
            Date orderDate = (Date) map.get("orderDate");
            map.put("orderDate" , DateUtils.parseDate2String(orderDate));
        }
        return map;
    }
}
