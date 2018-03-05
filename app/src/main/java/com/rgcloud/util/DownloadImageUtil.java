package com.rgcloud.util;

import android.os.Environment;

import com.rgcloud.http.RGCloudServices;
import com.rgcloud.http.RequestApi;
import com.tencent.qalsdk.im_open.QalMonitor;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by wangxuejiao on 2018/3/5.
 */

public class DownloadImageUtil {

    public static void downloadImage(final String url, final String imageName){
        Call<ResponseBody> resultCall = RequestApi.getSpalshBg(url);
        resultCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                writeResponseBodyToDisk(imageName,response.body());
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });
    }


    /**
     * 保存下载的图片流写入SD卡文件
     * @param imageName  xxx.jpg
     * @param body  image stream
     */
    public static void writeResponseBodyToDisk(String imageName, ResponseBody body) {

        if(body==null){
            return;
        }
        try {
            InputStream is = body.byteStream();
            File fileDr = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            if (!fileDr.exists()) {
                fileDr.mkdir();
            }
            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), imageName);
            if (file.exists()) {
                file.delete();
                file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), imageName );
            }
            FileOutputStream fos = new FileOutputStream(file);
            BufferedInputStream bis = new BufferedInputStream(is);
            byte[] buffer = new byte[1024];
            int len;
            while ((len = bis.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
            }
            fos.flush();
            fos.close();
            bis.close();
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
