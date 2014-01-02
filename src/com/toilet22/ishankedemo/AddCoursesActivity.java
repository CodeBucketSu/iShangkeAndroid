package com.toilet22.ishankedemo;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class AddCoursesActivity extends Activity{
	
	private final String Tag = "AddCoursesActivity";
	
	/*
	 * Announce all the components here.
	 */
	ListView lv_results;
	EditText edt_keyword;
	Button btn_moreOpts, btn_search, btn_home;
	// For listView
	CoursesListAdapter adapter;
	
	/*
	 * Announce all the other member fields here.
	 */
	// For file operation.
	FileHelper fh;
	
	// For http operation.
	String request_options;
	String search_keywords;
	
	// The courses.
	CourseList coursesChosen;
	CourseList coursesSearched;
	
	/*
	 * onCreate: 
	 * 1, Initialize all the components.
	 * 2, Get all the courses already chosen from local file.
	 * 3, If this activity is started by MoreOperationAcitvity, search for courses with the request
	 *    sent with Bundle object and display the results.
	 */
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/***********************************************************************
		 * Initialize all the component
		 ***********************************************************************/
		setContentView(R.layout.activity_addcourses);
		edt_keyword = (EditText)findViewById(R.id.editText_search);
		btn_moreOpts = (Button)findViewById(R.id.button_addCourse_advancedSearch);
		btn_search = (Button)findViewById(R.id.button_addcourses_search_go);
		btn_home = (Button)findViewById(R.id.button_addCourses_home);

		lv_results = (ListView)findViewById(R.id.listView_courses);
		adapter = new CoursesListAdapter(this);
		
		Log.v(Tag, "Finish the initialization of components.");
		

		/***********************************************************************
		 * Get all the chosen courses from local file.
		 ***********************************************************************/
		coursesChosen = new CourseList();
		Log.v(Tag, "before new FileHelper().");
		fh = new FileHelper(this);
		try {
			Log.v(Tag, "before readCoursesChosenFromFile().");
			coursesChosen = fh.readCoursesChosenFromFile();
			Log.v(Tag, "After readCoursesChosenFromFile, the length of coursesChosen: " + Integer.toString(coursesChosen.length()));
			Log.v(Tag, "");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.e(Tag, "Error in readCoursesChosenFromFile().");
			e.printStackTrace();
		}
		Log.v(Tag, "coursesChosen == null? :" + Boolean.toString(coursesChosen == null));
		
		
		/*************************************************************
		 * If this activity is started by MoreOptionActivity, it should use the request in the bundle
		 * to get the searching results from the server.
		 **************************************************************/
		Bundle bndl = getIntent().getExtras();
		if(bndl!=null){
			// Get search_keywords and request from bundle.
			search_keywords = bndl.getString("text");
			request_options = bndl.getString("request");
			
			Log.v(Tag, "From bundle: keyword: " + search_keywords + ", request: " + request_options);
			edt_keyword.setText(search_keywords);
			Log.v(Tag, "after setText.");
			
			if(request_options != null){
				// Search for couses.
				try {
					search_keywords = URLEncoder.encode(search_keywords, "utf-8");
				} catch (Exception e1) {
					Log.e(Tag, "Error in encoding.");
					e1.printStackTrace();
				}
				Log.v(Tag, "From bundle: keyword: " + search_keywords + ", request: " + request_options);
				searchForCoursesAndDisplay();
			}
		}
		

		

		/*************************************************************
		 * Set OnClickListener for btn_moreOpts.
		 **************************************************************/
		Log.v(Tag, "btn_moreOpts == null?: " + Boolean.toString(btn_moreOpts == null));
		btn_moreOpts.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				String currentKeyword = edt_keyword.getText().toString();
				Bundle bn = new Bundle();
				bn.putString("text", currentKeyword);
				Intent iMore = new Intent(AddCoursesActivity.this, MoreOptionActivity.class);
				iMore.putExtras(bn);
				startActivity(iMore);		
				AddCoursesActivity.this.finish();
			}		
		});
		

		/*************************************************************
		 * Set OnClickListener for btn_search.
		 **************************************************************/
		Log.v(Tag, "before btn_search.setOnClickListener.");
		btn_search.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				search_keywords = edt_keyword.getText().toString();
				try {
					search_keywords = URLEncoder.encode(search_keywords, "utf-8");
				} catch (UnsupportedEncodingException e1) {
					Log.e(Tag, "Error in encoding.");
					e1.printStackTrace();
				}
				searchForCoursesAndDisplay();
			}			
		});
		

		/*************************************************************
		 * Set OnClickListener for btn_home.
		 **************************************************************/
		Log.v(Tag, "before btn_search.setOnClickListener.");
		btn_home.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				Intent iMain = new Intent(AddCoursesActivity.this, MainActivity.class);
				startActivity(iMain);
				AddCoursesActivity.this.finish();
			}			
		});
		
		
	}
	
	
	/*
	 * This method search courses using the @search_kewords and @request_options variables and 
	 */
	public void searchForCoursesAndDisplay(){
		Log.v(Tag, "in searchForCoursesAndDisplay().");
		Log.v(Tag, "before new httpHelper.");
		HttpHelper httpHelper = new HttpHelper();
		request_options = IShangkeHeader.RQST_TEXT + "=" + search_keywords + "&" + request_options;
		try {
			Log.v(Tag, "before new searchCoursesFromServer.");
			Log.v(Tag, "request: " + request_options);
			coursesSearched = httpHelper.searchCoursesFromServer(request_options);
			if(coursesSearched != null){
				lv_results.setAdapter(adapter);
			}else{
				coursesSearched = new CourseList();
				Toast.makeText(getApplicationContext(), "没有符合要求的课程", 
						Toast.LENGTH_SHORT).show();
			}
		} catch (Exception e) {
			Log.e(Tag, "Error in .searchForCoursesAndDisplay");
			e.printStackTrace();
		}
	}
	
	
	/*
	 * This is the customed listAdapter for couses list
	 */
	public class CoursesListAdapter extends BaseAdapter{
		
		private LayoutInflater inflater;
		
		public CoursesListAdapter(Context context){
			this.inflater = LayoutInflater.from(context);
		}
		
		public int getCount() {
			Log.v(Tag, "length of courses is " + Integer.toString(coursesSearched.length()));
			return coursesSearched.length();
		}

	
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}

	
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

	
		public View getView(int position, View convertView, ViewGroup parent) {
			Log.v(Tag, "after getLayoutInflater");

			/**************************************************
			 * Declare and define all the other variables here.
			 **************************************************/
			Course c = coursesSearched.getCourse(position);
			Log.v(Tag, "c == null: " + Boolean.toString(c == null));
			Log.v(Tag, c.name + ": " + c.teacher + ", " + c.courseID);
			
			/**************************************************
			 * Declare and define all the components here.
			 **************************************************/
			View courseItem = inflater.inflate(R.layout.layout_courseslist_item, null);
			final TextView tv_configId = (TextView)courseItem.findViewById(R.id.textView_configid);
			final TextView tv_courseId = (TextView)courseItem.findViewById(R.id.textView_courseid);
			TextView tv_name = (TextView)courseItem.findViewById(R.id.textView_course_name);
			TextView tv_teacher = (TextView)courseItem.findViewById(R.id.textView_teacher);
			tv_name.setText(c.name);
			tv_teacher.setText(c.teacher);
			tv_configId.setText(c.configID);
			tv_courseId.setText(c.courseID);
			final Button btn_add = (Button)courseItem.findViewById(R.id.btn_addcourse);
			Log.v(Tag, "after findViews");
			

			/**************************************************
			 * The button should have different color depending on whether this course has 
			 * already in the chosen courses.
			 **************************************************/
			Log.v(Tag, "courseItem.confID = " + c.courseID);
			if(coursesChosen.ifContainsCourseID(c.courseID)){
				Log.v(Tag, "Course is already chosez.");
				btn_add.setText(R.string.delete_course);
				btn_add.setBackgroundDrawable(AddCoursesActivity.this
						.getResources().getDrawable(R.drawable.bkg_btn_deletecourse));
			}
			

			/**************************************************
			 * Set the OnClickListener for the button.
			 **************************************************/
			btn_add.setOnClickListener(new OnClickListener(){
				public void onClick(View v) {
					// Get the detailed information of the course from server.
					if(btn_add.getText().toString() == AddCoursesActivity.this.getResources().getString(R.string.add_course)){
						Log.v(Tag, "Click to add course.");
						HttpHelper httpHelper = new HttpHelper();
						Course newCourse;
						try {
							Log.v(Tag, "before getCourseFromServer in btn_add's onClickListener.");
							newCourse = httpHelper.getCourseFromServer(tv_configId.getText().toString());
						} catch (Exception e) {
							Log.e(Tag, "Error in btn_add's onClickListener.");
							e.printStackTrace();
							newCourse = null;
						}
						Log.v(Tag, "This is the config ID of the course returned by getCourseFromServer: "
									+ newCourse.configID);
						newCourse.configID = tv_configId.getText().toString();
						
						// Add the course into the file if there is no collisions in time.
						// judge
						Log.v(Tag, "before judging and adding.");
						if(newCourse != null){
							Log.v(Tag, "newCourse is not null.");
							if(coursesChosen!=null){
								Log.v(Tag, "coursesChosen is not null.");
								if(newCourse.ifConflict(coursesChosen)){
									Log.v(Tag, "Conflict!!");
									Toast.makeText(getApplicationContext(), "与现有课程有冲突", 
											Toast.LENGTH_SHORT).show();
									return;
								}
							}					
							
							// Add course
							Log.v(Tag, "No collision.");
							coursesChosen.addCourse(newCourse);
							
	
							// Update coursesChosen in file.
							fh.writeCoursesChosenIntoFile(coursesChosen);
							
							// Change the button's appearance
							btn_add.setText(R.string.delete_course);
							btn_add.setBackgroundDrawable(AddCoursesActivity.this
									.getResources().getDrawable(R.drawable.bkg_btn_deletecourse));

							Toast.makeText(getApplicationContext(), "课程已经成功添加", 
									Toast.LENGTH_SHORT).show();
						}									
					}else{
						Log.v(Tag, "Click to delete course.");
						coursesChosen.deleteCourse(tv_courseId.getText().toString());
						// Update coursesChosen in file.
						fh.writeCoursesChosenIntoFile(coursesChosen);

						// Change the button's appearance
						btn_add.setText(R.string.add_course);
						btn_add.setBackgroundDrawable(AddCoursesActivity.this
								.getResources().getDrawable(R.drawable.bkg_btn_addcourse));

						Toast.makeText(getApplicationContext(), "课程已经成功删除", 
								Toast.LENGTH_SHORT).show();
					}
					
				}
			});

			return courseItem;
		}
		
	}
	
	
	public boolean onKeyDown(int keyCode, KeyEvent event){
		if(keyCode == KeyEvent.KEYCODE_BACK){
			Intent iAdd = new Intent(AddCoursesActivity.this, MainActivity.class);
			startActivity(iAdd);
			this.finish();
			return true;
		}else{
			return super.onKeyDown(keyCode, event);
		}
	}
}
