package com.pw.schoolknow.helper;

import android.app.Activity;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;


//输入法帮助类
public class InputHelper {
	
	//隐藏输入法
	public static void Hide(Activity act){
		((InputMethodManager)act.getSystemService(Context.INPUT_METHOD_SERVICE))
                                .hideSoftInputFromWindow(act.getCurrentFocus().getWindowToken(),
                                                InputMethodManager.HIDE_NOT_ALWAYS);
	}
}
