package cn.itcast.service.impl;

import cn.itcast.dao.CheckGroupDao;
import cn.itcast.pojo.CheckGroup;
import cn.itcast.service.CheckGroupService;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;

@Service(interfaceClass = CheckGroupService.class)
@Transactional
public class CheckGroupServiceImpl implements CheckGroupService {

    @Autowired
    private CheckGroupDao checkGroupDao;

    //添加检查组,设置检查项与检查组关联关系
    public void add(CheckGroup checkGroup , Integer[] checkitemIds) {
        checkGroupDao.add(checkGroup);
        if (checkitemIds != null && checkitemIds.length > 0) {
            for (Integer checkitemId : checkitemIds) {
                //为了方便dao层获取多个数据,封装为map传递
                HashMap<String, Integer> map = new HashMap<>();
                map.put("checkgroup_id" , checkGroup.getId());
                map.put("checkitem_id" , checkitemId);
                checkGroupDao.setCheckGroupAssociationWithCheckItem(map);
            }
        }
    }
}
