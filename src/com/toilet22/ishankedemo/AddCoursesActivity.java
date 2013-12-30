package com.toilet22.ishankedemo;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
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

	// For file operation.
	Course[] coursesChosen;
	FileHelper fh;
	
	// For search results.
	Course[] courses;
	ListView lv_results;
	EditText edt_keyword;
	CoursesListAdapter adapter;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_addcourses);

		Log.v(Tag, "before new FileHelper().");
		fh = new FileHelper(this);
		try {
			Log.v(Tag, "before readCoursesChosenFromFile().");
			coursesChosen = fh.readCoursesChosenFromFile();
		} catch (Exception e2) {
			// TODO Auto-generated catch block
			Log.e(Tag, "Error in readCoursesChosenFromFile().");
			e2.printStackTrace();
			coursesChosen = null;
		}
		
		edt_keyword = (EditText)findViewById(R.id.editText_search);
		
		CheckBox ckb_Y, ckb_Z, ckb_H;
		ckb_Y = (CheckBox)findViewById(R.id.checkBox_campas_y);
		ckb_Z = (CheckBox)findViewById(R.id.checkBox_campas_z);
		ckb_H = (CheckBox)findViewById(R.id.checkBox_campas_h);
		
		Button btn_moreOpts, btn_search;
		btn_moreOpts = (Button)findViewById(R.id.button_search_moreopts);
		btn_search = (Button)findViewById(R.id.button_search_go);

		lv_results = (ListView)findViewById(R.id.listView_courses);
		
		adapter = new CoursesListAdapter(this);

		Drawable drw_btn_add = this.getResources().getDrawable(R.drawable.bkg_btn_addcourse);
		Drawable drw_btn_delete = this.getResources().getDrawable(R.drawable.bkg_btn_deletecourse);
		
		btn_search.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				String keywords = edt_keyword.getText().toString();
				try {
					keywords = URLEncoder.encode(keywords, "utf-8");
				} catch (UnsupportedEncodingException e1) {
					Log.e(Tag, "Error in encoding.");
					e1.printStackTrace();
				}
				String request = IShangkeHeader.RQST_TEXT + "=" + keywords;
				Log.v(Tag, "before new httpHelper.");
				HttpHelper httpHelper = new HttpHelper();
				try {
					Log.v(Tag, "before new searchCoursesFromServer.");
					Log.v(Tag, "request: " + request);
					courses = httpHelper.searchCoursesFromServer(request);
					lv_results.setAdapter(adapter);
				} catch (Exception e) {
					Log.e(Tag, "Error in btn_search's OnClickListener.");
					e.printStackTrace();
				}
			}			
		});
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
			Log.v(Tag, "length of courses is " + Integer.toString(courses.length));
			return courses.length;
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
			Course c = courses[position];
			Log.v(Tag, "c == null: " + Boolean.toString(c == null));
			Log.v(Tag, c.name + ": " + c.teacher + ", " + c.courseID);
			View courseItem = inflater.inflate(R.layout.layout_courseslist_item, null);
			TextView tv_name = (TextView)courseItem.findViewById(R.id.textView_course_name);
			TextView tv_teacher = (TextView)courseItem.findViewById(R.id.textView_teacher);
			final TextView tv_id = (TextView)courseItem.findViewById(R.id.textView_courseid);
			Log.v(Tag, "after findViews");
			tv_name.setText(c.name);
			tv_teacher.setText(c.teacher);
			tv_id.setText(c.configID);
			
			
			final Button btn_add = (Button)courseItem.findViewById(R.id.btn_addcourse);
			btn_add.setOnClickListener(new OnClickListener(){
				public void onClick(View v) {
					// Get the detailed information of the course from server.
					HttpHelper httpHelper = new HttpHelper();
					Course newCourse;
					try {
						Log.v(Tag, "before getCourseFromServer in btn_add's onClickListener.");
						newCourse = httpHelper.getCourseFromServer(tv_id.getText().toString());
					} catch (Exception e) {
						Log.e(Tag, "Error in btn_add's onClickListener.");
						e.printStackTrace();
						newCourse = null;
					}
					
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
						
						int length;
						if(coursesChosen != null){
							length= coursesChosen.length;
							Course[]newCoursesChosen = new Course[length+1];
							for(int k=0; k<length; k++){
								newCoursesChosen[k] = coursesChosen[k];
							}
							newCoursesChosen[length] = newCourse;
							coursesChosen = newCoursesChosen;
						}else{
							Course[]newCoursesChosen = new Course[1];
							newCoursesChosen[0] = newCourse;
							coursesChosen = newCoursesChosen;
						}
						

						// Update coursesChosen in file.
						fh.writeCoursesChosenIntoFile(coursesChosen);
						
						// Change the button's appearance
						btn_add.setText(R.string.delete_course);
						btn_add.setBackgroundDrawable(AddCoursesActivity.this
								.getResources().getDrawable(R.drawable.bkg_btn_deletecourse));
						
							
						
					}
					
				}
			});

			Log.v(Tag, "before return");
			return courseItem;
		}
		
	}
}
