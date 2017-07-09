package com.mingchu.common.tools;

import android.media.AudioManager;
import android.media.MediaPlayer;

import java.io.IOException;

/**
 * 语音播放辅助类
 *
 * @param <Holder>
 */
public class AudioPlayHelper<Holder> implements MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener {
    private MediaPlayer mediaPlayer;
    private RecordPlayListener<Holder> listener;
    private Holder holder;
    private String currentPath;

    public AudioPlayHelper(RecordPlayListener<Holder> listener) {
        this.listener = listener;
        this.mediaPlayer = createNewMediaPlayer();
    }

    /**
     * 创建MediaPlayer
     *
     * @return MediaPlayer
     */
    private MediaPlayer createNewMediaPlayer() {
        MediaPlayer mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setLooping(false);
        mediaPlayer.setOnErrorListener(this);
        mediaPlayer.setOnCompletionListener(this);
        return mediaPlayer;
    }

    public void trigger(Holder holder, String filePath) {
        if (mediaPlayer == null)
            return;

        // 如果是当前的
        if (mediaPlayer.isPlaying() && filePath.equalsIgnoreCase(currentPath)) {
            stop();
            return;
        }

        stop();
        play(holder, filePath);
    }

    private void play(Holder holder, String filePath) {
        if (mediaPlayer == null)
            return;

        this.holder = holder;
        this.currentPath = filePath;
        try {
            this.mediaPlayer.setDataSource(filePath);
            this.mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
            this.listener.onPlayError(holder);
            stop();
            return;
        }
        this.mediaPlayer.start();
        this.listener.onPlayStart(holder);
    }

    private void stop() {
        if (mediaPlayer == null)
            return;

        mediaPlayer.stop();
        mediaPlayer.reset();
        currentPath = null;

        Holder holder = this.holder;
        this.holder = null;
        if (holder != null) {
            listener.onPlayStop(holder);
        }
    }

    public void destroy() {
        MediaPlayer mediaPlayer = this.mediaPlayer;
        if (mediaPlayer != null) {
            this.mediaPlayer = null;
            stop();
            mediaPlayer.release();
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        stop();
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        Holder holder = this.holder;
        if (holder != null) {
            this.listener.onPlayError(holder);
        }
        stop();
        return true;
    }

    public interface RecordPlayListener<Holder> {
        void onPlayStart(Holder holder);

        void onPlayStop(Holder holder);

        void onPlayError(Holder holder);
    }
}
