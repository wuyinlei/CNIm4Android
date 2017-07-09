package com.mingchu.common.tools;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.SystemClock;
import android.util.Log;

import com.mingchu.common.app.Application;

import net.qiujuer.lame.Lame;
import net.qiujuer.lame.LameAsyncEncoder;
import net.qiujuer.lame.LameOutputStream;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 录制音频的辅助工具类
 */
public class AudioRecordHelper {
    private static final String TAG = AudioRecordHelper.class.getSimpleName();
    private static final int[] SAMPLE_RATES = new int[]{44100, 22050, 11025, 8000};
    private RecordCallback callback;
    private File tmpFile;
    private int minBufferSize;
    private boolean isDone;
    private boolean isCancel;

    public AudioRecordHelper(File tmpFile, RecordCallback callback) {
        this.tmpFile = tmpFile;
        this.callback = callback;
    }

    private AudioRecord initAudioRecord() {
        for (int rate : SAMPLE_RATES) {
            for (short audioFormat : new short[]{AudioFormat.ENCODING_PCM_16BIT, AudioFormat.ENCODING_PCM_8BIT}) {
                for (short channelConfig : new short[]{AudioFormat.CHANNEL_IN_STEREO, AudioFormat.CHANNEL_IN_MONO}) {
                    try {
                        Log.d(TAG, "Attempting rate " + rate + "Hz, bits: " + audioFormat + ", channel: "
                                + channelConfig);
                        int bufferSize = AudioRecord.getMinBufferSize(rate, channelConfig, audioFormat);

                        if (bufferSize != AudioRecord.ERROR_BAD_VALUE) {
                            // check if we can instantiate and have a success
                            AudioRecord recorder = new AudioRecord(MediaRecorder.AudioSource.DEFAULT, rate, channelConfig, audioFormat, bufferSize);

                            if (recorder.getState() == AudioRecord.STATE_INITIALIZED) {
                                minBufferSize = bufferSize;
                                return recorder;
                            }
                        }
                    } catch (Exception e) {
                        Log.e(TAG, rate + "Exception, keep trying.", e);
                    }
                }
            }
        }
        return null;
    }

    private File initTmpFile() {
        if (tmpFile.exists()) {
            //noinspection ResultOfMethodCallIgnored
            tmpFile.delete();
        }
        try {
            if (tmpFile.createNewFile())
                return tmpFile;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    /*
    private MediaRecorder createNewMediaRecorder() {
        MediaRecorder record = new MediaRecorder();
        record.setAudioSource(MediaRecorder.AudioSource.MIC);
        record.setAudioChannels(1);
        // 先写输出
        record.setOutputFormat(MediaRecorder.OutputFormat.AMR_WB);
        record.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_WB);
        record.setAudioEncodingBitRate(96000);
        record.setAudioSamplingRate(44100);
        record.setOutputFile(tmpFile.getAbsolutePath());

        // Start
        try {
            record.prepare();
            record.start();
        } catch (IOException | RuntimeException e) {
            e.printStackTrace();
            record.release();
            return null;
        }

        // Stop
        record.stop();
        record.release();

        return record;
    }
    */

    public void recordAsync() {
        Thread thread = new Thread() {
            @Override
            public void run() {
                record();
            }
        };
        thread.start();
    }

    public File record() {
        isCancel = false;
        isDone = false;

        AudioRecord audioRecorder;
        File file;
        if ((audioRecorder = initAudioRecord()) == null
                || (file = initTmpFile()) == null) {
            Application.showToast("Record init error!");
            return null;
        }


        BufferedOutputStream outputStream;
        try {
            outputStream = new BufferedOutputStream(new FileOutputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }

        final long startTime = SystemClock.uptimeMillis();
        final int bufferSize = minBufferSize;
        final RecordCallback callback = this.callback;

        Lame lame = new Lame(audioRecorder.getSampleRate(),
                audioRecorder.getChannelCount(),
                audioRecorder.getSampleRate());
        LameOutputStream lameOutputStream = new LameOutputStream(lame, outputStream, bufferSize);
        LameAsyncEncoder lameAsyncEncoder = new LameAsyncEncoder(lameOutputStream, bufferSize);

        int readSize;
        long endTime;

        callback.onRecordStart();
        audioRecorder.startRecording();

        while (true) {
            final short[] buffer = lameAsyncEncoder.getFreeBuffer();
            readSize = audioRecorder.read(buffer, 0, bufferSize);
            if (AudioRecord.ERROR_INVALID_OPERATION != readSize) {
                lameAsyncEncoder.push(buffer, readSize);
            }

            endTime = SystemClock.uptimeMillis();
            callback.onProgress(endTime - startTime);

            if (isDone) {
                break;
            }
        }

        audioRecorder.stop();
        audioRecorder.release();
        lameAsyncEncoder.awaitEnd();

        if (!isCancel) {
            callback.onRecordDone(file, endTime - startTime);
        }

        return file;
    }

    public void stop(boolean isCancel) {
        this.isCancel = isCancel;
        this.isDone = true;
    }

    public interface RecordCallback {
        void onRecordStart();

        void onProgress(long time);

        void onRecordDone(File file, long time);
    }
}
