package com.pw.schoolknow.helper;

import android.content.Context;
import android.media.MediaPlayer;
import android.provider.Settings;

public class TipHelper {
	
	//Ã· æ“Ù
	public static void PlaySound(Context context){
		String curMusic=Settings.System.getString(context.getContentResolver(), Settings.System.NOTIFICATION_SOUND);
		MediaPlayer palyer=new MediaPlayer();
		try{
			palyer.setDataSource(curMusic);
			palyer.prepare();
		}catch(Exception e){
			
		}
		palyer.start();
	}

}
