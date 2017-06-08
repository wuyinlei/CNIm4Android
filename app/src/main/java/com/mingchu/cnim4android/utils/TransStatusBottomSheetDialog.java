package com.mingchu.cnim4android.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.support.design.widget.BottomSheetDialog;
import android.view.ViewGroup;
import android.view.Window;

import com.mingchu.common.tools.UiTool;

/**
 * Created by wuyinlei on 2017/6/8.
 *
 * @function 为了解决dialog弹出之后状态栏变黑的工具类
 */

public class TransStatusBottomSheetDialog extends BottomSheetDialog {

    public TransStatusBottomSheetDialog(@NonNull Context context) {
        super(context);
    }

    public TransStatusBottomSheetDialog(@NonNull Context context, @StyleRes int theme) {
        super(context, theme);
    }

    protected TransStatusBottomSheetDialog(@NonNull Context context, boolean cancelable, DialogInterface.OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Window window = getWindow();
        if (window == null) {
            return;
        }
        //得到屏幕的高度
        int screenHeight = UiTool.getScreenHeight(getOwnerActivity());
        //得到状态栏的高度
        int statusBarHeight = UiTool.getStatusBarHeight(getOwnerActivity());
        int dialogHeight = screenHeight - statusBarHeight;
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, dialogHeight == 0 ?
                ViewGroup.LayoutParams.MATCH_PARENT : dialogHeight);
    }
}
