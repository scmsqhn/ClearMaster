package com.changhong.clearmaster;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TabHost.TabSpec;
import android.widget.TabWidget;
import android.widget.TextView;

public class SoftWareManagerActivity extends FragmentActivity {

	private final static String TAG = "SoftWareManagerActivity.java";
	private Context mContext;
	private TabHost mTabHost;
	private ViewPager mViewPager;
	private TabsAdapter mTabsAdapter;
	private TextView tabTv1, tabTv2, tabTv3;
	private View tabLine1, tabLine2, tabLine3, view1, view2, view3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initview();
		if (savedInstanceState != null) {
			mTabHost.setCurrentTabByTag(savedInstanceState.getString("clr"));
		}
	}
	
	private void initview() {
		// TODO Auto-generated method stub
		setContentView(R.layout.mypage_layout);
		//tabhost
		//	-linearlayout
		//	-tablewidget framelayout viewpager				
		mTabHost = (TabHost) findViewById(R.id.mypage_tabhost);
		mTabHost.setup();
		mViewPager = (ViewPager) findViewById(R.id.mypage_pager);
		mTabsAdapter = new TabsAdapter(this, mTabHost, mViewPager);

		view1 = (View) LayoutInflater.from(this).inflate(
				R.layout.tabwidget_layout, null);
		tabTv1 = (TextView) view1.findViewById(R.id.tabwidget_tv);
		tabLine1 = (View) view1.findViewById(R.id.tabwidget_line);
		tabTv1.setText("软件卸载");
		tabTv1.setTextColor(getResources().getColor(R.color.red));
		tabLine1.setBackgroundColor(getResources().getColor(R.color.red));

		view2 = (View) LayoutInflater.from(this).inflate(
				R.layout.tabwidget_layout, null);
		tabTv2 = (TextView) view2.findViewById(R.id.tabwidget_tv);
		tabLine2 = (View) view2.findViewById(R.id.tabwidget_line);
		tabTv2.setText("软件专清");
		tabTv2.setTextColor(getResources().getColor(R.color.red));
		tabLine2.setBackgroundColor(getResources().getColor(R.color.red));
		
		view3 = (View) LayoutInflater.from(this).inflate(
				R.layout.tabwidget_layout, null);
		tabTv3 = (TextView) view3.findViewById(R.id.tabwidget_tv);
		tabLine3 = (View) view3.findViewById(R.id.tabwidget_line);
		tabTv3.setText("软件下载");
		tabTv3.setTextColor(getResources().getColor(R.color.red));
		tabLine3.setBackgroundColor(getResources().getColor(R.color.red));

		Bundle bd1 = new Bundle();
		bd1.putInt("num", 1);
		Bundle bd2 = new Bundle();
		bd2.putInt("num", 2);
		Bundle bd3 = new Bundle();
		bd3.putInt("num", 3);
		
		mTabsAdapter.addTab(mTabHost.newTabSpec("del").setIndicator(view1),
				FragmentMyPager.class, bd1);
		mTabsAdapter.addTab(mTabHost.newTabSpec("clr").setIndicator(view2),
				SpecialClearFragmentMyPager.class, bd2);
		mTabsAdapter.addTab(mTabHost.newTabSpec("download").setIndicator(view3),
				FragmentMyPager.class, bd3);
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

	private class TabsAdapter extends FragmentPagerAdapter implements
			TabHost.OnTabChangeListener, ViewPager.OnPageChangeListener {
		private Context mContext;
		private TabHost mTabHost;
		private ViewPager mViewPager;
		private ArrayList<TabInfo> mTabs = new ArrayList<TabInfo>();

		public TabsAdapter(FragmentActivity activity, TabHost tabHost,
				ViewPager pager) {
			super(activity.getSupportFragmentManager());
			mContext = activity;
			mTabHost = tabHost;
			mViewPager = pager;
			mTabHost.setOnTabChangedListener(this);
			mViewPager.setAdapter(this);
			mViewPager.setOnPageChangeListener(this);
		}

		public void addTab(TabHost.TabSpec tabSpec, Class<?> clss, 
				Bundle args) {
			tabSpec.setContent(new DummyTabFactory(mContext));
			String tag = tabSpec.getTag();
			Log.i(TAG, "tag="+tag);
			TabInfo info = new TabInfo(tag, clss, args);
			Log.i(TAG, "args="+args.getInt("num"));
			mTabs.add(info);
			mTabHost.addTab(tabSpec);
			notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			return mTabs.size();
		}

		@Override
		public Fragment getItem(int position) {
			TabInfo info = mTabs.get(position);
			return Fragment.instantiate(mContext, info.clss.getName(),
					info.args);
		}

		@Override
		public void onTabChanged(String tabId) {
			int position = mTabHost.getCurrentTab();
			mViewPager.setCurrentItem(position);
			switchDisplay(position);
			if (position == 0) {
				
				} else if (position == 1) {
					} else if (position == 2) {
						}
		}

		private void switchDisplay(int position){
			tabTv1.setText("软件卸载");
			tabTv2.setText("软件专清");
			tabTv3.setText("软件下载");
			tabLine1.setBackgroundColor(getResources().getColor(
					R.color.black));
			tabLine2.setBackgroundColor(getResources().getColor(
					R.color.black));
			tabLine3.setBackgroundColor(getResources().getColor(
					R.color.black));
			switch (position) {
				case 0:
					tabTv1.setTextColor(getResources().getColor(R.color.red));
					tabTv2.setTextColor(getResources().getColor(R.color.blue));
					tabTv3.setTextColor(getResources().getColor(R.color.blue));
					break;
				case 1:
					tabTv1.setTextColor(getResources().getColor(R.color.blue));
					tabTv2.setTextColor(getResources().getColor(R.color.red));
					tabTv3.setTextColor(getResources().getColor(R.color.blue));
					break;
				case 2:
					tabTv1.setTextColor(getResources().getColor(R.color.blue));
					tabTv2.setTextColor(getResources().getColor(R.color.blue));
					tabTv3.setTextColor(getResources().getColor(R.color.red));
					break;
				}
		}
		
		@Override
		public void onPageScrolled(int position, float positionOffset,
				int positionOffsetPixels) {
		}

		@Override
		public void onPageSelected(int position) {
			TabWidget widget = mTabHost.getTabWidget();
			int oldFocusability = widget.getDescendantFocusability();
			widget.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
			mTabHost.setCurrentTab(position);
			widget.setDescendantFocusability(oldFocusability);
		}

		@Override
		public void onPageScrollStateChanged(int state) {

		}
	}
	
	private class TabInfo{  
        private String tag;  
        private Class<?> clss;  
        private Bundle args;  
        TabInfo(String _tag,Class<?> _class,Bundle _args)  
        {  
            tag=_tag;  
            clss=_class;  
            args=_args;  
        }  
    }  
	
	private class DummyTabFactory implements TabHost.TabContentFactory  
    {  
        private Context mContext;  

        public DummyTabFactory(Context context)  
        {  
            mContext=context;  
        }  
  
        @Override  
        public View createTabContent(String tag)  
        {  
            View v=new View(mContext);  
            v.setMinimumHeight(0);  
            v.setMinimumWidth(0);  
            return v;  
        }  
    }  
}