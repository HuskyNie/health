package cn.itcast.controller;

import cn.itcast.constant.MessageConstant;
import cn.itcast.entity.Result;
import cn.itcast.pojo.OrderSetting;
import cn.itcast.service.OrderSettingService;
import cn.itcast.utils.POIUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ordersetting")
public class OrderSettingController {

    @Reference
    private OrderSettingService orderSettingService;

    //文件上传,实现预约设置数据批量导入
    @RequestMapping("/upload")
    public Result upload(@RequestParam("excelFile") MultipartFile excelFile){
        try {
            //读取用户上传的excel文档
            List<String[]> list = POIUtils.readExcel(excelFile);
            //转换数据格式
            ArrayList<OrderSetting> data = new ArrayList<>();
            for (String[] strings : list) {
                OrderSetting orderSetting = new OrderSetting(new Date(strings[0]), Integer.parseInt(strings[1]));
                data.add(orderSetting);
            }
            orderSettingService.add(data);
        } catch (IOException e) {
            e.printStackTrace();
            return new Result(false , MessageConstant.IMPORT_ORDERSETTING_FAIL);
        }
        return new Result(true , MessageConstant.IMPORT_ORDERSETTING_SUCCESS);
    }

    @RequestMapping("/getOrderSettingByMonth")
    public Result getOrderSettingByMonth(String date) {
        try {
            List<Map> list = orderSettingService.getOrderSettingByMonth(date);
            return new Result(true , MessageConstant.GET_ORDERSETTING_SUCCESS , list);
        }catch (Exception e) {
            e.printStackTrace();
            return new Result(false , MessageConstant.GET_ORDERSETTING_FAIL);
        }
    }
}
