package com.simon.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;


public class FileUtils {
	
	 private boolean copyDatabaseFile(Context context,String fileName, String fullPath) {
         File file = new File(fullPath);
         if (file.exists()) {
             if (!file.delete()) {
                 return false;
             }
         }

         try {
             InputStream inputStream = context.getAssets().open(fileName);
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
             String localInfo = stack.getFileName() + ":" + stack.getLineNumber();
             String message = e.toString();
             return false;
         }
         return true;
     }

}
