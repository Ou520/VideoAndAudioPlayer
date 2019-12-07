package com.example.textthread.TabLayout.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.textthread.GifShow;
import com.example.textthread.ImgAsyncTaskActivity;
import com.example.textthread.ImgShow;
import com.example.textthread.R;
import com.example.textthread.TabLayout.domain.NetAudioData;
import com.example.textthread.TabLayout.utils.Utils;
import org.xutils.common.util.DensityUtil;
import org.xutils.x;
import java.util.List;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import pl.droidsonroids.gif.GifImageView;



public class NetAudioPagerAdapter extends BaseAdapter {

/*--------数据类型的标记标记---------------*/
     //视频
    private static final int TYPE_VIDEO = 0;
     //图片
    private static final int TYPE_IMAGE = 1;
     //文字
    private static final int TYPE_TEXT = 2;
     // GIF图片
    private static final int TYPE_GIF = 3;
    //软件推广
    private static final int TYPE_AD = 4;

    private  Context context;//上下文
    private final List<NetAudioData.ListEntity>  mDatas;//存放数据的列表
    private Utils utils;//工具类

    //构造方法
    public NetAudioPagerAdapter(Context context, List<NetAudioData.ListEntity> datas){
        this.context = context;
        this.mDatas = datas;
        utils = new Utils();
    }




    @Override
    //设置item的类型的数量
    public int getViewTypeCount() {
        return 5;
    }


    @Override
    //根据位置得到对应的类型
    public int getItemViewType(int position) {

        NetAudioData.ListEntity listEntity = mDatas.get(position);//根据item的位置得到对应的类型
        String type = listEntity.getType();//video,text,image,gif,ad（类型的标记）
        int itemViewType = -1;
        if ("video".equals(type)) {
            itemViewType = TYPE_VIDEO;
        } else if ("image".equals(type)) {
            itemViewType = TYPE_IMAGE;
        } else if ("text".equals(type)) {
            itemViewType = TYPE_TEXT;
        } else if ("gif".equals(type)) {
            itemViewType = TYPE_GIF;
        } else {
            itemViewType = TYPE_AD;//广告
        }
        return itemViewType;
    }

    @Override
    //设置item的数量
    public int getCount() {
        return mDatas.size();
    }


    @Override
    //获取视图
    public View getView(int position, View convertView, ViewGroup parent) {

        int itemViewType  = getItemViewType(position) ;//得到类型

        ViewHolder viewHolder;

        if(convertView ==null){
            //初始化
            //初始化item布局
            viewHolder = new ViewHolder();
            //根据不同的数据类型加载不同的布局和绑定对应的控件控件
            convertView = initView(convertView, itemViewType, viewHolder);
            //初始化公共的视图
            initCommonView(convertView, itemViewType, viewHolder);
            //设置Tag
            convertView.setTag(viewHolder);

        }else{
            //获取Tag
            viewHolder = (ViewHolder) convertView.getTag();
        }

        //绑定数据
        //根据位置得到数据,绑定数据
        NetAudioData.ListEntity mediaItem = mDatas.get(position);
        //绑定数据
        bindData(itemViewType, viewHolder, mediaItem,position);
        return  convertView;
    }


    @Override
    //获取单元格
    public Object getItem(int position) {
        return null;
    }

    @Override
    //获取单元格的id
    public long getItemId(int position) {
        return 0;
    }



/*----------------------------------------------------------------------------------*/

    /*--------------------------ViewHolder------------------------------------------*/

    static class ViewHolder {
        //user_info(顶部)
        ImageView iv_headpic;
        TextView tv_name;
        TextView tv_time_refresh;
        ImageView iv_right_more;
        //bottom（顶部）
        ImageView iv_video_kind;
        TextView tv_video_kind_text;
        TextView tv_shenhe_ding_number;
        TextView tv_shenhe_cai_number;
        TextView tv_posts_number;
        LinearLayout ll_download;

        //中间公共部分 -所有的都有
        TextView tv_context;

        //Video
        TextView tv_play_nums;
        TextView tv_video_duration;
        ImageView iv_commant;
        TextView tv_commant_context;
        JCVideoPlayer jcv_videoplayer;

        //Image
        ImageView iv_image_icon;

        //Gif
        GifImageView iv_image_gif;

        //软件推广
        Button btn_install;

