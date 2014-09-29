package com.pw.schoolknow.push;

import com.pw.schoolknow.app.MyApplication;
import com.pw.schoolknow.helper.NetHelper;
import com.pw.schoolknow.utils.T;

import android.os.AsyncTask;
import android.os.Handler;

public class AsyncSend {
	
	private BaiduPush mBaiduPush;
	private String mMessage;
	private Handler mHandler;
	private MyAsyncTask mTask;
	private String mUserId;
	private OnSendScuessListener mListener;
	
	public AsyncSend(String jsonMsg,String useId) {
		// TODO Auto-generated constructor stub
		mBaiduPush = MyApplication.getInstance().getBaiduPush();
		mMessage = jsonMsg;
		mUserId = useId;
		mHandler = new Handler();
	}
	
	public interface OnSendScuessListener {
		void sendScuess();
	}
	
	public void setOnSendScuessListener(OnSendScuessListener listener) {
		this.mListener = listener;
	}
	
	Runnable reSend = new Runnable() {
		public void run() {
			send();//重发
		}
	};
	
	// 发送
	public void send() {
		if (NetHelper.isNetConnected(MyApplication.getInstance())) {//如果网络可用
			mTask = new MyAsyncTask();
			mTask.execute();
		} else {
			T.showShort(MyApplication.getInstance(),"请保持网络连接正常");
		}
	}

	// 停止
	public void stop() {
		if (mTask != null)
			mTask.cancel(true);
	}
	
	
	class MyAsyncTask extends AsyncTask<Void, Void, String> {

		@Override
		protected String doInBackground(Void... message) {
			String result = "";
			if(mUserId.trim().length()!=0){
				result = mBaiduPush.PushMessage(mMessage, mUserId);
			}
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if (result.contains(BaiduPush.SEND_MSG_ERROR)) {// 如果消息发送失败，则100ms后重发
				mHandler.postDelayed(reSend, 100);
			} else {
				if (mListener != null)
					mListener.sendScuess();
			}
		}
	}

}
