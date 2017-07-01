package com.mingchu.cnim4android.fragment.account;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.mingchu.cnim4android.R;
import com.mingchu.cnim4android.activitys.MainActivity;
import com.mingchu.cnim4android.fragment.media.GalleryFragment;
import com.mingchu.common.app.Application;
import com.mingchu.common.app.BaseFragment;
import com.mingchu.common.app.PresenterFragment;
import com.mingchu.common.widget.custom.PortraitView;
import com.mingchu.factory.Factory;
import com.mingchu.factory.data.helper.UserHelper;
import com.mingchu.factory.model.db.User;
import com.mingchu.factory.net.UploadHelper;
import com.mingchu.factory.persistence.Account;
import com.mingchu.factory.presenter.account.UpdateInfoContract;
import com.mingchu.factory.presenter.account.UpdateInfoPresenter;
import com.yalantis.ucrop.UCrop;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;
import net.qiujuer.genius.ui.widget.Loading;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;

/**
 * 用户更新信息的界面
 */
public class UpdateUserInfoFragment extends PresenterFragment<UpdateInfoContract.Presenter>
        implements UpdateInfoContract.View {

    @BindView(R.id.iv_portrait)
    PortraitView mPortraitView;

    @BindView(R.id.iv_sex)
    ImageView mUserSex;

    @BindView(R.id.edit_name)
    EditText mUserName;

    @BindView(R.id.edit_desc)
    EditText mUserDesc;

    @BindView(R.id.loading)
    Loading mLoading;

    @BindView(R.id.btn_submit)
    Button mSubmit;

    @BindView(R.id.im_header)
    View mHeader;


    private String mPortraitPath;
    private boolean isMan = true;

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_update_user_info;
    }

    @OnClick(R.id.iv_portrait)
    void onPortraitClick() {
        new GalleryFragment().setListener(new GalleryFragment.OnSelectedListener() {
            @Override
            public void onSelectedImage(String[] paths) {
                UCrop.Options options = new UCrop.Options();
                //设置图片处理的格式
                options.setCompressionFormat(Bitmap.CompressFormat.JPEG);
                //设置处理后的图片质量
                options.setCompressionQuality(96);

                //得到头像的缓存地址
                File dPath = Application.getPortraitFile();

                UCrop.of(Uri.fromFile(new File(paths[0])), Uri.fromFile(dPath))
                        .withAspectRatio(1, 1)//一个正方形图片
                        .withMaxResultSize(520, 520) //最大尺寸
                        .withOptions(options)  //先关参数
                        .start(getActivity()); //启动

            }
            //show的时候使用getChildFragmentManager()
        }).show(getChildFragmentManager(), GalleryFragment.class.getName());
    }

    @Override
    protected void initData() {
        super.initData();
        User user = Account.getUser();
        mPortraitView.setup(Glide.with(getActivity()), user);
        isMan = 0==user.getSex();
        Drawable drawable = getResources().getDrawable(isMan ? R.drawable.ic_sex_man : R.drawable.ic_sex_woman);
        mUserSex.setImageDrawable(drawable);
        mUserSex.getBackground().setLevel(isMan ? 0 : 1);
        mUserName.setText(user.getName());
        if (!TextUtils.isEmpty(user.getDesc()))
            mUserDesc.setText(user.getDesc());

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //收到从activity传递过来的回调  取出值进行加载  如果使我们可以处理的类型  我们就去进行处理
        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            //通过UCrop得到对应的图片Uri
            final Uri resultUri = UCrop.getOutput(data);
            if (resultUri != null) {

                //加载裁剪过后的图片
                loadPortrait(resultUri);

            }
        } else if (resultCode == UCrop.RESULT_ERROR) {
            final Throwable coprError = UCrop.getError(data);
        }
    }

    /**
     * 加载头像
     */
    private void loadPortrait(Uri resultUri) {
        Glide.with(getActivity()).load(resultUri)
                .asBitmap()
                .centerCrop()
                .into(mPortraitView);

        final String localPath = resultUri.getPath(); //拿到本地文件的地址
        Log.d("UpdateUserInfoFragment", "localPath: " + localPath);
        Factory.runOnAsync(new Runnable() {
            @Override
            public void run() {
                //异步上传
                final String url = UploadHelper.uploadPortrait(localPath);
                Run.onUiAsync(new Action() {
                    @Override
                    public void call() {
                        mPortraitPath = url;
                    }
                });
            }
        });
    }


    @OnClick(R.id.iv_sex)
    void onSexClick() {
        isMan = !isMan;
        Drawable drawable = getResources().getDrawable(isMan ? R.drawable.ic_sex_man : R.drawable.ic_sex_woman);
        mUserSex.setImageDrawable(drawable);
        mUserSex.getBackground().setLevel(isMan ? 0 : 1);

    }


    @Override
    protected UpdateInfoContract.Presenter initPresenter() {
        return new UpdateInfoPresenter(this);
    }

    @Override
    public void updateSuccess() {
        MainActivity.show(getContext());
        getActivity().finish();
    }

    @Override
    public void showLoading() {
        super.showLoading();
        changeStatus(false);
    }

    @Override
    public void showError(@StringRes int str) {
        super.showError(str);
        changeStatus(true);
    }

    @OnClick(R.id.btn_submit)
    void onSubmit() {
        mPresenter.update(mUserName.getText().toString(),
                mPortraitPath,
                mUserDesc.getText().toString(),
                isMan);
    }


    /**
     * 改变提交按钮的状态
     *
     * @param enable 是否可以点击状态
     */
    private void changeStatus(boolean enable) {
        mSubmit.setEnabled(enable);

        if (enable) {
            mLoading.stop();
        } else {
            mLoading.start();
        }

    }
}
