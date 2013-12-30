package com.toilet22.ishankedemo;

import com.toilet22.ishankedemo.Course.CourseTimeLocation;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity {
	public static final String Tag = "MainActivity";
	AbsoluteLayout absLayout;
	LinearLayout lnLayout;
	LayoutInflater inflater;
	
	int xStart, yStart, heightBlock, widthBlock;
	AbsoluteLayout.LayoutParams lpCourse;
	ViewTreeObserver vto;
	boolean isLinearLayoutDrawn = false;
	FileHelper fh = new FileHelper(this);
	
	Button btn_addCourses;
	
	@SuppressWarnings("deprecation")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_coursestable_linearlayout);
		inflater = LayoutInflater.from(this);
		
		absLayout = (AbsoluteLayout)findViewById(R.id.table_container);
		lnLayout = (LinearLayout)findViewById(R.id.table);
		
		
		vto = lnLayout.getViewTreeObserver();
	
		vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
			public boolean onPreDraw() {
				Log.v("Toilet22","in vto.onPreDraw ");
				TextView tvSun1 = (TextView)findViewById(R.id.tv_sun_1);
				LinearLayout llSun = (LinearLayout)findViewById(R.id.ll_sunday);
				xStart = llSun.getLeft();
				yStart = tvSun1.getTop();
				heightBlock = tvSun1.getHeight();
				widthBlock = tvSun1.getWidth();
				Log.v("toilet", "x: " + Integer.toString(xStart) + ", y:"+Integer.toString(yStart)
						+ ", width:" + Integer.toString(widthBlock) + ", height: " + Integer.toString(heightBlock));
				if(!isLinearLayoutDrawn){
					Course[] coursesChosen;
					Log.v(Tag, "before new FileHelper().");
					try {
						Log.v(Tag, "before readCoursesChosenFromFile().");
						coursesChosen = fh.readCoursesChosenFromFile();
					} catch (Exception e2) {
						// TODO Auto-generated catch block
						Log.e(Tag, "Error in readCoursesChosenFromFile().");
						e2.printStackTrace();
						coursesChosen = null;
					}
							
					Log.v(Tag, "before drawCourses(coursesChosen)");
					drawCourses(coursesChosen);
					isLinearLayoutDrawn = true;
				}
				return true;
			}
		});
		
		btn_addCourses = (Button)findViewById(R.id.button_main_addCourse);
		btn_addCourses.setOnClickListener(new Button.OnClickListener(){
			@Override
			public void onClick(View v) {
				Intent iAdd = new Intent(MainActivity.this, AddCoursesActivity.class);
				startActivity(iAdd);				
			}		
		});
			
	}
	
	protected void onResume(){
		super.onResume();
		if(isLinearLayoutDrawn){
			Course[] coursesChosen;
			Log.v(Tag, "before new FileHelper().");
			try {
				Log.v(Tag, "before readCoursesChosenFromFile().");
				coursesChosen = fh.readCoursesChosenFromFile();
			} catch (Exception e2) {
				// TODO Auto-generated catch block
				Log.e(Tag, "Error in readCoursesChosenFromFile().");
				e2.printStackTrace();
				coursesChosen = null;
			}
					
			Log.v(Tag, "before drawCourses(coursesChosen)");
			drawCourses(coursesChosen);
		}
	}
	
	/*
	 * This method draw the chosen courses.
	 */
	@SuppressWarnings("deprecation")
	public void drawCourses(Course[] courses){
		int len = courses.length;
		for (int i=0; i<len; i++){
			Course c = courses[i];
			for(int j = 0; j<c.courseTimeLocations.length; j++){
				LinearLayout llNew = (LinearLayout) inflater.inflate(R.layout.layout_course_chosen, null);
				CourseTimeLocation cTL = c.courseTimeLocations[j];
				Log.v(Tag, "before llNew.findViewById");
				llNew.findViewById(R.layout.layout_course_chosen);
				Log.v(Tag, "after llNew.findViewById. llNew == null?: " + Boolean.toString(llNew == null));
				
				TextView tv_name = (TextView)llNew.findViewById(R.id.textView_courseChosen_name);
				TextView tv_classroom = (TextView)llNew.findViewById(R.id.textView_courseChosen_classroom);
				Log.v(Tag, "after TextViews.findViewById tv_name == null?: " + Boolean.toString(tv_name == null));
				tv_name.setText(c.name);
				tv_classroom.setText(c.courseTimeLocations[j].classroom);
				Log.v(Tag, "after TextViews.setText");
				int x = xStart + widthBlock * (c.courseTimeLocations[j].day);
				int y = yStart + heightBlock * (cTL.getStartEndOrder()[0]-1);
				int width = widthBlock;
				int height = heightBlock * (cTL.getStartEndOrder()[1]-cTL.getStartEndOrder()[0] + 1);
				Log.v(Tag, "x: " + Integer.toString(x) + ", y:"+Integer.toString(y)
						+ ", width:" + Integer.toString(width) + ", height: " + Integer.toString(height));
				lpCourse = new AbsoluteLayout.LayoutParams(width, height, x, y);
				Log.v(Tag, "before addView");
				absLayout.addView(llNew, lpCourse);
			}
			
		}
	}


}
