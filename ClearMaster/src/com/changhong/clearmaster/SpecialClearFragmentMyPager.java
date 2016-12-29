package com.changhong.clearmaster;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.zip.Inflater;

import android.opengl.Visibility;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SimpleCursorAdapter.ViewBinder;
import android.transition.ChangeBounds;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SpecialClearFragmentMyPager extends Fragment implements View.OnClickListener{

	private static final String TAG = "SpecialClearFragmentMyPager";
	private int mNum;
	public LinearLayout layout2;
	public LinearLayout layout1;
	public TextView mClrtv;
	public Button btnButton;
	public List<Map<String, Object>> rmlist;
	
    public static SpecialClearFragmentMyPager newInstance(int num){  
		Log.i(TAG,"newInstance num="+num);
		SpecialClearFragmentMyPager f=new SpecialClearFragmentMyPager();  
        Bundle args=new Bundle();  
        args.putInt("num",num);  
        f.setArguments(args);  
        return f;
    }  

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mNum = getArguments() != null ? getArguments().getInt("num") : 1;
		Log.i(TAG,"mNum="+mNum);
	}  
  
    @Override  
    public View onCreateView(LayoutInflater inflater,ViewGroup container,  
                             Bundle savedInstanceState){  
        View v=inflater.inflate(R.layout.clear_fragment_mypage,container,false);  
        btnButton = (Button) v.findViewById(R.id.btn);
        layout2 = (LinearLayout) v.findViewById(R.id.layout_2);
        layout1 = (LinearLayout) v.findViewById(R.id.layout_1);
        mClrtv = (TextView) v.findViewById(R.id.clrtv);
        View tv2=v.findViewById(R.id.text2);  
        View tv3=v.findViewById(R.id.text3);  
//        ((TextView)tv1).setText("Fragment # "+mNum);  
//        ((TextView)tv1).setTextColor(getResources().getColor(R.color.white));  
        ((TextView)tv2).setText("QQ专清");  
        ((TextView)tv2).setTextColor(getResources().getColor(R.color.white));  
        ((TextView)tv3).setText("微信专清");  
        ((TextView)tv3).setTextColor(getResources().getColor(R.color.white));  

    	tv2.setOnClickListener(this);
    	tv2.setOnClickListener(this);
    	btnButton.setOnClickListener(this);
    	
        return v;  
    }

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id = v.getId();
		switch (id) {
		case R.id.text2:
			layout2.setVisibility(View.GONE);
			layout1.setVisibility(View.VISIBLE);
			mClrtv.setText(QQClear());
			break;
		case R.id.text3:
			WechatClear();
			break;
		case R.id.btn:
			ClearBtn();
			break;
		}
	}  
	
	private void ClearBtn() {
		// TODO Auto-generated method stub
		int i = rmlist.size();
		for (int j = 0; j < i; j++) {
			StringBuilder strbd = new StringBuilder();
			strbd.append(rmlist.get(j).keySet().toString());
			Log.i(TAG, "rm -f " + strbd.substring(1, strbd.length()-1));
			String strsrm = "rm -f " + strbd.substring(1, strbd.length()-1);
			StorageSizeUtil.execCommand(strsrm);
		}
		QQClear();
	}

	public String QQClear(){
		ArrayList<Map<String, Object>> mArrayList = 
				new ArrayList<Map<String, Object>>();
		File SDFile = Environment.getExternalStorageDirectory();
		File sdPath = new File(SDFile.getAbsolutePath());
		List<Map<String, Object>> arrayList = StorageSizeUtil.getQQFile(mArrayList, sdPath,"QQ");
		rmlist = arrayList;
		ArrayList<String> arrayListStr = new ArrayList<String>();
		for (int i = 0; i < arrayList.size(); i++) {
			arrayListStr.add(arrayList.get(i).keySet().toString()+"\n"+"\n");
			Log.i(TAG, arrayList.get(i).keySet().toString());
		}
		return chg2String(arrayListStr);
	}
	
	private String chg2String(ArrayList<String> arrayListStr){
		final int SIZE = arrayListStr.size();
		String[] strs = new String[SIZE];
		StringBuilder strsbd = new StringBuilder();
		for (int i = 0; i < SIZE; i++) {
			strs[i] = arrayListStr.get(i).toString();
			strsbd.append(strs[i]);
		}
		return strsbd.toString();
}

	public void WechatClear(){
		
	}

}