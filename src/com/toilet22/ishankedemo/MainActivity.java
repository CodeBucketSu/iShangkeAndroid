package com.toilet22.ishankedemo;

import com.toilet22.ishankedemo.Course.CourseTimeLocation;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;
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
	

	/*
	 * Announce all the components here.
	 */
	// AbsoluteLayout
	AbsoluteLayout absLayout;
	AbsoluteLayout.LayoutParams lpCourse;
	// LinearLayout
	LinearLayout lnLayout;
	ViewTreeObserver vto;
	LayoutInflater inflater;
	// Button
	Button btn_addCourses, btn_settings;
	
	/*
	 * Announce all the other member variables here.
	 */
	int xStart, yStart, heightBlock, widthBlock;
	boolean isLinearLayoutDrawn = false;
	FileHelper fh = new FileHelper(this);
	CourseList coursesChosen;
	
	/*
	 * onCreate:
	 * 1, Initialize all the components.
	 * 2, Get all the chosen courses and display them in the table.
	 */
	@SuppressWarnings("deprecation")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/***************************************************************
		 * Initialize all the components. 
		 ***************************************************************/
		setContentView(R.layout.activity_coursestable_linearlayout);
		inflater = LayoutInflater.from(this);
		
		absLayout = (AbsoluteLayout)findViewById(R.id.table_container);
		lnLayout = (LinearLayout)findViewById(R.id.table);

		btn_addCourses = (Button)findViewById(R.id.button_addCourse_advancedSearch);
		btn_addCourses.setOnClickListener(new Button.OnClickListener(){
			@Override
			public void onClick(View v) {
				Intent iAdd = new Intent(MainActivity.this, AddCoursesActivity.class);
				startActivity(iAdd);
				MainActivity.this.finish();
			}		
		});
		
		
//
//		btn_settings.setOnClickListener(new Button.OnClickListener(){
//			public void onClick(View v) {
//				AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
//				builder.setTitle("选择您要进行的操作");
//				builder.setNegativeButton("快速导入已选课程", new DialogInterface.OnClickListener(){
//					
//				});
//			}
//		});
//		

		/***************************************************************
		 * Get all the chosen courses from local file.
		 * If the file does not exist, create it.
		 ***************************************************************/
		coursesChosen = new CourseList();
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
		
		

		/***************************************************************
		 * Display all the chosen courses in the table.
		 ***************************************************************/
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
							
					Log.v(Tag, "before drawCourses(coursesChosen)");
					drawCourses(coursesChosen);
					isLinearLayoutDrawn = true;
				}
				return true;
			}
		});
		
		
			
	}
	
	protected void onResume(){
		super.onResume();
		if(isLinearLayoutDrawn){
			Log.v(Tag, "before new FileHelper().");
			try {
				Log.v(Tag, "before readCoursesChosenFromFile().");
				coursesChosen = fh.readCoursesChosenFromFile();
			} catch (Exception e2) {
				// TODO Auto-generated catch block
				Log.e(Tag, "Error in readCoursesChosenFromFile().");
				e2.printStackTrace();
			}
					
			Log.v(Tag, "before drawCourses(coursesChosen)");
			drawCourses(coursesChosen);
		}
	}
	
	/*
	 * This method draw the chosen courses.
	 */
	@SuppressWarnings("deprecation")
	public void drawCourses(CourseList courseList){
		Log.v(Tag, "In drawCourses method. courseList == null?: " + Boolean.toString(courseList == null));
		if(courseList != null){
			int len = courseList.length();
			for (int i=0; i<len; i++){
				Course c = courseList.getCourse(i);
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
					final Bundle bndl = new Bundle();
					bndl.putInt("position", i);
					llNew.setOnClickListener(new View.OnClickListener() {						
						@Override
						public void onClick(View v) {
							Intent iDetail = new Intent(MainActivity.this, CourseDetailActivity.class);
							iDetail.putExtras(bndl);
							startActivity(iDetail);
							MainActivity.this.finish();
						}
					});
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
	
	


}
