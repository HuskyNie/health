package cn.itcast.controller;

import cn.itcast.constant.MessageConstant;
import cn.itcast.entity.Result;
import cn.itcast.service.MemberService;
import cn.itcast.service.SetMealService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/report")
public class ReportController {

    @Reference
    private MemberService memberService;
    @Reference
    private SetMealService setMealService;

    @RequestMapping("/getMemberReport")
    public Result getMemberReport(){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH , -12); //获取当前日期之前12个月的日期

        List<String> list = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            calendar.add(Calendar.MONTH , 1);
            list.add(new SimpleDateFormat("yyyy.MM").format(calendar.getTime()));
        }

        Map<String, Object> map = new HashMap<>();
        map.put("months" , list);

        List<Integer> memberCount = memberService.findMemberCountByMonth(list);
        map.put("memberCount" , memberCount);

        return new Result(true , MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS , map);
    }

    @RequestMapping("/getSetMealReport")
    public Result getSetMealReport(){
        try {
            Map<String , Object> data = new HashMap<>();
            List<Map<String , Object>> setMealCount = setMealService.findSetMealCount();
            data.put("setmealCount" , setMealCount);
            List<String> setMealNames = new ArrayList<>();
            for (Map<String, Object> map : setMealCount) {
                String name = (String)map.get("name");
                setMealNames.add(name);
            }
            data.put("setmealNames" , setMealNames);
            return new Result(true , MessageConstant.GET_SETMEAL_COUNT_REPORT_SUCCESS , data);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false , MessageConstant.GET_SETMEAL_COUNT_REPORT_FAIL);
        }
    }
}
