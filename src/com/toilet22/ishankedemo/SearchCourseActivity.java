package com.toilet22.ishankedemo;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.SearchView;



import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;


public class SearchCourseActivity extends SherlockActivity{
	public static final String Tag = "SearchCourseActivity";

	String[] strDepartmentNames = new String[]{
	    "所有开课学院",
	    "人文学院",
	    "外语系",
	    "数学科学学院",
	    "物理科学学院",
	    "化学与化学工程学院",
	    "生命科学学院",
	    "地球科学学院",
	    "计算机与控制工程学院",
	    "资源与环境学院",
	    "管理学院",
	    "体育教研室",
	    "电子与通信工程学院",
	    "工程管理与信息技术学院",
	    "材料科学与光电学院",
	    "继续教育学院"
	};
	
	String[] rqstDepartmentNames = new String[]{
		"X", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "T", "E", "G", "M", "C"
	};

	String[] strCourseTypes = new String[]{
		"任意课程类型",
		"公共必修课",
		"公共选修课",
		"学科专业课"
	};
	
	String[] rqstCourseTypes = new String[]{
    		"X", "GB", "GX", "S"
    };

	String[] strTeachingWays = new String[]{
		"任意授课方式",
		"课堂讲授为主",
		"授课、讨论",
		"讲课、上机",
		"讲课、实验",
		"其他（需说明）"
	};

	String[] strExamWays = new String[]{
		"任意考试方式",
		"闭卷考试",
		"课堂开卷",
		"大开卷",
		"读书报告",
		"其他（需说明）"
	};
	
	String[] strCreditRestrictions = new String[]{
		">", ">=", "=", "<=", "<"
	};
	
	String[] rqstCreditRestrictions = new String[]{
		"g", "ge", "e", "le", "l"
	};
	
	
	
	String[] strCredits = new String[]{
		"0", "0.5", "1", "1.5", "2", "2.5", "3"
	};

	
	/*
	 * Announce all the compunents here
	 */
	CheckBox ckbCampusY, ckbCampusZ, ckbCampusH;
	CheckBox ckbCollision;
	Spinner spnDepartment, spnCourseType, spnTeachingWay, spnExamWay;
	Spinner spnCreditRestriction, spnCredit;
	String keywordFromBundle, chosenConfigIDs;
	Bundle bn;
	SearchView searchView;

