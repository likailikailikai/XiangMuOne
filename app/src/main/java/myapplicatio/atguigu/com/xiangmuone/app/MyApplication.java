package myapplicatio.atguigu.com.xiangmuone.app;

import android.app.Application;

import org.xutils.x;


/**
 * Created by Baby on 2017/1/10.
 * 代表整个软件
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        x.Ext.setDebug(true); // 是否输出debug日志...
    }
}
