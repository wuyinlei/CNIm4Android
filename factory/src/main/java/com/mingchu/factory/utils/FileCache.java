package com.mingchu.factory.utils;

import android.net.Network;

import com.mingchu.common.app.Application;
import com.mingchu.common.tools.HashUtil;
import com.mingchu.common.tools.StreamUtil;
import com.mingchu.factory.net.NetWork;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FileCache<Holder> {
    private File baseDir;
    private String ext;
    private CacheCallback<Holder> callback;
    private SoftReference<Holder> holderSoftReference;

    /**
     * 构造函数
     *
     * @param baseDir       基础的文件地址
     * @param ext           后缀
     * @param cacheCallback 回调接口
     */
    public FileCache(String baseDir, String ext, CacheCallback<Holder> cacheCallback) {
        this.baseDir = new File(Application.getCacheDirFile(), baseDir);
        this.ext = ext;
        this.callback = cacheCallback;
    }

    /**
     * 创建缓存文件
     *
     * @param path 文件地址
     * @return File
     */
    private File buildFile(String path) {
        String key = HashUtil.getMD5String(path);
        return new File(baseDir, key + "." + ext);
    }

    /**
     * 下载音频
     *
     * @param holder holder
     * @param path   音频网络地址
     */
    public void download(Holder holder, String path) {
        if (path.startsWith(Application.getCacheDirFile().getAbsolutePath())) {
            callback.onDownloadSucceed(holder, new File(path));
            return;
        }

        final File cacheFile = buildFile(path);
        if (cacheFile.exists() && cacheFile.length() > 0) {
            callback.onDownloadSucceed(holder, cacheFile);
            return;
        }

        holderSoftReference = new SoftReference<Holder>(holder);

        OkHttpClient client = NetWork.getClient();
        Request request = new Request.Builder()
                .url(path)
                .get()
                .build();

        Call call = client.newCall(request);
        call.enqueue(new NetCallback(holder, cacheFile));
    }

    private Holder getHolderAndClear() {
        if (holderSoftReference == null)
            return null;
        else {
            Holder holder = holderSoftReference.get();
            holderSoftReference.clear();
            return holder;
        }
    }


    /**
     * 网络请求回来的回调
     */
    private class NetCallback implements Callback {
        private final SoftReference<Holder> holderSoftReference;
        private final File file;

        NetCallback(Holder holder, File file) {
            this.holderSoftReference = new SoftReference<Holder>(holder);
            this.file = file;
        }

        @Override
        public void onFailure(Call call, IOException e) {
            Holder holder = holderSoftReference.get();
            if (holder != null && holder == FileCache.this.getHolderAndClear()) {
                FileCache.this.callback.onDownloadFailed(holder);
            }
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            InputStream inputStream = response.body().byteStream();
            if (!StreamUtil.copy(inputStream, file)) {
                onFailure(call, null);
                return;
            }

            Holder holder = holderSoftReference.get();
            if (holder != null && holder == FileCache.this.getHolderAndClear()) {
                FileCache.this.callback.onDownloadSucceed(holder, file);
            }
        }
    }

    public interface CacheCallback<Holder> {

        /**
         * 成功的回调
         *
         * @param holder holder
         * @param cache  文件缓存
         */
        void onDownloadSucceed(Holder holder, File cache);

        /**
         * 失败的回调
         *
         * @param holder holder
         */
        void onDownloadFailed(Holder holder);
    }
}