        //设置控件的监听


    }

    //根据不同的数据类型加载不同的布局和绑定对应的控件控件
    private View initView(View convertView, int itemViewType, ViewHolder viewHolder) {
        switch (itemViewType) {
            case TYPE_VIDEO://视频布局
                convertView = View.inflate(context, R.layout.all_video_item, null);
                //在这里实例化特有的
                viewHolder.tv_play_nums = (TextView) convertView.findViewById(R.id.tv_play_nums);
                viewHolder.tv_video_duration = (TextView) convertView.findViewById(R.id.tv_video_duration);
                viewHolder.iv_commant = (ImageView) convertView.findViewById(R.id.iv_commant);
                viewHolder.tv_commant_context = (TextView) convertView.findViewById(R.id.tv_commant_context);
                viewHolder.jcv_videoplayer = (JCVideoPlayer) convertView.findViewById(R.id.jcv_videoplayer);
                break;
            case TYPE_IMAGE://图片布局
                convertView = View.inflate(context, R.layout.all_image_item, null);
                viewHolder.iv_image_icon = (ImageView) convertView.findViewById(R.id.iv_image_icon);
                break;
            case TYPE_TEXT://文字布局
                convertView = View.inflate(context, R.layout.all_text_item, null);
                break;
            case TYPE_GIF://gif布局
                convertView = View.inflate(context, R.layout.all_gif_item, null);
                viewHolder.iv_image_gif = (GifImageView) convertView.findViewById(R.id.iv_image_gif);
                break;
            case TYPE_AD://软件广告布局
                convertView = View.inflate(context, R.layout.all_ad_item, null);
                viewHolder.btn_install = (Button) convertView.findViewById(R.id.btn_install);
                viewHolder.iv_image_icon = (ImageView) convertView.findViewById(R.id.iv_image_icon);
                break;
        }
        return convertView;
    }


    //公共的视图
    private void initCommonView(View convertView, int itemViewType, ViewHolder viewHolder) {
        switch (itemViewType) {
            case TYPE_VIDEO://视频
            case TYPE_IMAGE://图片
            case TYPE_TEXT://文字
            case TYPE_GIF://gif

                //加载除开广告部分的公共部分视图
                //绑定除开广告部分的公共部分的控件
                //user info（用户部分）
                viewHolder.iv_headpic = (ImageView) convertView.findViewById(R.id.iv_headpic);
                viewHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
                viewHolder.tv_time_refresh = (TextView) convertView.findViewById(R.id.tv_time_refresh);
                viewHolder.iv_right_more = (ImageView) convertView.findViewById(R.id.iv_right_more);
                //bottom（底部点赞部分）
                viewHolder.iv_video_kind = (ImageView) convertView.findViewById(R.id.iv_video_kind);
                viewHolder.tv_video_kind_text = (TextView) convertView.findViewById(R.id.tv_video_kind_text);
                viewHolder.tv_shenhe_ding_number = (TextView) convertView.findViewById(R.id.tv_shenhe_ding_number);
                viewHolder.tv_shenhe_cai_number = (TextView) convertView.findViewById(R.id.tv_shenhe_cai_number);
                viewHolder.tv_posts_number = (TextView) convertView.findViewById(R.id.tv_posts_number);
                viewHolder.ll_download = (LinearLayout) convertView.findViewById(R.id.ll_download);

                break;
        }

        //中间公共部分 -所有的都有
        viewHolder.tv_context = (TextView) convertView.findViewById(R.id.tv_context);
    }



