package com.toilet22.ishankedemo;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.widget.SearchView;

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


public class SearchResultActivity extends SherlockActivity{
	
	private final String Tag = "AddCoursesActivity";
	
	/*
	 * Announce all the components here.
	 */
	ListView lstvwResults;
//	Button btnEditOptoins;
//	SearchView searchView;
	TextView txtvwOptions;
	
	/*
	 * Announce all the other member fields here.
	 */
	// For file operation.
	FileHelper fh;
	
	// For http operation.
	String requestOptions;
	String keywordsFromBundle, keywordsForSearch, keywordsFromSearchView;
	String stringOfCondition;
	
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
		setContentView(R.layout.activity_search_results);
	    ActionBar actionBar = getSupportActionBar();
	    actionBar.setDisplayHomeAsUpEnabled(true);

//		btnEditOptoins = (Button)findViewById(R.id.button_addCourse_advancedSearch);
		lstvwResults = (ListView)findViewById(R.id.listView_courses);
		txtvwOptions = (TextView)findViewById(R.id.textview_search_condition);
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
			keywordsFromBundle = bndl.getString("text");
			requestOptions = bndl.getString("request");
			stringOfCondition = bndl.getString("condition");
			txtvwOptions.setText(stringOfCondition);
			
			Log.v(Tag, "From bundle: keyword: " + keywordsFromBundle + ", request: " + requestOptions);
			
			
			if(requestOptions != null){
				// Search for couses.
				try {
					keywordsForSearch = URLEncoder.encode(keywordsFromBundle, "utf-8");
				} catch (Exception e1) {
					Log.e(Tag, "Error in encoding.");
					e1.printStackTrace();
				}
				Log.v(Tag, "From bundle: keyword: " + keywordsForSearch + ", request: " + requestOptions);
				searchCoursesAndDisplay();
			}else{
				keywordsForSearch = "";
			}
		}
		

		
//
//		/*************************************************************
//		 * Set OnClickListener for btn_moreOpts.
//		 **************************************************************/
//		btnEditOptoins.setOnClickListener(new OnClickListener(){
//			public void onClick(View v) {
//				try {
//					coursesChosen = fh.readCoursesChosenFromFile();
//					Log.v(Tag, "After readCoursesChosenFromFile, the length of coursesChosen: " + Integer.toString(coursesChosen.length()));
//				} catch (Exception e) {
//					// TODO Auto-generated catch block
//					Log.e(Tag, "Error in readCoursesChosenFromFile().");
//					e.printStackTrace();
//					coursesChosen = new CourseList();
//				}
//				String currentKeyword = searchView.getQuery().toString();
//				String chosenConfigIDs;
//				if(coursesChosen != null){
//					chosenConfigIDs = coursesChosen.getAllCoursesConfigID();
//				}else{
//					chosenConfigIDs = "";
//				}
//				Log.v(Tag, "chosenConfigIDs: " + chosenConfigIDs);
//				Bundle bn = new Bundle();
//				bn.putString("text", currentKeyword);
//				bn.putString("chosenConfigID", chosenConfigIDs);
//				Intent iMore = new Intent(getApplicationContext(), SearchCourseActivity.class);
//				iMore.putExtras(bn);
//				startActivity(iMore);
//			}		
//		});		
	}
	
	public boolean onCreateOptionsMenu(Menu menu){
		Log.v(Tag, "onCreateOptionMenu");

	    return super.onCreateOptionsMenu(menu);

	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                Intent intent = new Intent(this,MainActivity.class);
                startActivity(intent);
                return true;
            
            default:
            	return super.onOptionsItemSelected(item);
        }
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
		String request = IShangkeHeader.RQST_TEXT + "=" + keywordsForSearch + "&" + requestOptions;
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
			builder = new AlertDialog.Builder(SearchResultActivity.this);
			builder.setMessage("请耐心等待哦~")
			       .setTitle("正在搜索课程");
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
				CourseListAdapter adapter = new CourseListAdapter(SearchResultActivity.this, courses, coursesChosen);
				lstvwResults.setAdapter(adapter);
				Log.v(Tag, "after setAdapter.");
				
			}else{
				Toast.makeText(getApplicationContext(), "没有符合要求的课程", 
						Toast.LENGTH_SHORT).show();
			}
		}
		
	}

	
}
