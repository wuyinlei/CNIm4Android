package com.mingchu.common.tools;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Build;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static com.mingchu.common.tools.StreamUtil.close;


/**
 * Bitmap 工具类
 */
public final class BitmapUtil {
    /**
     * A default size to use to increase hit rates when the required size isn't defined.
     * Currently 64KB.
     */
    public final static int DEFAULT_BUFFER_SIZE = 64 * 1024;

    /**
     * 创建一个图片处理Options
     *
     * @return {@link BitmapFactory.Options}
     */
    public static BitmapFactory.Options createOptions() {
        return new BitmapFactory.Options();
    }

    /**
     * 把一个{@link BitmapFactory.Options}进行参数复原操作，
     * 避免重复创建新的 {@link BitmapFactory.Options}
     *
     * @param options {@link BitmapFactory.Options}
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static void resetOptions(BitmapFactory.Options options) {
        options.inTempStorage = null;
        options.inDither = false;
        options.inScaled = false;
        options.inSampleSize = 1;
        options.inPreferredConfig = null;
        options.inJustDecodeBounds = false;
        options.inDensity = 0;
        options.inTargetDensity = 0;
        options.outWidth = 0;
        options.outHeight = 0;
        options.outMimeType = null;

        if (Build.VERSION_CODES.HONEYCOMB <= Build.VERSION.SDK_INT) {
            options.inBitmap = null;
            options.inMutable = true;
        }
    }

    /**
     * 获取图片的真实后缀
     *
     * @param filePath 图片存储地址
     * @return 图片类型后缀
     */
    public static String getExtension(String filePath) {
        BitmapFactory.Options options = createOptions();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        String mimeType = options.outMimeType;
        return mimeType.substring(mimeType.lastIndexOf("/") + 1);
    }

    public static Bitmap decodeBitmap(final File file,
                                      final int maxWidth,
                                      final int maxHeight,
                                      byte[] byteStorage,
                                      BitmapFactory.Options options,
                                      boolean exactDecode) {
        InputStream is;
        try {
            // In this, we can set the buffer size
            is = new BufferedInputStream(new FileInputStream(file),
                    byteStorage == null ? DEFAULT_BUFFER_SIZE : byteStorage.length);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }

        if (options == null)
            options = createOptions();
        else
            resetOptions(options);

        // First decode with inJustDecodeBounds=true to check dimensions
        options.inJustDecodeBounds = true;

        // 5MB. This is the max image header size we can handle, we preallocate a much smaller buffer
        // but will resize up to this amount if necessary.
        is.mark(5 * 1024 * 1024);
        BitmapFactory.decodeStream(is, null, options);

        // Reset the inputStream
        try {
            is.reset();
        } catch (IOException e) {
            e.printStackTrace();
            close(is);
            resetOptions(options);
            return null;
        }

        // Calculate inSampleSize
        calculateScaling(options, maxWidth, maxHeight, exactDecode);

        // Init the BitmapFactory.Options.inTempStorage value
        if (byteStorage == null)
            byteStorage = new byte[DEFAULT_BUFFER_SIZE];
        options.inTempStorage = byteStorage;

        // Decode bitmap with inSampleSize set FALSE
        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeStream(is, null, options);

        // Close the Stream
        close(is);
        // And Reset the option
        resetOptions(options);

        // To scale bitmap to user set
        bitmap = scaleBitmap(bitmap, maxWidth, maxHeight, true);

        return bitmap;

    }

    /**
     * 按长宽比缩小一个Bitmap
     *
     * @param source        待缩小的{@link Bitmap}
     * @param scale         缩放比0～1，1代表不缩放
     * @param recycleSource 是否释放Bitmap源
     * @return 一个缩小后的Bitmap
     */
    public static Bitmap scaleBitmap(Bitmap source, float scale, boolean recycleSource) {
        if (scale <= 0 || scale >= 1)
            return source;
        Matrix m = new Matrix();
        final int width = source.getWidth();
        final int height = source.getHeight();
        m.setScale(scale, scale);
        Bitmap scaledBitmap = Bitmap.createBitmap(source, 0, 0, width, height, m, false);
        if (recycleSource)
            source.recycle();
        return scaledBitmap;
    }

    /**
     * 按照长宽比缩小一个Bitmap到指定尺寸，
     * 当传入的高宽都大于原始值时将不做缩小操作
     *
     * @param source          待缩小的{@link Bitmap}
     * @param targetMaxWidth  目标宽度
     * @param targetMaxHeight 目标高度
     * @param recycleSource   是否释放Bitmap源
     * @return 一个缩小后的Bitmap
     */
    public static Bitmap scaleBitmap(Bitmap source, int targetMaxWidth, int targetMaxHeight, boolean recycleSource) {
        int sourceWidth = source.getWidth();
        int sourceHeight = source.getHeight();

        Bitmap scaledBitmap = source;
        if (sourceWidth > targetMaxWidth || sourceHeight > targetMaxHeight) {
            float minScale = Math.min(targetMaxWidth / (float) sourceWidth,
                    targetMaxHeight / (float) sourceHeight);
            scaledBitmap = Bitmap.createScaledBitmap(scaledBitmap,
                    (int) (sourceWidth * minScale),
                    (int) (sourceHeight * minScale), false);
            if (recycleSource)
                source.recycle();
        }

        return scaledBitmap;
    }

