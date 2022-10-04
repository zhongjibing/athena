package com.icezhg.athena.service;

import com.icezhg.athena.dao.MenuDao;
import com.icezhg.athena.vo.MenuInfo;
import com.icezhg.athena.vo.MenuQuery;
import com.icezhg.athena.vo.Router;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by zhongjibing on 2022/09/20.
 */
@Service
public class RouterService {

    private MenuDao menuDao;

    @Autowired
    public void setMenuDao(MenuDao menuDao) {
        this.menuDao = menuDao;
    }

    public List<Router> buildRouters() {
        MenuQuery query = new MenuQuery();
        query.setStatus("0");
        query.setTypes(List.of("M", "C"));
        List<MenuInfo> menuInfos = menuDao.list(query.toMap());
        return null;

    }
}
