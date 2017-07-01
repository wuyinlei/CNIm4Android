package com.mingchu.cnim4android.fragment.media;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.mingchu.cnim4android.R;
import com.mingchu.cnim4android.utils.TransStatusBottomSheetDialog;
import com.mingchu.common.app.BaseFragment;
import com.mingchu.common.tools.UiTool;
import com.mingchu.common.widget.GalleryView;

import net.qiujuer.genius.ui.Ui;

/**
 * 图片选择Fragment
 */
public class GalleryFragment extends BottomSheetDialogFragment
        implements GalleryView.SelectedChangeListener {

    private GalleryView mGalleryView;
    private OnSelectedListener mListener;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        mGalleryView = (GalleryView) view.findViewById(R.id.gallery_view);
    }

    @Override
    public void onStart() {
        super.onStart();

        mGalleryView.setup(getLoaderManager(), this);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //先使用默认的
        return new TransStatusBottomSheetDialog(getActivity());
    }

    @Override
    public void onSelectedCountChanged(int count) {
        //如果选中的一张图片
        if (count > 0) {
            //隐藏自己
            dismiss();
            if (mListener != null) {
                String[] paths = mGalleryView.getSelectedPath();
                mListener.onSelectedImage(paths); //返回第一张
                //取消引用  加快内存回收
                mListener = null;
            }
        }
    }

    /**
     * 设置事件监听
     *
     * @param listener 事件
     * @return 返回自己
     */
    public GalleryFragment setListener(OnSelectedListener listener) {
        mListener = listener;
        return this;
    }

    public interface OnSelectedListener {
        void onSelectedImage(String[] path);
    }


}
