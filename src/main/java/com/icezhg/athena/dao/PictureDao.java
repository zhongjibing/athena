package com.icezhg.athena.dao;

import com.icezhg.athena.domain.Picture;
import com.icezhg.athena.vo.NameQuery;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by zhongjibing on 2022/09/10.
 */
@Repository
public interface PictureDao {

    Picture findById(String id);

    int deleteById(String id);

    int insert(Picture record);

    int update(Picture record);

    int count(NameQuery query);

    List<Picture> find(NameQuery query);

    Picture findByAvatar(String avatar);
}
