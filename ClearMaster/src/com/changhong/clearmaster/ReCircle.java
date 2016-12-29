package com.changhong.clearmaster;

import com.changhong.clearmaster.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.graphics.Paint.Cap;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;

public class ReCircle extends View {

	private int circleX;
	private int circleY;
	private int circleR;
	private int circleW;
	private float mScale;
	private Context mContext;
	private Paint mPaint;
	private int mPercent;
	private Bitmap mBitmap; 
	private RectF srcRectF;
	private RectF deskRectF;

	public ReCircle(Context context, int x, int y ,int r ,int w, int percent) {
		//the cover shadow;
		super(context);
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setDither(true);
		mContext = context;
		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		mScale = dm.density;
		circleX = x;//(int) (x + dm.widthPixels/2);
		circleY = y;//(int) (y * mScale + 0.5f); 
		circleR = r;//(int) (r * mScale + 0.5f); 
		circleW = w;//(int) (w * mScale + 0.5f); 
		mBitmap = BitmapFactory.decodeResource(context.getResources(), Color.BLACK);
 		srcRectF = new RectF(circleX - circleR,
                circleY - circleR,
                circleX + circleR,
                circleY + circleR);
 		setPercent(percent);
		LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		setLayoutParams(lp);
	}

	public void setPercent(float percent) {
		this.mPercent = (int) percent;
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setStrokeWidth((float) circleW);
		mPaint.setColor(Color.GREEN);
		canvas.drawArc(srcRectF, 0, (float) 360 * mPercent / 100, false, mPaint);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

}
