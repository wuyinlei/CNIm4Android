package com.mingchu.cnim4android.activitys;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mingchu.cnim4android.R;
import com.mingchu.cnim4android.fragment.assist.PermissionsFragment;
import com.mingchu.cnim4android.fragment.media.GalleryFragment;
import com.mingchu.common.app.Application;
import com.mingchu.common.app.BaseActivity;
import com.mingchu.common.widget.custom.PortraitView;
import com.mingchu.common.widget.recycler.RecyclerAdapter;
import com.mingchu.factory.model.db.User;
import com.yalantis.ucrop.UCrop;

import net.qiujuer.genius.ui.widget.EditText;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import butterknife.BindView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

public class GroupCreateActivity extends BaseActivity {


    @BindView(R.id.recycler)
    RecyclerView mRecycler;

    @BindView(R.id.edit_name)
    EditText mName;

    @BindView(R.id.edit_desc)
    EditText mDesc;

    @BindView(R.id.iv_portrait)
    PortraitView mPortrait;

    private RecyclerAdapter<User> mAdapter;
    private String mPortraitPath;
    private Set<String> mMembers = new HashSet<>();

    /**
     * 显示该activity
     * @param context  上下文
     */
    public static void show(Context context) {
        context.startActivity(new Intent(context, GroupCreateActivity.class));
    }

    @Override
    protected void initView() {
        super.initView();

        setTitle("");

        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new RecyclerAdapter<User>() {
            @Override
            protected ViewHolder<User> onCreateViewHolder(View root, int viewType) {
                return new GroupCreateActivity.ViewHolder(root);
            }

            @Override
            protected int getItemViewType(int position, User user) {
                return R.layout.cell_group_create_contact;
            }
        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.group_create, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_create) {
            onClickCreate();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.iv_portrait)
    void onClickPortrait() {
        if (!PermissionsFragment.hasWriteWorkPerms(this))
            return;


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
                        .start(GroupCreateActivity.this); //启动

            }
            //show的时候使用getChildFragmentManager()
        }).show(getSupportFragmentManager(), GalleryFragment.class.getName());

    }

    private void loadPortrait(Uri path) {
        this.mPortraitPath = path.getPath();
        Glide.with(this)
                .load(path)
                .asBitmap()
                .centerCrop()
                .into(mPortrait);
    }

    private void onClickCreate() {

    }


    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_group_create;
    }

    public class ViewHolder extends RecyclerAdapter.ViewHolder<User> {

        @BindView(R.id.iv_portrait)
        PortraitView mPortrait;
        @BindView(R.id.txt_name)
        TextView mName;
        @BindView(R.id.cb_select)
        CheckBox mSelect;

        public ViewHolder(View itemView) {
            super(itemView);
        }

        @OnCheckedChanged(R.id.cb_select)
        void onCheckedChanged(boolean checked) {
            if (checked) {
                mMembers.add(mData.getId());
            } else {
                mMembers.remove(mData.getId());
            }
        }

        @Override
        protected void onBind(User user) {
            mPortrait.setup(Glide.with(GroupCreateActivity.this), user);
            mName.setText(user.getName());
            mSelect.setChecked(mMembers.contains(user.getId()));
        }
    }
}
