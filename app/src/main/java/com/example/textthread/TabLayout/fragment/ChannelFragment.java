package com.example.textthread.TabLayout.fragment;


import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.example.textthread.R;
import com.example.textthread.TabLayout.activity.SystemAudioPlayer;
import com.example.textthread.TabLayout.adapter.HomePageAdapter;
import com.example.textthread.TabLayout.domain.MediaItem;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChannelFragment extends Fragment {
    //标题
    String title="本地音乐";
    //写一个get方法获取数据
    //得到标题
    @BindView(R.id.listview)
    ListView listview;
    @BindView(R.id.tv_nomedia)
    TextView tvNomedia;
    @BindView(R.id.pb_loading)
    ProgressBar pbLoading;
    Unbinder unbinder;
    //存放视频的数据集合
    private ArrayList<MediaItem> mediaItems;
    private HomePageAdapter homePageAdapter;
    //标题
    //写一个get方法获取数据
    //得到标题
    public String getTitle() {
        return title;
    }

    //构造方法
    public ChannelFragment() { }
    //定义Handler
    @SuppressLint("HandlerLeak")
    private Handler handler =new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (mediaItems !=null && mediaItems.size() >0){
                //有数据
                //设置适配器
                homePageAdapter=new HomePageAdapter(getContext(),mediaItems,false);
                listview.setAdapter(homePageAdapter);
                //把文本隐藏
                tvNomedia.setVisibility(View.GONE);
            }else {
                //没有数据
                //把文本显示
                tvNomedia.setText("没有发现音频...");
                tvNomedia.setVisibility(View.VISIBLE);
            }
            //把ProgressBar隐藏
            pbLoading.setVisibility(View.GONE);
        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //和本地视频共用一个布局
        View view =  inflater.inflate(R.layout.fragment_home_page2, container, false);

//        inflater.inflate(R.layout.fragment_channel, container, false);
        unbinder = ButterKnife.bind(this, view);
        //加载本地的音频数据
        getDataFromLocal();
        //设置ListViewItem的点击事件
        listview.setOnItemClickListener(new MyOnItemClickListener());
        return view;
    }

    //列表Item的监听
    class MyOnItemClickListener implements android.widget.AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //3.传递点击了列表的那一个Item的位置
            Intent intent = new Intent(getContext(), SystemAudioPlayer.class);
            intent.putExtra("position",position);
            startActivity(intent);

        }
    }


    //从本地的sdcard得到数据
    //通过子线程，从内容提供者里面获取音频
    private void getDataFromLocal() {
        mediaItems =new ArrayList<>();//初始化数组（用之前一定要初始化（new））
        new Thread(){
            @Override
            public void run() {
                super.run();
                ContentResolver resolver=getContext().getContentResolver();
                Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                String[] objs={
                        MediaStore.Audio.Media.DISPLAY_NAME,//音频文件在sdcard的名字
                        MediaStore.Audio.Media.DURATION,//音频总长
                        MediaStore.Audio.Media.SIZE,//音频文件的大小
                        MediaStore.Audio.Media.DATA,//音频的决对地址
                        MediaStore.Audio.Media.ARTIST,//歌曲的演唱者
                };
                Cursor cursor =resolver.query(uri,objs,null,null,null);
                if (cursor !=null){
                    while (cursor.moveToNext()){
                        MediaItem mediaItem = new MediaItem();

                        mediaItems.add(mediaItem);//写在上面和写在下面是一样的
                        //一个视频的信息
                        String name = cursor.getString(0);//音频的名称
                        mediaItem.setName(name);

                        long duration = cursor.getLong(1);//音频的时长
                        mediaItem.setDuration(duration);

                        long size = cursor.getLong(2);//音频的文件大小
                        mediaItem.setSize(size);

                        String data = cursor.getString(3);//音频的播放地址
                        mediaItem.setData(data);

                        String artist = cursor.getString(4);//艺术家
                        mediaItem.setArtist(artist);
                    }
                    cursor.close();//释放cursor
                }
                //Handle发消息
                handler.sendEmptyMessage(10);
            }
        }.start();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


}
