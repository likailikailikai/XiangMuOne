package myapplicatio.atguigu.com.xiangmuone.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.xutils.common.util.DensityUtil;
import org.xutils.image.ImageOptions;

import java.util.ArrayList;
import java.util.List;

import myapplicatio.atguigu.com.xiangmuone.R;
import myapplicatio.atguigu.com.xiangmuone.bean.SearchBean;
import myapplicatio.atguigu.com.xiangmuone.utils.Utils;

/**
 * Created by Baby on 2017/1/8.
 */

public class SearchAdapter extends BaseAdapter {

    private final Context mContext;
    private final ArrayList<SearchBean.ItemsBean> datas;
    private Utils utils;
    private ImageOptions imageOptions;


    public SearchAdapter(Context mContext, List<SearchBean.ItemsBean> items) {
        this.mContext = mContext;
        this.datas = (ArrayList<SearchBean.ItemsBean>) items;
        utils = new Utils();

        imageOptions = new ImageOptions.Builder()
                .setSize(DensityUtil.dip2px(120), DensityUtil.dip2px(120))
                .setRadius(DensityUtil.dip2px(5))
                // 如果ImageView的大小不是定义为wrap_content, 不要crop.
                .setCrop(true) // 很多时候设置了合适的scaleType也不需要它.
                // 加载中或错误图片的ScaleType
                //.setPlaceholderScaleType(ImageView.ScaleType.MATRIX)
                .setImageScaleType(ImageView.ScaleType.CENTER_CROP)
                .setLoadingDrawableId(R.drawable.video_default)//加载过程中的图片
                .setFailureDrawableId(R.drawable.video_default)//加载不出的图片
                .build();
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if(convertView==null) {
            convertView = View.inflate(mContext, R.layout.item_net_video,null);
            viewHolder = new ViewHolder();
            viewHolder.iv_icon = (ImageView) convertView.findViewById(R.id.iv_icon);
            viewHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            viewHolder.tv_duration = (TextView) convertView.findViewById(R.id.tv_duration);
            viewHolder.tv_size = (TextView) convertView.findViewById(R.id.tv_size);
            //设置tag
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        //根据位置得到对应的数据
        SearchBean.ItemsBean mediaItem = datas.get(position);
        viewHolder.tv_name.setText(mediaItem.getItemTitle());//设置名称
        //设置文件大小
        viewHolder.tv_size.setText(mediaItem.getDatecheck());
        //设置时间
        viewHolder.tv_duration.setText(mediaItem.getPubTime());
        //使用xutils3请求图片
//        x.image().bind(viewHolder.iv_icon,mediaItem.getImageUrl(),imageOptions);
        //使用Glide或者picasso请求图片
        Glide.with(mContext)
                .load(mediaItem.getItemImage().getImgUrl1())
                .placeholder(R.drawable.video_default)
                .error(R.drawable.video_default)
                .into(viewHolder.iv_icon);


        return convertView;
    }
    static class ViewHolder{
        TextView tv_name;
        TextView tv_duration;
        TextView tv_size;
        ImageView iv_icon;

    }
}
