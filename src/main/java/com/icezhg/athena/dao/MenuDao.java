package com.icezhg.athena.dao;

import com.icezhg.athena.domain.Menu;
import com.icezhg.athena.vo.MenuInfo;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by zhongjibing on 2022/09/07.
 */
@Repository
public interface MenuDao {

    List<MenuInfo> list(Map<String, Object> query);

    int insert(Menu menu);

    int update(Menu menu);

    MenuInfo findById(Integer id);

    int deleteById(Integer id);

    boolean hasChildren(Integer parentId);
}
