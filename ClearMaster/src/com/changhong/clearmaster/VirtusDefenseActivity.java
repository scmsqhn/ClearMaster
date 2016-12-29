package com.changhong.clearmaster;

import java.util.ArrayList;
import java.util.List;

import com.changhong.clearmaster.R.string;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SimpleCursorAdapter.ViewBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;

public class VirtusDefenseActivity extends Activity implements View.OnClickListener{
	
	public final static String TAG = "VirtusDefenseActivity"; 
	public ProgressDialog progressDialog;
	public Context mContext;
	public ArrayList<String> mArrayList;
	private static final Uri CONTENT_URI = Uri.parse("content://"
			+ "com.changhong.clearmaster" + ".VirtusLibProvider" + "/"
			+ "VIRTUSLIB");
	private TextView tv1;
	private Button btn;
	private ScrollView scrollView;
	
	Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				progressDialog.dismiss();
				tv1.setText((CharSequence) msg.obj);
				progressDialog=null;
				btn.setText("清理完毕");
				break;
			case 2:
				refTextShowNext(msg.obj.toString());
				refTextShowNext("\n");
				Log.i(TAG, "dele"+msg.obj.toString());
				tv1.setText((CharSequence) msg.obj);
				progressDialog.dismiss();
				progressDialog=null;
				btn.setText("清理完毕");
				break;
			}
		}

	};

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mContext = getBaseContext();
		setContentView(R.layout.virtus_def);
		Log.i(TAG, "setContentView();");
		tv1 = (TextView) findViewById(R.id.tv1);
		btn = (Button) findViewById(R.id.btn);
		scrollView = (ScrollView) findViewById(R.id.scroll_view);
		btn.setText("开始扫描");
		btn.setOnClickListener(this);
	}
	
	private void refText(String str) {
		Log.i(TAG,"refText()"+str);
		Log.i("reftext", "refText==> "+str);
		tv1.setText(str);
		tv1.invalidate();
		mHandler.post(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				int off = tv1.getMeasuredHeight() - scrollView.getHeight(); 
				Log.i("downshow","t.height:"+tv1.getMeasuredHeight());
				Log.i("downshow","s.height:"+scrollView.getHeight());
				Log.i("downshow","off:"+off);
				if (off > 0)  scrollView.scrollTo(0, off);
				scrollView.fullScroll(ScrollView.FOCUS_DOWN);
			}
		});
	}
	
	public void refTextShowNext(String str) {
		// TODO Auto-generated method stub
			StringBuffer strBf = new StringBuffer();
			strBf.append(tv1.getText().toString());
			strBf.append(str);
			strBf.append("\n");
			refText(strBf.toString());
	}


	
	@SuppressWarnings("deprecation")
	private void scanVirtus(Context ctx) {
		final Context context = ctx;
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				// TODO Auto-generated method stub
				Log.i(TAG, "scanVirtus(context);");
				mArrayList = new ArrayList<String>();
				mArrayList.clear();
				PackageManager pManager = context.getPackageManager();
				List<ApplicationInfo> listappinfosApplicationInfos =
						pManager.getInstalledApplications(PackageManager.COMPONENT_ENABLED_STATE_DEFAULT);
				ContentResolver cr = context.getContentResolver();
				Cursor cursor = cr.query(
						CONTENT_URI, 
						new String[]{"_id","md5","name","desc"},
						null, null, null);
				if (cursor.moveToFirst()) {
					do {
					Log.i(TAG, "cursor.moveToFirst()");
					int _id  = cursor.getInt(0);
					String md5  = cursor.getString(1);
					String name  = cursor.getString(2);
					String desc  = cursor.getString(3);
					Log.i(TAG, "_id:"+_id+"\n"
							+"name:"+name+"\n"
							+"md5:"+md5+"\n"
							+"desc:"+desc+"\n");
					for (int i = 0; i < listappinfosApplicationInfos.size(); i++) {
						String pkgname = listappinfosApplicationInfos.get(i).packageName;
						try {
							PackageInfo pkgInfo = pManager.getPackageInfo(pkgname, PackageManager.GET_SIGNATURES);
							Signature[] pkgmd5 =  pkgInfo.signatures;
							String pkgmd5str = pkgmd5.toString();
//							Log.i(TAG, "pkgname"+pkgname);
						boolean bname = name==pkgname?true:false;
						boolean bmd5 = md5==pkgmd5str?true:false;
						if (bname||bmd5) {
							Log.i(TAG,"list.add:"+pkgname+"/"+desc);
							mArrayList.add(pkgname + ":" + desc);
							break;
						}
						} catch (NameNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					Log.i(TAG, "cursor.moveToNext()");
					} while (cursor.moveToNext());
				}
				final int SIZE = mArrayList.size();
				String strs = "";
				for (int i = 0; i < SIZE; i++) {
					strs = strs + mArrayList.get(i)+"\n";
				}
				if (SIZE==0) {
					strs = "未监测到病毒";
				}
				Message msg = mHandler.obtainMessage();
				msg.what=2;
				msg.obj=strs;
				msg.sendToTarget();
			}
		}).start();
		Log.i(TAG, "scanVirtus.end;");
	}

	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		int id = view.getId();
		switch (id) {
		case R.id.btn:
			showDialog(1);
			scanVirtus(mContext);
			break;
		}
		
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
			case 1:
				progressDialog = new ProgressDialog(this);
				progressDialog.setMessage("waitting...");
				return progressDialog;
		}
		return null;
	}
}