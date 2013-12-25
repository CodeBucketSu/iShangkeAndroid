package com.toilet22.ishankedemo;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver;
import android.widget.AbsoluteLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity {
	AbsoluteLayout absLayout;
	LinearLayout lnLayout;
	int xStart, yStart, heightBlock, widthBlock;
	AbsoluteLayout.LayoutParams lpCourse;
	ViewTreeObserver vto;
	TextView tvNew;
	boolean isLinearLayoutDrawn = false;
	
	@SuppressWarnings("deprecation")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_coursestable_linearlayout);
		
		absLayout = (AbsoluteLayout)findViewById(R.id.table_container);
		lnLayout = (LinearLayout)findViewById(R.id.table);
		
		
		vto = lnLayout.getViewTreeObserver();
		tvNew = new TextView(this);
		tvNew.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.background_course));
		
		ViewTreeObserver.OnPreDrawListener opl = new ViewTreeObserver.OnPreDrawListener() {
			public boolean onPreDraw() {
				Log.v("Toilet22","in vto.onPreDraw ");
				TextView tvSun1 = (TextView)findViewById(R.id.tv_sun_1);
				xStart = tvSun1.getLeft();
				yStart = tvSun1.getTop();
				heightBlock = tvSun1.getHeight();
				widthBlock = tvSun1.getWidth();
				Log.v("toilet", "x: " + Integer.toString(xStart) + ", y:"+Integer.toString(yStart)
						+ ", width:" + Integer.toString(widthBlock) + ", height: " + Integer.toString(heightBlock));
				
				isLinearLayoutDrawn = true;
				return true;
			}
		};
		
		vto.addOnPreDrawListener(opl);
		
		if(isLinearLayoutDrawn){
			vto.removeOnPreDrawListener(opl);
		
			lpCourse = new AbsoluteLayout.LayoutParams(widthBlock, heightBlock*2,xStart, yStart);
			//lpCourse = new AbsoluteLayout.LayoutParams(50, 100, 100, 200);			
			tvNew.setText("test");
			tvNew.setLayoutParams(lpCourse);
			Log.v("toilet", "before addView");
			absLayout.addView(tvNew);
			Log.v("toilet", "after addView");
		}
			
	}


}
