package com.mingchu.factory.data.helper;

import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.mingchu.common.Common;
import com.mingchu.common.app.Application;
import com.mingchu.common.tools.PicturesCompressor;
import com.mingchu.common.tools.StreamUtil;
import com.mingchu.factory.Factory;
import com.mingchu.factory.model.api.RspModel;
import com.mingchu.factory.model.api.message.MsgCreateModel;
import com.mingchu.factory.model.card.MessageCard;
import com.mingchu.factory.model.card.UserCard;
import com.mingchu.factory.model.db.Message;
import com.mingchu.factory.net.NetWork;
import com.mingchu.factory.net.RemoteService;
import com.mingchu.factory.net.UploadHelper;
import com.raizlabs.android.dbflow.StringUtils;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.mingchu.factory.model.db.Message_Table;

import java.io.File;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by wuyinlei on 2017/6/15.
 *
 * @function 消息工具类
 */

public class MessageHelper {


    public static Message findLastWithGroup(String groupId) {

        return SQLite.select()
                .from(Message.class)
                .where(Message_Table.group_id.eq(groupId))
                .orderBy(Message_Table.createAt, false)
                .querySingle();
    }

    public static Message findLastWithUser(String userId) {
        return SQLite.select()
                .from(Message.class)
                .where(Message_Table.sender_id.eq(userId))
                .or(Message_Table.receiver_id.eq(userId))
                .orderBy(Message_Table.createAt, false)
                .querySingle();
    }

    /**
     * 从本地拿取数据
     *
     * @param id id
     * @return Message
     */
    public static Message findFromLocal(String id) {

        return SQLite.select()
                .from(Message.class)
                .where(Message_Table.id.eq(id))
                .querySingle();
    }

    //发送是异步进行的
    public static void push(final MsgCreateModel model) {

        Factory.runOnAsync(new Runnable() {
            @Override
            public void run() {

                //成功状态：如果是一个已经发送过的消息  则不能重新发送
                //正在发送状态  如果是一个消息正在发送  则不能重新发送
                //如果是一个已经发送过的消息  则不能重新发送
                Message message = findFromLocal(model.getId());

                //已经发送过的
                if (message != null && message.getStatus() != Message.STATUS_FAILED){
                    return;
                }

                //如果是文件类型(语音 图片  文件)  需要先上传后才发送

                //我们发送的时候要对界面进行更新  通过Card

                //正常情况的消息(文字)  直接发送

                final MessageCard card = model.buildCard();

                Factory.getMessageCenter().dispatch(card);

                if (card.getType() != Message.TYPE_STR){
                    //上传文件类型
                    //发送文件消息分两步   上传到云服务器   消息push到我们自己的服务器
                    if (!card.getContent().startsWith(UploadHelper.ENDPOINT)){
                       //没有上传到云服务器  还是本地手机文件
                        String content = null;

                        switch (card.getType()){
                            case Message.TYPE_PIC:

                                content = uploadPicture(card.getContent());

                                break;

                            case Message.TYPE_AUDIO:
                                content = uploadAudio(card.getContent());
                                break;

                            default:

                                break;

                        }

                        if (TextUtils.isEmpty(content)){
                            //失败
                            card.setStatus(Message.STATUS_FAILED);
                            Factory.getMessageCenter().dispatch(card);
                        }

                        //成功 则把网络路径进行替换
                        card.setContent(content);
                        Factory.getMessageCenter().dispatch(card);

                        //因为卡片的内容改变了  而我们上传到服务器的是model
                        //要更新model
                        model.refreshByCard();

                    }
                    //这个已经是外网的地址了


                }

                RemoteService service = NetWork.remote();
                service.msgPush(model)
                        .enqueue(new Callback<RspModel<MessageCard>>() {
                            @Override
                            public void onResponse(Call<RspModel<MessageCard>> call, Response<RspModel<MessageCard>> response) {
                                RspModel<MessageCard> rspModel = response.body();
                                if (rspModel != null && rspModel.success()) {

                                    MessageCard newCard = rspModel.getResult();
                                    if (newCard != null) {
                                        //成功的时候进行一次调度
                                        Factory.getMessageCenter().dispatch(newCard);
                                    }

                                } else {

                                    //解析一下是否是账户异常
                                    Factory.decodeRspCode(rspModel, null);

                                    //走失败流程
                                    onFailure(call, null);
                                }
                            }

                            @Override
                            public void onFailure(Call<RspModel<MessageCard>> call, Throwable t) {
                                //设置MessageCard的发送状态为失败状态
                                card.setStatus(Message.STATUS_FAILED);
                                //然后重新发送
                                Factory.getMessageCenter().dispatch(card);
                            }
                        });

            }
        });


    }

    private static String uploadAudio(String content) {
        // TODO: 2017/7/9   上传语音

        return null;
    }

    private static String uploadPicture(String path) {
        //上传图片
        File file = null;

        try {
            //通过glide的缓存区间 解决了图片外部权限的问题
            file = Glide.with(Factory.app())
                    .load(path)
                    .downloadOnly(Target.SIZE_ORIGINAL,Target.SIZE_ORIGINAL)
                    .get();

        }catch (Exception e){
            Log.d("MessageHelper", e.getMessage());
        }

        if (file != null){

            //进行压缩
            String cacheDir = Application.getCacheDirFile().getAbsolutePath();
            String tempFile = String.format("%s/image/Cache_%s.png",cacheDir, SystemClock.uptimeMillis());

//            if (Picture)
            if (PicturesCompressor.compressImage(path, tempFile, Common.Constance.MAX_UPLOAD_IMAGE_LENGTH)) {
                String ossPath = UploadHelper.uploadImage(tempFile);
                StreamUtil.delete(tempFile);
                return ossPath;
            }


            String ossPath = UploadHelper.uploadImage(tempFile);

            StreamUtil.delete(tempFile);

            return ossPath;

        }
        return null;

    }
}
