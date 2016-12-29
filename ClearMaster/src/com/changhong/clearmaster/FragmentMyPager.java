package com.changhong.clearmaster;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

public class FragmentMyPager extends Fragment{

	private static final String TAG = "FragmentMyPager";
	int mNum;
  
    public static FragmentMyPager newInstance(int num)  
    {  
		Log.i(TAG,"newInstance num="+num);
        FragmentMyPager f=new FragmentMyPager();  
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
                             Bundle savedInstanceState)  
    {  
        View v=inflater.inflate(R.layout.fragment_mypage,container,false);  
        View tv=v.findViewById(R.id.text);  
        ((TextView)tv).setText("Fragment # "+mNum);  
        ((TextView)tv).setTextColor(getResources().getColor(R.color.white));  
        return v;  
    }  
    
    
}
