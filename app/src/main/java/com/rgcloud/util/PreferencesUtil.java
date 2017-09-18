package com.rgcloud.util;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;


/**
 * Created by play on 2016/3/25.
 */
public class PreferencesUtil {

    private SharedPreferences mSharePreferences;
    private SharedPreferences.Editor mEditor;

    /**
     * 是否登录
     */
    public static final String HAS_LOGIN = "hasLogin";

    /**
     * sharePreferences的文件名称
     */
    private static final String PREFERENCES_FILE_NAME = "preferencesPeriodical";


    /**
     * 用户TOKEN
     */
    public static final String ACCESS_TOKEN = "accessToken";

    /**
     * 是否是微信登录
     */
    public static final String ISWXLOGIN = "isWXLogin";


    /**
     * PreferencesUtil构造函数
     * <p>在Application中初始化</p>
     *
     * @param context 上下文
     * @param
     */
    public PreferencesUtil(Context context) {
        mSharePreferences = context.getSharedPreferences(PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
        mEditor = mSharePreferences.edit();
        mEditor.apply();
    }

    public PreferencesUtil(Context context, String SharedPreferencesName) {
        mSharePreferences = context.getSharedPreferences(SharedPreferencesName, Context.MODE_PRIVATE);
        mEditor = mSharePreferences.edit();
        mEditor.apply();
    }


    /**
     * 保存数据
     *
     * @param key
     * @param object
     */
    public void put(String key, Object object) {

        if (object instanceof String) {
            mEditor.putString(key, (String) object);
        } else if (object instanceof Integer) {
            mEditor.putInt(key, (Integer) object);
        } else if (object instanceof Boolean) {
            mEditor.putBoolean(key, (Boolean) object);
        } else if (object instanceof Float) {
            mEditor.putFloat(key, (Float) object);
        } else if (object instanceof Long) {
            mEditor.putLong(key, (Long) object);
        } else if (object != null) {
            mEditor.putString(key, object.toString());
        }
        mEditor.commit();
    }


    /**
     * mSharePreferences中读取String
     *
     * @param key 键
     * @return 存在返回对应值，不存在返回默认值{@code null}
     */
    public String getString(String key) {
        return getString(key, null);
    }

    /**
     * mSharePreferences中读取String
     *
     * @param key          键
     * @param defaultValue 默认值
     * @return 存在返回对应值，不存在返回默认值{@code defaultValue}
     */
    public String getString(String key, String defaultValue) {
        return mSharePreferences.getString(key, defaultValue);
    }

    /**
     * mSharePreferences中读取int
     *
     * @param key 键
     * @return 存在返回对应值，不存在返回默认值-1
     */
    public int getInt(String key) {
        return getInt(key, -1);
    }

    /**
     * mSharePreferences中读取int
     *
     * @param key          键
     * @param defaultValue 默认值
     * @return 存在返回对应值，不存在返回默认值{@code defaultValue}
     */
    public int getInt(String key, int defaultValue) {
        return mSharePreferences.getInt(key, defaultValue);
    }

    /**
     * mSharePreferences中读取long
     *
     * @param key 键
     * @return 存在返回对应值，不存在返回默认值-1
     */
    public long getLong(String key) {
        return getLong(key, -1L);
    }

    /**
     * mSharePreferences中读取long
     *
     * @param key          键
     * @param defaultValue 默认值
     * @return 存在返回对应值，不存在返回默认值{@code defaultValue}
     */
    public long getLong(String key, long defaultValue) {
        return mSharePreferences.getLong(key, defaultValue);
    }

    /**
     * mSharePreferences中读取float
     *
     * @param key 键
     * @return 存在返回对应值，不存在返回默认值-1
     */
    public float getFloat(String key) {
        return getFloat(key, -1f);
    }

    /**
     * mSharePreferences中读取float
     *
     * @param key          键
     * @param defaultValue 默认值
     * @return 存在返回对应值，不存在返回默认值{@code defaultValue}
     */
    public float getFloat(String key, float defaultValue) {
        return mSharePreferences.getFloat(key, defaultValue);
    }

    /**
     * mSharePreferences中读取boolean
     *
     * @param key 键
     * @return 存在返回对应值，不存在返回默认值{@code false}
     */
    public boolean getBoolean(String key) {
        return getBoolean(key, false);
    }

    /**
     * mSharePreferences中读取boolean
     *
     * @param key          键
     * @param defaultValue 默认值
     * @return 存在返回对应值，不存在返回默认值{@code defaultValue}
     */
    public boolean getBoolean(String key, boolean defaultValue) {
        return mSharePreferences.getBoolean(key, defaultValue);
    }

    /**
     * mSharePreferences中获取所有键值对
     *
     * @return Map对象
     */
    public Map<String, ?> getAll() {
        return mSharePreferences.getAll();
    }

    /**
     * mSharePreferences中移除该key
     *
     * @param key 键
     */
    public void remove(String key) {
        mEditor.remove(key).apply();
    }

    /**
     * mSharePreferences中是否存在该key
     *
     * @param key 键
     * @return {@code true}: 存在<br>{@code false}: 不存在
     */
    public boolean contains(String key) {
        return mSharePreferences.contains(key);
    }

    /**
     * mSharePreferences中清除所有数据
     */
    public void clear() {
        mEditor.clear().apply();
    }

    public void loginOutRemove() {
        mEditor.remove(ACCESS_TOKEN)
                .remove(HAS_LOGIN)
                .remove(ISWXLOGIN);
        mEditor.commit();
    }

}
