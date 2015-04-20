package com.simon.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

public class GlobalUtils {
	private static int mScreenHeight = -1;
	private static int mScreenWidth = -1;
	private static float mDensity = -1f;
	public static void sleepToWait(int time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
            StackTraceElement stack = new Throwable().getStackTrace()[0];
            String localInfo = stack.getFileName() + ":" + stack.getLineNumber();
            String message = e.toString();
		}
	}

	public static boolean deleteDirectory(File path) {
		if (!path.exists()) {
			return true;
		}
        if (path.isFile()) {
            return path.delete();
        }

		File[] files = path.listFiles();
		if (files == null) {
			return true;
		}

		for (int i = 0; i < files.length; i++) {
			if (files[i].isDirectory()) {
				deleteDirectory(files[i]);

			} else {
				files[i].delete();
			}
		}
		return path.delete();
	}

	/**
	 * 获取整个文件夹总大小
	 * 
	 * @param path
	 *            文件夹
	 * @return 文件夹大小(Byte)
	 */
	public static long getDirectorySize(File path) {
		if (!path.exists()) {
			return 0;
		}

		File[] files = path.listFiles();
		if (files == null) {
			return 0;
		}

		long length = 0;
		for (File file : files) {
			if (file.isFile()) {
				length += file.length();
			} else {
				length += getDirectorySize(file);
			}
		}
		return length;
	}

	/**
	 * 打开应用市场，让用户评分鼓励本应用
	 * 
	 * @param context
	 */
	public static boolean openInMarket(Context context) {
		try {
			String address = "market://details?id=" + context.getPackageName();
			Uri uri = Uri.parse(address);
			Intent intent = new Intent(Intent.ACTION_VIEW, uri);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(intent);
		} catch (ActivityNotFoundException e) {
            StackTraceElement stack = new Throwable().getStackTrace()[0];
            String localInfo = stack.getFileName() + ":" + stack.getLineNumber();
            String message = e.toString();
            Report.reportError(localInfo, message);
            return false;
		}
        return true;
	}

	/**
	 * 获取当前时间
	 * 
	 * @return 返回当前时间字符串，如：2013-06-09 14:49:28.423
	 */
	public static String getCurrentTime() {
		String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss.S";
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT);
		return simpleDateFormat.format(calendar.getTime());
	}

	/**
	 * 格式化时间
	 * 
	 * @param time
	 *            需要格式化的时间(GMT时间)
	 * @return 格式化之后的时间
	 */
	public static String formatTime(long time) {
		String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss.S";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT);
		return simpleDateFormat.format(time);
	}



	public static int getInternalFieldReflect(String className, String fieldName) {
		int ret = 0;
		try {
			Class<?> theClass = Class.forName(className);
			Field field = theClass.getDeclaredField(fieldName);
			field.setAccessible(true);
			ret = (Integer) field.get(null);
		} catch (ClassNotFoundException e) {
            StackTraceElement stack = new Throwable().getStackTrace()[0];
            String localInfo = stack.getFileName() + ":" + stack.getLineNumber();
            String message = e.toString();
            Report.reportError(localInfo, message);
		} catch (NoSuchFieldException e) {
            StackTraceElement stack = new Throwable().getStackTrace()[0];
            String localInfo = stack.getFileName() + ":" + stack.getLineNumber();
            String message = e.toString();
            Report.reportError(localInfo, message);
		} catch (IllegalAccessException e) {
            StackTraceElement stack = new Throwable().getStackTrace()[0];
            String localInfo = stack.getFileName() + ":" + stack.getLineNumber();
            String message = e.toString();
            Report.reportError(localInfo, message);
		}
		return ret;
	}

	public static boolean arrayContainString(String[] array, String string) {
		if (array == null) {
			return false;
		}
		if (TextUtils.isEmpty(string)) {
			return false;
		}

		for (String theString : array) {
			if (string.equals(theString)) {
				return true;
			}
		}
		return false;
	}

	public static void openBrowser(Context context, String url) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		Uri uri = Uri.parse(url);
		intent.setData(uri);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		try {
			context.startActivity(intent);
		} catch (ActivityNotFoundException e) {
            StackTraceElement stack = new Throwable().getStackTrace()[0];
            String localInfo = stack.getFileName() + ":" + stack.getLineNumber();
            String message = e.toString();
            Report.reportError(localInfo, message);
		}
	}

	/**
	 * SD卡是否处于正常挂载状态
	 * 
	 * @return
	 */
	public static boolean isSdcardMounted() {
		String state = Environment.getExternalStorageState();
		return state.equals(Environment.MEDIA_MOUNTED);
	}

	/**
	 * 获取字符的Unicode编码
	 * 
	 * @param word
	 *            字符
	 * @return Unicode编码
	 */
	public static int getWordUnicode(String word) {
		if (TextUtils.isEmpty(word)) {
			return 0;
		}
		return word.charAt(0);
	}

	/**
	 * 判断字符串是否全为英文字符
	 * 
	 * @param string
	 *            待判断字符串
	 * @return
	 */
	public static boolean isFullCharacterString(String string) {
		for (int index = 0; index < string.length(); index++) {
			char c = string.charAt(index);
			if ((c < 'A' || c > 'Z') && (c < 'a' || c > 'z')) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 拷贝Stream，从InputStream到OutputStream
	 * 
	 * @param inputStream
	 *            Input Stream
	 * @param outputStream
	 *            Output Stream
	 */
	public static boolean copyStream(InputStream inputStream, OutputStream outputStream) {
		if (inputStream == null || outputStream == null) {
			return false;
		}
		final int BUFFER_SIZE = 8 * 1024;
		try {
			byte[] bytes = new byte[BUFFER_SIZE];
			while (true) {
				int count = inputStream.read(bytes, 0, BUFFER_SIZE);
				if (count == -1) {
					break;
				}
				outputStream.write(bytes, 0, count);
			}
		} catch (Exception e) {
            StackTraceElement stack = new Throwable().getStackTrace()[0];
            String localInfo = stack.getFileName() + ":" + stack.getLineNumber();
            String message = e.toString();
            Report.reportError(localInfo, message);
			return false;
		}
		return true;
	}

    public static boolean copyFile(File sourceFile, File targetFile) {
        try {
            FileInputStream fileInputStream = new FileInputStream(sourceFile);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);

            FileOutputStream fileOutputStream = new FileOutputStream(targetFile);
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);

            byte[] buffer = new byte[1024 * 32];
            int length;
            while ((length = bufferedInputStream.read(buffer)) != -1) {
                bufferedOutputStream.write(buffer, 0, length);
            }
            bufferedOutputStream.flush();

            bufferedInputStream.close();
            bufferedOutputStream.close();
            fileOutputStream.close();
            fileInputStream.close();

        } catch (Exception e) {
            StackTraceElement stack = new Throwable().getStackTrace()[0];
            String localInfo = stack.getFileName() + ":" + stack.getLineNumber();
            String message = e.toString();
            Report.reportError(localInfo, message);
            return false;
        }
        return true;
    }

    public static boolean copyDirectory(String sourceDir, String targetDir) {
        File fileTarget = new File(targetDir);
        if (!fileTarget.exists()) {
            boolean succ = fileTarget.mkdirs();
            if (!succ) {
                return false;
            }
        }

        File fileSource = new File(sourceDir);
        File[] files = fileSource.listFiles();
        if (files == null) {
            return false;
        }
        for (int i = 0; i < files.length; i++) {
            if (files[i].isFile()) {
                File sourceFile = files[i];
                File targetFile = new File(targetDir + "/" + files[i].getName());
                if (!copyFile(sourceFile, targetFile)) {
                    return false;
                }

            } else if (files[i].isDirectory()) {
                String dirSource = sourceDir + "/" + files[i].getName();
                String dirTarget = targetDir + "/" + files[i].getName();
                if (!copyDirectory(dirSource, dirTarget)) {
                    return false;
                }
            }
        }
        return true;
    }

    public static String getMD5(byte[] source) {
		String md5 = null;
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
		try {
			java.security.MessageDigest messageDigest = java.security.MessageDigest.getInstance("MD5");
			messageDigest.update(source);
			byte bytes[] = messageDigest.digest();
			char str[] = new char[16 * 2];
			int k = 0;
			for (int i = 0; i < 16; i++) {
				byte theByte = bytes[i];
				str[k++] = hexDigits[theByte >>> 4 & 0xf];
				str[k++] = hexDigits[theByte & 0xf];
			}
			md5 = new String(str);

		} catch (NoSuchAlgorithmException e) {
            StackTraceElement stack = new Throwable().getStackTrace()[0];
            String localInfo = stack.getFileName() + ":" + stack.getLineNumber();
            String message = e.toString();
            Report.reportError(localInfo, message);
		}
		return md5;
	}

    public static String getFileMD5(String filePath) {
        String md5 = null;
        char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
        try {
            java.security.MessageDigest messageDigest = java.security.MessageDigest.getInstance("MD5");

            byte[] buffer = new byte[32 * 1024];
            FileInputStream fileInputStream = new FileInputStream(filePath);
            int read = 0;
            while ((read = fileInputStream.read(buffer, 0, buffer.length)) != -1) {
                messageDigest.update(buffer, 0, read);
            }
            byte bytes[] = messageDigest.digest();
            char str[] = new char[16 * 2];
            int k = 0;
            for (int i = 0; i < 16; i++) {
                byte theByte = bytes[i];
                str[k++] = hexDigits[theByte >>> 4 & 0xf];
                str[k++] = hexDigits[theByte & 0xf];
            }
            md5 = new String(str);

        } catch (Exception e) {
            StackTraceElement stack = new Throwable().getStackTrace()[0];
            String localInfo = stack.getFileName() + ":" + stack.getLineNumber();
            String message = e.toString();
            Report.reportError(localInfo, message);
        }
        return md5;
    }

	/**
	 * 将整数转化为byte[]
	 * 
	 * @param value
	 *            整数
	 * @return byte[]
	 */
	public static byte[] intToBytes(int value) {
		int length = 4;
		byte[] bytes = new byte[length];
		for (int i = length - 1; i >= 0; i--) {
			int offset = i * 8; // 24, 16, 8
			bytes[i] = (byte) (value >> offset);
		}
		return bytes;
	}

	public static String formatSize(long bytes, int reserve) {
		if (reserve < 0) {
			return "";
		}

		String ret = "";
		String format = "%." + reserve + "f";

		if (bytes < 1024) {
			// Bytes
			format += " B";
			ret = String.format(format, (float) bytes);

		} else if (bytes < 1024 * 1024) {
			// K
			float kb = (float) bytes / (float) 1024;
			format += " K";
			ret = String.format(format, kb);

		} else if (bytes < 1024 * 1024 * 1024) {
			// M
			long mb = bytes / (1024 * 1024);
			float kb = (float) (bytes - mb * 1024 * 1024) / (float) (1024 * 1024);
			format += " M";
			ret = String.format(format, mb + kb);

		} else {
			// G
			long gb = bytes / (1024 * 1024 * 1024);
			float mb = (float) (bytes - gb * 1024 * 1024 * 1024) / (float) (1024 * 1024 * 1024);
			format += " G";
			ret = String.format(format, gb + mb);

		}
		return ret;
	}

    @SuppressWarnings({ "deprecation" })
    public static long getPartitionAvailableSize(String partition) {
        StatFs statFs = null;
        try {
            statFs = new StatFs(partition);
        } catch (Exception e) {
            StackTraceElement stack = new Throwable().getStackTrace()[0];
            String localInfo = stack.getFileName() + ":" + stack.getLineNumber();
            String message = e.toString();
            Report.reportError(localInfo, message);
            return 0;
        }

        long blockSize = 0;
        long availableBlock = 0;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2) {
            blockSize = statFs.getBlockSize();
            availableBlock = statFs.getAvailableBlocks();
        } else {
            try {
                blockSize = statFs.getBlockSizeLong();
                availableBlock = statFs.getAvailableBlocksLong();
            } catch (NoSuchMethodError e) {
                blockSize = statFs.getBlockSize();
                availableBlock = statFs.getAvailableBlocks();
            }
        }
        return availableBlock * blockSize;
    }

    @SuppressWarnings({ "deprecation" })
    public static long getPartitionTotalSize(String partition) {
        StatFs statFs = null;
        try {
            statFs = new StatFs(partition);
        } catch (Exception e) {
            StackTraceElement stack = new Throwable().getStackTrace()[0];
            String localInfo = stack.getFileName() + ":" + stack.getLineNumber();
            String message = e.toString();
            Report.reportError(localInfo, message);
            return 0;
        }

        long blockSize = 0;
        long totalCount = 0;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2) {
            blockSize = statFs.getBlockSize();
            totalCount = statFs.getBlockCount();
        } else {
            try {
                blockSize = statFs.getBlockSizeLong();
                totalCount = statFs.getBlockCountLong();
            } catch (NoSuchMethodError e) {
                blockSize = statFs.getBlockSize();
                totalCount = statFs.getBlockCount();
            }
        }
        return totalCount * blockSize;
    }

	@SuppressWarnings({ "deprecation" })
	public static long getInnerPartitionAvailableSize() {
		File file = Environment.getDataDirectory();
        StatFs statFs = null;
        try {
            statFs = new StatFs(file.getPath());
        } catch (Exception e) {
            StackTraceElement stack = new Throwable().getStackTrace()[0];
            String localInfo = stack.getFileName() + ":" + stack.getLineNumber();
            String message = e.toString();
            Report.reportError(localInfo, message);
            return 0;
        }

        long blockSize = 0;
		long availableBlock = 0;
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2) {
			blockSize = statFs.getBlockSize();
			availableBlock = statFs.getAvailableBlocks();
		} else {
            try {
                blockSize = statFs.getBlockSizeLong();
                availableBlock = statFs.getAvailableBlocksLong();
            } catch (NoSuchMethodError e) {
                blockSize = statFs.getBlockSize();
                availableBlock = statFs.getAvailableBlocks();
            }
		}
		return availableBlock * blockSize;
	}

	@SuppressWarnings({ "deprecation" })
	public static long getInnerPartitionTotalSize() {
		File file = Environment.getDataDirectory();
        StatFs statFs = null;
        try {
            statFs = new StatFs(file.getPath());
        } catch (Exception e) {
            StackTraceElement stack = new Throwable().getStackTrace()[0];
            String localInfo = stack.getFileName() + ":" + stack.getLineNumber();
            String message = e.toString();
            Report.reportError(localInfo, message);
            return 0;
        }

        long blockSize = 0;
		long totalCount = 0;
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2) {
			blockSize = statFs.getBlockSize();
			totalCount = statFs.getBlockCount();
		} else {
            try {
                blockSize = statFs.getBlockSizeLong();
                totalCount = statFs.getBlockCountLong();
            } catch (NoSuchMethodError e) {
                blockSize = statFs.getBlockSize();
                totalCount = statFs.getBlockCount();
            }
		}
		return totalCount * blockSize;
	}

	@SuppressWarnings({ "deprecation" })
	public static long getExternalPartitionAvailableSize() {
		File file = Environment.getExternalStorageDirectory();
        StatFs statFs = null;
        try {
            statFs = new StatFs(file.getPath());
        } catch (Exception e) {
            StackTraceElement stack = new Throwable().getStackTrace()[0];
            String localInfo = stack.getFileName() + ":" + stack.getLineNumber();
            String message = e.toString();
            Report.reportError(localInfo, message);
            return 0;
        }

        long blockSize = 0;
		long availableBlock = 0;
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2) {
			blockSize = statFs.getBlockSize();
			availableBlock = statFs.getAvailableBlocks();
		} else {
            try {
                blockSize = statFs.getBlockSizeLong();
                availableBlock = statFs.getAvailableBlocksLong();
            } catch (NoSuchMethodError e) {
                blockSize = statFs.getBlockSize();
                availableBlock = statFs.getAvailableBlocks();
            }
		}
		return availableBlock * blockSize;
	}
	

	@SuppressWarnings({ "deprecation" })
	public static long getExternalPartitionTotalSize() {
		File file = Environment.getExternalStorageDirectory();
        StatFs statFs = null;
        try {
            statFs = new StatFs(file.getPath());
        } catch (Exception e) {
            StackTraceElement stack = new Throwable().getStackTrace()[0];
            String localInfo = stack.getFileName() + ":" + stack.getLineNumber();
            String message = e.toString();
            Report.reportError(localInfo, message);
            return 0;
        }

		long blockSize = 0;
		long totalCount = 0;
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2) {
			blockSize = statFs.getBlockSize();
			totalCount = statFs.getBlockCount();
		} else {
            try {
                blockSize = statFs.getBlockSizeLong();
                totalCount = statFs.getBlockCountLong();
            } catch (NoSuchMethodError e) {
                blockSize = statFs.getBlockSize();
                totalCount = statFs.getBlockCount();
            }
		}
		return totalCount * blockSize;
	}

	/**
	 * 判断自己是否运行在前端
	 * 
	 * @param context
	 *            Context
	 * @return 自己App是否运行在前端
	 */
	public static boolean isAppForeGround(Context context) {
		ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningAppProcessInfo> processInfos = activityManager.getRunningAppProcesses();
		if (processInfos == null) {
			return false;
		}

		Iterator iterator = processInfos.iterator();
		while (iterator.hasNext()) {
			ActivityManager.RunningAppProcessInfo processInfo = (ActivityManager.RunningAppProcessInfo) iterator.next();
			if (processInfo.pid != android.os.Process.myPid()) {
				continue;
			}

			if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
				return true;
			}
		}
		return false;
	}


    public static String getAppChannel(Context context) {
        try {
            ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(),
                    PackageManager.GET_META_DATA);
            return applicationInfo.metaData.getString("UMENG_CHANNEL");
        } catch (PackageManager.NameNotFoundException e) {
            StackTraceElement stack = new Throwable().getStackTrace()[0];
            String localInfo = stack.getFileName() + ":" + stack.getLineNumber();
            String message = e.toString();
            Report.reportError(localInfo, message);
        }
        return "";
    }
    
    public static int getScreenHeight(Activity activity) {
    	if (mScreenHeight < 0) {
    		mScreenHeight = activity.getWindowManager().getDefaultDisplay().getHeight();
		}
    	return mScreenHeight;
	}
    
    public static int getScreenWidth(Activity activity) {
    	if (mScreenWidth < 0) {
			Display display = activity.getWindowManager().getDefaultDisplay();
    		mScreenWidth = display.getWidth();
		}
    	return mScreenWidth;
	}

	public static float getScreenDensity(Activity activity) {
		if(mDensity != -1f)return mDensity;
		DisplayMetrics metric = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(metric);
		mDensity = metric.density;
		return mDensity;
	}


	public static String getPinyinString(String pinyinDetail) {
		if (TextUtils.isEmpty(pinyinDetail)) {
			return "";
		}

		String[] pinyins = pinyinDetail.split("#");
		String pinyinString = "";
		if (pinyins.length > 2) {
			pinyinString += (pinyins[0] + ",");
			pinyinString += (pinyins[1] + "...");
		} else if (pinyins.length == 2) {
			pinyinString += (pinyins[0] + ",");
			pinyinString += pinyins[1];
		} else {
			pinyinString += pinyins[0];
		}
		return pinyinString;
	}


	
	
	private static String formatTimeToDate(long time) {
		String DATE_FORMAT = "yyyy-MM-dd";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT);
		return simpleDateFormat.format(time);
	}

	private static long getTimesMorning() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTimeInMillis();
	}
	

	
	/**
	 * 设置控件获取到焦点
	 * @param editText
	 */
	public static void setEditTextFocus(EditText editText){
		if (Build.VERSION.SDK_INT > Build.VERSION_CODES.GINGERBREAD_MR1) {
            editText.setTextIsSelectable(true);
        }
	}
	
	/** 判断处理器是不是ARM类型 */
	public static boolean isCpuARM() {
		boolean result = false;
		try {
			InputStream is = new FileInputStream("/proc/cpuinfo");
			InputStreamReader ir = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(ir);
			try {
				String nameProcessor = "Processor";
				while (true) {
					String line = br.readLine();
					String[] pair = null;
					if (line == null) {
						break;
					}
					pair = line.split(":");
					if (pair.length != 2)
						continue;
					String key = pair[0].trim();
					String val = pair[1].trim();
					if (key.compareTo(nameProcessor) == 0) {
						result = val.contains("ARMv");
						break;
					}
				}
			} finally {
				br.close();
				ir.close();
				is.close();
			}
		} catch (Exception e) {
            StackTraceElement stack = new Throwable().getStackTrace()[0];
            String localInfo = stack.getFileName() + ":" + stack.getLineNumber();
            String message = e.toString();
            Report.reportError(localInfo, message);
		}
		return result;
	}
}
