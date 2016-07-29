package com.example.printer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;

public class FileUtils {
	 public static void copyFiles(Context context, String fileName, File desFile) {  
	        InputStream in = null;  
	        OutputStream out = null;  
	        try {  
	            in = context.getApplicationContext().getAssets().open(fileName);  
	            out = new FileOutputStream(desFile.getAbsolutePath());  
	            byte[] bytes = new byte[1024];  
	            int i;  
	            while ((i = in.read(bytes)) != -1)  
	                out.write(bytes, 0 , i);  
	        } catch (IOException e) {  
	            e.printStackTrace();  
	        }finally {  
	            try {  
	                if (in != null)  
	                    in.close();  
	                if (out != null)  
	                    out.close();  
	            } catch (IOException e) {  
	                e.printStackTrace();  
	            }  
	  
	        }  
	    }  
	  
	    public static boolean hasExternalStorage() {  
	        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);  
	    }  
	  
	    /** 
	     * 获取缓存路径 
	     * 

	     * @param context 
	     * @return 返回缓存文件路径 
	     */  
	    public static File getCacheDir(Context context) {  
	        File cache;  
	        if (hasExternalStorage()) {  
	            cache = context.getExternalCacheDir();  
	        } else {  
	            cache = context.getCacheDir();  
	        }  
	        if (!cache.exists())  
	            cache.mkdirs();  
	        return cache;  
	    } 
	    public static void copyAssetDirToFiles(Context context, String dirname)
				throws IOException {
			File dir = new File(context.getFilesDir() + "/" + dirname);
			dir.mkdir();
			
			AssetManager assetManager = context.getAssets();
			String[] children = assetManager.list(dirname);
			for (String child : children) {
				child = dirname + '/' + child;
				String[] grandChildren = assetManager.list(child);
				if (0 == grandChildren.length)
					copyAssetFileToFiles(context, child);
				else
					copyAssetDirToFiles(context, child);
			}
		}
		
		public static void copyAssetFileToFiles(Context context, String filename)
				throws IOException {
			InputStream is = context.getAssets().open(filename);
			byte[] buffer = new byte[is.available()];
			is.read(buffer);
			is.close();
			File of = new File(context.getFilesDir() + "/" + filename);
			of.createNewFile();
			FileOutputStream os = new FileOutputStream(of);
			os.write(buffer);
			os.close();
		}
}
