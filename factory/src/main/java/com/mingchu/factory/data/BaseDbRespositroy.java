package com.mingchu.factory.data;

import android.support.annotation.NonNull;

import com.mingchu.common.factory.data.DataSource;
import com.mingchu.common.factory.data.DbDataSource;
import com.mingchu.common.tools.CollectionUtil;
import com.mingchu.factory.data.helper.DbHelper;
import com.mingchu.factory.model.db.BaseDbModel;
import com.mingchu.factory.model.db.User;
import com.mingchu.factory.persistence.Account;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.raizlabs.android.dbflow.structure.database.transaction.QueryTransaction;
import com.raizlabs.android.dbflow.structure.provider.ContentUtils;

import net.qiujuer.genius.kit.reflect.Reflector;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by wuyinlei on 2017/6/22.
 * <p>
 * 基础的数据库仓库
 */

public abstract class BaseDbRespositroy<Data extends BaseDbModel<Data>> implements DbDataSource<Data>,
        DbHelper.ChangedListener<Data> ,QueryTransaction.QueryResultListCallback<Data>{

    //和presenter交互的回调
    private SuccessCallback<List<Data>> mCallback;

    final protected LinkedList<Data> mDatas = new LinkedList<>();  //当前缓存的数据

    private Class<Data> mDataClass;  //当前泛型对应的真实的信息

    public BaseDbRespositroy() {
        //拿到当前类的类型信息
        Type[] types = Reflector.getActualTypeArguments(BaseDbRespositroy.class,this.getClass());
        mDataClass = (Class<Data>) types[0];
    }

    @Override
    public void load(SuccessCallback<List<Data>> callback) {
        this.mCallback = callback;

        //进行数据库监听
        registerDbChangedListener();
    }

    @Override
    public void dispose() {
        this.mCallback = null;
        //取消监听  销毁数据
        DbHelper.removeChangedListener(mDataClass,this);
        mDatas.clear();
    }

    /**
     * 数据库统一删除的地方
     */
    @Override
    public void onDataDelete(Data... datas) {
        //当数据删除  再删除情况下 不用进行判断
        boolean isChanged = false;

        for (Data data : datas) {
            boolean remove = mDatas.remove(data);
            if (remove)
                isChanged = true;
        }

        if (isChanged)
            notifyDataChange();
    }

    /**
     * 数据库统一通知的地方
     */
    @Override
    public void onDataSave(Data... datas) {
        boolean isChanged = false;
        //当数据库变更的操作
        for (Data data : datas) {
            //是我关注的人 同时不是我自己
            if (isRequired(data)) {
                insertOrUpdate(data);
                isChanged = true;
            }
        }

        if (isChanged) {
            notifyDataChange();
        }
    }

    /**
     * DbFlow框架的通知
     */
    @Override
    public void onListQueryResult(QueryTransaction transaction, @NonNull List<Data> tResult) {

        //数据库加载数据成功
        if (tResult.size() == 0){
            mDatas.clear();
            notifyDataChange();
            return;
        }

        Data[] datas = CollectionUtil.toArray(tResult,mDataClass);
        onDataSave(datas);

    }

    /**
     * 通知界面刷新的方法
     */
    private void notifyDataChange() {
        SuccessCallback<List<Data>> callback = this.mCallback;
        if (callback != null)
            callback.onDataLoaded(mDatas);
    }

    //插入或者更新
    protected void insertOrUpdate(Data data) {
        int index = indexOf(data);
        if (index >= 0) {
            replace(index, data);
        } else {
            insert(data);
        }
    }

    //更新操作
    private void replace(int index, Data data) {
        mDatas.remove(index);
        mDatas.add(index, data);
    }

    protected void insert(Data data) {
        mDatas.add(data);
    }


    //查询一个数据是否在当前缓存的数据中  如果是则返回坐标
    private int indexOf(Data newData) {
        int index = -1;
//        boolean index = users.contains(user);
        for (Data user1 : mDatas) {
            index++;
            if (user1.isSame(newData)) {
                return index;
            }
        }
        return -1;
    }

    /**
     * 添加数据库的监听操作
     */
    protected void registerDbChangedListener(){
        DbHelper.addChangedListener(mDataClass,this);
    }

    protected abstract boolean isRequired(Data data) ;
}
