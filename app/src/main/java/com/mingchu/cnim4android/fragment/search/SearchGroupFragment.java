package com.mingchu.cnim4android.fragment.search;


import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.mingchu.cnim4android.R;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchGroupFragment extends PresenterFragment<SearchContract.Presenter> implements
        SearchActivity.SearchFragment ,SearchContract.GroupView{


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
                return 0;
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
    public void onSearchDone(List<GroupCard> groupCards) {

    }

    class ViewHolder extends RecyclerAdapter.ViewHolder<GroupCard>{

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
        protected void onBind(GroupCard data) {

        }
    }

}
