package com.messi.cantonese.study;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.messi.cantonese.study.util.LogUtil;

public class DialogListFragment extends Fragment implements OnClickListener{

	private View view;
	private TextView self_introduction,everyday_expressions;
	
	private static DialogListFragment mReadAfterMeFragment;
	
	public static DialogListFragment getInstance(){
		if(mReadAfterMeFragment == null){
			mReadAfterMeFragment = new DialogListFragment();
		}
		return mReadAfterMeFragment;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.dialoglist_fragment, null);
		init();
		return view;
	}

	private void init() {
		self_introduction = (TextView)view.findViewById(R.id.self_introduction);
		everyday_expressions = (TextView)view.findViewById(R.id.everyday_expressions);
		
		self_introduction.setOnClickListener(this);
		everyday_expressions.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.self_introduction){
			Intent intent = new Intent(getActivity(),ReadAfterMeActivity.class);
			getActivity().startActivity(intent);
		}else if(v.getId() == R.id.everyday_expressions){
			
		}
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		LogUtil.DefalutLog("ReadAfterMeFragment-onDestroy");
	}

	@Override
	public void onPause() {
		super.onPause();
		LogUtil.DefalutLog("ReadAfterMeFragment-onPause");
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}
	
}
