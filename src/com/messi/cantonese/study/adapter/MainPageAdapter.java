package com.messi.cantonese.study.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.messi.cantonese.study.CollectedFragment;
import com.messi.cantonese.study.DialogListFragment;
import com.messi.cantonese.study.MainFragment;

public class MainPageAdapter extends FragmentPagerAdapter {

	public static final String[] CONTENT = new String[] { "粤语助手", "我的收藏" };//, , "我的收藏" "粤语练习", 
	
    public MainPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if( position == 0 ){
        	return MainFragment.getInstance();
        }
//        else if( position == 1 ){
//        	return DialogListFragment.getInstance();
//        }
        else if( position == 1 ){
        	return CollectedFragment.getInstance();
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