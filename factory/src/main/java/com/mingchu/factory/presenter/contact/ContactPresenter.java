package com.mingchu.factory.presenter.contact;

import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v7.util.DiffUtil;

import com.mingchu.common.factory.data.DataSource;
import com.mingchu.common.factory.presenter.BaseContract;
import com.mingchu.common.factory.presenter.BasePresenter;
import com.mingchu.factory.data.helper.UserHelper;
import com.mingchu.factory.model.card.UserCard;
import com.mingchu.factory.model.db.AppDatabase;
import com.mingchu.factory.model.db.User;
import com.mingchu.factory.persistence.Account;
import com.mingchu.factory.utils.DiffUiDataCallback;
import com.raizlabs.android.dbflow.config.DatabaseDefinition;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.mingchu.factory.model.db.User_Table;
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

public class ContactPresenter extends BasePresenter<ConactContract.View>
        implements ConactContract.Presenter {

    public ContactPresenter(ConactContract.View view) {
        super(view);

    }


    @Override
    public void start() {
        super.start();
        // TODO: 2017/6/14 加载数据

        //加载本地数据库
        SQLite.select()
                .from(User.class)
                .where(User_Table.isFollow.eq(true))
                .and(User_Table.id.notEq(Account.getUserId()))
                .orderBy(User_Table.name, true)
                .limit(100)
                .async()
                .queryListResultCallback(new QueryTransaction.QueryResultListCallback<User>() {
                    @Override
                    public void onListQueryResult(QueryTransaction transaction,
                                                  @NonNull List<User> tResult) {

                        getView().getRecyclerViewAadpter().replace(tResult);
                        getView().onAdapterDataChanged();

                    }
                }).execute();

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
}
