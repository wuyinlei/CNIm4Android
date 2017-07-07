package com.mingchu.cnim4android.fragment.main;


import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mingchu.cnim4android.R;
import com.mingchu.cnim4android.activitys.MessageActivity;
import com.mingchu.cnim4android.activitys.PersonalActivity;
import com.mingchu.common.app.BaseFragment;
import com.mingchu.common.app.PresenterFragment;
import com.mingchu.common.widget.EmptyView;
import com.mingchu.common.widget.custom.PortraitView;
import com.mingchu.common.widget.recycler.RecyclerAdapter;
import com.mingchu.factory.model.db.Group;
import com.mingchu.factory.model.db.User;
import com.mingchu.factory.presenter.group.GroupsContract;
import com.mingchu.factory.presenter.group.GroupsPresenter;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 *
 * @function 群组列表界面
 */
public class GroupFragment extends PresenterFragment<GroupsContract.Presenter> implements GroupsContract.View {


    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @BindView(R.id.empty_view)
    EmptyView mEmptyView;

    private RecyclerAdapter<Group> mRecyclerAdapter;


    public GroupFragment() {

    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));

        mRecyclerAdapter = new RecyclerAdapter<Group>() {
            @Override
            protected int getItemViewType(int position, Group data) {
                return R.layout.cell_group_list;
            }

            @Override
            protected ViewHolder<Group> onCreateViewHolder(View root, int viewType) {
                return new GroupFragment.ViewHolder(root);
            }
        };

        mRecyclerView.setAdapter(mRecyclerAdapter);


        //初始化占位布局
        mEmptyView.bind(mRecyclerView);
        setPlaceHolderView(mEmptyView);
    }

    @Override
    protected boolean isSaveView() {
        return true;  //返回这个true  是保存view不被重新创建
    }

    @Override
    protected void onFirstInit() {
        super.onFirstInit();
        mPresenter.start();

    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_group;
    }

    @Override
    public RecyclerAdapter<Group> getRecyclerViewAadpter() {
        return mRecyclerAdapter;
    }

    @Override
    public void onAdapterDataChanged() {

        mPlaceHolderView.triggerOkOrEmpty(mRecyclerAdapter.getItemCount() > 0);
    }

    @Override
    public void scrollRecyclerToPosition(int position) {

    }

    @Override
    protected GroupsContract.Presenter initPresenter() {
        return new GroupsPresenter(this);
    }


    class ViewHolder extends RecyclerAdapter.ViewHolder<Group> {

        @BindView(R.id.iv_portrait)
        PortraitView mIvPortrait;

        @BindView(R.id.tv_name)
        TextView mTvName;

        @BindView(R.id.tv_desc)
        TextView mTvDesc;

        @BindView(R.id.tv_member)
        TextView mTvMember;

        @OnClick(R.id.ll_group)
        public void groupChat(){
            MessageActivity.show(getActivity(),mData);
        }


        public ViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(Group group) {

            mIvPortrait.setup(Glide.with(GroupFragment.this),group.getPicture());

            if (!TextUtils.isEmpty(group.getName())) {
                mTvName.setText(group.getName());
                mTvName.setVisibility(View.VISIBLE);
            } else {
                mTvName.setVisibility(View.INVISIBLE);
            }

            if (!TextUtils.isEmpty(group.getDesc())) {
                mTvDesc.setText(group.getDesc());
                mTvDesc.setVisibility(View.VISIBLE);
            } else {
                mTvDesc.setVisibility(View.INVISIBLE);
            }

            if (group.holder != null && group.holder instanceof String){
                mTvMember.setText("" + group.holder);
            } else {
                mTvMember.setText("");
            }
        }
    }


}
