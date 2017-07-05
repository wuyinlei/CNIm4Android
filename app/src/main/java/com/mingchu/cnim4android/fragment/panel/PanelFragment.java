package com.mingchu.cnim4android.fragment.panel;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.mingchu.cnim4android.R;
import com.mingchu.common.app.Application;
import com.mingchu.common.app.BaseFragment;
import com.mingchu.common.face.Face;
import com.mingchu.common.face.FaceAdapter;
import com.mingchu.common.face.FaceListener;
import com.mingchu.common.tools.UiTool;
import com.mingchu.common.widget.GalleryView;

import net.qiujuer.genius.ui.Ui;

import java.io.File;

/**
 * A simple {@link Fragment} subclass.
 */
public class PanelFragment extends BaseFragment implements FaceListener {

    private View mFacePanel;
    private View mGalleryPanel;
//    private View mRecordPanel;
    private PanelCallback mCallback;

    public PanelFragment() {
        // Required empty public constructor
    }


    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_panel;
    }


    @Override
    protected void initView(View view) {
        super.initView(view);

        initFace(view);
        initGallery(view);
    }


    private void initFace(View root) {
        View facePanel = mFacePanel = root.findViewById(R.id.lay_face_panel);
        TabLayout tabLayout = (TabLayout) root.findViewById(R.id.tab);
        ViewPager pager = (ViewPager) facePanel.findViewById(R.id.pager);
        facePanel.findViewById(R.id.im_backspace).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackspaceClick();
            }
        });
        tabLayout.setupWithViewPager(pager);

        // Min 48dp
        final int minFaceSize = (int) Ui.dipToPx(getResources(), 48);
        final int totalWidth = UiTool.getScreenWidth(getActivity());
        final int spanCount = totalWidth / minFaceSize;

        pager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return Face.all(getContext()).size();
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                LayoutInflater inflater = LayoutInflater.from(container.getContext());
                RecyclerView recyclerView = (RecyclerView) inflater.inflate(R.layout.lay_face_content,
                        container, false);
                recyclerView.setLayoutManager(new GridLayoutManager(getContext(), spanCount));
                recyclerView.setAdapter(new FaceAdapter(Face.all(getContext()).get(position), PanelFragment.this));
                container.addView(recyclerView);
                return recyclerView;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return Face.all(getContext()).get(position).name;
            }
        });
    }



    private void initGallery(View root) {
        View galleryPanel = mGalleryPanel = root.findViewById(R.id.lay_gallery_panel);
        final GalleryView galleryView = (GalleryView) galleryPanel.findViewById(R.id.view_gallery);
        final TextView selectedSize = (TextView) galleryPanel.findViewById(R.id.txt_gallery_select_count);
        galleryView.setup(getLoaderManager(), new GalleryView.SelectedChangeListener() {
            @Override
            public void onSelectedCountChanged(int count) {
                selectedSize.setText(String.format(getText(R.string.label_gallery_selected_size).toString(),
                        count));
            }
        });
        galleryPanel.findViewById(R.id.btn_send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSendGalleryClick(galleryView, galleryView.getSelectedPath());
            }
        });
    }

    @Override
    public void onFaceClick(Face.Bean bean) {
        PanelCallback callback = mCallback;
        if (callback == null)
            return;
        EditText editText = callback.getInputEditText();
        Face.inputFace(editText.getContext(), editText.getText(), bean,
                (int) (editText.getTextSize() + Ui.dipToPx(getResources(), 2)));
    }

    private void onBackspaceClick() {
        PanelCallback callback = mCallback;
        if (callback == null)
            return;
        KeyEvent event = new KeyEvent(0, 0, 0, KeyEvent.KEYCODE_DEL, 0, 0, 0,
                0, KeyEvent.KEYCODE_ENDCALL);
        callback.getInputEditText().dispatchKeyEvent(event);
    }

    private void onSendGalleryClick(GalleryView galleryView, String[] paths) {
        galleryView.clear();
        PanelCallback callback = mCallback;
        if (callback == null)
            return;
        callback.onSendGalleryClick(paths);
    }


    public void setup(PanelCallback callback) {
        this.mCallback = callback;
    }

    public boolean isOpenFace() {
        return mFacePanel.getVisibility() == View.VISIBLE;
    }


    public boolean isOpenMore() {
        return mGalleryPanel.getVisibility() == View.VISIBLE;
    }

    public void showFace() {
        mFacePanel.setVisibility(View.VISIBLE);
        mGalleryPanel.setVisibility(View.GONE);
    }

    public void showRecord() {
        mFacePanel.setVisibility(View.GONE);
        mGalleryPanel.setVisibility(View.GONE);
    }

    public void showGallery() {
        mFacePanel.setVisibility(View.GONE);
        mGalleryPanel.setVisibility(View.VISIBLE);
        GalleryView view = (GalleryView) mGalleryPanel.findViewById(R.id.view_gallery);
        view.clear();
    }

    public void showMore() {
        showGallery();
    }

    public interface PanelCallback {
        EditText getInputEditText();

        void onSendGalleryClick(String[] paths);

        void onRecordDone(File file, long time);
    }
}
