package com.icezhg.athena.domain;

import com.icezhg.athena.util.IdGenerator;

/**
 * Created by zhongjibing on 2022/09/07.
 */
public record AvatarPicture(
        String avatar,
        String pictureId
) {

    public AvatarPicture(String pictureId) {
        this(IdGenerator.generateId(), pictureId);
    }
}
