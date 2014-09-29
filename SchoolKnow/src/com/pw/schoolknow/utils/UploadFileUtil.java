package com.pw.schoolknow.utils;



import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import com.pw.schoolknow.config.ServerConfig;
import com.pw.schoolknow.helper.LoginHelper;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;


public class UploadFileUtil extends AsyncTask<Object, Integer, Void> {
	
	Context context;
	@SuppressLint("SdCardPath")
	String filePath=null;
	
	private ProgressDialog dialog = null;
	HttpURLConnection connection = null;
	DataOutputStream outputStream = null;
	DataInputStream inputStream = null;	
	String end = "\r\n";
	String twoHyphens = "--";
	String boundary = "******";
	String uploadUrl=null;
	
	public UploadFileUtil(Context context,String filePath){
		this.context=context;
		this.filePath=filePath;
		try {
			String temp= URLEncoder.encode(new File(filePath).getName(),"UTF-8");
			String author=new LoginHelper(context).getStuId();
			uploadUrl=ServerConfig.HOST+"/schoolknow/uploadFile.php?name="+temp
					+"&author="+author;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	
	@Override
	protected void onPreExecute() {
		dialog = new ProgressDialog(context);
		dialog.setMessage("正在上传...");
		dialog.setIndeterminate(false);
		dialog.setCancelable(false);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		dialog.setProgress(0);
		dialog.show();
	}

	@Override
	protected Void doInBackground(Object... arg0) {
		File uploadFile = new File(filePath);
		long totalSize = uploadFile.length();
		long length = 0;
		int progress;
		int bytesRead, bytesAvailable, bufferSize;
		byte[] buffer;
		int maxBufferSize = 256 * 1024;// 256KB

		try {
			FileInputStream fileInputStream = new FileInputStream(new File(
					filePath));

			URL url = new URL(uploadUrl);
			connection = (HttpURLConnection) url.openConnection();

			// Set size of every block for post
			connection.setChunkedStreamingMode(256 * 1024);// 256KB

			// Allow Inputs & Outputs
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setUseCaches(false);

			// Enable POST method
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Connection", "Keep-Alive");
			connection.setRequestProperty("Charset", "UTF-8");
			connection.setRequestProperty("Content-Type",
					"multipart/form-data;boundary=" + boundary);

			outputStream = new DataOutputStream(
					connection.getOutputStream());
			outputStream.writeBytes(twoHyphens + boundary + end);
			outputStream
					.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\""
							+ filePath + "\"" + end);
			outputStream.writeBytes(end);

			bytesAvailable = fileInputStream.available();
			bufferSize = Math.min(bytesAvailable, maxBufferSize);
			buffer = new byte[bufferSize];

			// Read file
			bytesRead = fileInputStream.read(buffer, 0, bufferSize);

			while (bytesRead > 0) {
				outputStream.write(buffer, 0, bufferSize);
				length += bufferSize;
				progress = (int) ((length * 100) / totalSize);
				publishProgress(progress);

				bytesAvailable = fileInputStream.available();
				bufferSize = Math.min(bytesAvailable, maxBufferSize);
				bytesRead = fileInputStream.read(buffer, 0, bufferSize);
			}
			outputStream.writeBytes(end);
			outputStream.writeBytes(twoHyphens + boundary + twoHyphens
					+ end);
			publishProgress(100);

			fileInputStream.close();
			outputStream.flush();
			outputStream.close();

		} catch (Exception ex) {
		}
		return null;
	}
	
	@Override
	protected void onProgressUpdate(Integer... progress) {
		dialog.setProgress(progress[0]);
	}

	@Override
	protected void onPostExecute(Void result) {
		try {
			dialog.dismiss();
			Toast.makeText(context,"上传成功!", Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
		}
	}
}
