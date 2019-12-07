package com.example.textthread.TabLayout.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;

import com.example.textthread.R;
import com.example.textthread.TabLayout.fragment.ChannelFragment;
import com.example.textthread.TabLayout.fragment.DynamicConditionFragment;
import com.example.textthread.TabLayout.fragment.HomePageFragment;
import com.example.textthread.TabLayout.fragment.PeopleNearbyFragment;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    HomePageFragment homePageFragment=new HomePageFragment();
    ChannelFragment channelFragment=new ChannelFragment();
    DynamicConditionFragment dynamicConditionFragment=new DynamicConditionFragment();
    PeopleNearbyFragment peopleNearbyFragment=new PeopleNearbyFragment();
    TabLayout tabLayout;
    private int[] a={0,1,2,3};

    public ViewPagerAdapter(FragmentManager fm, TabLayout tabLayout) {
        super(fm);
        this.tabLayout=tabLayout;

    }
    @Override
    //根据位置返回对应的Fragment
    public Fragment getItem(int position) {
    if (position==0)
    {
        return homePageFragment;
    }
    if (position==1)
    {
        return channelFragment;
    }
    if (position==2)
    {
            return dynamicConditionFragment;
    }
    if (position==3)
    {
        return peopleNearbyFragment;
    }
       return null;
    }

    @Override
    public int getCount() {
        return a.length;
    }

    @Nullable
    @Override
    //得到页面的标题
    public CharSequence getPageTitle(int position) {
        if (position==0)
        {
            return homePageFragment.getTitle();
        }
        if (position==1)
        {

            return channelFragment.getTitle();
        }
        if (position==2)
        {
            return dynamicConditionFragment.getTitle();
        }
        if (position==3)
        {
            return peopleNearbyFragment.getTitle();
        }
       return null;
    }

}
