package com.mingchu.common.app;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.annotation.StringRes;

import com.mingchu.common.R;
import com.mingchu.common.factory.presenter.BaseContract;

public abstract class PresenterToolbarActivity<Presenter extends BaseContract.Presenter>
        extends ToolbarActivity  implements BaseContract.View<Presenter> {

    protected Presenter mPresenter;
    private ProgressDialog mLoadingProgressDialog;


    @Override
    public void initBefore() {
        super.initBefore();
        initPresenter();
    }


    protected abstract Presenter initPresenter();


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.destroy();
        }
    }

    @Override
    public void setPresenter(Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showError(@StringRes int str) {
        hideLoading();
        if (mPlaceHolderView != null)
            mPlaceHolderView.triggerError(str);
        else
            Application.showToast(str);
    }

    @Override
    public void showLoading() {
        if (mPlaceHolderView != null)
            mPlaceHolderView.triggerLoading();
        else {
            if (mLoadingProgressDialog == null) {
                mLoadingProgressDialog = new ProgressDialog(this, R.style.AppTheme_Dialog_Alert_Light);
                mLoadingProgressDialog.setCanceledOnTouchOutside(false);
                mLoadingProgressDialog.setCancelable(true);
                mLoadingProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        finish();
                    }
                });
            }
            mLoadingProgressDialog.setMessage(getText(R.string.prompt_loading));
            mLoadingProgressDialog.show();
        }
    }

    protected void hideLoading() {
        if (mLoadingProgressDialog != null) {
            mLoadingProgressDialog.dismiss();
            mLoadingProgressDialog = null;
        }
        if (mPlaceHolderView != null)
            mPlaceHolderView.triggerOk();
    }
}
