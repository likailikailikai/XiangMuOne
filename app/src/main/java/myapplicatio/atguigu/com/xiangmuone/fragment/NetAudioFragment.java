package myapplicatio.atguigu.com.xiangmuone.fragment;

import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import myapplicatio.atguigu.com.xiangmuone.base.BaseFragment;

/**
 * Created by Baby on 2017/1/6.
 */

public class NetAudioFragment extends BaseFragment {
    private TextView textView;
    @Override
    public View initView() {
        Log.e("TAG","网络音乐Ui正在初始化中...");
        textView = new TextView(mContext);
        textView .setTextColor(Color.RED);
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(25);
        return textView;
    }

    @Override
    public void initData() {
        super.initData();
        Log.e("TAG","网络音乐正在初始化中...");
        textView.setText("网络音乐");
    }
}
