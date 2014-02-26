package com.toilet22.ishankedemo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class Course {
	public static final String Tag = "Course";
	
	public String name, configID, courseID;
	
	public int period;
	public double credit;
	
	public String teacher, teachWay, examWay, type, department, campus;
	
	public String strJSON;
	
	public CourseTimeLocation[] courseTimeLocations;
	
	public Course(){
		name = "";
		configID = "";
		courseID = "";
		
		period = 0;
		credit = 0;
		
		teacher = "";
		
		teachWay = "";
		examWay = "";
		
		type = "";
		department = "";
		campus = "";
		
		strJSON = "";
		
		courseTimeLocations = null;
		
	}
	
	
	public void addConfigIDToJson() {
		if(strJSON!=""){
			try {
				JSONObject jobj = new JSONObject(strJSON);
				jobj.put(IShangkeHeader.LIST_ITEM_CONFIG_ID, configID);
				strJSON = jobj.toString();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Log.e(Tag, "strJSON: " + strJSON);
			}
		}
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
	
	public boolean ifConflict(CourseList courses){
		for(int i = 0; i<courses.length(); i++){
			if(ifConflict(courses.getCourse(i)))return true;
		}
		return false;
	}
	
	public CourseTimeLocation[] JSON2CourseTimeLocations(JSONArray jArry) throws JSONException{
		//Log.v(Tag, "in JSON2CourseTimeLocations().");

		int len_cTL = jArry.length();
		
		CourseTimeLocation[] cTLs = new CourseTimeLocation[len_cTL];
		for(int i=0; i<len_cTL; i++){
			cTLs[i] = JSON2CourseTimeLocation(jArry.getJSONObject(i));
		}
		
		return cTLs;
	}
	
	private CourseTimeLocation JSON2CourseTimeLocation(JSONObject jObj) throws JSONException{
		CourseTimeLocation cTL = new CourseTimeLocation();
		//Log.v(Tag, "in JSON2CourseTimeLocation().");
		
		// cTL.weeks
		int len_week = jObj.getJSONArray(IShangkeHeader.DETAIL_WEEK).length();
		cTL.weeks = new int[len_week];
		for(int i=0; i<len_week; i++){
			cTL.weeks[i] = jObj.getJSONArray(IShangkeHeader.DETAIL_WEEK).getInt(i);
		}
		//Log.v(Tag, "after cTL.weeks.");

		
		//cTL.day
		cTL.day = jObj.getInt(IShangkeHeader.DETAIL_DAY);
		//Log.v(Tag, "after cTL.day.");
		
		//cTL.order
		int len_order = jObj.getJSONArray(IShangkeHeader.DETAIL_ORDER).length();
		cTL.orders = new int[len_order];
		for(int i=0; i<len_order; i++){
			cTL.orders[i] = jObj.getJSONArray(IShangkeHeader.DETAIL_ORDER).getInt(i);
		}
		//Log.v(Tag, "after cTL.orders.");
		
		//cTL.classroom
		cTL.classroom = jObj.getString(IShangkeHeader.DETAIL_CLASSROOM);
		//Log.v(Tag, "after cTL.classroom.");
		
		return cTL;			
	}
	
	public String getTimeAndLocationInStrings(){
		String result = "";
		if(courseTimeLocations!=null){
			for(int i = 0; i < courseTimeLocations.length; i++){
				result += courseTimeLocations[i].getTimeAndLocationInString();
			}
			result = result.substring(0, result.length()-1);
		}
		return result;
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
					|| oCTL.orders[0]>orders[orders.length-1]) return false;
			else if(weeks[0]>oCTL.weeks[oCTL.weeks.length-1]
					|| oCTL.weeks[0]>weeks[weeks.length-1]) return false;
			else return true;
		}
		
		public int[] getStartEndOrder(){
			int[] o = new int[2];
			o[0] = orders[0];
			o[1] = orders[orders.length-1];
			return o;
		}
		
		public String getTimeAndLocationInString(){
			String tal = "";
			tal += getWeekInString();
			switch(day){
				case 1:
					tal += " ����һ   ";
					break;
				case 2:
					tal += " ���ڶ�   ";
					break;
				case 3:
					tal += " ������   ";
					break;
				case 4:
					tal += " ������   ";
					break;
				case 5:
					tal += " ������   ";
					break;
				case 6:
					tal += " ������   ";
					break;
				case 7:
					tal += " ������   ";
					break;
				default:
					break;
			}
			tal = tal + Integer.toString(orders[0]) + "~" + Integer.toString(orders[orders.length-1]) + "��";
			tal = tal + " " + classroom + "\n";
			return tal;
		}
		
		/*
		 * This method handles some strange case of the weeks of courses.
		 */
		public String getWeekInString(){
			String w = "";
			if(weeks[weeks.length-1]-weeks[0]==weeks.length-1){
				w = w + Integer.toString(weeks[0]) + "~" + Integer.toString(weeks[weeks.length-1]) + "��";
			}else{
				// The weeks are not in succession.
				String s="", e="";
				for(int i=0; i<weeks.length; i++){
					if(s == ""){	
						// a new serial.
						s = Integer.toString(weeks[i]);
					}else{
						if(weeks[i]==weeks[i-1]+1){
							// The current serial is longer.
							e = Integer.toString(weeks[i]);
						}else{
							// The current serial is ended.
							if(e == ""){
								// The current serial has only one member.
								w = w + s + ",";
								// Init a new serial.
								s = Integer.toString(weeks[i]);
							}else{
								// The current serial has more than one member.
								w = w + s + "~" + e + ",";
								// Init a new serial.
								s = Integer.toString(weeks[i]);
								e = "";
							}
						}
					}
				}
				//There is still one serial unprocessed.
				if(e != ""){
					w = w + s + "~" + e + "��";
				}else{
					w = w + s + "��";				
				}
			}
			return w;
		}
	}
	

	/*
	 * This method return the type of the course.
	 */
	public static String getTypeFromCourseID(String courseID){
		String type = "";
		if(courseID.contains("GX")){
			type = "����ѡ�޿γ�";
			return type;
		}else if(courseID.contains("GB")){
			type = "�������޿γ�";
			return type;
		}else{
			type += "רҵ�� | ";
			switch(courseID.charAt(2)){
			case '1':
				type += "ѧ�ƻ�����";
				return type;
			case '2':
				type += "רҵ������";
				return type;
			case '3':
				type += "רҵ��";
				return type;
			case '4':
				type += "ѧ���ۺϿ�";
				return type;
			case '5':
				type += "�߼�ǿ����";
				return type;
			case '6':
				type += "ϵ�н���";
				return type;
			case '7':
				type += "���ۿ�";
				return type;
			case '9':
				type += "�����γ�";
				return type;
			default:
				return type;
			}
		}
	}
	

	/*
	 * This method return the department of the course.
	 */
	public static String getDepartmentFromCourseID(String courseID){
		String dpm = "";
		switch(courseID.charAt(0)){
			case '0':
				dpm += "����ѧԺ";
			case '1':
				dpm += "����ϵ";
				return dpm;
			case '2':
				dpm += "��ѧԺ";
				return dpm;
			case '3':
				dpm += "����ѧԺ";
				return dpm;
			case '4':
				dpm += "��ѧѧԺ";
				return dpm;
			case '5':
				dpm += "����ѧԺ";
				return dpm;
			case '6':
				dpm += "��ѧԺ";
				return dpm;
			case '7':
				dpm += "�����ѧԺ";
				return dpm;
			case '8':
				dpm += "�ʻ�ѧԺ";
				return dpm;
			case '9':
				dpm += "����ѧԺ";
				return dpm;
			case 'E':
				dpm += "����ѧԺ";
				return dpm;
			case 'G':
				dpm += "����ѧԺ";
				return dpm;
			case 'M':
				dpm += "����ѧԺ";
				return dpm;
			default:
				dpm += "����ѧԺ";
				return dpm;
		}
	}
		

		/*
		 * This method return the campus of the course.
		 */
	public static String getCampusFromCourseID(String courseID){
		String dpm = "";
		switch(courseID.charAt(6)){
			case 'H':
				dpm += "���ܺ�У��";
				return dpm;
			case 'Y':
				dpm += "��Ȫ·У��";
				return dpm;
			case 'Z':
				dpm += "�йش�У��";
				return dpm;
			default:
				dpm += "����У��";
				return dpm;
		}
	}
	
	
}
