package com.mingchu.common;

/**
 * Created by wuyinlei on 2017/6/4.
 */

public class Common {

    public static class Path {
        public static String getExt(String path, String defaultExt) {
            int extIndex = path.lastIndexOf(".");
            String ext = extIndex >= 0 ? path.substring(extIndex + 1).toLowerCase() : defaultExt;
            if ("0".equalsIgnoreCase(ext))
                ext = defaultExt;
            return ext;
        }
    }
}
