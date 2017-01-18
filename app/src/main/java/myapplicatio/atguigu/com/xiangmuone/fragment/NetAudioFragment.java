package myapplicatio.atguigu.com.xiangmuone.fragment;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import myapplicatio.atguigu.com.xiangmuone.R;
import myapplicatio.atguigu.com.xiangmuone.activity.ShowImageAndGifActivity;
import myapplicatio.atguigu.com.xiangmuone.adapter.NetAudioFragmentAdapter;
import myapplicatio.atguigu.com.xiangmuone.base.BaseFragment;
import myapplicatio.atguigu.com.xiangmuone.bean.MediaItem;
import myapplicatio.atguigu.com.xiangmuone.bean.NetAudioBean;
import myapplicatio.atguigu.com.xiangmuone.utils.CacheUtils;
import myapplicatio.atguigu.com.xiangmuone.utils.Constant;

/**
 * Created by Baby on 2017/1/6.
 */

public class NetAudioFragment extends BaseFragment {
    private static final String TAG = NetAudioFragment.class.getSimpleName();
    @Bind(R.id.listview)
    ListView listview;
    @Bind(R.id.progressbar)
    ProgressBar progressbar;
    @Bind(R.id.tv_nomedia)
    TextView tvNomedia;
    private NetAudioFragmentAdapter myAdapter;
    private List<NetAudioBean.ListBean> datas;
    private ArrayList<MediaItem> mediaItems;


    @Bind(R.id.refresh)
    MaterialRefreshLayout refreshLayout;
    @Override
    public View initView() {
        Log.e(TAG, "网络音频UI被初始化了");
        View view = View.inflate(mContext, R.layout.fragment_net_audio, null);
        ButterKnife.bind(this, view);

        //设置点击事件
        //设置点击事件
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                NetAudioBean.ListBean listEntity = datas.get(position);
                if(listEntity !=null ){
                    //3.传递视频列表
                    Intent intent = new Intent(mContext,ShowImageAndGifActivity.class);
                    if(listEntity.getType().equals("gif")){
                        String url = listEntity.getGif().getImages().get(0);
                        intent.putExtra("url",url);
                        mContext.startActivity(intent);
                    }else if(listEntity.getType().equals("image")){
                        String url = listEntity.getImage().getBig().get(0);
                        intent.putExtra("url",url);
                        mContext.startActivity(intent);
                    }
                }


            }
        });
        //监听下拉和上拉刷新
        refreshLayout.setMaterialRefreshListener(new MyMaterialRefreshListener());
        return view;
    }
    //是否加载更多
    private boolean isLoadMore = false;

    class MyMaterialRefreshListener extends MaterialRefreshListener {

        @Override
        public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
//            Toast.makeText(mContext,"下拉刷新",Toast.LENGTH_SHORT).show();
            isLoadMore = false;
            getDataFromNet();
        }

        /**
         * 加载更多的回调
         * @param materialRefreshLayout
         */
        @Override
        public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
            super.onRefreshLoadMore(materialRefreshLayout);
            isLoadMore = true;
            //Toast.makeText(mContext,"加载更多",Toast.LENGTH_SHORT).show();
            getDataFromNet();
        }
    }


    @Override
    public void initData() {
        super.initData();
        Log.e("TAG", "网络视频数据初始化了...");

        String saveJson = CacheUtils.getString(mContext, Constant.NET_AUDIO_URL);
        if(!TextUtils.isEmpty(saveJson)){
            processData(saveJson);
        }
        getDataFromNet();
    }

    private void getDataFromNet() {
        RequestParams reques = new RequestParams(Constant.NET_AUDIO_URL);
        x.http().get(reques, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {

                //网络地址
                CacheUtils.putString(mContext,Constant.NET_AUDIO_URL,result);
                LogUtil.e("onSuccess==" + result);

                processData(result);

                if(!isLoadMore) {
                    //完成刷新
                    refreshLayout.finishRefresh();
                }else{
                    //把上拉隐藏
                    refreshLayout.finishRefreshLoadMore();
                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtil.e("onError==" + ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {
                LogUtil.e("onCancelled==" + cex.getMessage());
            }

            @Override
            public void onFinished() {
                LogUtil.e("onFinished==");
            }
        });

    }

    private void processData(String json) {
        if(!isLoadMore) {
            NetAudioBean netAudioBean = paraseJons(json);
//        LogUtil.e(netAudioBean.getList().get(0).getText()+"--------------");
            datas = netAudioBean.getList();
            if(datas != null && datas.size() >0){
                //有视频
                tvNomedia.setVisibility(View.GONE);
                //设置适配器
                myAdapter = new NetAudioFragmentAdapter(mContext,datas);
                listview.setAdapter(myAdapter);
            }else{
                //没有视频
                tvNomedia.setVisibility(View.VISIBLE);
            }
        }else{
            //加载更多
            ArrayList<MediaItem> mediaItem =  parsedJson(json);
            mediaItems.addAll(mediaItem);
            //刷新适配器
            myAdapter.notifyDataSetChanged();//getcount--getview
        }
        progressbar.setVisibility(View.GONE);
    }

    private ArrayList<MediaItem> parsedJson(String json) {
        ArrayList<MediaItem> mediaItems = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray =  jsonObject.getJSONArray("trailers");

            for (int i = 0 ;i<jsonArray.length();i++){

                MediaItem mediaItem = new MediaItem();

                mediaItems.add(mediaItem);//添加到集合中


                JSONObject jsonObjectItem = (JSONObject) jsonArray.get(i);
                String name = jsonObjectItem.optString("movieName");
                mediaItem.setName(name);
                String desc = jsonObjectItem.optString("videoTitle");
                mediaItem.setDesc(desc);
                String url = jsonObjectItem.optString("url");
                mediaItem.setData(url);
                String hightUrl = jsonObjectItem.optString("hightUrl");
                mediaItem.setHeightUrl(hightUrl);
                String coverImg = jsonObjectItem.optString("coverImg");
                mediaItem.setImageUrl(coverImg);
                int videoLength = jsonObjectItem.optInt("videoLength");
                mediaItem.setDuration(videoLength);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return mediaItems;
    }


    /**
     * json解析数据
     * @param json
     * @return
     */
    private NetAudioBean paraseJons(String json) {
        return new Gson().fromJson(json,NetAudioBean.class);
    }

    @Override
    public void onRefrshData() {
        super.onRefrshData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
