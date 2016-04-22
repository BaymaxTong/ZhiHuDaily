package com.rutong.zhihudaily.utils;

import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by baymax on 2016/1/22.
 */
public class ImageUtil {
    //Bitmap  to Bytes
    public static byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }
    //Save Image
    public static void saveImage(File file, byte[] bytes) {
        try {
            if (file.exists()) {
                file.delete();
            }
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bytes);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
