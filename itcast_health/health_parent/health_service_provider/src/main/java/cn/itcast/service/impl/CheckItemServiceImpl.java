package cn.itcast.service.impl;

import cn.itcast.dao.CheckItemDao;
import cn.itcast.entity.PageResult;
import cn.itcast.pojo.CheckItem;
import cn.itcast.service.CheckItemService;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Service(interfaceClass = CheckItemService.class)
@Transactional
public class CheckItemServiceImpl implements CheckItemService {

    @Autowired
    private CheckItemDao checkItemDao;

    public void add(CheckItem checkItem) {
        checkItemDao.add(checkItem);
    }

    public void edit(CheckItem checkItem) {
        checkItemDao.update(checkItem);
    }

    public CheckItem findById(Integer id) {
        return checkItemDao.findById(id);
    }

    public void delete(Integer id) {
        //删除检查项之前应该先判断此检查项是否与检查组关联,如果已经关联则不允许删除
        long count = checkItemDao.findCountByCheckItemId(id);
        if (count > 0){
            //当前检查项与检查组有关联,不能删除
            throw new RuntimeException("当前检查项被引用,不能删除");
        }
        checkItemDao.deleteById(id);
    }

    public PageResult findPage(Integer currentPage, Integer pageSize, String queryString) {
        PageHelper.startPage(currentPage , pageSize);
        Page<CheckItem> page =  checkItemDao.selectByCondition(queryString);
        return new PageResult(page.getTotal() , page.getResult());
    }
}
