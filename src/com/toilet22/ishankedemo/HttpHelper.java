package com.toilet22.ishankedemo;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;


public class HttpHelper {
	/*
	 * This is the class that is responsible for retrieving data from the server.
	 */
	private static final String Tag = "HttpHelper";
	private static final int TIMEOUT = 10000;
	
	private static final String SERVER_URL = "http://ishangke.net";
	
	private static final String SEARCH_COURSES = "/searchcourses.php";
	private static final String GET_COURSE = "/getcourse.php";
	
	private HttpURLConnection conn;
	
	public Course[] searchCoursesFromServer(String request) throws Exception{
		//request = URLEncoder.encode(request, "utf-8");
		String strURL = SERVER_URL + SEARCH_COURSES + "?" + request;
		Log.v(Tag, "strURL: " + strURL);
		Course[] courses;
		
		
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
		courses = JSON2Courses(jobj);
		Log.v(Tag, "courses == null? " + Boolean.toString(courses == null));
		in.close();
		return courses;
		

		
	}
	
	private Course[] JSON2Courses(JSONObject jObj) throws JSONException{
		Log.v(Tag, "before jobj.getJSONArray.");
		JSONArray jArry = jObj.getJSONArray(IShangkeHeader.LIST_ITEM_LIST);
		int len = jArry.length(); 
		Log.v(Tag, "the length of JSONArray is " + Integer.toString(len));
		Course[] courses = new Course[len];
		JSONObject jobj;

		Log.v(Tag, "before for loop.");
		for (int i = 0; i<len; i++){
			jobj = jArry.getJSONObject(i);
			Log.v(Tag, "before getJSONObject.");
			courses[i] = JSON2Course(jobj);
		}

		return courses;
	}
	
	private Course JSON2Course(JSONObject jObj){
		Course c = new Course();
		Log.v(Tag, "in JSON2Course,");
		try{
			c.name = jObj.getString(IShangkeHeader.LIST_ITEM_NAME);
			c.teacher = jObj.getString(IShangkeHeader.LIST_ITEM_TEACHER);
			c.configID = jObj.getString(IShangkeHeader.LIST_ITEM_CONFIG_ID);
			c.courseID = jObj.getString(IShangkeHeader.LIST_ITEM_COURSE_ID);
			Log.v(Tag, "name:" + c.name + ", teacher: " + c.teacher + ", configID: " + c.configID
					+ ", courseID: " + c.courseID);
			return c;
		}catch (Exception e) {
			Log.e(Tag, "Error in JSON2Course.");
			e.printStackTrace();
		}		
		return null;
	}
	
	
	
}
