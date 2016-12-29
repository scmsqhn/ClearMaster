package com.changhong.clearmaster;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.changhong.clearmaster.R.layout;
import com.changhong.clearmaster.R.string;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.StaticLayout;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class DeepClearActivity extends Activity{

	private final static String TAG = "DeepClearActivity";
	private ExpandableListView mlistView;
	public myBaseExpandableListAdapter mlistViewAdapter;
	private ArrayList<Long> mdataArrayList;
	private Context mContext;
	private ArrayList<Map<String, Object>> mMapArrayList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Log.i(TAG, "DeepClearActivity");
		setContentView(R.layout.deep_clear_root);
		mContext = getBaseContext();
		initView();
		initData();
	}
	
	
	private void initData() {
		// TODO Auto-generated method stub
		mMapArrayList = new ArrayList<Map<String, Object>>();
		mMapArrayList = (ArrayList<Map<String, Object>>) StorageSizeUtil.DeepScanSDCard(mContext);
		String[] strs1 = StorageSizeUtil.huancunlaji(mContext);
		String[] strs2 = StorageSizeUtil.xiezaicanliu(mMapArrayList);
		String[] strs3 = StorageSizeUtil.guanggaolaji(mMapArrayList);
		String[] strs4 = StorageSizeUtil.wuyonganzhuangbao(mMapArrayList);
		String[] strs5 = StorageSizeUtil.neicunjiasu(mContext);
		
		mlistViewAdapter.setSubClrTypes(0,strs1);
		mlistViewAdapter.setSubClrTypes(1,strs2);
		mlistViewAdapter.setSubClrTypes(2,strs3);
		mlistViewAdapter.setSubClrTypes(3,strs4);
		mlistViewAdapter.setSubClrTypes(4,strs5);
	}

	private void initView() {
		// TODO Auto-generated method stub
		mlistView = (ExpandableListView) findViewById(R.id.lv);
		mlistViewAdapter = new myBaseExpandableListAdapter();
		mlistView.setAdapter(mlistViewAdapter);
//		mlistViewAdapter.
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
		initData();
	}

	public class myBaseExpandableListAdapter extends BaseExpandableListAdapter{
		
		private String[] clrTypes = 
				new String[] { "��������", "ж�ز���", "�������", "���ð�װ��", "�ڴ����" }; 
		private String[][] subClrTypes = 
				new String[][] { 
				{"ϵͳ����","�����ʦ������"},
				{"���ж�غ��������","UC�����"},
				{"���˹��","δ֪���"},
				{"��װ�� 1","��װ�� 2"},
				{"�����","����"}
		}; 

		public void setClrTypes(String[] strs){
			clrTypes = strs;
			notifyDataSetChanged();
		}
		
		public void setSubClrTypes(int line, String[] strs){
			subClrTypes[line] = strs;
			notifyDataSetChanged();
		}
		
		@Override
		public Object getChild(int arg0, int arg1) {
			// TODO Auto-generated method stub
			return subClrTypes[arg0][arg1];
		}
		
		@Override
		public long getChildId(int arg0, int arg1) {
			// TODO Auto-generated method stub
			return arg1;
		}
		
		@Override
		public View getChildView(int arg0, int arg1, boolean arg2,
				View arg3, ViewGroup arg4) {
			// TODO Auto-generated method stub
			if(arg3 == null){  
				arg3 = new TextView(mContext);  
			}  
			((TextView)arg3).setText(subClrTypes[arg0][arg1]);  
			((TextView)arg3).setTextSize(20);
			return arg3;  
		}
		
		@Override
		public int getChildrenCount(int arg0) {
			// TODO Auto-generated method stub
			return subClrTypes[arg0].length;
		}
		
		@Override
		public Object getGroup(int arg0) {
			// TODO Auto-generated method stub
			return clrTypes[arg0];
		}
		
		@Override
		public int getGroupCount() {
			// TODO Auto-generated method stub
			return clrTypes.length;
		}
		
		@Override
		public long getGroupId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}
		
		@Override
		public View getGroupView(int arg0, boolean arg1, View arg2,
				ViewGroup arg3) {
			// TODO Auto-generated method stub
			if(arg2 == null){  
				arg2 = new TextView(mContext);  
			}  
			((TextView)arg2).setText(clrTypes[arg0]);  
			((TextView)arg2).setTextSize(40);
			
			return arg2;  
		}
		
		@Override
		public boolean hasStableIds() {
			// TODO Auto-generated method stub
			return true;
		}
		
		@Override
		public boolean isChildSelectable(int arg0, int arg1) {
			// TODO Auto-generated method stub
			return false;
		}
	}

}