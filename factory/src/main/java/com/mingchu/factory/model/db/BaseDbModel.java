package com.mingchu.factory.model.db;

import com.mingchu.factory.utils.DiffUiDataCallback;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by wuyinlei on 2017/6/22.
 *
 * 我们app基础的basemodel  继承的是数据库DbFolw的基础类 同时定义我们需要的方法
 */

public abstract class BaseDbModel<Model> extends BaseModel implements DiffUiDataCallback.UiDataDiffer<Model>{
}
