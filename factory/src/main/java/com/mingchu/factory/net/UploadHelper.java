package com.mingchu.factory.net;

import android.text.format.DateFormat;
import android.util.Log;

import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.mingchu.common.Common;
import com.mingchu.common.tools.HashUtil;
import com.mingchu.factory.Factory;

import java.io.File;
import java.util.Date;


/**
 * Created by wuyinlei on 2017/6/8.
 * <p>
 * 上传工具类 用于上传任意文件
 */

public class UploadHelper {

    private static final String TAG = UploadHelper.class.getName();

    //APP_KEY
    private static final String ACCESS_KEY_ID = "LTAImUdBwv9uCm0s";
    //KEY_SECRET
    private static final String ACCESS_KEY_SECRET = "3LzVjvZVbad14UziBKux1gsEnxugMn";

    //终结点  与我们自己的申请的存储区域有关系
    private static final String ENDPOINT = "http://oss-cn-shanghai.aliyuncs.com";

    //上传的仓库名
    private static final String BUCKET_NAME = "ruolan";

    private static OSS getClient() {
        OSSCredentialProvider credentialProvider = new
                OSSPlainTextAKSKCredentialProvider(ACCESS_KEY_ID, ACCESS_KEY_SECRET);

        return new OSSClient(Factory.app(), ENDPOINT, credentialProvider);
    }


    /**
     * 上传录音文件
     *
     * @param path 本地地址
     * @return 上传到服务器的录音文件地址
     */
    public static String uploadAudio(String path) {
        final String objKey = getAudioObjKey(path);
        return upload(objKey, path);
    }

    /**
     * 上传普通的图片
     *
     * @param path 本地图片地址
     * @return 返回服务器图片地址
     */
    public static String uploadImage(String path) {
        final String objKey = getImageObjKey(path);
        return upload(objKey, path);
    }

    /**
     * 上传头像
     *
     * @param path 本地头像地址
     * @return 服务器返回的头像地址
     */
    public static String uploadPortrait(String path) {
        final String objKey = getPortraitObjKey(path);
        return upload(objKey, path);
    }

    /**
     * 上传最终方法  成功返回一个路径
     *
     * @param objKey 上传上去后  在服务器上的独立的KEY
     * @param path   返回的图片地址
     * @return 返回存储的图片的地址
     */
    private static String upload(String objKey, String path) {
// 构造上传请求
        PutObjectRequest request = new PutObjectRequest(BUCKET_NAME, objKey, path);

        try {
            //初始化上传的client
            OSS client = getClient();
            //开始上传
            PutObjectResult result = client.putObject(request);
            //得到一个外网的可以访问的地址
            String url = client.presignPublicObjectURL(BUCKET_NAME, objKey);
            Log.d(TAG, String.format("PublicObjectURL:%s", url));
            return url;

        } catch (Exception e) {
            //如果有异常  返回null
            Log.d(TAG, e.getMessage());
            return null;
        }
    }


    private static String getDateStamp() {
        return DateFormat.format("yyyyMM", new Date()).toString();
    }


    /**
     * Image的KEY
     *
     * @param path 本地图片地址
     * @return KEY
     */
    private static String getImageObjKey(final String path) {
        String fileHash = HashUtil.getMD5String(new File(path));
        String ext = Common.Path.getExt(path, "png");
        return String.format("image/%s/%s.%s", getDateStamp(), fileHash, ext);
    }

    /**
     * 头像的KEY
     *
     * @param path 头像本地地址
     * @return KEY
     */
    private static String getPortraitObjKey(final String path) {
        String fileHash = HashUtil.getMD5String(new File(path));
        String ext = Common.Path.getExt(path, "png");
        return String.format("portrait/%s/%s.%s", getDateStamp(), fileHash, ext);
    }

    /**
     * 录音文件kEY
     *
     * @param path 本地录音文件地址
     * @return KEY
     */
    private static String getAudioObjKey(final String path) {
        String fileHash = HashUtil.getMD5String(new File(path));
        String ext = Common.Path.getExt(path, "amr");
        return String.format("audio/%s/%s.%s", getDateStamp(), fileHash, ext);
    }


}
