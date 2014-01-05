package com.toilet22.ishankedemo;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
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
		
		Log.v(Tag, "Finish the initialization of components.");
		

		/***********************************************************************
		 * Get all the chosen courses from local file. 
		 ***********************************************************************/
		fh = new FileHelper(this);
		
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
				searchCoursesAndDisplay();
			}
		}
		

		

		/*************************************************************
		 * Set OnClickListener for btn_moreOpts.
		 **************************************************************/
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
		btn_search.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				search_keywords = edt_keyword.getText().toString();
				try {
					Log.v(Tag, "keywords: " + search_keywords);
					search_keywords = URLEncoder.encode(search_keywords, "utf-8");
				} catch (UnsupportedEncodingException e1) {
					Log.e(Tag, "Error in encoding.");
					e1.printStackTrace();
				}
				searchCoursesAndDisplay();
			}			
		});
		

		/*************************************************************
		 * Set OnClickListener for btn_home.
		 **************************************************************/
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
	public void searchCoursesAndDisplay(){
		try {
			coursesChosen = fh.readCoursesChosenFromFile();
			Log.v(Tag, "After readCoursesChosenFromFile, the length of coursesChosen: " + Integer.toString(coursesChosen.length()));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.e(Tag, "Error in readCoursesChosenFromFile().");
			e.printStackTrace();
			coursesChosen = new CourseList();
		}
		Log.v(Tag, "in searchForCoursesAndDisplay().");
		String request = IShangkeHeader.RQST_TEXT + "=" + search_keywords + "&" + request_options;
		ConnectivityManager connMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = connMgr.getActiveNetworkInfo();
		if(info != null && info.isConnected()){
			SearchClassAndDisplay scd = new SearchClassAndDisplay();
			scd.execute(request);
		} else {
			Toast.makeText(getApplicationContext(), "没有网络连接", 
					Toast.LENGTH_SHORT).show();
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
	
	
	/*
	 * This is a subclass of AsyncTask, who searches courses from server in a background thread and 
	 * display them in the UI.
	 */
	private class SearchClassAndDisplay extends AsyncTask<String, Void, CourseList>{
		private AlertDialog dialog;
		private AlertDialog.Builder builder;
		CourseList coursesSearched;
		
		protected CourseList doInBackground(String... strRqst) {
			/************************************************************
			 * Search courses from sever.
			 ************************************************************/
			// For listView
			try {
				Log.v(Tag, "before new searchCoursesFromServer.");
				Log.v(Tag, "request: " + strRqst[0]);
				HttpHelper httpHelper = new HttpHelper();
				coursesSearched = httpHelper.searchCoursesFromServer(strRqst[0]);
				Log.v(Tag, "Background operation is finished.");
			} catch (Exception e) {
				Log.e(Tag, "Error in .searchForCoursesAndDisplay");
				e.printStackTrace();
				Log.v(Tag, "Background operation is finished.");
				coursesSearched = new CourseList();
			}
			return coursesSearched;
		}
		
		protected void onPreExecute(){
			// 1. Instantiate an AlertDialog.Builder with its constructor
			builder = new AlertDialog.Builder(AddCoursesActivity.this);

			// 2. Chain together various setter methods to set the dialog characteristics
			builder.setMessage("请耐心等待哦~")
			       .setTitle("正在搜索课程");

			// 3. Get the AlertDialog from create()
			dialog = builder.create();
			dialog.show();
			Log.v(Tag, "dialog == null?: " + Boolean.toString(dialog == null));
			Log.v(Tag, "Dialog is showed.");
		    super.onPreExecute();
		}
		
		protected void onPostExecute(CourseList courses){
			Log.v(Tag, "dialog == null?: " + Boolean.toString(dialog == null));
			if(dialog.isShowing()){
				dialog.dismiss();
			}
			Log.v(Tag, "Dialog is cancelled.");
			if(courses != null && courses.length() > 0){
				Log.v(Tag, "in onPostExecute.");
				Toast.makeText(getApplicationContext(), "找到" + Integer.toString(courses.length()) +"门符合条件的课程", 
						Toast.LENGTH_SHORT).show();
				CourseListAdapter adapter = new CourseListAdapter(AddCoursesActivity.this, courses, coursesChosen);
				lv_results.setAdapter(adapter);
				Log.v(Tag, "after setAdapter.");
				
			}else{
				Toast.makeText(getApplicationContext(), "没有符合要求的课程", 
						Toast.LENGTH_SHORT).show();
			}
		}
		
	}

	
}
