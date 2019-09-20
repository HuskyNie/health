package cn.itcast.controller;

import cn.itcast.constant.MessageConstant;
import cn.itcast.constant.RedisMessageConstant;
import cn.itcast.entity.Result;
import cn.itcast.pojo.Member;
import cn.itcast.service.MemberService;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/member")
public class MemberController {
    @Autowired
    private JedisPool jedisPool;
    @Reference
    private MemberService memberService;
    //1、校验用户输入的短信验证码是否正确，如果验证码错误则登录失败
    //2、如果验证码正确，则判断当前用户是否为会员，如果不是会员则自动完成会员注册
    //3、向客户端写入Cookie，内容为用户手机号
    //4、将会员信息保存到Redis，使用手机号作为key，保存时长为30分钟
    @RequestMapping("/login")
    public Result login(HttpServletResponse response , @RequestBody Map map){
        String telephone = (String) map.get("telephone");
        String validateCode = (String) map.get("validateCode");
        //从Redis中获取缓存的验证码
        String codeInRedis = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_LOGIN);
        if (codeInRedis == null || !codeInRedis.equals(validateCode)){
            //验证码错误
            return new Result(false , MessageConstant.VALIDATECODE_ERROR);
        }else {
            //验证码正确
            //判断是否是会员
            Member member = memberService.findByTelephone(telephone);
            if (member == null){
                //当前用户不是会员,自动注册
                Member newMember = new Member();
                newMember.setPhoneNumber(telephone);
                newMember.setRegTime(new Date());
                memberService.add(newMember);
                //保存用户信息到Redis中
                String json = JSON.toJSON(newMember).toString();
                jedisPool.getResource().setex(telephone , 60*30 , json);
            } else {
                //保存用户信息到Redis中
                String json = JSON.toJSON(member).toString();
                jedisPool.getResource().setex(telephone , 60*30 , json);
            }
            //登陆成功
            //写入Cookie,跟踪用户
            Cookie cookie = new Cookie("login_member_telephone", telephone);
            cookie.setPath("/");
            cookie.setMaxAge(60*60*24*30);
            response.addCookie(cookie);
            return new Result(true , MessageConstant.LOGIN_SUCCESS);
        }
    }
}
