package com.powernode.crm.commons.utils;

import java.util.UUID;

/**
 * @author LXM
 * @create 2022-05-05 22:07
 */
public class UUIDUtils {
    public static String getUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
