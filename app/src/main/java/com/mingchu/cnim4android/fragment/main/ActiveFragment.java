package com.mingchu.cnim4android.fragment.main;


import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mingchu.cnim4android.R;
import com.mingchu.cnim4android.activitys.MessageActivity;
import com.mingchu.common.app.BaseFragment;
import com.mingchu.common.app.PresenterFragment;
import com.mingchu.common.tools.DataTimeUtils;
import com.mingchu.common.widget.EmptyView;
import com.mingchu.common.widget.GalleryView;
import com.mingchu.common.widget.custom.PortraitView;
import com.mingchu.common.widget.recycler.RecyclerAdapter;
import com.mingchu.factory.model.db.Session;
import com.mingchu.factory.model.db.User;
import com.mingchu.factory.presenter.message.SessionContract;
import com.mingchu.factory.presenter.message.SessionPresenter;
import com.mingchu.factory.presenter.search.SearchContract;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class ActiveFragment extends PresenterFragment<SessionContract.Presenter> implements SessionContract.View {


    @BindView(R.id.empty_view)
    EmptyView mEmptyView;

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    private RecyclerAdapter<Session> mAdapter;

    public ActiveFragment() {

    }

    @Override
    protected void initView(View view) {
        super.initView(view);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mAdapter = new RecyclerAdapter<Session>() {
            @Override
            protected int getItemViewType(int position, Session session) {
                return R.layout.cell_chat_list;
            }

            @Override
            protected ViewHolder<Session> onCreateViewHolder(View root, int viewType) {
                return new ActiveFragment.ViewHolder(root);
            }
        });

        mAdapter.setAdapterItemClickListener(new RecyclerAdapter.AdapterItemClickListener<Session>() {
            @Override
            public void onItemClick(RecyclerAdapter.ViewHolder holder, Session session) {
                MessageActivity.show(getActivity(), session);
            }

            @Override
            public void onLongItemClick(RecyclerAdapter.ViewHolder holder, Session session) {

            }
        });

        mEmptyView.bind(mRecyclerView);
        setPlaceHolderView(mEmptyView);

    }

    @Override
    protected void onFirstInit() {
        super.onFirstInit();
        mPresenter.start();
    }

    @Override
    protected void initData() {
        super.initData();


    }

    @Override
    protected boolean isSaveView() {
        return true;
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_active;
    }

    @Override
    protected SessionContract.Presenter initPresenter() {
        return new SessionPresenter(this);
    }

    @Override
    public RecyclerAdapter<Session> getRecyclerViewAadpter() {
        return mAdapter;
    }

    @Override
    public void onAdapterDataChanged() {
        mPlaceHolderView.triggerOkOrEmpty(mAdapter.getItemCount() > 0);
    }

    @Override
    public void scrollRecyclerToPosition(int position) {

    }

    class ViewHolder extends RecyclerAdapter.ViewHolder<Session> {

        @BindView(R.id.iv_portrait)
        PortraitView mPortraitView;

        @BindView(R.id.tv_name)
        TextView mTvName;

        @BindView(R.id.tv_time)
        TextView mTvTime;

        @BindView(R.id.tv_content)
        TextView mTvContent;

        public ViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(Session session) {
            mPortraitView.setup(Glide.with(getActivity()), session.getPicture());
            mTvContent.setText(TextUtils.isEmpty(session.getContent()) ? "" : session.getContent());
            mTvTime.setText(DataTimeUtils.getSampleDate(session.getModifyAt()));
            mTvName.setText(session.getTitle());
        }
    }

}
