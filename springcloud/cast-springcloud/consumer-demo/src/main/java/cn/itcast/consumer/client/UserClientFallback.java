package cn.itcast.consumer.client;

import cn.itcast.consumer.pojo.User;
import org.springframework.stereotype.Component;

@Component
public class UserClientFallback implements UserClient {
    @Override
    public User queryById(Long id) {
        User user = new User();
        user.setId(id);
        user.setName("yichang");
        return user;
    }
}
