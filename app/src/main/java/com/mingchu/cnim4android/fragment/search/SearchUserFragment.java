package com.mingchu.cnim4android.fragment.search;


import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mingchu.cnim4android.R;
import com.mingchu.cnim4android.activitys.SearchActivity;
import com.mingchu.common.app.PresenterFragment;
import com.mingchu.common.widget.EmptyView;
import com.mingchu.common.widget.custom.PortraitView;
import com.mingchu.common.widget.recycler.RecyclerAdapter;
import com.mingchu.factory.model.card.UserCard;
import com.mingchu.factory.presenter.search.SearchContract;
import com.mingchu.factory.presenter.search.SearchUserPresenter;

import java.util.List;

import butterknife.BindView;

/**
 * 搜索用户的Fragment
 */
public class SearchUserFragment extends PresenterFragment<SearchContract.Presenter>
        implements SearchActivity.SearchFragment, SearchContract.UserView {

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @BindView(R.id.empty_view)
    EmptyView mEmptyView;
    private RecyclerAdapter<UserCard> mRecyclerAdapter;

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_search_user;
    }

    @Override
    public void search(String content) {
        mPresenter.search(content);
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerAdapter = new RecyclerAdapter<UserCard>() {

            @Override
            protected int getItemViewType(int position, UserCard data) {
                return R.layout.cell_search_list;
            }

            @Override
            protected ViewHolder<UserCard> onCreateViewHolder(View root, int viewType) {
                return new SearchUserFragment.ViewHolder(root);
            }
        };
        mRecyclerView.setAdapter(mRecyclerAdapter);
        //初始化占位布局
        mEmptyView.bind(mRecyclerView);
        setPlaceHolderView(mEmptyView);
    }


    class ViewHolder extends RecyclerAdapter.ViewHolder<UserCard> {

        @BindView(R.id.iv_portrait)
        PortraitView mIvPortrait;

        @BindView(R.id.txt_name)
        TextView mTvName;

        @BindView(R.id.iv_follow)
        ImageView mIvFollow;

        public ViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(UserCard data) {
            Glide.with(getActivity())
                    .load(data.getPortrait()).into(mIvPortrait);
            mTvName.setText(data.getName());
            mIvFollow.setEnabled(data.isFollow());
        }
    }

    @Override
    protected SearchContract.Presenter initPresenter() {
        return new SearchUserPresenter(this);
    }


    @Override
    public void onSearchDone(List<UserCard> userCards) {
        mRecyclerAdapter.replace(userCards);
        //如果有数据则是ok  如果没有数据则显示空布局
        mPlaceHolderView.triggerOkOrEmpty(mRecyclerAdapter.getItemCount() > 0);
    }
}
