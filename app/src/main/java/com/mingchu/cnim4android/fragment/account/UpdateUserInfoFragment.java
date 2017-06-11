package com.mingchu.cnim4android.fragment.account;


import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.mingchu.cnim4android.R;
import com.mingchu.cnim4android.fragment.media.GalleryFragment;
import com.mingchu.common.app.Application;
import com.mingchu.common.app.BaseFragment;
import com.mingchu.common.widget.custom.PortraitView;
import com.mingchu.factory.Factory;
import com.mingchu.factory.net.UploadHelper;
import com.yalantis.ucrop.UCrop;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;

/**
 * 用户更新信息的界面
 */
public class UpdateUserInfoFragment extends BaseFragment {

    @BindView(R.id.iv_portrait)
    PortraitView mPortraitView;

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_update_user_info;
    }

    @OnClick(R.id.iv_portrait)
    void onPortraitClick() {
        new GalleryFragment().setListener(new GalleryFragment.OnSelectedListener() {
            @Override
            public void onSelectedImage(String path) {
                UCrop.Options options = new UCrop.Options();
                //设置图片处理的格式
                options.setCompressionFormat(Bitmap.CompressFormat.JPEG);
                //设置处理后的图片质量
                options.setCompressionQuality(96);

                //得到头像的缓存地址
                File dPath = Application.getPortraitFile();

                UCrop.of(Uri.fromFile(new File(path)), Uri.fromFile(dPath))
                        .withAspectRatio(1, 1)//一个正方形图片
                        .withMaxResultSize(520, 520) //最大尺寸
                        .withOptions(options)  //先关参数
                        .start(getActivity()); //启动

            }
            //show的时候使用getChildFragmentManager()
        }).show(getChildFragmentManager(), GalleryFragment.class.getName());
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
                String url = UploadHelper.uploadPortrait(localPath);
                Log.d("UpdateUserInfoFragment", "上传过后的url:" + url);
            }
        });
    }
}
