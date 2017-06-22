package com.mingchu.factory.presenter.contact;

import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v7.util.DiffUtil;

import com.mingchu.common.factory.data.DataSource;
import com.mingchu.common.factory.presenter.BaseContract;
import com.mingchu.common.factory.presenter.BasePresenter;
import com.mingchu.common.factory.presenter.BaseRecyclerPresenter;
import com.mingchu.common.widget.recycler.RecyclerAdapter;
import com.mingchu.factory.data.helper.UserHelper;
import com.mingchu.factory.data.user.ContactDataSource;
import com.mingchu.factory.data.user.ContactRespository;
import com.mingchu.factory.model.card.UserCard;
import com.mingchu.factory.model.db.AppDatabase;
import com.mingchu.factory.model.db.User;
import com.mingchu.factory.persistence.Account;
import com.mingchu.factory.presenter.BaseSourcePresenter;
import com.mingchu.factory.utils.DiffUiDataCallback;
import com.raizlabs.android.dbflow.config.DatabaseDefinition;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import com.raizlabs.android.dbflow.structure.database.transaction.ITransaction;
import com.raizlabs.android.dbflow.structure.database.transaction.QueryTransaction;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by wuyinlei on 2017/6/14.
 * <p>
 * 联系人的Presenter
 */

public class ContactPresenter extends BaseSourcePresenter<User,User,ContactDataSource,ConactContract.View>
        implements ConactContract.Presenter, DataSource.SuccessCallback<List<User>> {


    public ContactPresenter(ConactContract.View view) {
        super(view, new ContactRespository());
    }

    @Override
    public void start() {
        super.start();

        UserHelper.refreshContact();

//
//        final List<User> users = new ArrayList<User>();
//        for (UserCard data : datas) {
//            users.add(data.build());
//        }
//
//        //第三中存储  放到事务中
//        DatabaseDefinition definition = FlowManager.getDatabase(AppDatabase.class);
//        definition.beginTransactionAsync(new ITransaction() {
//            @Override
//            public void execute(DatabaseWrapper databaseWrapper) {
//                FlowManager.getModelAdapter(User.class)
//                        .saveAll(users);
//            }
//        }).build().execute();
//
//        //网络的数据   我们需要直接刷新到界面
//
////                getView().getRecyclerViewAadpter().replace(users);
////                getView().onAdapterDataChanged();
//        List<User> olds = getView().getRecyclerViewAadpter().getItems();
//
//
//        diff(olds, users);

        // TODO: 2017/6/14    1.关注后虽然存储到了数据库  但是没有刷新联系人
        //2  如果刷新数据库  是全局的刷新列表
        //3、网络和本地数据库都是异步的  可能会在添加到界面的时候  冲突  导致数据显示异常
        //4、如何识别已经数据库中已经存在的数据
    }


    /**
     * diff比较
     *
     * @param oldLists 老的数据集合
     * @param newLists 新的数据回合
     */
    private void diff(List<User> oldLists, List<User> newLists) {
        DiffUtil.Callback callback = new DiffUiDataCallback<>(oldLists, newLists);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(callback);

        //在对比完成后进行数据的赋值
        getView().getRecyclerViewAadpter().replace(newLists);
        //尝试刷新界面
        diffResult.dispatchUpdatesTo(getView().getRecyclerViewAadpter());
        getView().onAdapterDataChanged();


    }


    //运行在这里的时候要是子线程
    @Override
    public void onDataLoaded(List<User> data) {
        //无论怎么操作  数据变更都会通知到这里来
        ConactContract.View view = getView();
        if (view == null)
            return;
        RecyclerAdapter<User> adapter = view.getRecyclerViewAadpter();
        List<User> oldItems = adapter.getItems();

        DiffUtil.Callback callback = new DiffUiDataCallback<>(oldItems, data);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(callback);

        //调用基类方法进行界面刷新
        refreshData(diffResult,data);

    }

}
