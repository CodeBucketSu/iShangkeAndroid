package com.toilet22.ishankedemo;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class CourseDetailActivity extends SherlockActivity {
	public static final String Tag = "CourseDetailActivity";
	private TextView tv_name, tv_teacher, tv_type, tv_period, tv_credit, 
				tv_time, tv_teachWay, tv_examWay, tv_campus, tv_department;
	private CourseList coursesChosen;
	private Course course;
	private FileHelper fh;
	
	
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_course_detail);
	    ActionBar actionBar = getSupportActionBar();
	    actionBar.setDisplayHomeAsUpEnabled(true);

		tv_name = (TextView)findViewById(R.id.tv_detail_cntnt_name);
		tv_teacher = (TextView)findViewById(R.id.tv_detail_cntnt_teacher);
		tv_type = (TextView)findViewById(R.id.tv_detail_cntnt_type);
		tv_period = (TextView)findViewById(R.id.tv_detail_cntnt_period);
		tv_credit = (TextView)findViewById(R.id.tv_detail_cntnt_credit);
		tv_time = (TextView)findViewById(R.id.tv_detail_cntnt_time);
		tv_teachWay = (TextView)findViewById(R.id.tv_detail_cntnt_teachingWay);
		tv_examWay = (TextView)findViewById(R.id.tv_detail_cntnt_examingWay);
		tv_campus = (TextView)findViewById(R.id.tv_detail_cntnt_campus);
		tv_department = (TextView)findViewById(R.id.tv_detail_cntnt_department);
		
		/***************************************************************
		 * Get all the chosen courses from local file.
		 * If the file does not exist, create it.
		 ***************************************************************/
		coursesChosen = new CourseList();	
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
		

		/***************************************************************
		 * Get the course to display.
		 ***************************************************************/
		Bundle bdl = getIntent().getExtras();
		int position = bdl.getInt("position");
		course = coursesChosen.getCourse(position);
		
		
		/***************************************************************
		 * Display all the information.
		 ***************************************************************/
		tv_name.setText(course.name);
		tv_teacher.setText(course.teacher);
		tv_period.setText(Integer.toString(course.period));
		tv_credit.setText(Double.toString(course.credit));
		tv_teachWay.setText(course.teachWay);
		tv_examWay.setText(course.examWay);
		tv_campus.setText(course.campus);
		tv_department.setText(course.department);
		tv_type.setText(course.type);
		
		tv_time.setText(course.getTimeAndLocationInStrings());
				
		
	}
	
	
	
	public boolean onCreateOptionsMenu(Menu menu){

		// Inflate the menu items for use in the action bar
		    MenuInflater inflater = getSupportMenuInflater();
		    inflater.inflate(R.menu.detail_actionbar_actions_menu, menu);
		    return super.onCreateOptionsMenu(menu);

	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.detail_action_delete:
            	AlertDialog.Builder builder = new AlertDialog.Builder(CourseDetailActivity.this);
				builder.setMessage("确定要删除这门课程？")
				       .setTitle("删除课程");
				builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {					
					public void onClick(DialogInterface dialog, int which) {
						Log.v(Tag, "Click to delete course.");
						coursesChosen.deleteCourse(course.courseID);
						// Update coursesChosen in file.
						fh.writeCoursesChosenIntoFile(coursesChosen);

						Toast.makeText(CourseDetailActivity.this, "课程已经成功删除", 
								Toast.LENGTH_SHORT).show();
						Intent iMain = new Intent(CourseDetailActivity.this, MainActivity.class);
						startActivity(iMain);
//						CourseDetailActivity.this.finish();
					}
				});
				builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {					
					public void onClick(DialogInterface dialog, int which) {

						return;
					}
				});
				AlertDialog dialog = builder.create();
				dialog.show();
                return true;
            
            default:
            	return super.onOptionsItemSelected(item);
        }
    }
}
