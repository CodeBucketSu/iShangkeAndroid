package com.toilet22.ishankedemo;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
	String name, password;
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
            	ConnectivityManager connMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        		NetworkInfo info = connMgr.getActiveNetworkInfo();
        		CourseList courseChosen = new CourseList();
        		if(info == null ){Log.e(Tag, "info == null");}
        		if(info != null && info.isConnected()){

//	            	name = edtName.getText().toString();
//	            	password = edtPassword.getText().toString();
        			name = "herui13@mails.ucas.ac.cn";
        			password = "420582199302256257";
					try {
						configIDs = httpHelper.getChosenCoursesConfigIDFromServer(name, password);
					} catch (Exception e) {
						Log.e(Tag, "download error.");
						configIDs = null;
						e.printStackTrace();
					}	
	            	
					if (configIDs == null){
						Log.e(Tag, "download faild.");
						Toast.makeText(getApplicationContext(), "用户名密码错误", 
								Toast.LENGTH_LONG).show();
					}else{
						Log.v(Tag, "configID: " + configIDs.toString());
						Course newCourse;
						for(int i= 0; i<configIDs.length; ++i){
							try {
								newCourse = httpHelper.getCourseFromServer(configIDs[i]);
							} catch (Exception e) {
								newCourse = null;
							}
							if(newCourse != null)courseChosen.addCourse(newCourse);
						}
						fileHelper.writeCoursesChosenIntoFile(courseChosen);
						Toast.makeText(getApplicationContext(), "导入完成！", 
								Toast.LENGTH_LONG).show();
						
					}
        		}else{
        			Toast.makeText(getApplicationContext(), "没有网络连接", 
    					Toast.LENGTH_SHORT).show();
        		}
//            	fh.writeCoursesChosenIntoFile(coursesChosen);
                return true;
            
            default:
            	return super.onOptionsItemSelected(item);
        }
    }
}
