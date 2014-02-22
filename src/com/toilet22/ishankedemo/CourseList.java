package com.toilet22.ishankedemo;

import java.util.ArrayList;

import android.util.Log;

public class CourseList {
	public static final String Tag = "CourseList";
	private ArrayList<Course> courseList;
	
	public CourseList(){
		courseList = new ArrayList<Course>();
	}
	
	public boolean deleteCourse(String courseID){
		for(int i = 0; i < courseList.size(); i++){
			if(courseList.get(i).courseID.equals(courseID)){
				courseList.remove(i);
				return true;
			}
		}
		return false;
	}
	
	public String getCoursesID(){
		String s = "";
		int i = 0;
		for(i = 0; i < courseList.size()-1; i++){
			s += (courseList.get(i).courseID + ",");
		}
		s += (courseList.get(i).courseID + ",");
		return s;		
	}
	
	public int length(){
		return courseList.size();
	}
	
	public Course getCourse(int index){
		if(0<=index && index < courseList.size()){
			return courseList.get(index);
		}else{
			return null;
		}
	}
	
	public String getAllCoursesConfigID(){
		String configIDs = "";
		if(length() > 0){
			for(int i=0; i<length(); ++i){
				configIDs += getCourse(i).configID + ",";
			}
			configIDs = configIDs.substring(0, configIDs.length()-1);
			return configIDs;
		}else{
			return "";
		}
	}
	
	public boolean addCourse(Course c){
		if(c!=null){
			courseList.add(c);
			return true;
		}else{
			return false;
		}
	}
	
	public boolean ifContainsCourseID(String cID){
		Log.v(Tag, "in CourseList.ifCantainsConfigID, length = " + Integer.toString(courseList.size()));
		for(int i = 0; i < courseList.size(); i++){
			Log.v(Tag, "couse.courseID: '" + courseList.get(i).courseID + "' '" + cID + "'");
			if(courseList.get(i).courseID.equals(cID)){
				Log.v(Tag,"courseID exists!");
				return true;
			}
		}
		return false;
		
	}
}
