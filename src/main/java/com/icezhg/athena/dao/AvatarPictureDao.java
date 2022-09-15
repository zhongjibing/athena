package com.icezhg.athena.dao;

import com.icezhg.athena.domain.AvatarPicture;
import org.springframework.stereotype.Repository;

/**
 * Created by zhongjibing on 2022/09/07.
 */
@Repository
public interface AvatarPictureDao {

    int create(AvatarPicture record);

    int update(AvatarPicture record);
}
