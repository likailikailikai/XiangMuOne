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
    private ArrayList<MediaItem> mediaItems;

    @Bind(R.id.listview)
    ListView listView;

    @Bind(R.id.progressbar)
    ProgressBar progressBar;

    @Bind(R.id.tv_nomedia)
    TextView tvNomedia;


    @Bind(R.id.refresh)
    MaterialRefreshLayout refreshLayout;

    private List<NetAudioBean.ListBean> datas;
    private NetAudioFragmentAdapter myAdapter;
    //是否加載更多
    private boolean isLoadMore = false;


//    private Notification.Builder tvNomedia;

    @Override
    public View initView() {
        Log.e(TAG, "网络音频UI被初始化了");
        View view = View.inflate(mContext, R.layout.fragment_net_audio, null);
        ButterKnife.bind(this, view);

        //设置点击事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                NetAudioBean.ListBean listEntity = datas.get(position);
                if (listEntity != null) {
                    //3.传递视频列表
                    Intent intent = new Intent(mContext, ShowImageAndGifActivity.class);
                    if (listEntity.getType().equals("gif")) {
                        String url = listEntity.getGif().getImages().get(0);
                        intent.putExtra("url", url);
                        mContext.startActivity(intent);
                    } else if (listEntity.getType().equals("image")) {
                        String url = listEntity.getImage().getBig().get(0);
                        intent.putExtra("url", url);
                        mContext.startActivity(intent);
                    }
                }
            }
        });

        //监听下拉和上拉刷新

        refreshLayout.setMaterialRefreshListener(new MyMaterialRefreshListener());

        return view;
    }


    @Override
    public void initData() {
        super.initData();
        Log.e(TAG, "网络音频数据初始化了");

        String saveJson = CacheUtils.getString(mContext, Constant.NET_AUDIO_URL);
        if (!TextUtils.isEmpty(saveJson)) {
            processData(saveJson);
        }
        getDataFromNet();
    }


    private void getDataFromNet() {
        RequestParams reques = new RequestParams(Constant.NET_AUDIO_URL);
        x.http().get(reques, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                CacheUtils.putString(mContext, Constant.NET_AUDIO_URL, result);
                LogUtil.e("onSuccess==" + result);
                processData(result);


                if (!isLoadMore) {
                    //完成刷新
                    refreshLayout.finishRefresh();
                } else {
                    //把上拉刷新隐藏
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

/*    */

    /**
     * 使用Gson解析json数据
     *
     * @param json
     * @return
     *//*

    private List<NetAudioBean.ListBean> parsedJson(String json) {
        NetAudioBean netAudioBean = new Gson().fromJson(json,NetAudioBean.class);
        return netAudioBean.getList();
    }*/

    //适配器代码
    private void processData(String json) {
        NetAudioBean netAudioBean = paraseJson(json);
        LogUtil.e(netAudioBean.getList().get(0).getText() + "-----------");

        datas = netAudioBean.getList();
        if (!isLoadMore) {
            mediaItems = parsedJson(json);
            if (datas != null && datas.size() > 0) {

                //有视频
                tvNomedia.setVisibility(View.GONE);

                //设置适配器
                myAdapter = new NetAudioFragmentAdapter(mContext, datas);
                listView.setAdapter(myAdapter);
            } else {
                //没有视频
                tvNomedia.setVisibility(View.VISIBLE);

            }
        } else {

            //加载更多
            ArrayList<MediaItem> mediaItem = parsedJson(json);
            mediaItems.addAll(mediaItem);
            //刷新适配器
            myAdapter.notifyDataSetChanged();

        }
        progressBar.setVisibility(View.GONE);
    }

    /**
     * json解析数据
     *
     * @param json
     * @return
     */
    private NetAudioBean paraseJson(String json) {

        NetAudioBean netAudioBean = new Gson().fromJson(json, NetAudioBean.class);
        return netAudioBean;
    }


    @Override
    public void onRefrshData() {
        super.onRefrshData();
        ButterKnife.unbind(this);
    }

    private ArrayList<MediaItem> parsedJson(String json) {
        ArrayList<MediaItem> mediaItems = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(json);

            JSONArray jsonArray = jsonObject.getJSONArray("trailers");

            for (int i = 0; i < jsonArray.length(); i++) {

                MediaItem mediaItem = new MediaItem();

                //添加到集合中
                mediaItems.add(mediaItem);

                JSONObject jsonObject1Item = (JSONObject) jsonArray.get(i);

                String name = jsonObject1Item.optString("movieName");
                mediaItem.setName(name);

                String desc = jsonObject1Item.optString("videoTitle");
                mediaItem.setDesc(desc);

                String url = jsonObject1Item.optString("url");
                mediaItem.setData(url);

                String hightUrl = jsonObject1Item.optString("hightUrl");
                mediaItem.setHeightUrl(hightUrl);

                String coverImg = jsonObject1Item.optString("coverImg");
                mediaItem.setImageUrl(coverImg);

                int videoLength = jsonObject1Item.optInt("videoLength");
                mediaItem.setDuration(videoLength);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return mediaItems;
    }


    class MyMaterialRefreshListener extends MaterialRefreshListener {
        @Override
        public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
            isLoadMore = false;

            getDataFromNet();
        }

        @Override
        public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {

            super.onRefreshLoadMore(materialRefreshLayout);

            isLoadMore = true;

            //Toast.makeText(mContext, "加载更多", Toast.LENGTH_SHORT).show();

            getDataFromNet();
        }
    }
}
