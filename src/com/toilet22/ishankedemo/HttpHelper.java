package com.toilet22.ishankedemo;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;


public class HttpHelper {
	/*
	 * This is the class that is responsible for retrieving data from the server.
	 */
	private static final String Tag = "HttpHelper";
	private static final int TIMEOUT = 10000;


	private static final String SERVER_URL = "http://ishangke.net";
//	private static final String SERVER_URL = "124.16.154.220";

	
	private static final String SEARCH_COURSES = "/searchcourses.php";
	private static final String GET_COURSE = "/getcourse.php";
	private static final String GRAB = "/grab.php";
	
	private HttpURLConnection conn;
	
	/*
	 * This method post a SEARCH_COURSES request to the server and 
	 * return an array of conciese information of courses returned by server.
	 */
	public CourseList searchCoursesFromServer(String request) throws Exception{
		//request = URLEncoder.encode(request, "utf-8");
		String strURL = SERVER_URL + SEARCH_COURSES + "?" + request;
		Log.v(Tag, "strURL: " + strURL);
		CourseList courses;
		
		
		URL url= new URL(strURL);
		Log.v(Tag, "before url.openConnection.");
		HttpURLConnection conn = (HttpURLConnection)url.openConnection();
		//conn.setRequestMethod("POST");
		conn.setConnectTimeout(TIMEOUT);
		Log.v(Tag, "after setConnectionTimeout.");
		
		Log.v(Tag, "before conn.getInputStream.");
		InputStream in = conn.getInputStream();
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while((len = in.read(buffer))!=-1){
			bos.write(buffer, 0, len);
		}
		Log.v(Tag, "before new JSONObject.");
		String jsonstr = bos.toString();
		JSONObject jobj = new JSONObject(jsonstr);

		Log.v(Tag, "before JSON2Courses.");
		Log.v(Tag, jsonstr);
		courses = JSONHelper.searchResultsJSON2Courses(jobj);
		Log.v(Tag, "courses == null? " + Boolean.toString(courses == null));
		in.close();
		return courses;
		

		
	}
	
	/*
	 * This method post a GET_COURSE request to the server and 
	 * return the detailed information of the course chosen.
	 */
	public Course getCourseFromServer(String configID) throws Exception {
		String strURL = SERVER_URL + GET_COURSE + "?" + IShangkeHeader.RQST_COURSE_ID + "=" + configID;
		Log.v(Tag, "strURL: " + strURL);
		URL url= new URL(strURL);
		Log.v(Tag, "before url.openConnection.");
		HttpURLConnection conn = (HttpURLConnection)url.openConnection();
		conn.setConnectTimeout(TIMEOUT);
		Log.v(Tag, "after setConnectionTimeout.");
		
		Log.v(Tag, "before conn.getInputStream.");
		InputStream in = conn.getInputStream();
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while((len = in.read(buffer))!=-1){
			bos.write(buffer, 0, len);
		}
		Log.v(Tag, "before new JSONObject.");
		String jsonstr = bos.toString();
		JSONObject jobj = new JSONObject(jsonstr);

		Log.v(Tag, "before JSON2Courses.");
		Log.v(Tag, jsonstr);
		Course course = JSONHelper.courseInfoJSON2Course(jobj);
		
		Log.v(Tag, "course == null? " + Boolean.toString(course == null));
		in.close();
		return course;
	}
	
	/*
	 * This method post a GRAB request to the server and 
	 * return the selected courses' configID of the user.
	 */
	public String[] getChosenCoursesConfigIDFromServer(String name, String password) throws Exception {
		String strURL = SERVER_URL + GRAB + "?" + IShangkeHeader.RQST_NAME + "=" + name 
				+ "&" + IShangkeHeader.RQST_PASSWORD + "=" + password;
		Log.v(Tag, "strURL: " + strURL);
		
		URL url= new URL(strURL);

		String configIDs[];
		Log.v(Tag, "before url.openConnection.");
		HttpURLConnection conn = (HttpURLConnection)url.openConnection();
		conn.setConnectTimeout(TIMEOUT);
		Log.v(Tag, "after setConnectionTimeout.");
		
		Log.v(Tag, "before conn.getInputStream.");
		InputStream in = conn.getInputStream();
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while((len = in.read(buffer))!=-1){
			bos.write(buffer, 0, len);
		}
		Log.v(Tag, "before new JSONObject.");
		String jsonstr = bos.toString();
		JSONObject jobj = new JSONObject(jsonstr);

		Log.v(Tag, "before JSON2Courses.");
		Log.v(Tag, jsonstr);
		
		boolean success = jobj.getBoolean(IShangkeHeader.CHOSEN_COURSES_SUCCESS);
		if(success){
			JSONArray jArry= jobj.getJSONArray(IShangkeHeader.CHOSEN_COURSES_LIST);
			int length = jArry.length();
			configIDs = new String[length];
			for(int i = 0; i < jArry.length(); ++i){
				configIDs[i] = jArry.getString(i);
			}
		}else{
			Log.e(Tag, "download chosen courses configID error");
			configIDs = null;
		}
		in.close();
		return configIDs;
	}
	
	
	
	
	
}
