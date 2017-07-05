package com.mingchu.common.face;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.mingchu.common.R;

import static com.bumptech.glide.load.DecodeFormat.PREFER_ARGB_8888;


class FaceHolder extends RecyclerView.ViewHolder {
    private ImageView mFace;

    FaceHolder(View itemView) {
        super(itemView);
        mFace = (ImageView) itemView.findViewById(R.id.im_face);
    }

    void set(Face.Bean bean) {
        if (bean != null && (bean.resId instanceof Integer | bean.resId instanceof String)) {
            Glide.with(itemView.getContext())
                    .load(bean.previewId)
                    .asBitmap()
                    .format(PREFER_ARGB_8888)
                    .placeholder(R.drawable.default_face)
                    .into(mFace);
        }
    }
}
