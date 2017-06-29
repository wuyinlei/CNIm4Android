package com.mingchu.cnim4android.fragment.main;


import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mingchu.cnim4android.R;
import com.mingchu.cnim4android.activitys.MessageActivity;
import com.mingchu.cnim4android.activitys.PersonalActivity;
import com.mingchu.cnim4android.fragment.search.SearchUserFragment;
import com.mingchu.common.app.BaseFragment;
import com.mingchu.common.app.PresenterFragment;
import com.mingchu.common.widget.EmptyView;
import com.mingchu.common.widget.custom.PortraitView;
import com.mingchu.common.widget.recycler.RecyclerAdapter;
import com.mingchu.factory.model.card.UserCard;
import com.mingchu.factory.model.db.User;
import com.mingchu.factory.presenter.contact.ConactContract;
import com.mingchu.factory.presenter.contact.ContactPresenter;

import javax.microedition.khronos.opengles.GL;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactFragment extends PresenterFragment<ConactContract.Presenter>
implements ConactContract.View{

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @BindView(R.id.empty_view)
    EmptyView mEmptyView;

    private RecyclerAdapter<User> mRecyclerAdapter;


    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_contact;
    }


    @Override
    protected void initView(View view) {
        super.initView(view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mRecyclerAdapter = new RecyclerAdapter<User>() {
            @Override
            protected int getItemViewType(int position, User data) {
                return R.layout.cell_contact_list;
            }

            @Override
            protected ViewHolder<User> onCreateViewHolder(View root, int viewType) {


                return new ContactFragment.ViewHolder(root);
            }
        };

        mRecyclerView.setAdapter(mRecyclerAdapter);

        /**
         * Item事件监听方法
         */

        //初始化占位布局
        mEmptyView.bind(mRecyclerView);
        setPlaceHolderView(mEmptyView);
    }

    @Override
    protected void onFirstInit() {
        super.onFirstInit();
        mPresenter.start();
    }

    @Override
    protected boolean isSaveView() {
        return true;  //返回这个true  是保存view不被重新创建
    }

    @Override
    protected void initData() {
        super.initData();

    }

    @Override
    protected ConactContract.Presenter initPresenter() {
        return new ContactPresenter(this);
    }

    @Override
    public RecyclerAdapter<User> getRecyclerViewAadpter() {
        return mRecyclerAdapter;
    }

    @Override
    public void onAdapterDataChanged() {
        //进行界面操作
        mPlaceHolderView.triggerOkOrEmpty(mRecyclerAdapter.getItemCount() > 0 );
    }

    class ViewHolder extends RecyclerAdapter.ViewHolder<User> {

        @BindView(R.id.iv_portrait)
        PortraitView mIvPortrait;

        @BindView(R.id.tv_name)
        TextView mTvName;

        @BindView(R.id.tv_desc)
        TextView mTvDesc;

        @OnClick(R.id.fragment_container)
        void onItemClick(){
            MessageActivity.show(getContext(),mData);
        }

        @OnClick(R.id.iv_portrait)
            //发起关注
        void onPortraitClick() {  //用户详情界面
            PersonalActivity.show(getContext(), mData.getId());
        }

        public ViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(User data) {

            mIvPortrait.setup(Glide.with(ContactFragment.this), data);

            if (!TextUtils.isEmpty(data.getName())) {
                mTvName.setText(data.getName());
                mTvName.setVisibility(View.VISIBLE);
            } else {
                mTvName.setVisibility(View.INVISIBLE);
            }

            if (!TextUtils.isEmpty(data.getDesc())) {
                mTvDesc.setText(data.getDesc());
                mTvDesc.setVisibility(View.VISIBLE);
            } else {
                mTvDesc.setVisibility(View.INVISIBLE);
            }

        }
    }


}
