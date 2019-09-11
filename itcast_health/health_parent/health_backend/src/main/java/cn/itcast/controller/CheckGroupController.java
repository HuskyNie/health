package cn.itcast.controller;


import cn.itcast.constant.MessageConstant;
import cn.itcast.entity.Result;
import cn.itcast.pojo.CheckGroup;
import cn.itcast.service.CheckGroupService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/checkGroup")
public class CheckGroupController {

    @Reference
    private CheckGroupService checkGroupService;

    @RequestMapping("/add")
    public Result add(@RequestBody CheckGroup checkGroup , Integer[] checkitemIds){
        try {
            checkGroupService.add(checkGroup , checkitemIds);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false , MessageConstant.ADD_CHECKGROUP_FAIL);
        }
        return new Result(true , MessageConstant.ADD_CHECKGROUP_SUCCESS);
    }
}
