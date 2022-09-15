package com.icezhg.athena.util;

import java.util.UUID;

/**
 * Created by zhongjibing on 2022/09/15.
 */
public class IdGenerator {

    public static String generateId() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
