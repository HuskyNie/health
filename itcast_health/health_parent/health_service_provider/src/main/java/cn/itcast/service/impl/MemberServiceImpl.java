package cn.itcast.service.impl;

import cn.itcast.dao.MemberDao;
import cn.itcast.pojo.Member;
import cn.itcast.service.MemberService;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Service(interfaceClass = MemberService.class)
@Transactional
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberDao memberDao;

    //根据手机号查询用户方法
    public Member findByTelephone(String telephone) {
        return memberDao.findByTelephone(telephone);
    }

    //新建用户
    public void add(Member member) {
        memberDao.add(member);
    }
}
