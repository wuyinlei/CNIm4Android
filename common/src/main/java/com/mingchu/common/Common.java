package com.mingchu.common;

/**
 * Created by wuyinlei on 2017/6/4.
 *
 * @function 公共的
 */

public class Common {

    /**
     * 一些不可变的永恒的参数  通常用于以下配置
     */
    public interface Constance {
        //手机号的正则 检测11位手机号
        String REGEX_MOBILE = "[1][3,4,5,7,8][0-9]{9}$";

        //基础的网络请求地址
        String API_URL = "http://192.168.0.105:8080/api/";
    }

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