    /**
     * 通过{@link BitmapFactory.Options}计算图片的缩放比,
     * 并将缩放后的信息存储在传入的{@link BitmapFactory.Options}中，
     * 以便后续的 {@link BitmapFactory#decodeStream(InputStream)}等操作
     *
     * @param options            一个图片的{@link BitmapFactory.Options}， 含有图片的基础信息
     * @param requestedMaxWidth  目标宽度
     * @param requestedMaxHeight 目标高度
     * @param exactDecode        是否精确计算，该参数只在 {@link Build.VERSION#SDK_INT} 大于 Api19 时有效
     */
    private static BitmapFactory.Options calculateScaling(BitmapFactory.Options options,
                                                          final int requestedMaxWidth,
                                                          final int requestedMaxHeight,
                                                          boolean exactDecode) {
        int sourceWidth = options.outWidth;
        int sourceHeight = options.outHeight;

        if (sourceWidth <= requestedMaxWidth && sourceHeight <= requestedMaxHeight) {
            return options;
        }

        final float maxFloatFactor = Math.max(sourceHeight / (float) requestedMaxHeight,
                sourceWidth / (float) requestedMaxWidth);
        final int maxIntegerFactor = (int) Math.floor(maxFloatFactor);
        final int lesserOrEqualSampleSize = Math.max(1, Integer.highestOneBit(maxIntegerFactor));

        options.inSampleSize = lesserOrEqualSampleSize;
        // Density scaling is only supported if inBitmap is null prior to KitKat. Avoid setting
        // densities here so we calculate the final Bitmap size correctly.
        if (exactDecode && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            float scaleSize = sourceWidth / (float) lesserOrEqualSampleSize;
            float outSize = sourceWidth / maxFloatFactor;

            options.inTargetDensity = 1000;
            options.inDensity = (int) (1000 * (scaleSize / outSize) + 0.5);

            // If isScaling
            if (options.inTargetDensity != options.inDensity) {
                options.inScaled = true;
            } else {
                options.inDensity = options.inTargetDensity = 0;
            }
        }
        return options;
    }

    public final static class Compressor {

        public static File compressImage(final File sourceFile, final long maxSize,
                                         final int minQuality, final int maxWidth,
                                         final int maxHeight) {
            return compressImage(sourceFile, maxSize, minQuality, maxWidth, maxHeight, true);
        }

        public static File compressImage(final File sourceFile, final long maxSize,
                                         final int minQuality, final int maxWidth,
                                         final int maxHeight, boolean exactDecode) {
            return compressImage(sourceFile, maxSize, minQuality, maxWidth, maxHeight, null, null, exactDecode);
        }

        /**
         * 压缩图片
         *
         * @param sourceFile  原图地址
         * @param maxSize     最大文件地址byte
         * @param minQuality  最小质量
         * @param maxWidth    最大宽度
         * @param maxHeight   最大高度
         * @param byteStorage 用于批量压缩时的buffer，不必要为null，
         *                    需要时，推荐 {{@link #DEFAULT_BUFFER_SIZE}}
         * @param options     批量压缩时复用参数，可调用 {{@link #createOptions()}} 得到
         * @param exactDecode 是否精确解码， TRUE： 在4.4及其以上机器中能更节约内存
         * @return 返回压缩后的图片文件，该图片存储在原图同级目录下，以compress.temp结尾
         */
        public static File compressImage(final File sourceFile,
                                         final long maxSize,
                                         final int minQuality,
                                         final int maxWidth,
                                         final int maxHeight,
                                         byte[] byteStorage,
                                         BitmapFactory.Options options,
                                         boolean exactDecode) {
            // build source file
            if (sourceFile == null || !sourceFile.exists() || !sourceFile.canRead())
                return null;

            // create new temp file
            final File tempFile = new File(sourceFile.getParent(),
                    String.format("compress_%s.temp", System.currentTimeMillis()));

            if (!tempFile.exists()) {
                try {
                    if (!tempFile.createNewFile())
                        return null;
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }

            // build to bitmap
            Bitmap bitmap = decodeBitmap(sourceFile, maxWidth, maxHeight, byteStorage, options, exactDecode);
            if (bitmap == null)
                return null;

            // Get the bitmap format
            Bitmap.CompressFormat compressFormat = bitmap.hasAlpha() ?
                    Bitmap.CompressFormat.PNG : Bitmap.CompressFormat.JPEG;

            // Write to out put file
            boolean isOk = false;
            for (int i = 1; i <= 10; i++) {
                // In this we change the quality start 92%
                int quality = 92;
                for (; ; ) {
                    BufferedOutputStream outputStream = null;
                    try {
                        outputStream = new BufferedOutputStream(new FileOutputStream(tempFile));
                        bitmap.compress(compressFormat, quality, outputStream);
                    } catch (IOException e) {
                        e.printStackTrace();
                        // on IOException we need recycle the bitmap
                        bitmap.recycle();
                        return null;
                    } finally {
                        close(outputStream);
                    }
                    // Check file size
                    long outSize = tempFile.length();
                    if (outSize <= maxSize) {
                        isOk = true;
                        break;
                    }
                    if (quality < minQuality)
                        break;
                    quality--;
                }

                if (isOk) {
                    break;
                } else {
                    // If not ok, we need scale the Bitmap to small
                    // In this, once subtract 2%, most 20%
                    bitmap = scaleBitmap(bitmap, 1 - (0.2f * i), true);
                }
            }
            // recycle bitmap
            bitmap.recycle();

            // The end, If not success, return false
            if (!isOk)
                return null;

            // Rename to out file
            return tempFile;
        }
    }
}
