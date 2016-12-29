package com.changhong.clearmaster;

import java.util.List;

import com.changhong.clearmaster.R;














import android.app.ActionBar.LayoutParams;
//import android.R;
import android.app.Activity;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Build;
import android.provider.Settings;

public class MainActivity extends Activity implements OnClickListener {
	
	private final static String TAG = "MainActivity";
	private final static int circleStorX = 150;
	private final static int circleStorY = 150;
	private final static int circleFlashX = 500;
	private final static int circleFlashY = 150;
	private final static int W = 20;
	private final static int RI = 100;
	private int perRAM;
	private int perROM;
	private String perRAMString;	
	private String perROMString;	
	private View mCircleViewStorage;
	private View mCircleViewFlash;
	private View mCircleViewStorageCover;
	private View mCircleViewFlashCover;
	private View mClearUselessFile;
	private View mMobileAcc;
	private View mVirtusDoctor;
	private View mSoftWareManager;

	private TextView mTextView1;
	private TextView mTextView2;
	
	private TextView View1;
	private TextView View2;
	private TextView View3;	
	private TextView View4;
	
	private RadioGroup mBootomRadioGroup;
	private Context mContext;
	private FrameLayout mTopView;
	private int mPercent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = getBaseContext();
		checkForPermission(mContext);
		setContentView(R.layout.activity_main);
		getFileSize();
		initView();
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	private void initView() {
		// TODO Auto-generated method stub
		mTopView = (FrameLayout)findViewById(R.id.top_view);
		mCircleViewStorage = new Circle(mContext, circleStorX, circleStorY, W, RI);
		mCircleViewStorageCover = new ReCircle(mContext, circleStorX, circleStorY, RI, W, perROM);
		mCircleViewFlash = new Circle(mContext, circleFlashX, circleFlashY, W, RI);
		mCircleViewFlashCover = new ReCircle(mContext, circleFlashX, circleFlashY, RI, W, perRAM);

		mTextView1 = new TextView(mContext);
		mTextView2 = new TextView(mContext);
		
		mTopView.addView(mCircleViewFlash);
		mTopView.addView(mCircleViewFlashCover);
		mTopView.addView(mCircleViewStorage);
		mTopView.addView(mCircleViewStorageCover);
		mTopView.addView(mTextView1);
		mTopView.addView(mTextView2);
		mTextView1.setPadding(circleFlashX-60, circleFlashY+120, 0, 0);
		mTextView2.setPadding(circleStorX-60, circleStorY+120, 0, 0);

		mCircleViewFlash.invalidate();
		mCircleViewFlashCover.invalidate();
		mCircleViewStorage.invalidate();
		mCircleViewStorageCover.invalidate();
		
		View1 = (TextView) findViewById(R.id.view_1);
		View2 = (TextView) findViewById(R.id.view_2);
		View3 = (TextView) findViewById(R.id.view_3);
		View4 = (TextView) findViewById(R.id.view_4);
		
		View1.setText("垃圾清理");
		View2.setText("手机加速");
		View3.setText("病毒查杀");
		View4.setText("软件管理");
		
		View1.setTextSize(30);
		View2.setTextSize(30);
		View3.setTextSize(30);
		View4.setTextSize(30);

		View1.setOnClickListener(this);
		View2.setOnClickListener(this);
		View3.setOnClickListener(this);
		View4.setOnClickListener(this);
		
		mTextView1.setText(perRAMString);
		mTextView2.setText(perROMString);
	}
	
