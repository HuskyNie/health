package cn.itcast.service;

import cn.itcast.pojo.Member;

public interface MemberService {
    Member findByTelephone(String telephone);

    void add(Member newMember);
}
