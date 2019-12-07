package com.example.textthread.TabLayout.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.textthread.R;
import com.example.textthread.TabLayout.activity.SystemVideoPlayer;
import com.example.textthread.TabLayout.adapter.NetVideoAdapter;
import com.example.textthread.TabLayout.domain.MediaItem;
import com.example.textthread.TabLayout.utils.CacheUtils;
import com.example.textthread.TabLayout.utils.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;


public class DynamicConditionFragment extends Fragment {
    //标题
    String title = "网络视频";

    /*
    通过xUtils3来初始化控件（2）
 */
    @ViewInject(R.id.listview)
    private ListView mListView;
    @ViewInject(R.id.tv_nonet)
    private TextView mTv_nonet;
    @ViewInject(R.id.pb_loading)
    private ProgressBar mProgressBar;
    private ArrayList<MediaItem> mediaItems;
    private NetVideoAdapter adapter;
    private boolean isLoadMore =false;//是否已经加载更多的数据

    //写一个get方法获取数据
    //得到标题
    public String getTitle() {
        return title;
    }

    public DynamicConditionFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dynamic_condition, container, false);
        /*
            通过xUtils3来初始化控件（1）
         */
        //第一个参数是：DynamicConditionFragment.this，第二个参数：布局
        x.view().inject(this,view);
        initData();
        mListView.setOnItemClickListener(new MyOnItemClickListener());


        return view;
    }

    private void initData() {

        String saveJson = CacheUtils.getString(getContext(),Constants.NET_URL);
        if (!TextUtils.isEmpty(saveJson)){
            processData(saveJson);
        }
        getDataFromNet();//下拉刷新时再调用一下这个方法就OK了
    }

    //联网请求数据（下拉刷新数据的方法）
    private void getDataFromNet() {
        //联网请求数据
        //视频内容
        RequestParams params =new RequestParams(Constants.NET_URL);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("联网成功==");
                //缓存数据
                CacheUtils.putString(getContext(),Constants.NET_URL,result);
                //主线程
                processData(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtil.e("联网失败=="+ex.getMessage());
                showData();
            }

            @Override
            public void onCancelled(CancelledException cex) {
                LogUtil.e("onCancelled=="+cex.getMessage());

            }

            @Override
            public void onFinished() {
                LogUtil.e("onFinished==");
            }
        });
    }

 //联网请求加载更多的数据(上拉加载更多数据的方法)
    private void getMoreDataFromNet(){
        //联网请求数据
        //视频内容
        RequestParams params =new RequestParams(Constants.NET_URL);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("联网成功==");
                //主线程
                isLoadMore=true;
                processData(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtil.e("联网失败=="+ex.getMessage());
                isLoadMore=true;
            }

            @Override
            public void onCancelled(CancelledException cex) {
                LogUtil.e("取消联网加载=="+cex.getMessage());
                isLoadMore=false;
            }

            @Override
            public void onFinished() {
                LogUtil.e("加载数据完成！");
                isLoadMore=false;
            }
        });
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

            /*
            调用上拉加载消失的方法
             */

        }

    }

    //展示数据
    private void showData() {
        //设置适配器
        adapter =new NetVideoAdapter(getContext(),mediaItems);
        mListView.setAdapter(adapter);
  /*
        在这里调用加载结束的方法，使下拉刷新的效果消失
  * */
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
