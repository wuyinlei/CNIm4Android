package com.mingchu.common.tools;

import android.graphics.BitmapFactory;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.mingchu.common.app.Application;


import java.io.File;


/**
 * 图片压缩处理
 */
@SuppressWarnings("WeakerAccess")
public final class PicturesCompressor {
    private PicturesCompressor() {

    }

    public static boolean compressImage(final String srcPath,
                                        final String savePath,
                                        final long targetSize) {
        return compressImage(srcPath, savePath, targetSize, 75, 1280, 1280 * 6, null, null, true);
    }

    public static File loadWithGlideCache(String path) {
        File tmp;
        try {
            tmp = Glide.with(Application.getInstance())
                    .load(path)
                    .downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                    .get();
            String absPath = tmp.getAbsolutePath();
            Log.d("PicturesCompressor", "loadWithGlideCache:" + absPath);
            return tmp;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 压缩图片
     *
     * @param srcPath     原图地址
     * @param savePath    存储地址
     * @param maxSize     最大文件地址byte
     * @param minQuality  最小质量
     * @param maxWidth    最大宽度
     * @param maxHeight   最大高度
     * @param byteStorage 用于批量压缩时的buffer，不必要为null，
     *                    需要时，推荐 {{@link BitmapUtil#DEFAULT_BUFFER_SIZE}}
     * @param options     批量压缩时复用参数，可调用 {{@link BitmapUtil#createOptions()}} 得到
     * @param exactDecode 是否精确解码， TRUE： 在4.4及其以上机器中能更节约内存
     * @return 是否压缩成功
     */
    public static boolean compressImage(final String srcPath,
                                        final String savePath,
                                        final long maxSize,
                                        final int minQuality,
                                        final int maxWidth,
                                        final int maxHeight,
                                        byte[] byteStorage,
                                        BitmapFactory.Options options,
                                        boolean exactDecode) {
        boolean loadWithGlide = false;
        // build source file
        File inTmp = new File(srcPath);
        final File sourceFile;
        if (inTmp.exists()) {
            sourceFile = inTmp;
        } else {
            File tmp = loadWithGlideCache(srcPath);
            if (tmp == null)
                return false;
            sourceFile = tmp;
            loadWithGlide = true;
        }

        // build save file
        final File saveFile = new File(savePath);
        File saveDir = saveFile.getParentFile();
        if (!saveDir.exists()) {
            if (!saveDir.mkdirs())
                return false;
        }

        // End clear the out file data
        if (saveFile.exists()) {
            if (!saveFile.delete())
                return false;
        }

        // if the in file size <= maxSize, we can copy to savePath
        if (sourceFile.length() <= maxSize && confirmImage(sourceFile, options)) {
            return StreamUtil.copy(sourceFile, saveFile);
        }

        File realCacheFile;
        if (loadWithGlide) {
            realCacheFile = sourceFile;
        } else {
            realCacheFile = loadWithGlideCache(sourceFile.getAbsolutePath());
            if (realCacheFile == null)
                return false;
        }

        // Doing
        File tempFile = BitmapUtil.Compressor.compressImage(realCacheFile, maxSize, minQuality, maxWidth,
                maxHeight, byteStorage, options, exactDecode);

        // Rename to out file
        return tempFile != null && StreamUtil.copy(tempFile, saveFile) && tempFile.delete();
    }

    public static boolean confirmImage(File file, BitmapFactory.Options opts) {
        if (opts == null) opts = BitmapUtil.createOptions();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(file.getAbsolutePath(), opts);
        String mimeType = opts.outMimeType.toLowerCase();
        return mimeType.contains("jpeg") || mimeType.contains("png") || mimeType.contains("gif");
    }
}
