package com.simon.shelf;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;

import com.simon.utils.NormalConfig;
import com.simon.utils.Report;
import com.svo.mmreader.R;
import com.umeng.analytics.MobclickAgent;

public class SplashActivity extends Activity {

	private static boolean sIsSplashed;

	private boolean mIsUpdateFailed;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ac_splash);
		MobclickAgent.updateOnlineConfig(this);

		if (sIsSplashed) {
			startHomeActivity();
			finish();

		} else {
			if(isFirstTime()){
				
			}
				
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		Report.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		Report.onPause(this);
	}

	@Override
	public void onBackPressed() {
		if (mIsUpdateFailed) {
			super.onBackPressed();
		}
	}

	private void startHomeActivity() {
		Intent intent = new Intent(this, BookShelf.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}
	
	
	private void loadAssertBook(){
		try {
			String[]  flLists= this.getAssets().list("book");
			boolean sdCardExist = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED); //判断sd卡是否存在
			if(!sdCardExist) return;
			File sdDir = Environment.getExternalStorageDirectory();           
            File root = new File(sdDir.getPath()+"/"+ NormalConfig.ROOT_DIR);  
            root.mkdir();
			if(flLists != null && flLists.length > 0){
				
			}
		   
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private boolean copyDatabaseFile(String fileName, String fullPath) {
		File file = new File(fullPath);
		if (file.exists()) {
			if (!file.delete()) {
				return false;
			}
		}

		try {
			InputStream inputStream = getAssets().open(fileName);
			OutputStream outputStream = new FileOutputStream(file);
			byte[] buffer = new byte[4096];
			int length = 0;
			while ((length = inputStream.read(buffer)) > 0) {
				outputStream.write(buffer, 0, length);
			}
			inputStream.close();
			outputStream.close();

		} catch (IOException e) {
			StackTraceElement stack = new Throwable().getStackTrace()[0];
			String localInfo = stack.getFileName() + ":"
					+ stack.getLineNumber();
			String message = e.toString();
			Report.reportError(localInfo, message);
			return false;
		}
		return true;
	}

	private boolean isFirstTime() {
		SharedPreferences sharedPreferences = getSharedPreferences(NormalConfig.NORMAL_CONFIG,MODE_PRIVATE);
		return sharedPreferences.getBoolean(NormalConfig.FIRST_START, true);
	}


}
