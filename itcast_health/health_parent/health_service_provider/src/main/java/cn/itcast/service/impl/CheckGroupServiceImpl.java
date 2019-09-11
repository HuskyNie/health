package cn.itcast.service.impl;

import cn.itcast.dao.CheckGroupDao;
import cn.itcast.entity.PageResult;
import cn.itcast.pojo.CheckGroup;
import cn.itcast.service.CheckGroupService;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

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

    //分页查询
    public PageResult findPage(Integer currentPage, Integer pageSize, String queryString) {
        PageHelper.startPage(currentPage , pageSize);
        Page<CheckGroup> page = checkGroupDao.selectByCondition(queryString);
        return new PageResult(page.getTotal() , page.getResult());
    }

    //基本数据回显
    public CheckGroup findById(Integer id) {
        return checkGroupDao.findById(id);
    }

    //当前检查组关联检查项数据回显
    public List<Integer> findCheckItemIdsByCheckGroupId(Integer id) {
        return checkGroupDao.findCheckItemIdsByCheckGroupId(id);
    }

    //更新
    public void update(CheckGroup checkGroup, Integer[] checkItemIds) {
        //更新基本信息
        checkGroupDao.updateCheckGroup(checkGroup);
        //更新中间表关联关系
        if (checkItemIds != null && checkItemIds.length > 0){
            //清空中间表关联关系
            checkGroupDao.deleteAssociation(checkGroup.getId());
            //重新设置中间表关联关系
            for (Integer checkItemId : checkItemIds) {
                //为了方便dao层获取多个数据,封装为map传递
                HashMap<String, Integer> map = new HashMap<>();
                map.put("checkgroup_id" , checkGroup.getId());
                map.put("checkitem_id" , checkItemId);
                checkGroupDao.setCheckGroupAssociationWithCheckItem(map);
            }
        }
    }
}