	// For file operation.
	FileHelper fh;
	CourseList coursesChosen;
	
	
	/*
	 * onCreate: 
	 * 1, Initialize all the components.
	 * 2, When search button pushed, construct a request with the contents of 
	 * 		all the componentsstart, and start the AddCourseActivity with the 
	 * 		request in a Bundle object.
	 */
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_courses);
	    ActionBar actionBar = getSupportActionBar();
	    actionBar.setDisplayHomeAsUpEnabled(true);

		
		/***********************************************************************
		 * Get all the chosen courses from local file. 
		 ***********************************************************************/
		fh = new FileHelper(this);
		try {
			coursesChosen = fh.readCoursesChosenFromFile();
			Log.v(Tag, "After readCoursesChosenFromFile, the length of coursesChosen: " + Integer.toString(coursesChosen.length()));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.e(Tag, "Error in readCoursesChosenFromFile().");
			e.printStackTrace();
		}
		
		if(coursesChosen != null){
			chosenConfigIDs = coursesChosen.getAllCoursesConfigID();
		}else{
			chosenConfigIDs = "";
		}
		/*******************************************************
		 * Initialize all the components.
		 ******************************************************/
		ckbCollision = (CheckBox)findViewById(R.id.checkBox_collision);		
		Log.v(Tag, "before find CheckBox.");
		ckbCampusY = (CheckBox)findViewById(R.id.checkBox_campas_y);
		ckbCampusZ = (CheckBox)findViewById(R.id.checkBox_campas_z);
		ckbCampusH = (CheckBox)findViewById(R.id.checkBox_campas_h);
		Log.v(Tag, "before setCheckBox.");
		ckbCampusY.setChecked(true);
		ckbCampusZ.setChecked(true);
		ckbCampusH.setChecked(true);
		
		Log.v(Tag, "before setAdapter.");
		
		bn = getIntent().getExtras();

		//This is the spinner for department
		spnDepartment= (Spinner) findViewById(R.id.spinner_department);
		ArrayAdapter<String> departmentAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, strDepartmentNames);
		spnDepartment.setAdapter(departmentAdapter);
		Log.v(Tag, "after set spinner for department.");
		
		//This is the spinner for course types
		spnCourseType= (Spinner) findViewById(R.id.spinner_coursetype);
		ArrayAdapter<String> CourseTypeAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, strCourseTypes);
		spnCourseType.setAdapter(CourseTypeAdapter);
		
		//This is the spinner for TeachingWay
		spnTeachingWay= (Spinner) findViewById(R.id.spinner_teachingway);
		ArrayAdapter<String> TeachingWayAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, strTeachingWays);
		spnTeachingWay.setAdapter(TeachingWayAdapter);
		
		// This is the spinner for exam ways
		spnExamWay= (Spinner) findViewById(R.id.spinner_examway);
		ArrayAdapter<String> ExamWayAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, strExamWays);
		spnExamWay.setAdapter(ExamWayAdapter);
		
		// This is the spinner for credit restrictions.
		spnCreditRestriction= (Spinner) findViewById(R.id.spinner_creditrestrc);
		ArrayAdapter<String> CreditRestrictionAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, strCreditRestrictions);
		spnCreditRestriction.setAdapter(CreditRestrictionAdapter);
		
		// This is the spinner for credit.
		spnCredit= (Spinner) findViewById(R.id.spinner_credit);
		ArrayAdapter<String> CreditAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, strCredits);
		spnCredit.setAdapter(CreditAdapter);
		

		Log.v(Tag, "after setAdapter.");
		

	}
	
	
	
	public boolean onCreateOptionsMenu(Menu menu){
		Log.v(Tag, "onCreateOptionMenu");
		searchView = new SearchView(getSupportActionBar().getThemedContext());
        searchView.setQueryHint("请输入课程名称");
        searchView.setIconifiedByDefault(false);
        searchView.setSubmitButtonEnabled(true);
        
	    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener (){

	    	/**************************************************************
	    	 * Go searching!
	    	 **************************************************************/

			@Override
			public boolean onQueryTextChange(String arg0) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean onQueryTextSubmit(String arg0) {
				// TODO Auto-generated method stub
				Log.v(Tag, "OnClick.");
				String request = "";
				String stringOfCondition = "搜索条件：";
				
				String keywords = searchView.getQuery().toString();
				Log.v(Tag, "after getting editText's text. keywords: " + keywords);
				
				// Campus
				request = request  + IShangkeHeader.RQST_CAMPUS + "=";
				stringOfCondition += "校区：";
				if(!ckbCampusY.isChecked() && !ckbCampusZ.isChecked() && !ckbCampusH.isChecked()){
					ckbCampusY.setChecked(true);
					ckbCampusZ.setChecked(true);
					ckbCampusH.setChecked(true);
				}
				if(ckbCampusY.isChecked() && ckbCampusZ.isChecked() && ckbCampusH.isChecked()){
					stringOfCondition += "所有校区";
					request += "YZH";
				}else{
					if(ckbCampusY.isChecked()){	request += "Y"; stringOfCondition += " 玉泉路";}
					if(ckbCampusZ.isChecked()){ request += "Z"; stringOfCondition += " 中关村";}
					if(ckbCampusH.isChecked()){ request += "H"; stringOfCondition += " 雁栖湖";}
				}
				Log.v(Tag, "after chekBoxes.");
				
				// Department
				request = request + "&" + IShangkeHeader.RQST_DEPARTMENT + "=";
				request += rqstDepartmentNames[spnDepartment.getSelectedItemPosition()];
				stringOfCondition += "；学院：" + spnDepartment.getSelectedItem().toString();
				Log.v(Tag, "after department.");
				
				// Coursetype
				request = request + "&" + IShangkeHeader.RQST_COURSE_TYPE + "=";
				try {
					request += URLEncoder.encode(rqstCourseTypes[spnCourseType.getSelectedItemPosition()], "utf-8");
				} catch (UnsupportedEncodingException e) {
					Log.e(Tag, "Error in courseType request.");
					request += "";
					e.printStackTrace();
				}
				stringOfCondition += "；类型：" + spnCourseType.getSelectedItem().toString();
				Log.v(Tag, "after courseType.");
				
				// Teaching way
				if(spnTeachingWay.getSelectedItemPosition()!=0){
					request = request + "&" + IShangkeHeader.RQST_TEACHING_WAY + "=";
					try {
						request += URLEncoder.encode(spnTeachingWay.getSelectedItem().toString(), "utf-8");
					} catch (UnsupportedEncodingException e) {
						Log.e(Tag, "Error in courseType request.");
						request += "";
						e.printStackTrace();
					}
					Log.v(Tag, "after teaching way.");
				}
				stringOfCondition += "；授课：" + spnTeachingWay.getSelectedItem().toString();
				
				// Examing way
				if(spnExamWay.getSelectedItemPosition()!=0){
					request = request + "&" + IShangkeHeader.RQST_EXAM_WAY + "=";
					try {
						request += URLEncoder.encode(spnExamWay.getSelectedItem().toString(), "utf-8");
					} catch (UnsupportedEncodingException e) {
						Log.e(Tag, "Error in courseType request.");
						request += "";
						e.printStackTrace();
					}
					Log.v(Tag, "after exam way.");
				}
				stringOfCondition += "；考试：" + spnExamWay.getSelectedItem().toString();

				// Credit
				request = request + "&" + IShangkeHeader.RQST_CREDIT_RESTRICTION + "=";
				request += rqstCreditRestrictions[spnCreditRestriction.getSelectedItemPosition()];
				request = request + "&" + IShangkeHeader.RQST_CREDIT + "=";
				request += spnCredit.getSelectedItem().toString();
				stringOfCondition += "；" + "学分：" + spnCreditRestriction.getSelectedItem().toString() + spnCredit.getSelectedItem().toString();
				
				// Conflict
				if(ckbCollision.isChecked()){
					request += "&chose=" + chosenConfigIDs;		
					stringOfCondition += "；时间：与已选课程不冲突";
				}
				
				// Return the request to the AddCoursesActivity
				Log.v(Tag, "before Intent.");
				Intent iSearch = new Intent(SearchCourseActivity.this, SearchResultActivity.class);
				Bundle bndl = new Bundle();
				bndl.putString("text", keywords);
				bndl.putString("request", request);
				bndl.putString("condition", stringOfCondition);
				iSearch.putExtras(bndl);
				startActivity(iSearch);
				//SearchCourseActivity.this.finish();
				return true;
			}		    	
	    });
	    
	    menu.add("Search")
        .setIcon(R.drawable.search)
        .setActionView(searchView)
        .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        		
	    return super.onCreateOptionsMenu(menu);

	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                Intent intent = new Intent(this,MainActivity.class);
                startActivity(intent);
                return true;
            
            default:
            	return super.onOptionsItemSelected(item);
        }
    }
	
//	public Intent getSupportParentActivityIntent(){
//		Log.v(Tag, "getSupportParentActivityIntent");
//		Intent intent = new Intent(this,MainActivity.class);
//        return intent;
//	}
//	public boolean onOptionsItemSelected(MenuItem item){
//		// Handle presses on the action bar items
//		switch (item.getItemId()){
//			case R.id.addcourses_action_search:
//				return true;
//			
//			default:
//				return super.onOptionsItemSelected(item);
//		}
//		
//	}
	
		
	
//	
//	public boolean onKeyDown(int keyCode, KeyEvent event){
//		if(keyCode == KeyEvent.KEYCODE_BACK){
//			Intent iAdd = new Intent(SearchCourseActivity.this, SearchResultActivity.class);
//			iAdd.putExtras(bn);
//			startActivity(iAdd);
//			this.finish();
//			return true;
//		}else{
//			return super.onKeyDown(keyCode, event);
//		}
//	}
	
}
