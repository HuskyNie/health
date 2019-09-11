package cn.itcast.controller;

import cn.itcast.constant.MessageConstant;
import cn.itcast.entity.PageResult;
import cn.itcast.entity.QueryPageBean;
import cn.itcast.entity.Result;
import cn.itcast.pojo.CheckItem;
import cn.itcast.service.CheckItemService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/checkItem")
public class CheckItemController {

    //通过Dubbo注入Service实现类增强类
    @Reference
    private CheckItemService checkItemService;

    //检查项新增方法
    @RequestMapping("/add")
    public Result add(@RequestBody CheckItem checkItem){
        try {
            checkItemService.add(checkItem);
        } catch (Exception e){
            e.printStackTrace();
            return new Result(false , MessageConstant.ADD_CHECKITEM_FAIL);
        }
        return new Result(true , MessageConstant.ADD_CHECKITEM_SUCCESS);
    }

    //检查项分页查询方法
    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        return checkItemService.findPage(
                //传入分页查询参数
                queryPageBean.getCurrentPage(),
                queryPageBean.getPageSize(),
                queryPageBean.getQueryString()
        );
    }

    //检查项删除方法
    @RequestMapping("/delete")
    public Result delete(Integer id){
        try {
            checkItemService.delete(id);
        } catch (RuntimeException e) {
            //当前检查项与检查组有关联,不能删除
            e.printStackTrace();
            return new Result(false , e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false , MessageConstant.DELETE_CHECKITEM_FAIL);
        }
        return new Result(true , MessageConstant.DELETE_CHECKITEM_SUCCESS);
    }

    //检查项编辑功能数据回显方法
    @RequestMapping("/findById")
    public Result findById(Integer id){
        try {
            CheckItem checkItem = checkItemService.findById(id);
            //判断查询到的数据是否为空
            if (null != checkItem){
                return new Result(true , MessageConstant.QUERY_CHECKITEM_SUCCESS , checkItem);
            }else{
                return new Result(false , MessageConstant.QUERY_CHECKITEM_FAIL);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false , MessageConstant.QUERY_CHECKITEM_FAIL);
        }
    }

    //检查项编辑功能更新方法
    @RequestMapping("/edit")
    public Result edit(@RequestBody CheckItem checkItem) {
        try {
            checkItemService.edit(checkItem);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false , MessageConstant.EDIT_CHECKITEM_FAIL);
        }
        return new Result(true , MessageConstant.EDIT_CHECKITEM_SUCCESS);
    }

    //检查组新建编辑窗口数据回显查询所有方法
    @RequestMapping("/findAll")
    public Result findAll(){
        try {
            List<CheckItem> checkItemList = checkItemService.findAll();
            if (null != checkItemList && checkItemList.size() > 0) {
                return new Result(true , MessageConstant.QUERY_CHECKITEM_SUCCESS , checkItemList);
            } else {
                return new Result(false , MessageConstant.QUERY_CHECKITEM_FAIL);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false , MessageConstant.QUERY_CHECKITEM_FAIL);
        }

    }
}
