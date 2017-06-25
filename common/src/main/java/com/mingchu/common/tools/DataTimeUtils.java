package com.mingchu.common.tools;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by wuyinlei on 2017/6/25.
 */

public class DataTimeUtils {

    private static final SimpleDateFormat FORMAT = new SimpleDateFormat("yy-MM-dd", Locale.ENGLISH);

    /**
     * 获取一个简单的时间字符串
     * @param date  date
     * @return string
     */
    public static String getSampleDate(Date date){
        return FORMAT.format(date);
    }
}
