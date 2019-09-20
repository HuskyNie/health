package cn.itcast.service;

import cn.itcast.pojo.Member;

import java.util.List;

public interface MemberService {
    Member findByTelephone(String telephone);

    void add(Member newMember);

    List<Integer> findMemberCountByMonth(List<String> list);
}
