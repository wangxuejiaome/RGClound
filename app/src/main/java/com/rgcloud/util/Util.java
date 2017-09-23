package com.rgcloud.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.io.File;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 工具类
 * Created by Administrator on 2016/7/8.
 */
public class Util {


    /**
     * 判断token是否过期
     *
     * @param expireTime token过期时间
     * @return
     */
    public static boolean isTokenExpire(String expireTime) {
        //如果想比较日期则写成"yyyy-MM-dd"就可以了  
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        //将字符串形式的时间转化为Date类型的时间  
        Date tokenTime = null;
        Date now = null;
        try {
            tokenTime = sdf.parse(expireTime);
            now = new Date();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //Date类的一个方法，如果a早于b返回true，否则返回false
        assert tokenTime != null;
        return tokenTime.before(now);
    }




    //首先计算出用数字表示的值
    public static long getFolderSize(File file) {
        long size = 0;
        File[] files = file.listFiles();
        for (File f : files) {
            size += f.isDirectory() ? getFolderSize(f) : f.length();
        }
        return size;
    }
    //然后将得到的值格式化
    public static String getFormatSize (double size) {
        double kB = size / 1024;
        if (kB < 1) {
            return size + "B";
        }
        double mB = kB / 1024;
        if (mB < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kB));
            //2表示保留两位小数，ROUND_HALF_UP表示四舍五入
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "KB";
        }
        double gB = mB / 1024;
        if (gB < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(mB));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "MB";
        }
        double tB = gB / 1024;
        if (tB < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gB));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "GB";
        }
        BigDecimal result4 = new BigDecimal(tB);
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "TB";
    }



    public static void clearFileDirect(File file) {
        if (file.isFile()) {
            file.delete();
        } else if (file.isDirectory()) {
            //清空文件夹
            File[] files = file.listFiles();
            int length = files.length;
            int index = 0;
            while (length != 0) {
                clearFileDirect(files[index]);
                index++;
                length--;
            }
        }
    }




    public static void callPhone(final Activity activity,String phone) {

        if (Build.VERSION.SDK_INT >= 23) {
            int checkCallPhonePermission = ContextCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE);
            if(checkCallPhonePermission != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.CALL_PHONE},0);
                return;
            }else{
                //上面已经写好的拨号方法
                Intent phoneIntent = new Intent("android.intent.action.CALL", Uri.parse("tel:" + phone));
                activity.startActivity(phoneIntent);
            }
        } else {
            //上面已经写好的拨号方法
            Intent phoneIntent = new Intent("android.intent.action.CALL", Uri.parse("tel:" + phone));
            activity.startActivity(phoneIntent);
        }
    }

    /**
     * 手机号验证  验证手机号是否正确
     *
     * @param str
     * @return 验证通过返回true
     */
    public static boolean isMobile(String str) {
        if (str.length() != 11) {
            ToastUtil.showShortToast("请输入长度为11位的手机号");
            return false;
        }
        Pattern p = null;
        Matcher m = null;
        boolean b = false;
        p = Pattern.compile("^[1][3,4,5,7,8][0-9]{9}$"); // 验证手机号
        m = p.matcher(str);
        b = m.matches();
        if (!b) {
            ToastUtil.showShortToast("手机号有误");
            return false;
        }
        return b;
    }

    /**
     * 电话号码验证
     *
     * @param str
     * @return 验证通过返回true
     */
    public static boolean isPhone(String str) {
        Pattern p1 = null, p2 = null;
        Matcher m = null;
        boolean b = false;
        p1 = Pattern.compile("^[0][1-9]{2,3}-[0-9]{5,10}$");  // 验证带区号的
        p2 = Pattern.compile("^[1-9]{1}[0-9]{5,8}$");         // 验证没有区号的
        if (str.length() > 9) {
            m = p1.matcher(str);
            b = m.matches();
        } else {
            m = p2.matcher(str);
            b = m.matches();
        }
        return b;
    }

    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }


    /**
     * 判断版本更新
     *
     * @param version1 服务器应用版本
     * @param version2 应用本身版本
     * @return 0=>版本相同，1=>版本需要更新，-1版本不需要更新
     */
    public static int compareVersion(String version1, String version2) {
        if (version1.equals(version2)) {
            return 0;
        }
        String[] version1Array = version1.split("\\.");
        String[] version2Array = version2.split("\\.");
        int minLen = Math.min(version1Array.length, version2Array.length);
        int index = 0;
        int diff = 0;
        while (index < minLen
                && (diff = Integer.parseInt(version1Array[index])
                - Integer.parseInt(version2Array[index])) == 0) {
            index++;
        }
        if (diff == 0) {
            for (int i = index; i < version1Array.length; i++) {
                if (Integer.parseInt(version1Array[i]) > 0) {
                    return 1;
                }
            }
            for (int i = index; i < version2Array.length; i++) {
                if (Integer.parseInt(version2Array[i]) > 0) {
                    return -1;
                }
            }
            return 0;
        } else {
            return diff > 0 ? 1 : -1;
        }
    }
}
