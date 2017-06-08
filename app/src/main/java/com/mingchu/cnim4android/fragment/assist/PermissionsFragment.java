package com.mingchu.cnim4android.fragment.assist;


import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mingchu.cnim4android.R;
import com.mingchu.cnim4android.utils.TransStatusBottomSheetDialog;
import com.mingchu.common.app.Application;

import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * 权限申请弹出框
 */
public class PermissionsFragment extends BottomSheetDialogFragment
        implements EasyPermissions.PermissionCallbacks {

    private static final int RX = 0X0122; //权限回调的标识


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //先使用默认的
        return new TransStatusBottomSheetDialog(getActivity());
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_permissions, container, false);
        initView(view);

        return view;
    }

    /**
     * 初始化布局
     *
     * @param view
     */
    private void initView(View view) {

        view.findViewById(R.id.btn_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPerm();
            }
        });

        Context context = getContext();
        view.findViewById(R.id.iv_state_permission_network)
                .setVisibility(hasNetWorkPerms(context) ? View.VISIBLE : View.GONE);
        view.findViewById(R.id.iv_state_permission_read)
                .setVisibility(hasReadWorkPerms(context) ? View.VISIBLE : View.GONE);
        view.findViewById(R.id.iv_state_permission_write)
                .setVisibility(hasWriteWorkPerms(context) ? View.VISIBLE : View.GONE);
        view.findViewById(R.id.iv_state_permission_audio)
                .setVisibility(hasAudioWorkPerms(context) ? View.VISIBLE : View.GONE);
    }

    /**
     * 获取是否有网络权限
     *
     * @param context 上下文
     * @return false  没有  true  有
     */
    private static boolean hasNetWorkPerms(Context context) {
        //准备需要检查的网络权限
        String[] perms = new String[]{
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.ACCESS_WIFI_STATE
        };
        return EasyPermissions.hasPermissions(context, perms);
    }


    /**
     * 获取是否有外部读取权限
     *
     * @param context 上下文
     * @return false  没有  true  有
     */
    private static boolean hasReadWorkPerms(Context context) {
        //准备需要检查的读取权限
        String[] perms = new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE
        };
        return EasyPermissions.hasPermissions(context, perms);
    }

    /**
     * 获取是否有外部存储权限
     *
     * @param context 上下文
     * @return false  没有  true  有
     */
    private static boolean hasWriteWorkPerms(Context context) {
        //准备需要检查的写入权限
        String[] perms = new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };
        return EasyPermissions.hasPermissions(context, perms);
    }

    /**
     * 获取是否有录音权限
     *
     * @param context 上下文
     * @return false  没有  true  有
     */
    private static boolean hasAudioWorkPerms(Context context) {
        //准备需要检查的录音权限
        String[] perms = new String[]{
                Manifest.permission.RECORD_AUDIO
        };
        return EasyPermissions.hasPermissions(context, perms);
    }

    /**
     * 私有的show方法
     *
     * @param manager FragmentManager
     */
    private static void show(FragmentManager manager) {
        //调用BottomSheetDialogFragment已经准备好的显示方法
        new PermissionsFragment().show(manager, PermissionsFragment.class.getName());
    }

    /**
     * 检查是否具有所有的权限
     *
     * @param context 上下文
     * @param manager FragmentManager
     * @return true有所有权限  false没有权限
     */
    public static boolean haveAllPerms(Context context, FragmentManager manager) {
        boolean haveAllPers = hasAudioWorkPerms(context)
                && hasNetWorkPerms(context)
                && hasReadWorkPerms(context)
                && hasWriteWorkPerms(context);
        if (!haveAllPers) {
            show(manager);
        }
        return haveAllPers;
    }

    /**
     * 申请权限的方法
     */
    @AfterPermissionGranted(RX)
    private void requestPerm() {
        String[] perms = new String[]{
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO
        };

        if (EasyPermissions.hasPermissions(getContext(), perms)) {
            Application.showToast(R.string.label_permission_ok);
            //从Fragment中的getView()拿到根布局  前提必须是在onCreate()方法之后才能调用
            initView(getView());
        } else {
            EasyPermissions.requestPermissions(getActivity(),
                    getActivity().getString(R.string.title_assist_permissions), RX, perms);
        }
    }

    /**
     * 权限申请的时候回调的方法 把这个方法中对应的权限申请状态交给EasyPermissions框架尽心处理
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //传递对应的参数 并且告知接收权限的处理者
        EasyPermissions.onRequestPermissionsResult(requestCode,permissions,grantResults,this);

    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        //如果有权限没有申请成功的情况存在  则弹出对话框 需要用户去设置界面进行申请权限
        if (EasyPermissions.somePermissionPermanentlyDenied(this,perms)) {
            new AppSettingsDialog
                    .Builder(this)
                    .build()
                    .show();
        }
    }
}
