package com.messi.cantonese.study.util;

import com.messi.cantonese.study.bean.DialogBean;

public class StringUtils {

	public static void isMandarinOrCantonese(DialogBean mBean,boolean isPlayResult){
		if(isPlayResult){
			if(mBean.getResult_lan().equals(Settings.cantonese)){
				Settings.role = Settings.cantonese_role;
			}else{
				Settings.role = Settings.mandarin_role;
			}
		}else{
			if(mBean.getQuestion_lan().equals(Settings.cantonese)){
				Settings.role = Settings.cantonese_role;
			}else{
				Settings.role = Settings.mandarin_role;
			}
		}
	}
	
	public static void isChOrEn(String content) {
		Settings.role = "vimary";	
		char[] ch = content.toCharArray();
		for (int i = 0; i < ch.length; i++) {
			char c = ch[i];
			if (isChinese(c)) {
				Settings.role = "vixy";	
				break;
			}
		}
	}

	// 根据Unicode编码完美的判断中文汉字和符号
	private static boolean isChinese(char c) {
		Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
		if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
				|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
				|| ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
				|| ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
				|| ub == Character.UnicodeBlock.GENERAL_PUNCTUATION) {
			return true;
		}
		return false;
	}

}
