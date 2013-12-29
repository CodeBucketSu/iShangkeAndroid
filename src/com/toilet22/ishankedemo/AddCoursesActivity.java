package com.toilet22.ishankedemo;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import android.app.Activity;
import android.content.Context;
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


public class AddCoursesActivity extends Activity{
	private final String Tag = "AddCoursesActivity";

	Course[] courses;
	ListView lv_results;
	EditText edt_keyword;
	CoursesListAdapter adapter;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_addcourses);
		
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
			TextView tv_id = (TextView)courseItem.findViewById(R.id.textView_courseid);
			Log.v(Tag, "after findViews");
			tv_name.setText(c.name);
			tv_teacher.setText(c.teacher);
			tv_id.setText(c.courseID);

			Log.v(Tag, "before return");
			return courseItem;
		}
		
	}
}
