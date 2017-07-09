package com.mingchu.cnim4android.fragment.message;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mingchu.cnim4android.R;
import com.mingchu.cnim4android.activitys.MessageActivity;
import com.mingchu.cnim4android.fragment.panel.PanelFragment;
import com.mingchu.common.app.BaseFragment;
import com.mingchu.common.app.PresenterFragment;
import com.mingchu.common.face.Face;
import com.mingchu.common.widget.adapter.TextWatcherAdapter;
import com.mingchu.common.widget.custom.PortraitView;
import com.mingchu.common.widget.recycler.RecyclerAdapter;
import com.mingchu.factory.model.db.Message;
import com.mingchu.factory.model.db.User;
import com.mingchu.factory.persistence.Account;
import com.mingchu.factory.presenter.message.ChatContract;

import net.qiujuer.genius.ui.Ui;
import net.qiujuer.genius.ui.compat.UiCompat;
import net.qiujuer.genius.ui.widget.Loading;
import net.qiujuer.widget.airpanel.AirPanel;
import net.qiujuer.widget.airpanel.Util;

import java.io.File;
import java.util.Map;
import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by wuyinlei on 2017/6/23.
 *
 * @function 聊天的基本fragment
 */

public abstract class ChatFragment<InitModel> extends
        PresenterFragment<ChatContract.Presenter> implements
        AppBarLayout.OnOffsetChangedListener, ChatContract.View<InitModel>, PanelFragment.PanelCallback {

    protected String mReceiverId;

    protected Adapter mAdapter;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.recycler)
    RecyclerView mRecyclerView;

    @BindView(R.id.appBarLayout)
    AppBarLayout mAppBarLayout;


    @BindView(R.id.edit_content)
    EditText mEtContent;

    @BindView(R.id.btn_submit)
    ImageView mBtSumbmit;


    @Override
    protected void initArgs(Bundle bundle) {
        super.initArgs(bundle);
        mReceiverId = bundle.getString(MessageActivity.KEY_RECEIVER_ID);
    }


    private PanelFragment mPanelContent;
    private AirPanel.Boss mPanelBoss;


    @Override
    protected void initView(View view) {
        super.initView(view);

        initToolbar();
        initAppbar();

        mPanelBoss = (AirPanel.Boss) view.findViewById(R.id.lay_container);
        mPanelBoss.setPanelListener(new AirPanel.Listener() {
            @Override
            public void requestHideSoftKeyboard() {
                Util.hideKeyboard(mEtContent);
            }
        });

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new Adapter();
        mRecyclerView.setAdapter(mAdapter);

        mEtContent.addTextChangedListener(new TextWatcherAdapter() {
            @Override
            public void afterTextChanged(Editable editable) {
                String content = editable.toString().trim();
                boolean sendMsg = !TextUtils.isEmpty(content);
                mBtSumbmit.setActivated(sendMsg);
            }
        });

        PanelFragment fragment = (PanelFragment) getChildFragmentManager().findFragmentById(R.id.frag_panel);
        fragment.setup(this);
        mPanelContent = fragment;

        // 添加一个激活监听
        mEtContent.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                onOptionStatusChange(hasFocus);
            }
        });

        if (mAppBarLayout != null)
            mAppBarLayout.setExpanded(false);


    }

    @Override
    protected void initData() {
        super.initData();
        mPresenter.start();

    }

    @Override
    public void scrollRecyclerToPosition(final int position) {

        //貌似这个地方又是出现了之前遇到的一个疑点   就是直接刷新 没起作用  但是延迟一段时间是可以的
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mRecyclerView.scrollToPosition(position);
            }
        }, 300);

    }


    protected void initToolbar() {
        mToolbar.setNavigationIcon(R.drawable.ic_back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
    }

    @Override
    public boolean onBackPressed() {
        if (mPanelBoss.isOpen()) {
            mPanelBoss.closePanel();
            return true;
        }
        return false;
    }

    @OnClick(R.id.btn_record)
    void onRecordClick() {
        if (mPanelBoss.isOpen()) {
            Util.showKeyboard(mEtContent);
        } else {
            mPanelContent.showRecord();
            mPanelBoss.openPanel();
        }
    }

    protected void onOptionStatusChange(boolean isActive) {
        if (isActive && mAppBarLayout != null) {
            mAppBarLayout.setExpanded(false, true);
        }
    }

    @OnClick(R.id.btn_submit)
    void onSubmitClick() {
        if (mBtSumbmit.isActivated()) {
            //发送
            String content = mEtContent.getText().toString();
            mEtContent.setText("");
            mPresenter.pushText(content);  //发送文字

        } else {
            onMoreClick();
        }
    }

    private void onMoreClick() {
        if (mPanelBoss.isOpen() && mPanelContent.isOpenMore()) {
            Util.showKeyboard(mEtContent);
        } else {
            mPanelContent.showMore();
            mPanelBoss.openPanel();
        }

    }


    /**
     * 给界面的appbar设置一个监听  得到关闭和打开的时候的进度
     */
    private void initAppbar() {
        mAppBarLayout.addOnOffsetChangedListener(this);
    }


    @Override
    public RecyclerAdapter<Message> getRecyclerViewAadpter() {
        return mAdapter;
    }

    @Override
    public void onAdapterDataChanged() {

    }


    @OnClick(R.id.btn_face)
    void onFaceClick() {
        if (mPanelBoss.isOpen() && mPanelContent.isOpenFace()) {
            Util.showKeyboard(mEtContent);
        } else {
            mPanelContent.showFace();
            mPanelBoss.openPanel();
        }
    }


    @Override
    public EditText getInputEditText() {
        return mEtContent;
    }

    @Override
    public void onSendGalleryClick(String[] paths) {
        mPresenter.pushImages(paths);
    }

    @Override
    public void onRecordDone(File file, long time) {
//        mPresenter.pushAudio(file); // TODO: 2017/7/9
    }

    private class Adapter extends RecyclerAdapter<Message> {

        @Override
        protected int getItemViewType(int position, Message message) {


            boolean isRight = Objects.equals(message.getSender().getId(),
                    Account.getUserId());

            switch (message.getType()) {
                case Message.TYPE_STR:
                    //文字内容
                    return isRight ? R.layout.cell_chat_txt_right :
                            R.layout.cell_chat_txt_left;

                case Message.TYPE_PIC:
                    //图片内容
                    return isRight ? R.layout.cell_chat_pic_right :
                            R.layout.cell_chat_pic_left;

                case Message.TYPE_AUDIO:
                    //语音内容
                    return isRight ? R.layout.cell_chat_audio_right :
                            R.layout.cell_chat_audio_left;


                default:
                    //其他内容
                    return isRight ? R.layout.cell_chat_txt_right :
                            R.layout.cell_chat_txt_left;

            }

        }

        @Override
        protected ViewHolder<Message> onCreateViewHolder(View root, int viewType) {

            switch (viewType) {
                case R.layout.cell_chat_txt_left:
                case R.layout.cell_chat_txt_right:
                    //左右都是同一个
                    return new TextHolder(root);

                case R.layout.cell_chat_audio_left:
                case R.layout.cell_chat_audio_right:
                    //左右都是同一个
                    return new AudioHolder(root);

                case R.layout.cell_chat_pic_left:
                case R.layout.cell_chat_pic_right:
                    //左右都是同一个
                    return new PicHolder(root);

                default:  //默认的就是
                    return new TextHolder(root);
            }
        }
    }

    class BaseHolder extends RecyclerAdapter.ViewHolder<Message> {

        @BindView(R.id.iv_portrait)
        PortraitView mPortraitView;

        //允许为空  左边没有 右边有
        @Nullable
        @BindView(R.id.loading)
        Loading mLoading;

        public BaseHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(Message message) {
            User sender = message.getSender();
            sender.load();  //进行数据加载  dbflow自带的
            mPortraitView.setup(Glide.with(ChatFragment.this), sender);

            //如果不为空  布局应该在右边
            if (mLoading != null) {
                int status = message.getStatus();
                if (status == Message.STATUS_DONE) {
                    //正常状态  隐藏loading
                    mLoading.stop();
                    mLoading.setVisibility(View.GONE);

                } else if (status == Message.STATUS_CREATED) {
                    //正在发送中的状态
                    mLoading.setVisibility(View.VISIBLE);
                    mLoading.setProgress(0);
                    mLoading.setForegroundColor(UiCompat.getColor(getResources(), R.color.colorAccent));
                    mLoading.start();
                } else if (status == Message.STATUS_FAILED) {
                    //发送失败  允许重新发送
                    mLoading.setVisibility(View.VISIBLE);
                    mLoading.setProgress(1);
                    mLoading.setForegroundColor(UiCompat.getColor(getResources(), R.color.alertImportant));
                    mLoading.stop();
                }

                mPortraitView.setEnabled(status == Message.STATUS_FAILED);

            }
        }

        //重新发送
        @OnClick(R.id.iv_portrait)
        void onRePushClick() {
            //重新发送
            if (mLoading != null && mPresenter.rePush(mData)) {
                //必须是右边的 才有可能重新发送
                updateData(mData);
            }

        }
    }


    //文字的holder
    class TextHolder extends BaseHolder {

        @BindView(R.id.tv_content)
        TextView mContent;

        public TextHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(Message message) {
            super.onBind(message);

            Spannable spannable = new SpannableString(message.getContent());

            mContent.setText(Face.decodeFace(mContent, spannable, (int) Ui.dipToPx(getResources(), 20)));

            //把内容设置到布局上
//            mContent.setText(spannable);
        }
    }

    //语音的holder
    class AudioHolder extends BaseHolder {

        public AudioHolder(View itemView) {
            super(itemView);
        }
    }


    //图片的holder
    class PicHolder extends BaseHolder {

        @BindView(R.id.iv_pic)
        ImageView mIvPic;

        public PicHolder(View itemView) {
            super(itemView);

        }

        @Override
        protected void onBind(Message message) {
            super.onBind(message);

            //当是图片类型的时候  content中就是具体的地址
            String content = message.getContent();

            Glide.with(ChatFragment.this)
                    .load(content)
                    .fitCenter()
                    .into(mIvPic);
        }
    }


}
