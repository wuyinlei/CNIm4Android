package com.mingchu.cnim4android.fragment.account;


import com.mingchu.cnim4android.R;
import com.mingchu.cnim4android.fragment.media.GalleryFragment;
import com.mingchu.common.app.BaseFragment;
import com.mingchu.common.widget.custom.PortraitView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 用户更新信息的界面
 */
public class UpdateUserInfoFragment extends BaseFragment {

    @BindView(R.id.im_portrait)
    PortraitView mPortraitView;

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_update_user_info;
    }

    @OnClick(R.id.im_portrait)
    void onPortraitClick() {
        new GalleryFragment().setListener(new GalleryFragment.OnSelectedListener() {
            @Override
            public void onSelectedImage(String path) {

            }
            //show的时候使用getChildFragmentManager()
        }).show(getChildFragmentManager(), GalleryFragment.class.getName());
    }

}
