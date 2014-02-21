package com.toilet22.ishankedemo;


import java.lang.reflect.Field;

import com.toilet22.ishankedemo.Course.CourseTimeLocation;


import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.widget.SearchView;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends  SherlockActivity {
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
		
		

//		btn_settings.setOnClickListener(new Button.OnClickListener(){
//			public void onClick(View v) {
//				AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
//				builder.setTitle("快速导入已选课程").setMessage("该操作将会覆盖目前课表中所有的课程，是否继续导入？");
//				builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//					public void onClick(DialogInterface dialog, int which) {
//						
//					}
//				});
//				builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//					public void onClick(DialogInterface dialog, int which) {
//						return;
//					}
//				});
//			}
//		});
		

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
	
	
	public boolean onCreateOptionsMenu(Menu menu){

		// Inflate the menu items for use in the action bar
		    MenuInflater inflater = getSupportMenuInflater();
		    inflater.inflate(R.menu.main_actionbar_actions_menu, menu);
		    return super.onCreateOptionsMenu(menu);

	}
	
	public boolean onOptionsItemSelected(MenuItem item){
		// Handle presses on the action bar items
		switch (item.getItemId()){
			case R.id.main_action_add:
				Intent itentAddCourse = new Intent(MainActivity.this, SearchCourseActivity.class);
				startActivity(itentAddCourse);
				return true;
			case R.id.main_action_overflow:
				return true;
			case R.id.main_action_contactus:
				return true;
			
			default:
				return super.onOptionsItemSelected(item);
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
