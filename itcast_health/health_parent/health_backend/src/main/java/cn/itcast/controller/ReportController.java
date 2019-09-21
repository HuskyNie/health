package cn.itcast.controller;

import cn.itcast.constant.MessageConstant;
import cn.itcast.entity.Result;
import cn.itcast.service.MemberService;
import cn.itcast.service.ReportService;
import cn.itcast.service.SetMealService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/report")
public class ReportController {

    @Reference
    private MemberService memberService;
    @Reference
    private SetMealService setMealService;
    @Reference
    private ReportService reportService;

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

    @RequestMapping("/getBusinessReportData")
    public Result getBusinessReportData(){
        try {
            Map<String , Object> map = reportService.getBusinessReport();
            return new Result(true , MessageConstant.GET_BUSINESS_REPORT_SUCCESS , map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false , MessageConstant.GET_BUSINESS_REPORT_FAIL);
        }
    }

    @RequestMapping("/exportBusinessReport")
    public Result exportBusinessReport(HttpServletRequest request , HttpServletResponse response){
        try {
            Map<String , Object> data = reportService.getBusinessReport();
            //动态获取模板Excel文件绝对路径
            String templateRealPath = request.getSession().getServletContext().getRealPath("template") + File.separator + "report_template.xlsx";

            //取出返回结果数据，准备将报表数据写入到Excel文件中
            String reportDate = (String) data.get("reportDate");
            Integer todayNewMember = (Integer) data.get("todayNewMember");
            Integer totalMember = (Integer) data.get("totalMember");
            Integer thisWeekNewMember = (Integer) data.get("thisWeekNewMember");
            Integer thisMonthNewMember = (Integer) data.get("thisMonthNewMember");
            Integer todayOrderNumber = (Integer) data.get("todayOrderNumber");
            Integer thisWeekOrderNumber = (Integer) data.get("thisWeekOrderNumber");
            Integer thisMonthOrderNumber = (Integer) data.get("thisMonthOrderNumber");
            Integer todayVisitsNumber = (Integer) data.get("todayVisitsNumber");
            Integer thisWeekVisitsNumber = (Integer) data.get("thisWeekVisitsNumber");
            Integer thisMonthVisitsNumber = (Integer) data.get("thisMonthVisitsNumber");
            List<Map> hotSetmeal = (List<Map>) data.get("hotSetmeal");

            //读取模板文件创建Excel表格对象
            XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(new File(templateRealPath)));
            XSSFSheet sheet = workbook.getSheetAt(0);

            //将数据写入Excel表格对象
            XSSFRow row = sheet.getRow(2);
            row.getCell(5).setCellValue(reportDate);//日期

            row = sheet.getRow(4);
            row.getCell(5).setCellValue(todayNewMember);//新增会员数（本日）
            row.getCell(7).setCellValue(totalMember);//总会员数

            row = sheet.getRow(5);
            row.getCell(5).setCellValue(thisWeekNewMember);//本周新增会员数
            row.getCell(7).setCellValue(thisMonthNewMember);//本月新增会员数

            row = sheet.getRow(7);
            row.getCell(5).setCellValue(todayOrderNumber);//今日预约数
            row.getCell(7).setCellValue(todayVisitsNumber);//今日到诊数

            row = sheet.getRow(8);
            row.getCell(5).setCellValue(thisWeekOrderNumber);//本周预约数
            row.getCell(7).setCellValue(thisWeekVisitsNumber);//本周到诊数

            row = sheet.getRow(9);
            row.getCell(5).setCellValue(thisMonthOrderNumber);//本月预约数
            row.getCell(7).setCellValue(thisMonthVisitsNumber);//本月到诊数

            int rowNum = 12;
            for(Map map : hotSetmeal){//热门套餐
                String name = (String) map.get("name");
                Long setmeal_count = (Long) map.get("setmeal_count");
                BigDecimal proportion = (BigDecimal) map.get("proportion");
                row = sheet.getRow(rowNum ++);
                row.getCell(4).setCellValue(name);//套餐名称
                row.getCell(5).setCellValue(setmeal_count);//预约数量
                row.getCell(6).setCellValue(proportion.doubleValue());//占比
            }

            //通过输出流完成文件下载
            ServletOutputStream outputStream = response.getOutputStream();
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("context-Disposition" , "attachment;filename=report.xlsx");
            workbook.write(outputStream);

            outputStream.flush();
            outputStream.close();
            workbook.close();

            return new Result(true , MessageConstant.GET_BUSINESS_REPORT_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false , MessageConstant.GET_BUSINESS_REPORT_FAIL);
        }
    }
}
