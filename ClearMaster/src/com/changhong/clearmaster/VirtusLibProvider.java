package com.changhong.clearmaster;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.Provider;
import java.sql.SQLException;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

public class VirtusLibProvider extends ContentProvider{

	public final static String TAG = "VirtusLibProvider";
	public final static String ANTI_VIRUS_DB = "antivirus.db";
	private static String DB_PATH = "/data/data/com.changhong.clearmaster/databases/";  
	
	private static final UriMatcher matcher = new UriMatcher(
			UriMatcher.NO_MATCH);
	private final static int VIRTUS_LIBS = 1;
	private VirtusLibOpenHelper mVirtusLibOpenHelper;
	private Context mContext;
	
	static {
		matcher.addURI("com.changhong.clearmaster.VirtusLibProvider","VIRTUSLIB", VIRTUS_LIBS);
	}

	
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean onCreate() {
		// TODO Auto-generated method stub
		Log.i(TAG,"onCreate();");
		mVirtusLibOpenHelper = new VirtusLibOpenHelper(getContext());
		return false;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		Log.i(TAG,"query");
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		qb.setTables("datable");
		Log.i(TAG,"mVirtusLibOpenHelper.getReadableDatabase();");
		SQLiteDatabase sysSql = mVirtusLibOpenHelper.getReadableDatabase();
		Cursor c = qb.query(sysSql, projection, selection, selectionArgs, null, null, sortOrder);
		return c;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public class VirtusLibOpenHelper extends SQLiteOpenHelper{
		private Context mContext;
		public static final String DATABASE_NAME = "antivirus.db";
		public static final int DATABASE_VERSION = 1;
		public SQLiteDatabase mSqLiteDatabase;

		public VirtusLibOpenHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
			mContext = context;
			Log.i(TAG, "VirtusLibOpenHelper.constructor");
			try {
				createDataBase();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub
//			db.execSQL("CREATE TABLE " + " SYSTEMAPP" + "(_"
//					+ "id integer primary key autoincrement," + " PKGNAME,"
//					+ " APPLABLE," + " APPICON blob," + "ACTIVITYNAME " + ")");
			Log.i(TAG, "VirtusLibOpenHelper.onCreate()");
		}
		
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			//db.execSQL("DROP TABLE IF EXISTS SYSTEMAPP");
		}
		
		public void createDataBase() throws IOException{  
			Log.i(TAG, "createDataBase()");
			boolean dbExist = checkDataBase();  
			if(dbExist){  
            //do nothing - database already exist  
				}else{  
//					this.getReadableDatabase();  
					try {  
						copyDataBase();  
						} catch (IOException e) {  
							throw new Error("Error copying database");  
							}  
					}
		}
			
		private boolean checkDataBase(){  
			Log.i(TAG, "checkDataBase()");
			SQLiteDatabase checkDB = null;  
				try{  
	            String myPath = DB_PATH + ANTI_VIRUS_DB;  
	            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);  
			        }catch(SQLiteException e){  
			            //database does't exist yet.  
			        	}  
				if(checkDB != null){  
					checkDB.close();  
		        }  
		        return checkDB != null ? true : false;  
		}  
		
		private void copyDataBase() throws IOException{  
			Log.i(TAG, "copyDataBase()");
	        //Open your local db as the input stream  
	        InputStream myInput = mContext.getAssets().open(ANTI_VIRUS_DB);  
	        String outFileName = DB_PATH + ANTI_VIRUS_DB;  
	        OutputStream myOutput = new FileOutputStream(outFileName);  
	        byte[] buffer = new byte[1024];  
	        int length;  
	        while ((length = myInput.read(buffer))>0){  
	            myOutput.write(buffer, 0, length);  
	        }  
	        myOutput.flush();  
	        myOutput.close();  
	        myInput.close();  
	    }  
		
		 public SQLiteDatabase openDataBase() throws SQLException{  
		        //Open the database  
				Log.i(TAG,"openDataBase();");
		        String myPath = DB_PATH + ANTI_VIRUS_DB;  
				Log.i(TAG,"openDataBase.mypath."+myPath);
		        mSqLiteDatabase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);  
				Log.i(TAG,"openDataBase.return();");
		        return mSqLiteDatabase;
	    }  
		
	}
}