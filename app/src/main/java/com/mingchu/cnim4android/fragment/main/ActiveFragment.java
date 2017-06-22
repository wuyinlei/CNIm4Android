package com.mingchu.cnim4android.fragment.main;


import android.support.v4.app.Fragment;

import com.mingchu.cnim4android.R;
import com.mingchu.common.app.BaseFragment;
import com.mingchu.common.widget.GalleryView;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class ActiveFragment extends BaseFragment {

//    @BindView(R.id.gallery_view)
//    GalleryView mGalleryView;

    public ActiveFragment() {
        // Required empty public constructor
    }

    @Override
    protected void initData() {
        super.initData();
//        mGalleryView.setup(getLoaderManager(), new GalleryView.SelectedChangeListener() {
//            @Override
//            public void onSelectedCountChanged(int count) {
//
//            }
//        });
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_active;
    }

}
