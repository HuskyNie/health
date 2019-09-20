package cn.itcast.controller;

import cn.itcast.constant.MessageConstant;
import cn.itcast.entity.Result;
import cn.itcast.pojo.Setmeal;
import cn.itcast.service.SetMealService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/setMeal")
public class SetMealController {

    @Reference
    private SetMealService setMealService;

    @RequestMapping("/getAllSetMeal")
    public Result getAllSetMeal() {
        try{
            List<Setmeal> list = setMealService.findAll();
            return new Result(true, MessageConstant.GET_SETMEAL_LIST_SUCCESS,list);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(true,MessageConstant.GET_SETMEAL_LIST_FAIL);
        }
    }

    @RequestMapping("/findById")
    public Result findById(Integer id) {
        try {
            Setmeal setmeal = setMealService.findById(id);
            return new Result(true , MessageConstant.QUERY_SETMEAL_SUCCESS , setmeal);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false , MessageConstant.QUERY_SETMEAL_FAIL);
        }
    }
}
