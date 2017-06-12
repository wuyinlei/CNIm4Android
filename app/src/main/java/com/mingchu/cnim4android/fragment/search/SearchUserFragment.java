package com.mingchu.cnim4android.fragment.search;


import android.support.annotation.StringRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.GenericRequest;
import com.mingchu.cnim4android.R;
import com.mingchu.cnim4android.activitys.SearchActivity;
import com.mingchu.common.app.PresenterFragment;
import com.mingchu.common.widget.EmptyView;
import com.mingchu.common.widget.custom.PortraitView;
import com.mingchu.common.widget.recycler.RecyclerAdapter;
import com.mingchu.factory.model.card.UserCard;
import com.mingchu.factory.net.UploadHelper;
import com.mingchu.factory.presenter.contact.FollowContract;
import com.mingchu.factory.presenter.contact.FollowPresenter;
import com.mingchu.factory.presenter.search.SearchContract;
import com.mingchu.factory.presenter.search.SearchUserPresenter;

import net.qiujuer.genius.ui.Ui;
import net.qiujuer.genius.ui.compat.UiCompat;
import net.qiujuer.genius.ui.drawable.LoadingCircleDrawable;
import net.qiujuer.genius.ui.drawable.LoadingDrawable;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

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


    class ViewHolder extends RecyclerAdapter.ViewHolder<UserCard> implements FollowContract.View {

        private FollowContract.Presenter mPresenter;

        @BindView(R.id.iv_portrait)
        PortraitView mIvPortrait;

        @BindView(R.id.txt_name)
        TextView mTvName;

        @BindView(R.id.iv_follow)
        ImageView mIvFollow;

        public ViewHolder(View itemView) {
            super(itemView);
            mPresenter = new FollowPresenter(this);
        }

        @Override
        protected void onBind(UserCard data) {

//            Glide.with(getActivity())
//                    .load(data.getPortrait()).into(mIvPortrait);
//
            mIvPortrait.setup(Glide.with(SearchUserFragment.this), data);
            if (!TextUtils.isEmpty(data.getName())) {
                mTvName.setText(data.getName());
            }
            mIvFollow.setEnabled(!data.isFollow());
        }

        @OnClick(R.id.iv_portrait)
            //发起关注
        void onFollowClick() {
            mPresenter.follow(mData.getId());
        }

        @Override
        public void showError(@StringRes int str) {
            if (mIvFollow.getDrawable() instanceof LoadingDrawable) {
                LoadingDrawable drawable = (LoadingDrawable) mIvFollow.getDrawable();
//                drawable.stop();  //停止动画
                drawable.setProgress(1);  //圆圈
//                drawable.setForegroundColor();
                drawable.stop();  //失败则停止动画   并且显示一个圆圈
                mIvFollow.setImageResource(R.drawable.sel_opt_done_add);  //设置成默认的
            }
        }

        @Override
        public void showLoading() {
            int minSize = (int) Ui.dipToPx(getResources(), 22);
            int maxSize = (int) Ui.dipToPx(getResources(), 30);

            //初始化一个圆形的动画
            LoadingDrawable drawable = new LoadingCircleDrawable(minSize, maxSize);
            drawable.setBackgroundColor(0);
            drawable.setForegroundColor(new int[]{UiCompat.getColor(getResources(), R.color.white_alpha_208)});
            mIvFollow.setImageDrawable(drawable);  //设置
            drawable.start();//启动动画

        }

        @Override
        public void setPresenter(FollowContract.Presenter presenter) {
            mPresenter = presenter;
        }

        @Override
        public void onFollowSuccess(UserCard userCard) {
            if (mIvFollow.getDrawable() instanceof LoadingDrawable) {
                ((LoadingDrawable) mIvFollow.getDrawable()).stop();  //停止动画
                mIvFollow.setImageResource(R.drawable.sel_opt_done_add);  //设置成默认的
            }

            //发起更新
            updateData(userCard);
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
