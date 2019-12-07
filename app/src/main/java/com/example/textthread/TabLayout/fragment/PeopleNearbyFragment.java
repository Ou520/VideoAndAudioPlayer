package com.example.textthread.TabLayout.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.textthread.R;
import com.example.textthread.TabLayout.adapter.NetAudioPagerAdapter;
import com.example.textthread.TabLayout.domain.NetAudioData;
import com.example.textthread.TabLayout.utils.CacheUtils;
import com.example.textthread.TabLayout.utils.Constants;
import com.google.gson.Gson;
import com.scwang.smartrefresh.header.BezierCircleHeader;
import com.scwang.smartrefresh.header.WaterDropHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.xutils.common.Callback;
import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class PeopleNearbyFragment extends Fragment {

    //标题
    String title = "分类型列表";
    @BindView(R.id.listview)
    ListView listview;
    @BindView(R.id.tv_nonet)
    TextView tvNonet;
    @BindView(R.id.pb_loading)
    ProgressBar pbLoading;
    Unbinder unbinder;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;

    private List<NetAudioData.ListEntity> datas;
    private NetAudioPagerAdapter adapter;
    private View view;


    //写一个get方法获取数据
    //得到标题
    public String getTitle() {
        return title;
    }

    //构造方法
    public PeopleNearbyFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_people_nearby, container, false);
        unbinder = ButterKnife.bind(this, view);
        initData();
        setListener();
        return view;
    }

    private void setListener() {
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
                getDataFromNet();
                adapter.notifyDataSetChanged();
                refreshlayout.finishRefresh(2000/*,false*/);//传入false表示刷新失败
                Toast.makeText(getContext(), "刷新成功！！", Toast.LENGTH_SHORT).show();

            }
        });

        //上拉加载的监听
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                Toast.makeText(getContext(), "加载成功！！", Toast.LENGTH_SHORT).show();
                refreshlayout.finishLoadMore(2000/*,false*/);//传入false表示加载失败
            }
        });


    }


    private void initData() {
        String savaJson = CacheUtils.getString(getContext(), Constants.ALL_RES_URL);//缓存中获取数据
        if (!TextUtils.isEmpty(savaJson)) {
            //解析数据
            processData(savaJson);
        }
        //联网
        getDataFromNet();
    }

    //获取数据
    private void getDataFromNet() {
        //使用Xutils去请求数据
        RequestParams params = new RequestParams(Constants.ALL_RES_URL);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("请求数据成功==" + result);
                //保持数据
                CacheUtils.putString(getContext(), Constants.ALL_RES_URL, result);//把数据存入缓存中
                processData(result);//解析数据
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtil.e("请求数据失败==" + ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {
                LogUtil.e("onCancelled==" + cex.getMessage());
            }

            @Override
            public void onFinished() {
                LogUtil.e("onFinished");
            }
        });
    }

    //解析json数据和显示数据
    private void processData(String json) {
        NetAudioData data = parsedJson(json);//解析数据
        datas = data.getList();

        if (datas != null && datas.size() > 0) {
            //有数据
            tvNonet.setVisibility(View.GONE);
            //设置适配器
            adapter = new NetAudioPagerAdapter(getContext(), datas);
            listview.setAdapter(adapter);

        } else {
            tvNonet.setText("没有对应的数据...");
            //没有数据
            tvNonet.setVisibility(View.VISIBLE);
        }

        pbLoading.setVisibility(View.GONE);

    }

    //Gson解析数据
    private NetAudioData parsedJson(String json) {
        return new Gson().fromJson(json, NetAudioData.class);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
