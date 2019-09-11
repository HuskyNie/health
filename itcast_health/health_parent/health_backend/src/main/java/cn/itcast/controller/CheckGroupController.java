package cn.itcast.controller;


import cn.itcast.constant.MessageConstant;
import cn.itcast.entity.PageResult;
import cn.itcast.entity.QueryPageBean;
import cn.itcast.entity.Result;
import cn.itcast.pojo.CheckGroup;
import cn.itcast.service.CheckGroupService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/checkGroup")
public class CheckGroupController {

    @Reference
    private CheckGroupService checkGroupService;

    //新增方法
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

    //分页/条件查询方法
    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
            PageResult pageResult = checkGroupService.findPage(
                    queryPageBean.getCurrentPage(),
                    queryPageBean.getPageSize(),
                    queryPageBean.getQueryString()
            );
            return pageResult;
    }

    //基本信息数据回显方法
    @RequestMapping("/findById")
    public Result findById(Integer id) {
        try {
            CheckGroup checkGroup = checkGroupService.findById(id);
            return new Result(true , MessageConstant.QUERY_CHECKGROUP_SUCCESS , checkGroup);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false , MessageConstant.QUERY_CHECKGROUP_FAIL);
        }
    }

    //当前检查组关联检查项数据回显方法
    @RequestMapping("/findCheckItemIdsByCheckGroupId")
    public Result findCheckItemIdsByCheckGroupId(Integer id) {
        try {
            List<Integer> checkItemIds = checkGroupService.findCheckItemIdsByCheckGroupId(id);
            return new Result(true , MessageConstant.QUERY_CHECKITEM_SUCCESS , checkItemIds);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false , MessageConstant.QUERY_CHECKITEM_FAIL);
        }
    }

    //更新方法
    @RequestMapping("/update")
    public Result update(@RequestBody CheckGroup checkGroup , Integer[] checkItemIds) {
        try {
            checkGroupService.update(checkGroup , checkItemIds);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false , MessageConstant.EDIT_CHECKGROUP_FAIL);
        }
        return new Result(true , MessageConstant.EDIT_CHECKGROUP_SUCCESS);
    }
}
