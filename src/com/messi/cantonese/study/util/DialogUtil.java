package com.messi.cantonese.study.util;

import java.util.ArrayList;

import android.content.Context;

import com.messi.cantonese.study.R;
import com.messi.cantonese.study.bean.DialogBean;

public class DialogUtil {
	
	public static ArrayList<DialogBean> getDialogList(Context mContext){
		ArrayList<DialogBean> list = new ArrayList<DialogBean>();
		String content = mContext.getResources().getString(R.string.dialog1);
		String[] strList = content.split("#");
		for(String item : strList){
			DialogBean bean = new DialogBean();
			bean.setQuestion(item);
			list.add(bean);
		}
		return list;
	}

}
