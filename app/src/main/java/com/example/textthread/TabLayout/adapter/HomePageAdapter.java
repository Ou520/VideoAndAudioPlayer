package com.example.textthread.TabLayout.adapter;

import android.content.Context;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.textthread.R;
import com.example.textthread.TabLayout.domain.MediaItem;
import com.example.textthread.TabLayout.utils.Utils;

import java.util.ArrayList;

//HomePageAdapter
public class HomePageAdapter extends BaseAdapter {
    private  ArrayList<MediaItem> mediaItems;
    private Context context;
    private Utils utils;
    boolean isVideo;//判断是来自于视频（true），还是音频(false)
    public HomePageAdapter(Context context, ArrayList<MediaItem> mediaItems,boolean isVideo)
    {
        this.context=context;
        this.mediaItems=mediaItems;
        this.isVideo=isVideo;
        utils=new Utils();
    }

    @Override
    public int getCount() {
        return mediaItems.size();
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
            convertView =View.inflate(context, R.layout.item_video_pager,null);

            viewHoder=new ViewHoder();
            viewHoder.iv_icon=convertView.findViewById(R.id.iv_icon);
            viewHoder.tv_name=convertView.findViewById(R.id.tv_name);
            viewHoder.tv_time=convertView.findViewById(R.id.tv_time);
            viewHoder.tv_size=convertView.findViewById(R.id.tv_size);

            convertView.setTag(viewHoder);
        }else {
            viewHoder= (ViewHoder) convertView.getTag();
        }
        //根据position得到列表中对应位置的数据
        MediaItem mediaItem= mediaItems.get(position);
        viewHoder.tv_name.setText(mediaItem.getName());//设置视频的名字
        viewHoder.tv_size.setText(Formatter.formatFileSize(context,mediaItem.getSize()));//设置视频的文件大小
        viewHoder.tv_time.setText(utils.stringForTime((int) mediaItem.getDuration()));//设置视频的总长\



        if (!isVideo){
           //音频界面的图标
           viewHoder.iv_icon.setImageResource(R.drawable.cahngpian);
       }else {
            //视频界面的图标
            Glide.with(context)

                    .load(mediaItem.getData())

                    .placeholder(R.drawable.bofang2) //占位图

                    .error(R.drawable.bofang2)  //出错的占位图

                    .centerCrop()

                    .fitCenter()

                    .into(viewHoder.iv_icon);
        }
        return convertView;
    }
    static class ViewHoder{
        ImageView iv_icon;
        TextView tv_name;
        TextView tv_time;
        TextView tv_size;
    }
}
