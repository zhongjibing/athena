package com.icezhg.athena.util;

import com.icezhg.athena.domain.User;
import com.icezhg.authorization.core.SecurityUtil;
import org.apache.commons.lang3.StringUtils;

public class MaskSensitiveUtil {

    public static String maskMobile(String mobile) {
        if (User.isRoot(SecurityUtil.currentUserId())) {
            return mobile;
        }

        if (StringUtils.isEmpty(mobile)) {
            return mobile;
        }
        if (mobile.length() <= 4) {
            return "*******" + mobile.substring(mobile.length() - 1);
        }
        if (mobile.length() <= 7) {
            return mobile.charAt(0) + "****" + mobile.substring(mobile.length() - 3);
        }
        return mobile.substring(0, 3) + "****" + mobile.substring(mobile.length() - 4);
    }

}
