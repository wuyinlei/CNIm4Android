package com.mingchu.common.widget;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.mingchu.common.R;
import com.mingchu.common.widget.recycler.RecyclerAdapter;


import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


/**
 * @author qiujuer Email:qiujuer@live.cn
 * @version 1.0.0
 */

public class GalleryView extends RecyclerView {
    private static final int LOADER_ID = 0x0100;
    private static final long MIN_IMAGE_LEN = 10 * 1024;  //最大的照片的大小  10MB
    private static final long MAX_IMAGE_COUNT = 9;  //最大选择的照片的数量

    private LoaderCallback callback = new LoaderCallback();
    private Adapter mAdapter = new Adapter();
    private List<Image> mSelectedImages = new LinkedList<>();
    private SelectedChangeListener mListener;

    public GalleryView(Context context) {
        super(context);
        init();
    }

    public GalleryView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GalleryView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        //四列图片
        setLayoutManager(new GridLayoutManager(getContext(), 4));
        setAdapter(mAdapter);
        mAdapter.setAdapterListener(new RecyclerAdapter.AdapterListener<Image>() {
            @Override
            public void onItemClick(RecyclerAdapter.ViewHolder holder, Image image) {
                if (onItemSelectClick(image)) {
                    //noinspection unchecked
                    holder.updateData(image);
                }
            }

            @Override
            public void onLongItemClick(RecyclerAdapter.ViewHolder holder, Image data) {

            }
        });
    }

    /**
     * 更新选择的数据
     *
     * @param images 相册中的图片集合
     */
    private void updateSource(List<Image> images) {
        mAdapter.replace(images);
    }

    /**
     * 初始化方法
     * @param manager  LoaderManager Loader管理器
     * @param listener  选择改变监听
     * @return  任何一个LOADER_ID  可以用于销毁Loader
     */
    public int setup(LoaderManager manager, SelectedChangeListener listener) {
        mListener = listener;
        manager.initLoader(LOADER_ID, null, callback);
        return LOADER_ID;
    }

    /**
     * 获取到选择过的图片的路径
     *
     * @return 图片路径集合
     */
    public String[] getSelectedPath() {
        String[] paths = new String[mSelectedImages.size()];
        int index = 0;
        for (Image mSelectedImage : mSelectedImages) {
            paths[index++] = mSelectedImage.path;
        }
        return paths;
    }

    /**
     * 清楚选择的数据
     */
    public void clear() {
        for (Image image : mSelectedImages) {
            image.isSelect = false;
        }
        mSelectedImages.clear();
        mAdapter.notifyDataSetChanged();
    }

    private void notifySelectChanged() {
        SelectedChangeListener listener = mListener;
        if (listener != null)
            listener.onSelectedCountChanged(mSelectedImages.size());
    }

    /**
     * item点击事件逻辑处理
     *
     * @param image 图片Item
     * @return true 选择  false 未选择
     */
    private boolean onItemSelectClick(Image image) {
        boolean notifyRefresh;
        if (mSelectedImages.contains(image)) {
            mSelectedImages.remove(image);
            image.isSelect = false;
            notifyRefresh = true;
        } else {
            if (mSelectedImages.size() >= MAX_IMAGE_COUNT) {
                //Cell点击操作  如果说我们的点击是允许的  那么更新对应的Cell状态
                //然后去更新界面  如果不允许点击(已经达到我们最大的选择数量)  那么就不需要刷新数据
                Toast.makeText(getContext(), String.format(
                                        getResources().getText(R.string.label_gallery_select_max_size).toString(),
                                        MAX_IMAGE_COUNT), Toast.LENGTH_SHORT).show();
//                showToast(String.format(
//                        getResources().getText(R.string.label_gallery_select_max_size).toString(),
//                        MAX_IMAGE_COUNT));
                notifyRefresh = false;
            } else {
                mSelectedImages.add(image);
                image.isSelect = true;
                notifyRefresh = true;
            }
        }
        if (notifyRefresh)
            notifySelectChanged();
        return notifyRefresh;
    }

    /**
     * 用于实际数据加载的Loader
     */
    private class LoaderCallback implements LoaderManager.LoaderCallbacks<Cursor> {
        private final String[] IMAGE_PROJECTION = {
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.DATE_ADDED};

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            if (id == LOADER_ID) {
                return new CursorLoader(getContext(),
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION,
                        null, null, IMAGE_PROJECTION[2] + " DESC");
            }
            return null;
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, final Cursor data) {
            //当Loader加载完成的时候回调方法
            List<Image> images = new ArrayList<>();
            if (data != null) {
                int count = data.getCount();
                if (count > 0) {
                    data.moveToFirst();
                    do {
                        int id = data.getInt(data.getColumnIndexOrThrow(IMAGE_PROJECTION[0]));
                        String path = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[1]));
                        long dateTime = data.getLong(data.getColumnIndexOrThrow(IMAGE_PROJECTION[2]));

                        File file = new File(path);
                        if (!file.exists() || file.length() < MIN_IMAGE_LEN)
                            continue;

                        Image image = new Image();
                        image.id = id;
                        image.path = path;
                        image.date = dateTime;
                        images.add(image);
                    } while (data.moveToNext());
                }
            }
            updateSource(images);
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
            //当Loader销毁或者重置
            updateSource(null);
        }
    }

    private static class Image {
        int id;  //数据的id
        String path;  //图片的路径
        boolean isSelect; //图片是否选择
        long date; //图片创建的日期

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Image image = (Image) o;

            return path != null ? path.equals(image.path) : image.path == null;

        }

        @Override
        public int hashCode() {
            return path != null ? path.hashCode() : 0;
        }
    }

    private class Adapter extends RecyclerAdapter<Image> {

        @Override
        protected ViewHolder<Image> onCreateViewHolder(View root, int viewType) {
            return new GalleryView.ViewHolder(root);
        }

        @Override
        protected int getItemViewType(int position, Image image) {
            return R.layout.cell_gallery;
        }

    }

    private class ViewHolder extends RecyclerAdapter.ViewHolder<Image> {
        private ImageView mPic;
        private View mShade;
        private CheckBox mSelected;


        public ViewHolder(View itemView) {
            super(itemView);
            mPic = (ImageView) itemView.findViewById(R.id.im_image);
            mShade = itemView.findViewById(R.id.view_shade);
            mSelected = (CheckBox) itemView.findViewById(R.id.cb_select);
        }

        @Override
        protected void onBind(Image image) {
            Glide.with(getContext())
                    .load(image.path)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .centerCrop()
                    .placeholder(R.color.grey_200)
                    .into(mPic);
            mShade.setVisibility(image.isSelect ? VISIBLE : INVISIBLE);
            mSelected.setChecked(image.isSelect);

            mSelected.setVisibility(image.isSelect ? VISIBLE : INVISIBLE);
        }
    }

    public interface SelectedChangeListener {
        void onSelectedCountChanged(int count);
    }
}
