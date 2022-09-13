package com.icezhg.athena.dao;

import com.icezhg.athena.domain.Menu;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by zhongjibing on 2022/09/07.
 */
@Repository
public interface MenuDao {
    List<Menu> findByUserId(Long currentUserId);

    List<Menu> listAll();
}
