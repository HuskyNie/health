package cn.itcast.controller;

import cn.itcast.constant.MessageConstant;
import cn.itcast.constant.RedisConstant;
import cn.itcast.entity.PageResult;
import cn.itcast.entity.QueryPageBean;
import cn.itcast.entity.Result;
import cn.itcast.pojo.Setmeal;
import cn.itcast.service.SetMealService;
import cn.itcast.utils.QiniuUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.JedisPool;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/setMeal")
public class SetMealController {

    @Reference
    private SetMealService setMealService;
    @Autowired
    private JedisPool jedisPool;

    //图片上传
    @PreAuthorize("hasAuthority('SETMEAL_ADD')")
    @RequestMapping("/upload")
    public Result upload(@RequestParam("imgFile")MultipartFile imgFile) {
        try {
            //获取原始文件名
            String originalFilename = imgFile.getOriginalFilename();
            //获取原始文件后缀
            assert originalFilename != null;
            int lastIndexOf = originalFilename.lastIndexOf(".");
            String suffix = originalFilename.substring(lastIndexOf - 1);
            //使用UUID随机产生文件名,添加文件后缀
            String fileName = UUID.randomUUID().toString() + suffix;
            //上传图片
            QiniuUtils.upload2Qiniu(imgFile.getBytes() , fileName);
            //将上传图片名称存入Redis，基于Redis的Set集合存储
            jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_RESOURCES,fileName);
            //返回结果
            return new Result(true , MessageConstant.PIC_UPLOAD_SUCCESS , fileName);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false , MessageConstant.PIC_UPLOAD_FAIL);
        }
    }

    //新增方法
    @PreAuthorize("hasAuthority('SETMEAL_ADD')")
    @RequestMapping("/add")
    public Result add(@RequestBody Setmeal setmeal , Integer[] checkGroupIds){
        try {
            setMealService.add(setmeal , checkGroupIds);
            return  new Result(true , MessageConstant.ADD_SETMEAL_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false , MessageConstant.ADD_SETMEAL_FAIL);
        }
    }

    //分页查询方法
    @PreAuthorize("hasAuthority('SETMEAL_QUERY')")
    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {
        return setMealService.findPage(
                queryPageBean.getCurrentPage(),
                queryPageBean.getPageSize(),
                queryPageBean.getQueryString()
        );
    }

    //删除方法
    @PreAuthorize("hasAuthority('SETMEAL_DELETE')")
    @RequestMapping("/delete")
    public Result delete(Integer id) {
        try {
            setMealService.delete(id);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false , "删除体检套餐失败");
        }
        return new Result(true , "删除体检套餐成功");
    }

    //编辑数据回显方法
    @PreAuthorize("hasAuthority('SETMEAL_QUERY')")
    @RequestMapping("/findById")
    public Result findById(Integer id){
        try {
            Setmeal setmeal = setMealService.findById(id);
            return new Result(true , MessageConstant.QUERY_SETMEAL_SUCCESS , setmeal);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false , MessageConstant.QUERY_SETMEAL_FAIL);
        }
    }

    //根据套餐id查询关联检查组方法
    @PreAuthorize("hasAuthority('SETMEAL_QUERY')")
    @RequestMapping("/findCheckGroupIdsBySetMealId")
    public Result findCheckGroupIdsBySetMealId(Integer id) {
        try {
            List<Integer> checkGroupIds = setMealService.findCheckGroupIdsBySetMealId(id);
            return new Result(true , MessageConstant.QUERY_CHECKGROUP_SUCCESS , checkGroupIds);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false , MessageConstant.QUERY_CHECKGROUP_FAIL);
        }
    }

    //编辑数据提交方法
    @PreAuthorize("hasAuthority('SETMEAL_EDIT')")
    @RequestMapping("/update")
    public Result update(@RequestBody Setmeal setmeal , Integer[] checkGroupIds) {
        try {
            setMealService.update(setmeal , checkGroupIds);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false , "编辑套餐失败");
        }
        return new Result(true , "编辑套餐成功");
    }
}
