package com.example.textthread.TabLayout.fragment;


import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.textthread.HttpUtils.HttpUtils;
import com.example.textthread.ImdAsynTaak.NetworkUtil;
import com.example.textthread.OkhttpActivity;
import com.example.textthread.R;
import com.example.textthread.TabLayout.activity.SystemVideoPlayer;
import com.example.textthread.TabLayout.adapter.NetVideoAdapter;
import com.example.textthread.TabLayout.domain.MediaItem;
import com.example.textthread.TabLayout.utils.CacheUtils;
import com.example.textthread.TabLayout.utils.Constants;
import com.scwang.smartrefresh.header.WaterDropHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


import java.io.IOException;
import java.util.ArrayList;


public class DynamicConditionFragment extends Fragment {
    //标题
    String title = "网络视频";
    private  View view;
    private ListView mListView;
    private TextView mTv_nonet;
    private ProgressBar mProgressBar;
    private SmartRefreshLayout refreshLayout;
    private ArrayList<MediaItem> mediaItems;
    private NetVideoAdapter adapter;
    private boolean isLoadMore =false;//是否已经加载更多的数据

    //写一个get方法获取数据
    //得到标题
    public String getTitle() {
        return title;
    }

    public DynamicConditionFragment() { }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_dynamic_condition, container, false);
        initView(view);
        initData();
        setListener();
        return view;
    }


    private void initView(View view) {
        mListView=view.findViewById(R.id.listview);
        mTv_nonet=view.findViewById(R.id.tv_nonet);
        mProgressBar=view.findViewById(R.id.pb_loading);
        refreshLayout=view.findViewById(R.id.refreshLayout);
    }

    //通过Handler()方法来接收子线程传回来的消息（Message），并在相应的组件上显示
    @SuppressLint("HandlerLeak")
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what==1) {//通过msg.what判断消息是来自那个字线程的，并在UI线程的组件显示或者其他操作
                String data=(String) msg.obj;
                //缓存数据
                CacheUtils.putString(getContext(),Constants.NET_URL,data);
                //解析JSON数据
                processData(data);
            }else if (msg.what==2){
                //主线程
                String data=(String) msg.obj;
                isLoadMore=true;
                processData(data);
            }
        }
    };

    private void initData() {

        //从缓存中获取数据
        String saveJson = CacheUtils.getString(getContext(),Constants.NET_URL);
        if (!TextUtils.isEmpty(saveJson)){
            processData(saveJson);
        }
        //联网请求数据
        if (NetworkUtil.isReady(getContext()))//判断网络是否可以，如果不可用就跳到系统的设置Activity
        {
            getDataFromNet();//联网请求数据
        }else
        {
            //弹出对话框如果按确定按钮就让他跳转到系统的设置网络的Activity
            new AlertDialog.Builder(getContext())
                    .setTitle("当前网络不可用")
                    .setMessage("请设置网络！！！")
                    .setPositiveButton("是", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent=new Intent(Settings.ACTION_SETTINGS);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("否", null)
                    .show();
        }


    }

    private void setListener() {
        mListView.setOnItemClickListener(new MyOnItemClickListener());

        //设置RefreshLayout的属性
        //设置 Header 样式:
        refreshLayout.setRefreshHeader(new WaterDropHeader(getContext()));
        //设置 Footer 为 球脉冲 样式
        refreshLayout.setRefreshFooter(new BallPulseFooter(getContext()).setSpinnerStyle(SpinnerStyle.Scale));

        //下拉刷新的监听
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                //建议使用异步任务
                //获取数据
                if (NetworkUtil.isReady(getContext()))//判断网络是否可以，如果不可用就跳到系统的设置Activity
                {
                    getDataFromNet();
                }else
                {
                    //弹出对话框如果按确定按钮就让他跳转到系统的设置网络的Activity
                    new AlertDialog.Builder(getContext())
                            .setTitle("当前网络不可用")
                            .setMessage("请设置网络！！！")
                            .setPositiveButton("是", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent=new Intent(Settings.ACTION_SETTINGS);
                                    startActivity(intent);
                                }
                            })
                            .setNegativeButton("否", null)
                            .show();
                }

                adapter.notifyDataSetChanged();
                refreshlayout.finishRefresh(2000/*,false*/);//传入false表示刷新失败
                Toast.makeText(getContext(), "刷新成功！！", Toast.LENGTH_SHORT).show();

            }
        });

        //上拉加载的监听
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                if (NetworkUtil.isReady(getContext()))//判断网络是否可以，如果不可用就跳到系统的设置Activity
                {
                    getMoreDataFromNet();
                }else
                {
                    //弹出对话框如果按确定按钮就让他跳转到系统的设置网络的Activity
                    new AlertDialog.Builder(getContext())
                            .setTitle("当前网络不可用")
                            .setMessage("请设置网络！！！")
                            .setPositiveButton("是", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent=new Intent(Settings.ACTION_SETTINGS);
                                    startActivity(intent);
                                }
                            })
                            .setNegativeButton("否", null)
                            .show();
                }

                refreshlayout.finishLoadMore(2000/*,false*/);//传入false表示加载失败
                Toast.makeText(getContext(), "加载成功！！", Toast.LENGTH_SHORT).show();
            }
        });

    }


    //联网请求数据（下拉刷新数据的方法）
    private void getDataFromNet() {
        //联网请求数据
        HttpUtils.handleOkhttp(Constants.NET_URL,"get",callback);
    }



    //OkHttp的回调
   private Callback callback =new Callback() {
        @Override
        //解析失败
        public void onFailure(Call call, IOException e) {
            Log.v("fail",e.getMessage());
            showData();
        }

        @Override
        //解析成功
        public void onResponse(Call call, Response response) throws IOException {

            String result=response.body().string();
            Message msg=new Message();//创建Message对象
            //给对象指定参数
            msg.what=1;//标志（整型变量），用来判断消息是来自那个字线程
            msg.obj=result;
            handler.sendMessage(msg);
        }
    };



 //联网请求加载更多的数据(上拉加载更多数据的方法)
    private void getMoreDataFromNet(){
        Callback callback2 =new Callback() {
            @Override
            //解析失败
            public void onFailure(Call call, IOException e) {
                Log.v("fail", e.getMessage());
                isLoadMore=true;
            }

            @Override
            //解析成功
            public void onResponse(Call call, Response response) throws IOException {

                String result = response.body().string();
                Message msg = new Message();//创建Message对象
                //给对象指定参数
                msg.what = 2;//标志（整型变量），用来判断消息是来自那个字线程
                msg.obj = result;
                handler.sendMessage(msg);
            }
        };

        HttpUtils.handleOkhttp(Constants.NET_URL,"get",callback2);

    }



    //解析JSON数据
    private void processData(String json) {
        if(!isLoadMore){
            mediaItems=parseJson(json);
            showData();//展示数据

        }else {
            isLoadMore=false;
            //加载更多
            //要把的到更多的数据，添加到原来的集合中
            ArrayList<MediaItem> moreData=parseJson(json);
            mediaItems.addAll(moreData);
            //刷新适配器
            adapter.notifyDataSetChanged();
        }

    }

    //展示数据
    private void showData() {
        //设置适配器
        adapter =new NetVideoAdapter(getContext(),mediaItems);
        mListView.setAdapter(adapter);
        if (mediaItems !=null && mediaItems.size() >0){
            mTv_nonet.setVisibility(View.GONE);
        }else {
            mTv_nonet.setVisibility(View.VISIBLE);
        }

        mProgressBar.setVisibility(View.GONE);
    }

    /*
    1.用系统接口解析json数据
    2.使用第三方解析工具（Gson，fastjson（阿里巴巴））
     */
    private ArrayList<MediaItem> parseJson(String json) {
        ArrayList<MediaItem> mediaItems =new ArrayList<>();
        try {
            JSONObject jsonObject =new JSONObject(json);
            JSONArray jsonArray =jsonObject.optJSONArray("trailers");
            if (jsonArray !=null && jsonArray.length() >0){
                for (int i=0;i<jsonArray.length();i++){
                    JSONObject jsonObjectItem = (JSONObject) jsonArray.get(i);
                    if (jsonObjectItem !=null){
                        MediaItem mediaItem =new MediaItem();
                        String movieName =jsonObjectItem.optString("movieName");//name
                        mediaItem.setName(movieName);
                        String videoTitle =jsonObjectItem.optString("videoTitle");//destc
                        mediaItem.setDesc(videoTitle);
                        String imageUrl =jsonObjectItem.optString("coverImg");//imageUrl
                        mediaItem.setImageUrl(imageUrl);
                        String hightUrl =jsonObjectItem.optString("hightUrl");//data
                        mediaItem.setData(hightUrl);
                        //把数据添加到集合中
                        mediaItems.add(mediaItem);

                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return mediaItems;
    }


    //列表Item的监听
    class MyOnItemClickListener implements android.widget.AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //3.传递列表数据-对象-要序列化
            Intent intent = new Intent(getContext(), SystemVideoPlayer.class);
            Bundle bundle =new Bundle();
            bundle.putSerializable("videolist",mediaItems);
            intent.putExtras(bundle);
            intent.putExtra("position",position);//如果使用了上拉加载时position是position-1（因为有头部的存在）
            startActivity(intent);

        }
    }

}
