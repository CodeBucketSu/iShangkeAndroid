package com.toilet22.ishankedemo;

import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class CourseListAdapter extends BaseAdapter{
	public static final String Tag = "CourseListAdapter";
	
	private Activity context;
	private LayoutInflater inflater;
	private CourseList courses, coursesChosen;
	private FileHelper fh;
	Course newCourse;
	
	static class ViewHolder{
		public TextView tv_configId;
		public TextView tv_courseId;
		public TextView tv_name;
		public TextView tv_teacher; 
		public Button btn_add;
	}
	
	public CourseListAdapter(Activity contxt, CourseList c, CourseList cChosen){
		courses = c;
		coursesChosen = cChosen;
		context = contxt;
		inflater = context.getLayoutInflater();
		fh = new FileHelper(context);
	}
	
	public int getCount() {
		Log.v(Tag, "length of courses is " + Integer.toString(courses.length()));
		return courses.length();
	}


	public Object getItem(int arg0) {
		return null;
	}


	public long getItemId(int position) {
		return 0;
	}


	public View getView(int position, View convertView, ViewGroup parent) {

		Course c = courses.getCourse(position);
		Log.v(Tag, "In getView: " + c.name + ": " + c.teacher + ", " + c.courseID);
		final ViewHolder holder;
		if(convertView == null){
			
			convertView = inflater.inflate(R.layout.layout_courseslist_item, null);
			
			holder = new ViewHolder();
			holder.tv_configId = (TextView)convertView.findViewById(R.id.textView_configid);
			holder.tv_courseId = (TextView)convertView.findViewById(R.id.textView_courseid);
			holder.tv_name = (TextView)convertView.findViewById(R.id.textView_course_name);
			holder.tv_teacher = (TextView)convertView.findViewById(R.id.textView_teacher);
			holder.btn_add = (Button)convertView.findViewById(R.id.btn_addcourse);
			
			convertView.setTag(holder);
			
		}else{
			holder = (ViewHolder)convertView.getTag();
		}

		Log.v(Tag, "In getView: before setText for textViews.");
		holder.tv_name.setText(c.name);
		holder.tv_teacher.setText(c.teacher);
		holder.tv_configId.setText(c.configID);
		holder.tv_courseId.setText(c.courseID); 

		/**************************************************
		 * The button should have different color depending on whether this course has 
		 * already in the chosen courses.
		 **************************************************/
		
		if(coursesChosen.ifContainsCourseID(c.courseID)){
			Log.v(Tag, "In getView: This course is in the chosen courses list.");
			holder.btn_add.setText(R.string.delete_course);
			holder.btn_add.setBackgroundResource(R.drawable.bkg_btn_deletecourse);
		}else{
			Log.v(Tag, "In getView: This course is not in the chosen courses list.");
			holder.btn_add.setText(R.string.add_course);
			holder.btn_add.setBackgroundResource(R.drawable.bkg_btn_addcourse);
		}
		
		//final ViewHolder finalholder = holder;
		/**************************************************
		 * Set the OnClickListener for the button.
		 **************************************************/
		holder.btn_add.setOnClickListener(new Button.OnClickListener(){
			public void onClick(View v) {
				if(holder.btn_add.getText().toString() == context.getResources().getString(R.string.add_course)){
					GetCourseInfo gci = new GetCourseInfo();
					try {
						newCourse = gci.execute(holder.tv_configId.getText().toString()).get();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ExecutionException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					// Add the course into the file if there is no collisions in time.
					// judge
					if(newCourse != null){
						newCourse.configID = holder.tv_configId.getText().toString();
						if(courses!=null){
							if(newCourse.ifConflict(coursesChosen)){
								Toast.makeText(context, "与现有课程有冲突", 
										Toast.LENGTH_SHORT).show();
								return;
							}
						}					
						
						// Add course
						coursesChosen.addCourse(newCourse);
						

						// Update coursesChosen in file.
						fh.writeCoursesChosenIntoFile(coursesChosen);
						
						// Change the button's appearance
						holder.btn_add.setText(R.string.delete_course);
						holder.btn_add.setBackgroundResource(R.drawable.bkg_btn_deletecourse);

						Toast.makeText(context, "课程已经成功添加", 
								Toast.LENGTH_SHORT).show();
					}									
				}else{
					Log.v(Tag, "Click to delete course.");
					coursesChosen.deleteCourse(holder.tv_courseId.getText().toString());
					// Update coursesChosen in file.
					fh.writeCoursesChosenIntoFile(coursesChosen);

					// Change the button's appearance
					holder.btn_add.setText(R.string.add_course);
					holder.btn_add.setBackgroundResource(R.drawable.bkg_btn_addcourse);

					Toast.makeText(context, "课程已经成功删除", 
							Toast.LENGTH_SHORT).show();
				}
				
			}
		});

		return convertView;
	}
	
	
	/*
	 * This is a subclass of AsyncTask, who gets course information from server in a background thread and 
	 * display them in the UI.
	 */
	private class GetCourseInfo extends AsyncTask<String, Void, Course>{

		
		protected Course doInBackground(String... strConfigID) {
			/************************************************************
			 * Search courses from sever.
			 ************************************************************/
			HttpHelper httpHelper = new HttpHelper();
			try {
				// Get the detailed information of the course from server.
				newCourse = httpHelper.getCourseFromServer(strConfigID[0]);
			} catch (Exception e) {
				Log.e(Tag, "Error in btn_add's onClickListener.");
				e.printStackTrace();
				newCourse = null;
			}
			return newCourse;
		}
		
	}

	
	
}