	private void getFileSize(){
		//全部可用RAM；
		Long totalSizeLong = StorageSizeUtil.getTotalMemorySize(mContext);
		//外部ROM；
		Long totalExtSizeLong = StorageSizeUtil.getTotalExternalMemorySize();
		//内部ROM；
		Long totalInSizeLong = StorageSizeUtil.getTotalInternalMemorySize();
		//可用RAM；
		Long totalAvaiSizeLong = StorageSizeUtil.getAvailableMemory(mContext);
		//可用外部ROM；
		Long extAvaiSizeLong = StorageSizeUtil.getAvailableExternalMemorySize();
		//可用内部ROM；
		Long inAvaiSizeLong = StorageSizeUtil.getAvailableInternalMemorySize();
		
		String totalSize = StorageSizeUtil.formatFileSize(totalSizeLong, false);
		String totalExtSize = StorageSizeUtil.formatFileSize(totalExtSizeLong, false);
		String totalInSize = StorageSizeUtil.formatFileSize(totalInSizeLong, false);
		
		String totalAvaiSize = StorageSizeUtil.formatFileSize(totalAvaiSizeLong, false);
		String extAvaiSize = StorageSizeUtil.formatFileSize(extAvaiSizeLong, false);
		String inAvaiSize = StorageSizeUtil.formatFileSize(inAvaiSizeLong, false);
		
		String toastText = "totalSize:"+ totalSize + 
							"; totalExtSize:"+ totalExtSize +
							"; totalInSize:"+ totalInSize +
							"; totalAvaiSize:"+ totalAvaiSize +
							"; extAvaiSize:"+ extAvaiSize +
							"; inAvaiSize:"+ inAvaiSize + ";";
		
		perRAM = (int) (totalAvaiSizeLong*1.0/totalSizeLong*100);
		Log.i(TAG, "perRAM="+perRAM);
		perRAMString = perRAM+"%"+"\n"+"available RAM:"+"\n"+ totalAvaiSize;
		long totalROMlong = (totalExtSizeLong + totalInSizeLong);
		long totalAvaiROMlong = extAvaiSizeLong + inAvaiSizeLong;
		String totalAvaiROMStr = StorageSizeUtil.formatFileSize(totalAvaiROMlong, false);
		perROM = (int) (totalAvaiROMlong*1.0/totalROMlong*100);
		Log.i(TAG, "perROM="+totalAvaiROMlong + "/" + totalROMlong + "=" + perROM);
		perROMString = perROM+"%"+"\n"+"available ROM:"+"\n"+ totalAvaiROMStr;
		Toast.makeText(mContext, toastText, Toast.LENGTH_LONG).show();
	}

	private void refreshCircle() {
		// TODO Auto-generated method stub
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id = v.getId();
		switch (id) {
		case R.id.view_1:
			Log.i(TAG,"view_1 is pressed");
			Intent intent = new Intent();
			intent.setAction("com.changhong.clearmaster.DeepClearActivity");
			startActivity(intent);
			break;
		case R.id.view_3:
			Log.i(TAG,"view_1 is pressed");
			Intent intent1 = new Intent();
			intent1.setAction("com.changhong.clearmaster.VirtusDefenseActivity");
			startActivity(intent1);
			break;
		case R.id.view_2:
			break;
		case R.id.view_4:
			Log.i(TAG,"view_1 is pressed");
			Intent intent4 = new Intent();
			intent4.setAction("com.changhong.clearmaster.SoftWareManagerActivity");
			startActivity(intent4);
			break;
		}
	}
	private boolean isNoOption() {  
        PackageManager packageManager = getApplicationContext()  
                .getPackageManager();  
        Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);  
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent,  
                PackageManager.MATCH_DEFAULT_ONLY);  
        return list.size() > 0;  
    }  

	private boolean isNoSwitch() {  
        long ts = System.currentTimeMillis();  
        UsageStatsManager usageStatsManager = (UsageStatsManager) getApplicationContext()  
                .getSystemService("usagestats");  
        List<UsageStats> queryUsageStats = usageStatsManager.queryUsageStats(  
                UsageStatsManager.INTERVAL_BEST, 0, ts);  
        if (queryUsageStats == null || queryUsageStats.isEmpty()) {  
            return false;  
        }  
        return true;  
    }

	public void checkForPermission(Context context){
		if (isNoOption()&&!isNoSwitch()) {  
			Intent intent = new Intent(  
					Settings.ACTION_USAGE_ACCESS_SETTINGS);  
			startActivity(intent);  
		}  
	}
	
}
