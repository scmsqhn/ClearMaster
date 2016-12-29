package com.changhong.clearmaster;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

public class Circle extends View {
	private int circleX;
	private int circleY;
	private int circleR;
	private int circleW;
	private float mScale;
	private Context mContext;
	private Paint paint ;

	public Circle(Context context, int x,int y, int w, int r) {
		super(context);
		mContext = context;
		paint = new Paint();
		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		mScale = dm.density;
		circleX = x;//(int) (x + dm.widthPixels/2);
		circleY = y;//(int) (y * mScale + 0.5f); 
		circleR = r;//(int) (r * mScale + 0.5f); 
		circleW = w;//(int) (w * mScale + 0.5f); 
		LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		setLayoutParams(lp);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		paint.setAntiAlias(true);
		paint.setDither(true);
		paint.setColor(Color.RED);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth((float) circleW);
		canvas.drawCircle(circleX, circleY, circleR, paint);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		float pointX = event.getX();
		float pointY = event.getY();
		switch (event.getAction()) {
		case MotionEvent.ACTION_UP:			

			break;
		case MotionEvent.ACTION_DOWN:

			break;
		default:
			break;

		}
		return true;
	}

	private boolean isInCircle(float x, float y) {
		float distance = (circleX - x) * (circleX - x)
				                                + (circleY - y)* (circleY - y);
		if (distance > circleR * circleR)
			return false;
		else
			return true;
	}
}
