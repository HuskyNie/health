package cn.itcast.controller;

import cn.itcast.constant.MessageConstant;
import cn.itcast.entity.Result;
import cn.itcast.utils.QiniuUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/setMeal")
public class SetMealController {

    //图片上传
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
            //返回结果
            return new Result(true , MessageConstant.PIC_UPLOAD_SUCCESS , fileName);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false , MessageConstant.PIC_UPLOAD_FAIL);
        }
    }
}
