package cn.itcast.consumer.controller;

import cn.itcast.consumer.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequestMapping("/consumer")
public class ConsumerController {

    @Autowired
    private DiscoveryClient discoveryClient;
    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("{id}")
    public User queryById(@PathVariable Long id){
        String url = "http://localhost:9091/user/" + id;
        List<ServiceInstance> instances = discoveryClient.getInstances("user-service");
        ServiceInstance serviceInstance = instances.get(0);
        url = "http://" + serviceInstance.getHost() + ":" + serviceInstance.getPort() + "/user/" + id;
        return restTemplate.getForObject(url, User.class);
    }

}
