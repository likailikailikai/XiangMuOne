package myapplicatio.atguigu.com.xiangmuone.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Baby on 2017/1/6.
 */

public abstract class BaseFragment extends Fragment {

    //上下文
    public Context mContext;

    //当系统创建当前BaseFragment类的时候回调

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }
    //当系统要创建Fragment的视图的时候会地哦啊这个方法

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return initView();
    }

    public abstract View initView();

    /**
     * 当Activity创建成功的时候回调该方法
     * 初始化数据
     * 联网请求数据
     * 绑定数据
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    /**
     *当子类需要：
     * 1.联网请求网络，的时候重写该方法
     * 2.绑定数据
     */
    public void initData() {

    }


    /**
     *
     * @param hidden false：当前类显示
     *               true:当前类隐藏
     */
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        Log.e("TAG","onHiddenChanged。。"+this.toString()+",hidden=="+hidden);
        if(!hidden){
            onRefrshData();
        }

    }



    /**
     * 当子类要刷新数据的时候重写该方法
     */
    public void onRefrshData() {

    }


}
