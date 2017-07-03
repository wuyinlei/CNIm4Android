package com.mingchu.cnim4android.fragment.search;


import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mingchu.cnim4android.R;
import com.mingchu.cnim4android.activitys.PersonalActivity;
import com.mingchu.cnim4android.activitys.SearchActivity;
import com.mingchu.common.app.PresenterFragment;
import com.mingchu.common.factory.presenter.BaseContract;
import com.mingchu.common.widget.EmptyView;
import com.mingchu.common.widget.custom.PortraitView;
import com.mingchu.common.widget.recycler.RecyclerAdapter;
import com.mingchu.factory.model.card.GroupCard;
import com.mingchu.factory.model.card.UserCard;
import com.mingchu.factory.presenter.search.SearchContract;
import com.mingchu.factory.presenter.search.SearchGroupPresenter;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchGroupFragment extends PresenterFragment<SearchContract.Presenter> implements
        SearchActivity.SearchFragment, SearchContract.GroupView {


    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @BindView(R.id.empty_view)
    EmptyView mEmptyView;

    private RecyclerAdapter<GroupCard> mRecyclerAdapter;

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_search_group;
    }

    @Override
    public void search(String content) {
        mPresenter.search(content);
    }

    @Override
    protected SearchContract.Presenter initPresenter() {
        return new SearchGroupPresenter(this);
    }

    @Override
    protected void initView(View view) {
        super.initView(view);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mRecyclerAdapter = new RecyclerAdapter<GroupCard>() {

            @Override
            protected int getItemViewType(int position, GroupCard data) {
                return R.layout.cell_search_list;
            }

            @Override
            protected ViewHolder<GroupCard> onCreateViewHolder(View root, int viewType) {
                return new SearchGroupFragment.ViewHolder(root);
            }
        };

        mRecyclerView.setAdapter(mRecyclerAdapter);

        //初始化占位布局
        mEmptyView.bind(mRecyclerView);
        setPlaceHolderView(mEmptyView);
    }

    @Override
    protected void initData() {
        super.initData();
        search("");
    }

    @Override
    public void onSearchDone(List<GroupCard> groupCards) {
        mRecyclerAdapter.replace(groupCards);
        //如果有数据则是ok  如果没有数据则显示空布局
        mPlaceHolderView.triggerOkOrEmpty(mRecyclerAdapter.getItemCount() > 0);
    }

    class ViewHolder extends RecyclerAdapter.ViewHolder<GroupCard> {

        @BindView(R.id.iv_portrait)
        PortraitView mIvPortrait;

        @BindView(R.id.txt_name)
        TextView mTvName;

        @BindView(R.id.iv_follow)
        ImageView mIvFollow;

        @OnClick(R.id.iv_follow)
            //发起关注
        void onFollowClick() {

        }


        public ViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(GroupCard data) {

            mIvPortrait.setup(Glide.with(SearchGroupFragment.this), data.getPicture());
            if (!TextUtils.isEmpty(data.getName())) {
                mTvName.setText(data.getName());
            }

            mIvFollow.setEnabled(data.getJoinAt() != null);
        }
    }

}
