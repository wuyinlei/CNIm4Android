package com.mingchu.factory.model.db;

import com.raizlabs.android.dbflow.annotation.Database;

/**
 * Created by wuyinlei on 2017/6/11.
 *
 * 数据库的基本信息
 */
@Database(name = AppDatabase.NAME,version = AppDatabase.VERSION)
public class AppDatabase {

    public static final String NAME = "AppDatabase";

    public static final  int VERSION = 1;

}
