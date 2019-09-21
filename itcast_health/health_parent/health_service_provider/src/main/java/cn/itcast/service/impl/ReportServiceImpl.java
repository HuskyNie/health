package cn.itcast.service.impl;

import cn.itcast.dao.MemberDao;
import cn.itcast.dao.OrderDao;
import cn.itcast.service.ReportService;
import cn.itcast.utils.DateUtils;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = ReportService.class)
@Transactional
public class ReportServiceImpl implements ReportService {

    @Autowired
    private MemberDao memberDao;
    @Autowired
    private OrderDao orderDao;

    /**
     * 获得运营统计数据
     * Map数据格式：
     *      todayNewMember -> number
     *      totalMember -> number
     *      thisWeekNewMember -> number
     *      thisMonthNewMember -> number
     *      todayOrderNumber -> number
     *      todayVisitsNumber -> number
     *      thisWeekOrderNumber -> number
     *      thisWeekVisitsNumber -> number
     *      thisMonthOrderNumber -> number
     *      thisMonthVisitsNumber -> number
     *      hotSetmeal -> List<Setmeal>
     */
    //获取运营数据统计方法
    public Map<String, Object> getBusinessReport() throws Exception {
        //当前日期
        String today = DateUtils.parseDate2String(DateUtils.getToday());
        //本周一日期
        String thisWeekMonday = DateUtils.parseDate2String(DateUtils.getThisWeekMonday());
        //本月第一天日期
        String firstDay4ThisMonth = DateUtils.parseDate2String(DateUtils.getFirstDay4ThisMonth());

        //今日新增会员数
        Integer todayNewMember = memberDao.findMemberCountByDate(today);
        //总会员数
        Integer totalMember = memberDao.findMemberTotalCount();
        //本周新增会员数
        Integer thisWeekNewMember = memberDao.findMemberCountAfterDate(thisWeekMonday);
        //本月新增会员数
        Integer thisMonthNewMember = memberDao.findMemberCountAfterDate(firstDay4ThisMonth);
        //今日预约数
        Integer todayOrderNumber = orderDao.findOrderCountByDate(today);
        //今日就诊数
        Integer todayVisitsNumber = orderDao.findVisitsCountByDate(today);
        //本周预约数
        Integer thisWeekOrderNumber = orderDao.findOrderCountAfterDate(thisWeekMonday);
        //本周就诊数
        Integer thisWeekVisitsNumber = orderDao.findVisitsCountAfterDate(thisWeekMonday);
        //本月预约数
        Integer thisMonthOrderNumber = orderDao.findOrderCountAfterDate(firstDay4ThisMonth);
        //本月就诊数
        Integer thisMonthVisitsNumber = orderDao.findVisitsCountAfterDate(firstDay4ThisMonth);
        //热卖套餐
        List<Map> hotSetMeal = orderDao.findHotSetmeal();

        //构造返回数据
        Map<String , Object> data = new HashMap<>();
        data.put("reportDate",today);
        data.put("todayNewMember",todayNewMember);
        data.put("totalMember",totalMember);
        data.put("thisWeekNewMember",thisWeekNewMember);
        data.put("thisMonthNewMember",thisMonthNewMember);
        data.put("todayOrderNumber",todayOrderNumber);
        data.put("thisWeekOrderNumber",thisWeekOrderNumber);
        data.put("thisMonthOrderNumber",thisMonthOrderNumber);
        data.put("todayVisitsNumber",todayVisitsNumber);
        data.put("thisWeekVisitsNumber",thisWeekVisitsNumber);
        data.put("thisMonthVisitsNumber",thisMonthVisitsNumber);
        data.put("hotSetmeal",hotSetMeal);
        return data;
    }
}
