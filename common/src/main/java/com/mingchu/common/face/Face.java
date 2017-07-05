package com.mingchu.common.face;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.util.ArrayMap;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.mingchu.common.R;
import com.mingchu.common.tools.StreamUtil;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * @author qiujuer Email:qiujuer@live.cn
 * @version 1.0.0
 */

public final class Face {
    private static List<FaceTab> FACE_TABS = null;
    private static final ArrayMap<String, Bean> FACE_MAP = new ArrayMap<>();

    private static void init(Context context) {
        if (FACE_TABS == null) {
            synchronized (Face.class) {
                if (FACE_TABS == null) {
                    ArrayList<FaceTab> faceTabs = new ArrayList<>();
                    faceTabs.add(initResourceFace(context));
                    initMap(faceTabs);
                    FACE_TABS = Collections.unmodifiableList(faceTabs);
                }
            }
        }
    }

    private static FaceTab initResourceFace(Context context) {
        ArrayList<Bean> faces = new ArrayList<>();
        Resources resources = context.getResources();
        String packageName = context.getApplicationInfo().packageName;
        for (int i = 1; i <= 142; i++) {
            String key = String.format(Locale.ENGLISH, "fb%03d", i);
            String resStr = String.format(Locale.ENGLISH, "face_base_%03d", i);
            int resId = resources.getIdentifier(resStr, "drawable", packageName);
            faces.add(new Bean(key, resId));
        }
        return new FaceTab("BASE", faces.get(0).previewId, faces);
    }


    private static void initMap(ArrayList<FaceTab> faceTabs) {
        for (FaceTab faceTab : faceTabs) {
            if (faceTab != null)
                faceTab.copyToMap(FACE_MAP);
        }
    }


    private static void upZipFile(File zipFile, File desDir) throws IOException {
        final String folderPath = desDir.getAbsolutePath();
        ZipFile zf = new ZipFile(zipFile);
        for (Enumeration<?> entries = zf.entries(); entries.hasMoreElements(); ) {
            ZipEntry entry = ((ZipEntry) entries.nextElement());
            InputStream in = zf.getInputStream(entry);
            String name = entry.getName();
            if (name.startsWith("f-")) {
                String str = folderPath + File.separator + name;
                str = new String(str.getBytes("8859_1"), "GB2312");
                File desFile = new File(str);
                StreamUtil.copy(in, desFile);
            }
        }
    }

    public static List<FaceTab> all(@NonNull Context context) {
        init(context);
        return FACE_TABS;
    }

    public static Bean get(@NonNull Context context, @NonNull String key) {
        init(context);

        if (FACE_MAP.containsKey(key)) {
            return FACE_MAP.get(key);
        }

        return null;
    }

    @SuppressWarnings("WeakerAccess")
    public static class FaceTab {
        FaceTab(String name, Object preview, List<Bean> faces) {
            this.name = name;
            this.preview = preview;
            // Un Modifiable contacts
            this.faces = Collections.unmodifiableList(faces);
        }

        public List<Bean> faces = new ArrayList<>();
        public String name;
        public Object preview;

        void copyToMap(Map<String, Bean> map) {
            for (Bean face : this.faces) {
                map.put(face.key, face);
            }
        }
    }


    @SuppressWarnings("WeakerAccess")
    public static class Bean {
        public String key;
        public Object resId;
        public Object previewId;
        public String desc = "表情";

        Bean(String key, Object resId) {
            this(key, resId, resId);
        }

        Bean(String key, Object resId, Object previewId) {
            this.key = key;
            this.resId = resId;
            this.previewId = previewId;
        }
    }

    public static void inputFace(final Context context, final Editable editable, final Face.Bean bean, final int size) {
        Glide.with(context)
                .load(bean.previewId)
                .asBitmap()
                //.fitCenter()
                .into(new SimpleTarget<Bitmap>(size, size) {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        Spannable spannable = new SpannableString(String.format("[%s]", bean.key));
                        ImageSpan span = new ImageSpan(context, resource, ImageSpan.ALIGN_BASELINE);
                        spannable.setSpan(span, 0, spannable.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        editable.append(spannable);
                    }
                });
    }


    public static Spannable decodeFace(final View target, final Spannable spannable, final int size) {
        if (spannable == null)
            return null;

        String str = spannable.toString();
        if (TextUtils.isEmpty(str))
            return null;

        final Context context = target.getContext();

        //Pattern pattern = Pattern.compile("(\\[[^\\[\\]:\\s\\n]+\\])|(:[^:\\[\\]\\s\\n]+:)");
        Pattern pattern = Pattern.compile("(\\[[^\\[\\]:\\s\\n]+\\])");
        Matcher matcher = pattern.matcher(str);

        boolean isFirst = true;
        while (matcher.find()) {
            String key = matcher.group();
            if (TextUtils.isEmpty(key)) continue;
            Bean bean = get(context, key.replace("[", "").replace("]", ""));
            if (bean == null)
                continue;

            final int start = matcher.start();
            final int end = matcher.end();

            FaceSpan span = new FaceSpan(context, target, bean, size);
            spannable.setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        return spannable;
    }


    public static class FaceSpan extends ImageSpan implements Drawable.Callback {
        private Bean bean;
        private Drawable mDrawable;
        private final View mView;
        private final int mSize;

        public FaceSpan(Context context, View view, Bean bean, int size) {
            super(context, R.drawable.default_face, ALIGN_BASELINE);
            this.bean = bean;
            this.mView = view;
            this.mSize = size;

            Glide.with(context)
                    .load(bean.previewId)
                    .fitCenter()
                    .into(new SimpleTarget<GlideDrawable>(size, size) {
                        @Override
                        public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                            mDrawable = resource;
                            int width = mDrawable.getIntrinsicWidth();
                            int height = mDrawable.getIntrinsicHeight();
                            mDrawable.setBounds(0, 0, width > 0 ? width : 0, height > 0 ? height : 0);
                            mDrawable.setCallback(FaceSpan.this);
                            /*
                            if (resource.isAnimated()) {
                                resource.setLoopCount(GlideDrawable.LOOP_FOREVER);
                                resource.start();
                            }
                            */
                            mDrawable.invalidateSelf();
                        }
                    });
        }

        @Override
        public int getSize(Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt fm) {
            if (mDrawable != null)
                return super.getSize(paint, text, start, end, fm);

            Rect rect = new Rect(0, 0, mSize, mSize);

            if (fm != null) {
                fm.ascent = -rect.bottom;
                fm.descent = 0;

                fm.top = fm.ascent;
                fm.bottom = 0;
            }

            return rect.right;
        }

        @Override
        public Drawable getDrawable() {
            return mDrawable == null ? null : mDrawable;
        }

        @Override
        public void invalidateDrawable(@NonNull Drawable who) {
            if (mView != null) mView.invalidate();
        }

        @Override
        public void scheduleDrawable(@NonNull Drawable who, @NonNull Runnable what, long when) {
            if (mView != null) mView.postDelayed(what, when);
        }

        @Override
        public void unscheduleDrawable(@NonNull Drawable who, @NonNull Runnable what) {
            if (mView != null) mView.removeCallbacks(what);
        }

        @Override
        public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint) {
            if (mDrawable != null)
                super.draw(canvas, text, start, end, x, top, y, bottom, paint);
        }
    }
}
