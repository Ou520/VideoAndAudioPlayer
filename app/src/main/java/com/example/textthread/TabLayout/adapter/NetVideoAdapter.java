package com.example.textthread.TabLayout.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.example.textthread.R;
import com.example.textthread.TabLayout.domain.MediaItem;
import com.example.textthread.TabLayout.utils.Utils;
import com.squareup.picasso.Picasso;


import org.xutils.x;

import java.util.ArrayList;

//NetVideoAdapter
public class NetVideoAdapter extends BaseAdapter {
    private  ArrayList<MediaItem> mediaItems;
    private Context context;

    public NetVideoAdapter(Context context, ArrayList<MediaItem> mediaItems)
    {
        this.context=context;
        this.mediaItems=mediaItems;

    }

    @Override
    public int getCount() {
        if (mediaItems !=null &&mediaItems.size()>0){
            return mediaItems.size();
        }
       return 0;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHoder viewHoder;
        if (convertView ==null){
            convertView =View.inflate(context, R.layout.item_net_video_pager,null);

            viewHoder=new ViewHoder();
            viewHoder.iv_icon=convertView.findViewById(R.id.iv_icon1);
            viewHoder.tv_name=convertView.findViewById(R.id.tv_name);
            viewHoder.tv_desc=convertView.findViewById(R.id.tv_desc);

            convertView.setTag(viewHoder);
        }else {
            viewHoder= (ViewHoder) convertView.getTag();
        }
        //根据position得到列表中对应位置的数据
        MediaItem mediaItem= mediaItems.get(position);
        viewHoder.tv_name.setText(mediaItem.getName());//设置视频的名字
        viewHoder.tv_desc.setText(mediaItem.getDesc());
        //1使用xUtils3请求图片
//        x.image().bind(viewHoder.iv_icon,mediaItem.getImageUrl());
        //2使用Glide请求图片
        String url =mediaItem.getImageUrl();
//        Glide.with(context).load(url).into(viewHoder.iv_icon);
        //Picasso请求图片（最新版方法with(context)改为get（））
        Picasso.get().load(url).into(viewHoder.iv_icon);
        return convertView;
    }
    static class ViewHoder{
        ImageView iv_icon;
        TextView tv_name;
        TextView tv_desc;

    }
}
