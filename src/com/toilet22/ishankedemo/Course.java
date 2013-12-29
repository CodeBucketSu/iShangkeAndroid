package com.toilet22.ishankedemo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class Course {
	public static final String Tag = "Course";
	
	public String name;
	public String configID;
	public String courseID;
	
	public int period;
	public double credit;
	
	public String teacher;
	
	public String teachWay;
	public String examWay;
	
	public String strJSON;
	
	public CourseTimeLocation[] courseTimeLocations;
	
	public Course(){
		name = null;
		configID = null;
		courseID = null;
		
		period = 0;
		credit = 0;
		
		teacher = null;
		
		teachWay = null;
		examWay = null;
		
		courseTimeLocations = null;
		
	}
	
	
	public boolean ifConflict(Course otherCourse){
		int len_this = courseTimeLocations.length;
		int len_other = otherCourse.courseTimeLocations.length;
		for(int i = 0; i<len_this; i++){
			for(int j=0; j<len_other; j++){
				if (courseTimeLocations[i].ifConflict(otherCourse.courseTimeLocations[j]))
					return true;
			}
		}
		return false;
	}
	
	public boolean ifConflict(Course[] courses){
		for(int i = 0; i<courses.length; i++){
			if(ifConflict(courses[i]))return true;
		}
		return false;
	}
	
	public CourseTimeLocation[] JSON2CourseTimeLocations(JSONArray jArry) throws JSONException{
		Log.v(Tag, "in JSON2CourseTimeLocations().");

		int len_cTL = jArry.length();
		
		CourseTimeLocation[] cTLs = new CourseTimeLocation[len_cTL];
		for(int i=0; i<len_cTL; i++){
			cTLs[i] = JSON2CourseTimeLocation(jArry.getJSONObject(i));
		}
		
		return cTLs;
	}
	
	private CourseTimeLocation JSON2CourseTimeLocation(JSONObject jObj) throws JSONException{
		CourseTimeLocation cTL = new CourseTimeLocation();
		Log.v(Tag, "in JSON2CourseTimeLocation().");
		
		// cTL.weeks
		int len_week = jObj.getJSONArray(IShangkeHeader.DETAIL_WEEK).length();
		cTL.weeks = new int[len_week];
		for(int i=0; i<len_week; i++){
			cTL.weeks[i] = jObj.getJSONArray(IShangkeHeader.DETAIL_WEEK).getInt(i);
		}
		Log.v(Tag, "after cTL.weeks.");

		
		//cTL.day
		cTL.day = jObj.getInt(IShangkeHeader.DETAIL_DAY);
		Log.v(Tag, "after cTL.day.");
		
		//cTL.order
		int len_order = jObj.getJSONArray(IShangkeHeader.DETAIL_ORDER).length();
		cTL.orders = new int[len_order];
		for(int i=0; i<len_order; i++){
			cTL.orders[i] = jObj.getJSONArray(IShangkeHeader.DETAIL_ORDER).getInt(i);
		}
		Log.v(Tag, "after cTL.orders.");
		
		//cTL.classroom
		cTL.classroom = jObj.getString(IShangkeHeader.DETAIL_CLASSROOM);
		Log.v(Tag, "after cTL.classroom.");
		
		return cTL;			
	}
	
	public class CourseTimeLocation{
		public int[] weeks;
		public int day;
		public int[] orders;
		public String classroom;
		
		public CourseTimeLocation(){
			weeks = null;
			day = 0;
			orders = null;
			classroom = null;
		}
		
		public boolean ifConflict(CourseTimeLocation oCTL){
			if (day != oCTL.day) return false;
			else if(orders[0]>oCTL.orders[oCTL.orders.length-1]
					|| oCTL.orders[0]>orders[oCTL.orders.length-1]) return false;
			else if(weeks[0]>oCTL.weeks[oCTL.weeks.length-1]
					|| oCTL.weeks[0]>weeks[oCTL.weeks.length-1]) return false;
			else return true;
		}
		
	}
	
	
}
