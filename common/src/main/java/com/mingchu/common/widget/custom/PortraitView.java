package com.mingchu.common.widget.custom;

import android.content.Context;
import android.util.AttributeSet;

import com.bumptech.glide.RequestManager;
import com.mingchu.common.R;
import com.mingchu.common.factory.model.Author;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by wuyinlei on 2017/6/5.
 * <p>
 * 圆形头像
 */

public class PortraitView extends CircleImageView {

    public PortraitView(Context context) {
        super(context);
    }

    public PortraitView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PortraitView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    public void setup(RequestManager manager, int resouseId, String url) {

        if (url == null)
            url = "";

        manager.load(url)
                .placeholder(resouseId)
                .centerCrop()
                .dontAnimate()  //不能使用动画
                .into(this);
    }

    public void setup(RequestManager manager, String url) {
        this.setup(manager, R.drawable.default_user_img, url);
    }


    public void setup(RequestManager manager, Author author) {
        if (author == null)
            return;
        setup(manager, author.getPortrait());
    }


}
