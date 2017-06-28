package com.mingchu.factory.presenter.message;

import android.support.v7.util.DiffUtil;
import android.text.TextUtils;

import com.mingchu.factory.data.message.SessionDataSource;
import com.mingchu.factory.data.message.SessionRespository;
import com.mingchu.factory.model.db.Session;
import com.mingchu.factory.presenter.BaseSourcePresenter;
import com.mingchu.factory.presenter.search.SearchContract;
import com.mingchu.factory.utils.DiffUiDataCallback;

import java.util.HashSet;
import java.util.List;

/**
 * Created by wuyinlei on 2017/6/25.
 *
 * @function 最近聊天列表的Presenter
 */

public class SessionPresenter extends BaseSourcePresenter<Session, Session, SessionDataSource, SessionContract.View>
        implements SessionContract.Presenter {


    public SessionPresenter(SessionContract.View view) {
        super(view, new SessionRespository());
    }

    @Override
    public void onDataLoaded(List<Session> sessions) {
        SessionContract.View view = getView();
        if (view == null)
            return;

//        for (int i = 0; i < sessions.size() - 1; i++) {
//            for (int j = sessions.size() - 1; j > i; j--) {
//                if (sessions.get(j).equals(sessions.get(i))) {
//                    if (sessions.get(j).getMessage() == null || TextUtils.isEmpty(sessions.get(j).getMessage().getContent()))
//                        sessions.remove(j);
//                }
//            }
//        }

        HashSet<Session> hashSessions = new HashSet<>(sessions);
        sessions.clear();
        for (Session session : hashSessions) {
            sessions.add(session);
        }

        //差异对比
        List<Session> oldSessions = view.getRecyclerViewAadpter().getItems();
        DiffUiDataCallback<Session> callback = new DiffUiDataCallback<>(oldSessions, sessions);
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(callback);

        //刷新界面
        refreshData(result, sessions);
    }
}
