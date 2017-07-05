package com.mingchu.cnim4android.activitys;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mingchu.cnim4android.R;
import com.mingchu.cnim4android.fragment.assist.PermissionsFragment;
import com.mingchu.cnim4android.fragment.media.GalleryFragment;
import com.mingchu.common.app.Application;
import com.mingchu.common.app.BaseActivity;
import com.mingchu.common.app.PresenterToolbarActivity;
import com.mingchu.common.widget.custom.PortraitView;
import com.mingchu.common.widget.fglass.Fglass;
import com.mingchu.common.widget.recycler.RecyclerAdapter;
import com.mingchu.factory.Factory;
import com.mingchu.factory.model.db.User;
import com.mingchu.factory.net.UploadHelper;
import com.mingchu.factory.presenter.group.GroupCreateContract;
import com.mingchu.factory.presenter.group.GroupCreatePresenter;
import com.yalantis.ucrop.UCrop;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;
import net.qiujuer.genius.ui.widget.EditText;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

public class GroupCreateActivity extends PresenterToolbarActivity<GroupCreateContract.Presenter>
        implements GroupCreateContract.View {


    @BindView(R.id.recycler)
    RecyclerView mRecycler;

    @BindView(R.id.edit_name)
    EditText mName;

    @BindView(R.id.edit_desc)
    EditText mDesc;

    @BindView(R.id.iv_portrait)
    PortraitView mPortrait;


    private RecyclerAdapter<User> mAdapter;

    //头像地址
    private String mPortraitPath;

    //用户选中的队列
    public List<String> mMembers = new ArrayList<>();

    /**
     * 显示该activity
     *
     * @param context 上下文
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
            protected int getItemViewType(int position,User user) {
                return R.layout.cell_group_create_contact;
            }
        };

        mRecycler.setAdapter(mAdapter);


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

        hideSoftKeyboard();


//        if (!PermissionsFragment.hasWriteWorkPerms(this))
//            return;


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
        }).show(getSupportFragmentManager(), GalleryFragment.class.getName());

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
     * 加载图片
     * @param path
     */
    private void loadPortrait(Uri path) {

        final String picturePath = path.getPath();

        Glide.with(this)
                .load(path)
                .asBitmap()
                .centerCrop()
                .into(mPortrait);


        //异步上传图片到阿里的云OSS服务器
        Factory.runOnAsync(new Runnable() {
            @Override
            public void run() {
                //异步上传
                final String url = UploadHelper.uploadPortrait(picturePath);
                Run.onUiAsync(new Action() {
                    @Override
                    public void call() {
                        mPortraitPath = url;
                    }
                });
            }
        });


    }

    @Override
    protected void initData() {
        super.initData();

        mPresenter.start();


    }

    /**
     * 群创建提交按钮
     */
    private void onClickCreate() {
        hideSoftKeyboard();
        String groupName = mName.getText().toString().trim();
        String groupDesc = mDesc.getText().toString().trim();
        mPresenter.create(groupName, groupDesc, mPortraitPath,mMembers);
    }


    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_group_create;
    }

    @Override
    protected GroupCreateContract.Presenter initPresenter() {
        return new GroupCreatePresenter(this);
    }

    @Override
    public RecyclerAdapter<User> getRecyclerViewAadpter() {
        return mAdapter;
    }

    @Override
    public void onAdapterDataChanged() {

        //数据加载成功之后要进行消失loading对话框
        hideLoading();
    }

    @Override
    public void scrollRecyclerToPosition(int position) {

    }


    /**
     * 隐藏软键盘
     */
    private void hideSoftKeyboard(){
        View view = getCurrentFocus();  //当前焦点的view
        if (view == null)
            return;
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(),0);
    }

    @Override
    public void onCreateSuccessed() {
        hideLoading();
        Application.showToast(R.string.label_group_create_succeed);
        finish();
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
                String id = mData.getId();
                mMembers.add(id);
            } else {
                mMembers.remove(mData.getId());
            }
        }

        @Override
        protected void onBind(User user) {
            mPortrait.setup(Glide.with(GroupCreateActivity.this),user);
            mName.setText(user.getName());
            mSelect.setSelected(mMembers.contains(user.getId()));

        }
    }
}
