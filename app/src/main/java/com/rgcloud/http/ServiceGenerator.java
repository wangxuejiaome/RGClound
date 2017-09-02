package com.rgcloud.http;

import android.util.Log;

import com.rgcloud.application.AppActivityManager;
import com.rgcloud.config.Constant;
import com.rgcloud.util.CirCleLoadingDialogUtil;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by play on 2016/6/21.
 */
public class ServiceGenerator {

    private static final String TAG = "ServiceGenerator";

    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    private static Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl(Constant.apiBaseUrl)
            .addConverterFactory(GsonConverterFactory.create());

    private static HttpLoggingInterceptor.Logger logger = new HttpLoggingInterceptor.Logger() {
        @Override
        public void log(String message) {
            Log.i("HttpLogging", message);
        }
    };


    private static HttpLoggingInterceptor logging = new HttpLoggingInterceptor(logger);

    /**
     * 创建服务，请求时默认加载对话框
     *
     * @param serviceClass
     * @param <S>
     * @return
     */
    public static <S> S createService(Class<S> serviceClass) {

        return createService(serviceClass, true, "请稍后");
    }


    /**
     * 创建服务，并控制是否加载对话框
     *
     * @param serviceClass
     * @param isShowLoad   是否加载对话框
     * @param <S>
     * @return
     */
    public static <S> S createService(Class<S> serviceClass, Boolean isShowLoad) {
        return createService(serviceClass, isShowLoad, "请稍后");
    }

    /***
     * 创建服务，更改默认的加载对话框提示语
     *
     * @param serviceClass
     * @param loadStr
     * @param <S>
     * @return
     */
    public static <S> S createService(Class<S> serviceClass, String loadStr) {
        return createService(serviceClass, true, loadStr);
    }

    /**
     * @param serviceClass
     * @param isShowLoad   是否加载对话框
     * @param loadStr      加载对话框message
     * @param <S>
     * @return
     */
    private static <S> S createService(Class<S> serviceClass, Boolean isShowLoad, String loadStr) {

        if (isShowLoad) {
             CirCleLoadingDialogUtil.showCircleProgressDialog(AppActivityManager.getActivityManager().getCurrentActivity());
        }

       /* if (TextUtils.isEmpty(loadStr)) {
            CirCleLoadingDialogUtil.showCircleProgressDialog(AppActivityManager.getActivityManager().getCurrentActivity(), loadStr);
        }*/

        if (httpClient.interceptors() != null) {
            httpClient.interceptors().clear();
        }

        // set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        // add logging as last interceptor
        httpClient.addInterceptor(logging);

        Retrofit retrofit = builder.client(httpClient.build()).build();
        return retrofit.create(serviceClass);
    }
}
