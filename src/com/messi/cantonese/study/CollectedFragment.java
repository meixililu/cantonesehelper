package com.messi.cantonese.study;

import java.util.List;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;

import com.baidu.mobstat.StatService;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.iflytek.cloud.speech.SpeechRecognizer;
import com.iflytek.cloud.speech.SpeechSynthesizer;
import com.messi.cantonese.study.adapter.CollectedListItemAdapter;
import com.messi.cantonese.study.bean.DialogBean;
import com.messi.cantonese.study.db.DataBaseUtil;
import com.messi.cantonese.study.util.LogUtil;
import com.messi.cantonese.study.util.Settings;
import com.messi.cantonese.study.util.ToastUtil;
import com.messi.cantonese.study.wxapi.WXEntryActivity;

public class CollectedFragment extends Fragment implements OnClickListener {

	private PullToRefreshListView recent_used_lv;
	private View view;
	private LayoutInflater mInflater;
	private CollectedListItemAdapter mAdapter;
	private List<DialogBean> beans;

	// 识别对象
	private SpeechRecognizer recognizer;
	// 缓存，保存当前的引擎参数到下一次启动应用程序使用.
	private SharedPreferences mSharedPreferences;
	//合成对象.
	private SpeechSynthesizer mSpeechSynthesizer;

	private DataBaseUtil mDataBaseUtil;
	private int maxNumber = 0;
	
	public static boolean isRespondWX;
	public static boolean isRefresh;
	private static CollectedFragment mMainFragment;
	
	public static CollectedFragment getInstance(){
		if(mMainFragment == null){
			mMainFragment = new CollectedFragment();
		}
		return mMainFragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.collected_main, null);
		init();
		return view;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		isRespondWX = false;
		LogUtil.DefalutLog("CollectedFragment-onDestroy");
	}

	private void init() {
		mInflater = LayoutInflater.from(getActivity());
		mSharedPreferences = getActivity().getSharedPreferences(getActivity().getPackageName(), Activity.MODE_PRIVATE);
		recent_used_lv = (PullToRefreshListView) view.findViewById(R.id.collected_listview);
		mSpeechSynthesizer = SpeechSynthesizer.createSynthesizer(getActivity());
		recognizer = SpeechRecognizer.createRecognizer(getActivity());
		mDataBaseUtil = new DataBaseUtil(getActivity());
		beans = mDataBaseUtil.getDataListCollected(0, Settings.offset);
		mAdapter = new CollectedListItemAdapter(getActivity(), mInflater, beans, 
				mSpeechSynthesizer, mSharedPreferences, mDataBaseUtil, "CollectedFragment");
		recent_used_lv.setAdapter(mAdapter);
		
		recent_used_lv.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				Mode mCurrentMode = refreshView.getCurrentMode();
				switch (mCurrentMode) {
				case PULL_FROM_START:
					maxNumber = 0;
					break;
				case PULL_FROM_END:
					maxNumber += Settings.offset;
					break;
				}
				new WaitTask().execute();				
			}
		});
	}
	
	@Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
        	if(isRefresh){
        		isRefresh = false;
        		maxNumber = 0;
        		new WaitTask().execute();
        	}
        	StatService.onEvent(getActivity(), "1.1_viewcollectedpage", "浏览收藏页面", 1);
        }
	}
	
	class WaitTask extends AsyncTask<Void, Void, Void>{
		@Override
		protected void onPreExecute() {
			WXEntryActivity.mWXEntryActivity.setSupportProgressBarIndeterminateVisibility(true);
			WXEntryActivity.mWXEntryActivity.setSupportProgressBarVisibility(true);
		}
		@Override
		protected Void doInBackground(Void... params) {
			try {
				beans = mDataBaseUtil.getDataListCollected(maxNumber, maxNumber+Settings.offset);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			recent_used_lv.onRefreshComplete();
			WXEntryActivity.mWXEntryActivity.setSupportProgressBarIndeterminateVisibility(false);
			WXEntryActivity.mWXEntryActivity.setSupportProgressBarVisibility(false);
			int size = beans.size();
			if(size < Settings.offset){
				recent_used_lv.setMode(Mode.PULL_FROM_START);
			}else{
				recent_used_lv.setMode(Mode.BOTH);
			}
			mAdapter.notifyDataChange(beans,maxNumber);
		}
	}
	
	@Override
	public void onClick(View v) {
		
	}
	
}
