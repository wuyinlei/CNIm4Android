package com.mingchu.common.tools;

import android.text.TextUtils;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@SuppressWarnings("WeakerAccess")
public class StreamUtil {

    public static boolean copy(File in, OutputStream outputStream) {
        if (!in.exists())
            return false;

        InputStream stream;
        try {
            stream = new FileInputStream(in);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }

        return copy(stream, outputStream);
    }

    public static boolean copy(File in, File out) {
        if (!in.exists())
            return false;

        InputStream stream;
        try {
            stream = new FileInputStream(in);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }

        return copy(stream, out);
    }

    public static boolean copy(InputStream inputStream, File out) {
        if (!out.exists()) {
            File fileParentDir = out.getParentFile();
            if (!fileParentDir.exists()) {
                if (!fileParentDir.mkdirs())
                    return false;
            }
            try {
                if (!out.createNewFile())
                    return false;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        OutputStream outputStream;
        try {
            outputStream = new FileOutputStream(out);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        return copy(inputStream, outputStream);
    }

    public static boolean copy(InputStream inputStream, OutputStream outputStream) {
        try {
            byte buffer[] = new byte[1024];
            int realLength;
            while ((realLength = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, realLength);
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            close(inputStream);
            close(outputStream);
        }
    }

    public static void close(Closeable... closeables) {
        if (closeables == null)
            return;
        for (Closeable closeable : closeables) {
            if (closeable != null) {
                try {
                    closeable.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public static boolean delete(String path) {
        if (TextUtils.isEmpty(path))
            return false;
        File file = new File(path);
        return file.exists() && file.delete();
    }
}
