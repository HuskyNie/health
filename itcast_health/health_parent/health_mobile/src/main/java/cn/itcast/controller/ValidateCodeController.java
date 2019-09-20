package cn.itcast.controller;

import cn.itcast.constant.MessageConstant;
import cn.itcast.constant.RedisMessageConstant;
import cn.itcast.entity.Result;
import cn.itcast.utils.SMSUtils;
import cn.itcast.utils.ValidateCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

@RestController
@RequestMapping("/validateCode")
public class ValidateCodeController {

    @Autowired
    private JedisPool jedisPool;

    @RequestMapping("/send4Order")
    public Result send4Order(String telephone) {
        Integer validateCode = ValidateCodeUtils.generateValidateCode(4); //生成4位验证码
        try {
            //发送短信
//            SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE , telephone , validateCode.toString());
        } catch (Exception e) {
            e.printStackTrace();
            //验证码发送失败
            return new Result(false , MessageConstant.SEND_VALIDATECODE_FAIL);
        }
        System.out.println("发送的验证码为" + validateCode);
        //将验证码存入redis
        jedisPool.getResource().setex(telephone + RedisMessageConstant.SENDTYPE_ORDER , 5*60 , validateCode.toString());
        return new Result(true , MessageConstant.SEND_VALIDATECODE_SUCCESS);
    }

    @RequestMapping("/send4Login")
    public Result send4Login(String telephone) {
        Integer validateCode = ValidateCodeUtils.generateValidateCode(6); //生成6位验证码
        try {
            //发送短信
//            SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE , telephone , validateCode.toString());
        } catch (Exception e) {
            e.printStackTrace();
            //验证码发送失败
            return new Result(false , MessageConstant.SEND_VALIDATECODE_FAIL);
        }
        System.out.println("发送的验证码为" + validateCode);
        //将验证码存入redis
        jedisPool.getResource().setex(telephone + RedisMessageConstant.SENDTYPE_LOGIN , 5*60 , validateCode.toString());
        return new Result(true , MessageConstant.SEND_VALIDATECODE_SUCCESS);
    }
}
