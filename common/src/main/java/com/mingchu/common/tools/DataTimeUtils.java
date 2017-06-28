package com.mingchu.common.tools;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by wuyinlei on 2017/6/25.
 */

public class DataTimeUtils {

    //  06-28 12:30  显示的时间格式
    private static final SimpleDateFormat FORMAT = new SimpleDateFormat("MM-dd HH:mm", Locale.ENGLISH);

    /**
     * 获取一个简单的时间字符串
     * @param date  date
     * @return string
     */
    public static String getSampleDate(Date date){
        return FORMAT.format(date);
    }
}
