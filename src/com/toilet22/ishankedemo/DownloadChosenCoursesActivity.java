package com.toilet22.ishankedemo;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

public class DownloadChosenCoursesActivity extends SherlockActivity {
	public static final String Tag = "DownloadChosenCoursesActivity";

	EditText edtName, edtPassword;
	ProgressDialog mProgressDialog;
	DownLoadChosenCoursesAndSaveAsyncTask downloader;
	CourseList coursesChosen;
	String name, password;
    public static final int DIALOG_DOWNLOAD_PROGRESS = 0;
	String []configIDs;
	HttpHelper httpHelper = new HttpHelper();
	FileHelper fileHelper = new FileHelper(this);
	/**
	 * @param args
	 */
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_download_courses);
	    ActionBar actionBar = getSupportActionBar();
	    actionBar.setDisplayHomeAsUpEnabled(true);

	    edtName = (EditText)findViewById(R.id.edt_name);
	    edtPassword = (EditText)findViewById(R.id.edt_password);
						
		
	}
	
	
	
	public boolean onCreateOptionsMenu(Menu menu){

		// Inflate the menu items for use in the action bar
		    MenuInflater inflater = getSupportMenuInflater();
		    inflater.inflate(R.menu.download_actionbar_actions_menu, menu);
		    return super.onCreateOptionsMenu(menu);

	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
	        case android.R.id.home:
	            Intent intent = new Intent(this,MainActivity.class);
	            startActivity(intent);
	            return true;
	            
            case R.id.download_action_accept:
            	downloadChesenCoursesAndWriteIntoFile();
                return true;
            
            default:
            	return super.onOptionsItemSelected(item);
        }
    }
	
	
	
	protected Dialog onCreateDialog(int id){
		switch (id) {  
        case DIALOG_DOWNLOAD_PROGRESS: //we set this to 0  
            mProgressDialog = new ProgressDialog(this);  
            mProgressDialog.setMessage("正在导入课程");  
            mProgressDialog.setIndeterminate(false);  
            mProgressDialog.setMax(100);  
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);  
            mProgressDialog.setCancelable(false);  
            mProgressDialog.show();  
            return mProgressDialog;  
        default:  
            return null;  
		} 
	}
	
	
	public void downloadChesenCoursesAndWriteIntoFile(){
		ConnectivityManager connMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = connMgr.getActiveNetworkInfo();
		CourseList courseChosen = new CourseList();
		if(info == null ){Log.e(Tag, "info == null");}
		if(info != null && info.isConnected()){

        	name = edtName.getText().toString();
        	password = edtPassword.getText().toString();
//			name = "herui13@mails.ucas.ac.cn";
//			password = "420582199302256257";
			downloader = new DownLoadChosenCoursesAndSaveAsyncTask();
			downloader.execute();

		}else{
			Toast.makeText(getApplicationContext(), "没有网络连接", 
				Toast.LENGTH_SHORT).show();
		}
	}
	
	
	private class DownLoadChosenCoursesAndSaveAsyncTask extends AsyncTask<Void, Integer, CourseList>{
		
		CourseList courses = new CourseList();
		
		@SuppressWarnings("deprecation")
		protected void onPreExecute() {  
            super.onPreExecute();  
            showDialog(DIALOG_DOWNLOAD_PROGRESS);  
        }
		
		protected CourseList doInBackground(Void... arg0) {
			/************************************************************
			 * Search courses from sever.
			 ************************************************************/
			try {
				configIDs = httpHelper.getChosenCoursesConfigIDFromServer(name, password);
			} catch (Exception e) {
				Log.e(Tag, "download error.");
				configIDs = null;
				e.printStackTrace();
			}	
        	
			if (configIDs == null){
				Log.e(Tag, "configIDs == null");
			}else{
				int progressDone = 10;
				publishProgress(progressDone);
				Log.v(Tag, "configID: " + configIDs.toString());
				Course newCourse;
				for(int i= 0; i<configIDs.length; ++i){
					try {
						newCourse = httpHelper.getCourseFromServer(configIDs[i]);
					} catch (Exception e) {
						newCourse = null;
					}
					if(newCourse != null){
						Log.v(Tag, "newCourse != null");
						newCourse.configID = configIDs[i];
						courses.addCourse(newCourse);
					}
					progressDone += (int)(90/configIDs.length);
					publishProgress(progressDone);
				}
				
			}
			return courses;
		}
		
		
		protected void onProgressUpdate(Integer... progress){
			mProgressDialog.setProgress(progress[0]);
		}
		
		protected void onPostExecute(CourseList courses){
			Log.v(Tag, "onPostExecute.");
			if(mProgressDialog.isShowing()){
				mProgressDialog.dismiss();
			}
			Log.v(Tag, "Dialog is cancelled.");
			if(courses != null && courses.length() > 0){
				Log.v(Tag, "courses.length = " + Integer.toString(courses.length()));
				Toast.makeText(getApplicationContext(), "导入课程已完成",Toast.LENGTH_SHORT)
				.show();
				coursesChosen = courses;
				fileHelper.writeCoursesChosenIntoFile(coursesChosen);
			}else{
				Toast.makeText(getApplicationContext(), "您没有选课", 
						Toast.LENGTH_SHORT).show();
			}
		}


		
	}
}
