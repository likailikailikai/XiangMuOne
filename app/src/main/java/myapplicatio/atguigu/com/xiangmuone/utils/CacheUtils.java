package myapplicatio.atguigu.com.xiangmuone.utils;

import android.content.Context;
import android.content.SharedPreferences;

import myapplicatio.atguigu.com.xiangmuone.service.MusicPlayerService;

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
    //保存播放模式
    public static void setPlaymode(Context context, String key, int value) {
        SharedPreferences sp= context.getSharedPreferences("atguigu",Context.MODE_PRIVATE);
        sp.edit().putInt(key,value).commit();

    }
    //得到保存播放模式
    public static int getPlaymode(Context context, String key) {
        SharedPreferences sp= context.getSharedPreferences("atguigu",Context.MODE_PRIVATE);
        return sp.getInt(key, MusicPlayerService.REPEATE_NOMAL);
    }
}
