package com.changhong.clearmaster;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import android.R.integer;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Debug.MemoryInfo;
import android.os.Debug;
import android.os.Environment;
import android.os.Parcel;
import android.os.PatternMatcher;
import android.os.StatFs;
import android.os.DropBoxManager.Entry;
import android.text.TextUtils;
import android.util.Log;

public class StorageSizeUtil {

    private static final int ERROR = -1;
    private final static String TAG = "StorageSizeUtil";
    public static ArrayList<Map<String, Object>> mMapList;

    /**
     * SDCARD是否存
     */
    public static boolean externalMemoryAvailable() {
        return android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED);
    }

    /**
     * 获取手机内部剩余存储空间
     * 
     * @return
     */
    public static long getAvailableInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return availableBlocks * blockSize;
    }

    /**
     * 获取手机内部总的存储空间
     * 
     * @return
     */
    
    public static long getTotalInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();
        Log.i(TAG,path.toString() + totalBlocks);
        return totalBlocks * blockSize;
    }

    /**
     * 获取SDCARD剩余存储空间
     * 
     * @return
     */
    public static long getAvailableExternalMemorySize() {
        if (externalMemoryAvailable()) {
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSize();
            long availableBlocks = stat.getAvailableBlocks();
            Log.i(TAG,path.toString() + availableBlocks);
            return availableBlocks * blockSize;
        } else {
            return ERROR;
        }
    }

    /**
     * 获取SDCARD总的存储空间
     * 
     * @return
     */
    public static long getTotalExternalMemorySize() {
        if (externalMemoryAvailable()) {
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSize();
            long totalBlocks = stat.getBlockCount();
            Log.i(TAG,path.toString() + totalBlocks);
            return totalBlocks * blockSize;
        } else {
            return ERROR;
        }
    }

    /**
     * 获取系统总内存
     * 
     * @param context 可传入应用程序上下文。
     * @return 总内存大单位为B。
     */
    public static long getTotalMemorySize(Context context) {
        String dir = "/proc/meminfo";
        try {
            FileReader fr = new FileReader(dir);
            BufferedReader br = new BufferedReader(fr, 2048);
            String memoryLine = br.readLine();
            String subMemoryLine = memoryLine.substring(memoryLine.indexOf("MemTotal:"));
            Log.i(TAG, "subMemoryLine:" + subMemoryLine);
            br.close();
            return Integer.parseInt(subMemoryLine.replaceAll("\\D+", "")) * 1024l;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 获取 /proc/meminfo 下的指定参数；
     * 
     * @param context 可传入应用程序上下文。
     * @param str 取出的指定字段。
     * @return 总内存大单位为B。
     */
    public static long getMemoryItemSize(Context context, String str) {
        String dir = "/proc/meminfo";
        try {
            FileReader fr = new FileReader(dir);
            BufferedReader br = new BufferedReader(fr, 2048);
            String memoryLine = br.readLine();
            String subMemoryLine = memoryLine.substring(memoryLine.indexOf(str+":"));
            Log.i(TAG, str+":" + subMemoryLine);
            br.close();
            return Integer.parseInt(subMemoryLine.replaceAll("\\D+", "")) * 1024l;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 获取当前可用内存，返回数据以字节为单位。
     * 
     * @param context 可传入应用程序上下文。
     * @return 当前可用内存单位为B。
     */
    public static long getAvailableMemory(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(memoryInfo);
        return memoryInfo.availMem;
    }

    private static DecimalFormat fileIntegerFormat = new DecimalFormat("#0");
    private static DecimalFormat fileDecimalFormat = new DecimalFormat("#0.#");

    /**
     * 单位换算
     * 
     * @param size 单位为B
     * @param isInteger 是否返回取整的单位
     * @return 转换后的单位
     */
    
    public static String formatFileSize(long size, boolean isInteger) {
        DecimalFormat df = isInteger ? fileIntegerFormat : fileDecimalFormat;
        String fileSizeString = "0M";
        if (size < 1024 && size > 0) {
            fileSizeString = df.format((double) size) + "B";
        } else if (size < 1024 * 1024) {
            fileSizeString = df.format((double) size / 1024) + "K";
        } else if (size < 1024 * 1024 * 1024) {
            fileSizeString = df.format((double) size / (1024 * 1024)) + "M";
        } else {
            fileSizeString = df.format((double) size / (1024 * 1024 * 1024)) + "G";
        }
        return fileSizeString;
    }
    
    @SuppressLint("NewApi")
	public static String[] neicunjiasu(Context context) {
		// TODO Auto-generated method stub
    	ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);  
        List<RunningAppProcessInfo> infoList = am.getRunningAppProcesses();
        final int SIZE = infoList.size();
        int[] PIDS = new int[SIZE];
        String[] strarr = new String[SIZE];
        for (int i = 0; i < SIZE; i++) {
        	PIDS[i] = infoList.get(i).pid;
        	final int SIZE2 = infoList.get(i).pkgList.length;
        	String[] PKGS = new String[SIZE2];
        	PKGS = infoList.get(i).pkgList;
        	String strs = "";
            for (int j = 0; j < SIZE2; j++) {
            	strs = strs+infoList.get(i).pkgList[j]+"\n";
			}
            int[] pids = new int[]{PIDS[i]};
            Debug.MemoryInfo[] dbmInfo = am.getProcessMemoryInfo(pids);
            int mem = dbmInfo[0].getTotalPrivateDirty()/1024;
            strarr[i] = strs + mem + "MB";
		}
        return strarr;
	}

	public static String[] wuyonganzhuangbao(ArrayList<Map<String, Object>> maplist) {
		// TODO Auto-generated method stub
		return changeToStrEndWithLog(maplist,1);
	}

	public static String[] guanggaolaji(ArrayList<Map<String, Object>> maplist) {
		// TODO Auto-generated method stub		
		return changeToStrEndWithLog(maplist,0);
	}

	public static String[] xiezaicanliu(ArrayList<Map<String, Object>> maplist) {
		// TODO Auto-generated method stub
		return changeToStringArr(maplist);
	}

	public static List<Map<String, Object>> DeepScanSDCard(Context context) {
		// TODO Auto-generated method stub
		ArrayList<Map<String, Object>> maplist = new ArrayList<Map<String, Object>>();		
		String extState = Environment.getExternalStorageState();
		if(extState.equalsIgnoreCase(Environment.MEDIA_MOUNTED)){
			File SDFile = Environment.getExternalStorageDirectory();
			File sdPath = new File(SDFile.getAbsolutePath());
			maplist = (ArrayList<Map<String, Object>>) getFile(maplist,sdPath);
		}
		return maplist;
	}

	private static List<Map<String, Object>> getFile(ArrayList<Map<String, Object>>arrayList, File pathFile) {
		// TODO Auto-generated method stub
		if (pathFile!=null&&pathFile.canRead()&&pathFile.listFiles().length>0) {
			for (File file:pathFile.listFiles()) {
				if (file.isDirectory()) {
					getFile(arrayList,file);
				} else {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put(file.getAbsolutePath(), file.getAbsoluteFile());
					Log.i(TAG, "getFile"+file.getAbsolutePath()+"."+file.getAbsoluteFile());
					arrayList.add(map);
				}
			}
		}
		return arrayList;
	}

	public static String[] huancunlaji(Context context) {
		// TODO Auto-generated method stub
		return changeToStringArr(StorageSizeUtil.calculateExternalCache(context));
	}
	
	public static String[] changeToStringArr(List<Map<String, Object>> list){
		String[] substrs = null;
		int length = list.size();
		final int SIZE = length;
		String strs = new String();
		substrs = new String[SIZE];
		Log.i(TAG, "list.length="+ length);
		int i = 0;
		for (Iterator<Map<String, Object>> it = list.iterator();
				it.hasNext(); ) {
			Map<String, Object> et = it.next();
//			String key = et.getke
			String key = et.keySet().toString();
			String values = et.values().toString();
			strs = "KEY:  " + key + "\n" + "VALUES:  " + values + "\n";
			Log.i(TAG, "list.get"+ strs);
			substrs[i] = strs;
			i++;
		}
		return substrs;
	}

	public static String[] changeToStrEndWithLog(List<Map<String, Object>> list, int flag){
		if (list.size()!=0) {
			ArrayList<Map<String, Object>> arrayList = new ArrayList<Map<String, Object>>();
			for (Iterator<Map<String, Object>> it = list.iterator();
					it.hasNext();) {
				Map<String,Object> map = it.next();
				String key = map.keySet().toString();
				String values = map.values().toString();
				long size = new File(key).length();
				Log.i(TAG, key+"matches regular exception;");
				switch (flag) {
					case 0:
						if (size==0 || key.matches(".*log\\d{4}-\\d{2}-\\d{2}.txt]$")||key.matches(".*log]$")){
							Log.i(TAG,"EndWithLog+add:"+key);
							arrayList.add(map);
						}
						break;
					case 1:
						if (key.matches(".*apk]$")){
							Log.i(TAG,"EndWithLog+add:"+key);
							arrayList.add(map);
						}
						break;
				}
			}
			final int SIZE = arrayList.size();
			String[] strsArr = new String[SIZE];
			for (int i = 0; i < strsArr.length; i++) {
				Map<String,Object> map = arrayList.get(i);
				String key = map.keySet().toString();
				String values = map.values().toString();
				long size = new File(key).length();
				strsArr[i] = "KEY:  "+key+"\n"+"SIZE:  "+size+"\n";
			}
			return strsArr;
		}

		
		String[] substrs = null;
		int length = list.size();
		final int SIZE = length;
		substrs = new String[SIZE];
		String strs = new String();
		ArrayList<String> arrayList = new ArrayList<String>();
		Log.i(TAG, "list.length="+ length);
		int i = 0;
		for (Iterator<Map<String, Object>> it = list.iterator();
				it.hasNext(); ) {
			Map<String, Object> et = it.next();
			String key = et.keySet().toString();
			String values = et.values().toString();
			long size = new File(key).length();
			strs = "KEY:  " + key + "\n" + "VALUES:  " + values + "\n"+"SIZE： "+size;
			Log.i(TAG, "list.get"+ strs);
			substrs[i] = strs;
			i++;
		}
		return substrs;
	}

	private static ArrayList<Map<String, Object>> calculateExternalCache(Context context) {
		// TODO Auto-generated method stub
        if (Environment.getExternalStorageState().equals(  
                Environment.MEDIA_MOUNTED)) {  
        	ArrayList<Map<String, Object>> arrayList = new ArrayList<>();
	        String dir = "/android/data";
	        String basedir = Environment.getExternalStorageDirectory().toString();
	        File directory = new File(basedir+dir);
	        if (directory != null && directory.exists() && directory.isDirectory()) {  
	            for (File item : directory.listFiles()) {  
//	                boolean bresult = item.getAbsoluteFile().delete();  
	            	HashMap<String, Object> hashMap = new HashMap<>();
	            	try {
						hashMap.put(item.getName(), getFolderSize(item));
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	                Log.i(TAG, "|-->"+item.toString()+" delete;");
	                arrayList.add(hashMap);
	            }
	        }
	        Log.i(TAG,"cleanFiles"+directory.toString());
	        return arrayList;
        }
		return null;
	}

	/** * 本应用数据清除管理器 */  
	    /** 
	     * * 清除本应用内部缓存(/data/data/com.xxx.xxx/cache) * * 
	     *  
	     * @param context 
	     */  
	    public static void cleanInternalCache(Context context) {  
	        deleteFilesByDirectory(Environment.getExternalStorageDirectory());  
	    }  
	  
	    /** 
	     * * 清除本应用所有数据库(/data/data/com.xxx.xxx/databases) * * 
	     *  
	     * @param context 
	     */  
	    public static void cleanDatabases(Context context) {  
	        deleteFilesByDirectory(new File("/data/data/"  
	                + context.getPackageName() + "/databases"));  
	    }  
	  
	    /** 
	     * * 清除本应用SharedPreference(/data/data/com.xxx.xxx/shared_prefs) * 
	     *  
	     * @param context 
	     */  
	    public static void cleanSharedPreference(Context context) {  
	        deleteFilesByDirectory(new File("/data/data/"  
	                + context.getPackageName() + "/shared_prefs"));  
	    }  
	  
	    /** 
	     * * 按名字清除本应用数据库 * * 
	     *  
	     * @param context 
	     * @param dbName 
	     */  
	    public static void cleanDatabaseByName(Context context, String dbName) {  
	        context.deleteDatabase(dbName);  
	    }  
	  
	    /** 
	     * * 清除/data/data/com.xxx.xxx/files下的内容 * * 
	     *  
	     * @param context 
	     */  
	    public static void cleanFiles(Context context) {  
//	        String dir = "/Android/data";
//	        String basedir = Environment.getExternalStorageDirectory().toString();
//	        File file = new File(basedir+dir);
//	        Log.i(TAG,"cleanFiles"+file.toString());
//	        deleteFilesByDirectory(file);
	        deleteFilesByDirectory(context.getFilesDir());  
	    }  
	  
	    /** 
	     * * 清除外部cache下的内容(/mnt/sdcard/android/data/com.xxx.xxx/cache) 
	     *  
	     * @param context 
	     */  
	    public static void cleanExternalCache(Context context) {  
	        if (Environment.getExternalStorageState().equals(  
	                Environment.MEDIA_MOUNTED)) {  
		        String dir = "/android/data";
		        String basedir = Environment.getExternalStorageDirectory().toString();
		        File file = new File(basedir+dir);
		        Log.i(TAG,"cleanFiles"+file.toString());
		        deleteFilesByDirectory(file);
	        	
//	            deleteFilesByDirectory(context.getExternalCacheDir());  
	        }  
	    }  
	    /** 
	     * * 清除自定义路径下的文件，使用需小心，请不要误删。而且只支持目录下的文件删除 * * 
	     *  
	     * @param filePath 
	     * */  
	    public static void cleanCustomCache(String filePath) {  
	        deleteFilesByDirectory(new File(filePath));  
	    }  
	  
	    /** 
	     * * 清除本应用所有的数据 * * 
	     *  
	     * @param context 
	     * @param filepath 
	     */  
	    public static void cleanApplicationData(Context context, String... filepath) {  
	        cleanInternalCache(context);  
	        cleanExternalCache(context);  
	        cleanDatabases(context);  
	        cleanSharedPreference(context);  
	        cleanFiles(context);  
	        if (filepath == null) {  
	            return;  
	        }  
	        for (String filePath : filepath) {  
	            cleanCustomCache(filePath);  
	        }  
	    }  
	  
	    /** 
	     * * 删除方法 这里只会删除某个文件夹下的文件，如果传入的directory是个文件，将不做处理 * * 
	     *  
	     * @param directory 
	     */  
	    private static void deleteFilesByDirectory(File directory) {  
            Log.i(TAG, "deleteFilesByDirectory();"+directory);
	        if (directory != null && directory.exists() && directory.isDirectory()) {  
	            for (File item : directory.listFiles()) {  
//	                boolean bresult = item.getAbsoluteFile().delete();  
	                deleteFolderFile(item.getAbsolutePath(),true);
	                Log.i(TAG, "|-->"+item.toString()+" delete;");
	            }  
	        }  
            Log.i(TAG, "deleteFilesByDirectory.quit;");
	    }  
	      
	    // 获取文件  
	    //Context.getExternalFilesDir() --> SDCard/Android/data/你的应用的包名/files/ 目录，一般放一些长时间保存的数据  
	    //Context.getExternalCacheDir() --> SDCard/Android/data/你的应用包名/cache/目录，一般存放临时缓存数据  
	    public static long getFolderSize(File file) throws Exception {  
	        long size = 0;  
	        try {  
	            File[] fileList = file.listFiles();  
	            for (int i = 0; i < fileList.length; i++) {  
	                // 如果下面还有文件  
	                if (fileList[i].isDirectory()) {  
	                    size = size + getFolderSize(fileList[i]);  
	                } else {  
	                    size = size + fileList[i].length();  
	                }  
	            }  
	        } catch (Exception e) {  
	            e.printStackTrace();  
	        }  
	        return size;  
	    }  
	      
	    /** 
	     * 删除指定目录下文件及目录 
	     *  
	     * @param deleteThisPath 
	     * @param filepath 
	     * @return 
	     */  
	    public static void deleteFolderFile(String filePath, boolean deleteThisPath) {  
	        if (!TextUtils.isEmpty(filePath)) {  
	            try {  
	                File file = new File(filePath);  
	                if (file.isDirectory()) {// 如果下面还有文件  
	                    File files[] = file.listFiles();  
	                    for (int i = 0; i < files.length; i++) {  
	                        deleteFolderFile(files[i].getAbsolutePath(), true);  
	                    }  
	                }  
	                if (deleteThisPath) {  
	                    if (!file.isDirectory()) {// 如果是文件，删除  
	                        file.delete();  
	                    } else {// 目录  
	                        if (file.listFiles().length == 0) {// 目录下没有文件或者目录，删除  
	                            file.delete();  
	                        }  
	                    }  
	                }  
	            } catch (Exception e) {  
	                // TODO Auto-generated catch block  
	                e.printStackTrace();  
	            }  
	        }  
	    }  
	      
	    /** 
	     * 格式化单位 
	     *  
	     * @param size 
	     * @return 
	     */  
	    public static String getFormatSize(double size) {  
	        double kiloByte = size / 1024;  
	        if (kiloByte < 1) {  
	            return size + "Byte";  
	        }  
	  
	        double megaByte = kiloByte / 1024;  
	        if (megaByte < 1) {  
	            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));  
	            return result1.setScale(2, BigDecimal.ROUND_HALF_UP)  
	                    .toPlainString() + "KB";  
	        }  
	  
	        double gigaByte = megaByte / 1024;  
	        if (gigaByte < 1) {  
	            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));  
	            return result2.setScale(2, BigDecimal.ROUND_HALF_UP)  
	                    .toPlainString() + "MB";  
	        }  
	  
	        double teraBytes = gigaByte / 1024;  
	        if (teraBytes < 1) {  
	            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));  
	            return result3.setScale(2, BigDecimal.ROUND_HALF_UP)  
	                    .toPlainString() + "GB";  
	        }  
	        BigDecimal result4 = new BigDecimal(teraBytes);  
	        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString()  
	                + "TB";  
	    }  

	    public static String getCacheSize(File file) throws Exception {  
	        return getFormatSize(getFolderSize(file));  
	    }

	    /**
	     * 获取手机与QQ相关的文件夹
	     * 
	     * @parameter null;
	     * @return ArrayList<String>;
	     */
		public static List<Map<String, Object>> getQQList() {
			// TODO Auto-generated method stub
			ArrayList<Map<String, Object>> maplist = new ArrayList<Map<String, Object>>();		
			String extState = Environment.getExternalStorageState();
			if(extState.equalsIgnoreCase(Environment.MEDIA_MOUNTED)){
				File SDFile = Environment.getExternalStorageDirectory();
				File sdPath = new File(SDFile.getAbsolutePath());
				maplist = (ArrayList<Map<String, Object>>) getFile(maplist,sdPath);
			}
			return maplist;
		}
		
	    /**
	     * 获得与QQ相关路径下的全部文件;
	     * 
	     * @parameter null;
	     * @return ArrayList<String>;
	     */
		public static List<Map<String, Object>> getQQFile(ArrayList<Map<String, Object>>arrayList, File pathFile, String str) {
			if ("QQ".equalsIgnoreCase(str)) {
				// TODO Auto-generated method stub
				if (pathFile!=null&&pathFile.canRead()&&pathFile.listFiles().length>0) {
					for (File file:pathFile.listFiles()) {
						if (file.isDirectory()) {
								boolean bl1 = file.getAbsolutePath().matches(".*Tencent.*");
								boolean bl2 = file.getAbsolutePath().matches(".*QQ.*");
								boolean bl3 = file.getAbsolutePath().matches(".*qq.*");
								boolean bl4 = file.getAbsolutePath().matches(".*wechat.*");
							if (bl1||bl2||bl3||bl4) {
								Log.i(TAG,file.getAbsolutePath()+":"+bl1+"/"+bl2+"/"+bl3+"/"+bl4);
								getFile(arrayList,file);
							} else {
								getQQFile(arrayList,file,"QQ");
							}
						} else {
							boolean bl1 = file.getAbsolutePath().matches(".*Tencent.*");
							boolean bl2 = file.getAbsolutePath().matches(".*QQ.*");
							boolean bl3 = file.getAbsolutePath().matches(".*qq.*");
							boolean bl4 = file.getAbsolutePath().matches(".*wechat.*");
							Log.i(TAG,file.getAbsolutePath()+":"+bl1+"/"+bl2+"/"+bl3+"/"+bl4);
							if (bl1||bl2||bl3||bl4) {
								Map<String, Object> map = new HashMap<String, Object>();
								map.put(file.getAbsolutePath(), file.getAbsoluteFile());
								arrayList.add(map);
							}
						}
					}
				}
			}
			return arrayList;
		}
    public static void execCommand(String command){
    	Process proc = null;
    		try {
    			Runtime runtime = Runtime.getRuntime();
				proc = runtime.exec(command);
    			} catch (IOException e1) {
    				// TODO Auto-generated catch block
                   e1.printStackTrace();
    				} 
   }
}