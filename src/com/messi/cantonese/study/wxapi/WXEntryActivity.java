package com.messi.cantonese.study.wxapi;


import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;
import com.baidu.mobstat.StatService;
import com.iflytek.cloud.speech.SpeechUser;
import com.messi.cantonese.study.AboutActivity;
import com.messi.cantonese.study.HelpActivity;
import com.messi.cantonese.study.R;
import com.messi.cantonese.study.RecommendActivity;
import com.messi.cantonese.study.SettingActivity;
import com.messi.cantonese.study.adapter.MainPageAdapter;
import com.messi.cantonese.study.views.PagerSlidingTabStrip;

public class WXEntryActivity extends SherlockFragmentActivity implements OnClickListener {
	
	private DrawerLayout mDrawerLayout;
	private ActionBarDrawerToggle mDrawerToggle;
    private ListView mDrawerList;
	private ViewPager viewPager;
	private PagerSlidingTabStrip indicator;
	private MainPageAdapter mAdapter;
	
	private String[] mPlanetTitles;
	private long exitTime = 0;
	private boolean isRespondWX;
	public static int currentIndex = 0;
	public ActionBar mActionBar;
	public static WXEntryActivity mWXEntryActivity;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		try {
			requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
			requestWindowFeature(Window.FEATURE_PROGRESS);
			super.onCreate(savedInstanceState);
			setContentView(R.layout.content_frame);
			mWXEntryActivity = this;
	        
			setSupportProgressBarIndeterminateVisibility(false);
			setSupportProgressBarVisibility(false);
	        mActionBar = getSupportActionBar();
	        mActionBar.setBackgroundDrawable(getResources().getDrawable(R.color.load_blue));
	        mActionBar.setDisplayHomeAsUpEnabled(true);
	        mActionBar.setDisplayShowHomeEnabled(true);
	        mActionBar.setHomeButtonEnabled(true);
	        
			initDatas();
			initViews();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void initDatas(){
		setMiddleVolume();
		SpeechUser.getUser().login(this, null, null, "appid=" + getString(R.string.app_id), null);
	}
	
	private void initViews(){
		mPlanetTitles = getResources().getStringArray(R.array.planets_array);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
		viewPager = (ViewPager) findViewById(R.id.pager);
		indicator = (PagerSlidingTabStrip) findViewById(R.id.indicator);
		mAdapter = new MainPageAdapter(this.getSupportFragmentManager());
		viewPager.setAdapter(mAdapter);
		viewPager.setOffscreenPageLimit(3);
		indicator.setViewPager(viewPager);
		mDrawerList.setAdapter(new ArrayAdapter<String>(this,R.layout.drawer_list_item, mPlanetTitles));
        // Set the list's click listener
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
		
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
		mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
                ) {
            public void onDrawerClosed(View view) {
            	mActionBar.setTitle("粤语助手");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
            	mActionBar.setTitle("更多");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
	}
	
	private class DrawerItemClickListener implements ListView.OnItemClickListener {
	    @Override
	    public void onItemClick(AdapterView parent, View view, int position, long id) {
	    	try {
				if(position == 2){
					Intent intent = new Intent(Intent.ACTION_VIEW);
					intent.setData(Uri.parse("market://details?id=com.messi.cantonese.study"));
					startActivity(intent);
					StatService.onEvent(WXEntryActivity.this, "1.1_commend", "吐槽评价按钮", 1);
				}else{
					Intent intent = new Intent();
					if(position == 0){
						intent.setClass(WXEntryActivity.this, SettingActivity.class);
						StatService.onEvent(WXEntryActivity.this, "1.1_settingbtn", "应用设置按钮", 1);
					}else if(position == 1){
						intent.setClass(WXEntryActivity.this, RecommendActivity.class);
						StatService.onEvent(WXEntryActivity.this, "1.1_recommendbtn", "推荐应用按钮", 1);
					}else if(position == 3){
						intent.setClass(WXEntryActivity.this, HelpActivity.class);
						StatService.onEvent(WXEntryActivity.this, "1.1_help", "使用帮助按钮", 1);
					}else if(position == 4){
						intent.setClass(WXEntryActivity.this, AboutActivity.class);
						StatService.onEvent(WXEntryActivity.this, "1.1_aboutus", "关于我们按钮", 1);
					}
					WXEntryActivity.this.startActivity(intent);
					if (mDrawerLayout.isDrawerOpen(mDrawerList)) {
						mDrawerLayout.closeDrawer(mDrawerList);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
	    }
	}
	
	@Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }
	
	@Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
//		menu.add("MENU").setIcon(R.drawable.menu_d)
//        .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:  
			menu();
			StatService.onEvent(this, "1.1_homemenu", "主页左上角菜单", 1);
		}
       return super.onOptionsItemSelected(item);
	}
	
	private void menu(){
		if (mDrawerLayout.isDrawerOpen(mDrawerList)) {
        	mDrawerLayout.closeDrawer(mDrawerList);
        } else {
        	mDrawerLayout.openDrawer(mDrawerList);
        }
	}

	@Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
		
//        menu.findItem(R.drawable.menu_d).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }
	
	@Override
	public void onClick(View v) {
//		if (v.getId() == R.id.top_menu_btn) {
//			
//		}
	}
	
	/**
	 * 设置为最大声
	 */
	private void setMiddleVolume(){
		AudioManager mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);  
		int max = mAudioManager.getStreamMaxVolume( AudioManager.STREAM_MUSIC );
		int currentVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
		int middle = max/2;
		if(currentVolume < middle){
			mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, middle, 0);  
		}
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
	}


	@Override
	protected void onResume() {
		super.onResume();
		StatService.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		StatService.onPause(this);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_MENU:
			 menu();
			 StatService.onEvent(this, "1.1_xitongmenu", "系统菜单按钮", 1);
			 return true;
		case KeyEvent.KEYCODE_VOLUME_UP:
			 adjustStreamVolume(keyCode);
	         return true;
	    case KeyEvent.KEYCODE_VOLUME_DOWN:
	    	 adjustStreamVolume(keyCode);
		     return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	private void adjustStreamVolume(int action){
		AudioManager mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE); 
		if(action == KeyEvent.KEYCODE_VOLUME_UP){
			mAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI);
		}else{
			mAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI);
		}
	}
	
	@Override
	public void onBackPressed() {
		if ((System.currentTimeMillis() - exitTime) > 2000) {
			Toast.makeText(getApplicationContext(), "再按一次退出程序", 0).show();
			exitTime = System.currentTimeMillis();
		} else {
			finish();
		}
	}
}
