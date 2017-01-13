package myapplicatio.atguigu.com.xiangmuone.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Baby on 2017/1/11.
 * 缓存工具类
 *
 */

/**
 * 得到缓存文本数据
  */
public class CacheUtils {
    public static String getString(Context mContext, String key) {
        SharedPreferences sp= mContext.getSharedPreferences("atguigu",Context.MODE_PRIVATE);
        return  sp.getString(key,"");

    }

    public static void putString(Context mContext, String key, String value) {
        SharedPreferences sp= mContext.getSharedPreferences("atguigu",Context.MODE_PRIVATE);
        sp.edit().putString(key,value).commit();
    }
}
