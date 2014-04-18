package com.messi.cantonese.study.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.messi.cantonese.study.CollectedFragment;
import com.messi.cantonese.study.MainFragment;
import com.messi.cantonese.study.ReadAfterMeFragment;

public class MainPageAdapter extends FragmentPagerAdapter {

	public static final String[] CONTENT = new String[] { "粤语助手", "我的收藏" };//, "情景对话", "我的收藏"
	private Bundle bundle;
	
    public MainPageAdapter(FragmentManager fm,Bundle bundle) {
        super(fm);
        this.bundle = bundle;
    }

    @Override
    public Fragment getItem(int position) {
        if( position == 0 ){
        	return MainFragment.getInstance(bundle);
        }else if( position == 1 ){
        	return CollectedFragment.getInstance(bundle);
        }
        return null;
//        else if( position == 2 ){
//        	return new ReadAfterMeFragment();
//        }else {
//        	return MainFragment.getInstance();
//        }
    }

    @Override
    public int getCount() {
        return CONTENT.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return CONTENT[position].toUpperCase();
    }
}