    @SuppressLint("SetTextI18n")
    //绑定控件数据
    private void bindData(int itemViewType, ViewHolder viewHolder, final NetAudioData.ListEntity mediaItem, final int position) {
        switch (itemViewType) {
            case TYPE_VIDEO://视频
                //绑定公共部分的数据
                bindData(viewHolder, mediaItem);

                //第一个参数是视频播放地址，第二个参数是显示封面的地址，第三参数是标题
                viewHolder.jcv_videoplayer.setUp(mediaItem.getVideo().getVideo().get(0), mediaItem.getVideo().getThumbnail().get(0), null);//要初始化ImageLoader（MyApplication.class里）
                viewHolder.tv_play_nums.setText(mediaItem.getVideo().getPlaycount() + "次播放");
                viewHolder.tv_video_duration.setText(utils.stringForTime(mediaItem.getVideo().getDuration() * 1000) + "");
                break;
            case TYPE_IMAGE://图片
                //绑定公共部分的数据
                bindData(viewHolder, mediaItem);

                //设置控件的监听
                setListener(viewHolder,mediaItem,position);

                viewHolder.iv_image_icon.setImageResource(R.drawable.bg_item);
                int  height = mediaItem.getImage().getHeight()<= DensityUtil.getScreenHeight()*0.75?mediaItem.getImage().getHeight(): (int) (DensityUtil.getScreenHeight() * 0.75);

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(DensityUtil.getScreenWidth(),height);
                viewHolder.iv_image_icon.setLayoutParams(params);
                if(mediaItem.getImage() != null &&  mediaItem.getImage().getBig()!= null&&mediaItem.getImage().getBig().size() >0){
                    x.image().bind(viewHolder.iv_image_icon, mediaItem.getImage().getBig().get(0));
                    Glide.with(context).load(mediaItem.getImage().getBig().get(0)).placeholder(R.drawable.bg_item).error(R.drawable.bg_item).diskCacheStrategy(DiskCacheStrategy.ALL).into(viewHolder.iv_image_icon);
                }
                break;
            case TYPE_TEXT://文字
                //绑定公共部分的数据
                bindData(viewHolder, mediaItem);

                break;
            case TYPE_GIF://gif
                //绑定公共部分的数据
                bindData(viewHolder, mediaItem);

                System.out.println("mediaItem.getGif().getImages().get(0)" + mediaItem.getGif().getImages().get(0));
                Glide.with(context).load(mediaItem.getGif().getImages().get(0)).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(viewHolder.iv_image_gif);

                viewHolder.iv_image_gif.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //让intent携带数据跳转到ImgShow
                        Intent intent =new Intent(context, GifShow.class);
                        String url=mediaItem.getGif().getImages().get(0);
                        intent.putExtra("GifurlKey",url);
                        context.startActivity(intent);
//                Toast.makeText(context, "点击了图片"+position+"\n"+mediaItem.getImage().getBig().get(0), Toast.LENGTH_SHORT).show();
                    }
                });


                break;
            case TYPE_AD://软件广告
                break;
        }



        //设置文本
        viewHolder.tv_context.setText(mediaItem.getText());


    }



    //绑定公共部分的数据
    private void bindData(ViewHolder viewHolder, NetAudioData.ListEntity mediaItem) {
        if(mediaItem.getU()!=null&& mediaItem.getU().getHeader()!=null&&mediaItem.getU().getHeader().get(0)!=null){
            x.image().bind(viewHolder.iv_headpic, mediaItem.getU().getHeader().get(0));
        }
        if(mediaItem.getU() != null&&mediaItem.getU().getName()!= null){
            viewHolder.tv_name.setText(mediaItem.getU().getName()+"");
        }

        viewHolder.tv_time_refresh.setText(mediaItem.getPasstime());


        //设置标签
        List<NetAudioData.ListEntity.TagsEntity> tagsEntities = mediaItem.getTags();
        if (tagsEntities != null && tagsEntities.size() > 0) {
            StringBuffer buffer = new StringBuffer();
            for (int i = 0; i < tagsEntities.size(); i++) {
                buffer.append(tagsEntities.get(i).getName() + " ");
            }
            viewHolder.tv_video_kind_text.setText(buffer.toString());
        }

        //设置点赞，踩,转发
        viewHolder.tv_shenhe_ding_number.setText(mediaItem.getUp());
        viewHolder.tv_shenhe_cai_number.setText(mediaItem.getDown() + "");
        viewHolder.tv_posts_number.setText(mediaItem.getForward()+"");

    }

    //设置控件的监听
    private void setListener(ViewHolder viewHolder, final NetAudioData.ListEntity mediaItem, final int position) {
        viewHolder.iv_image_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //让intent携带数据跳转到ImgShow
                Intent intent =new Intent(context, ImgShow.class);
                String url=mediaItem.getImage().getBig().get(0);
                intent.putExtra("urlKey",url);
                context.startActivity(intent);
//                Toast.makeText(context, "点击了图片"+position+"\n"+mediaItem.getImage().getBig().get(0), Toast.LENGTH_SHORT).show();
            }
        });
    }

/*---------------------------------------------------------------------------------*/


}

