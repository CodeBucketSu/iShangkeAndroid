package com.toilet22.ishankedemo;

import org.json.JSONException;
import org.json.JSONObject;

public class Course {
	public String name;
	public String configID;
	public String courseID;
	
	public int period;
	public float credit;
	
	public String teacher;
	
	public int[] courseWeek;
	public int[] courseDay;
	public int[] courseOrder;
	public String classroom;
	
	public String teachWay;
	public String examWay;
	
	public Course(){
		name = null;
		configID = null;
		courseID = null;
		
		period = 0;
		credit = 0;
		
		teacher = null;
		
		courseWeek = null;
		courseDay = null;
		courseOrder = null;
		classroom = null;
		
		teachWay = null;
		examWay = null;
	}
	
	
}
