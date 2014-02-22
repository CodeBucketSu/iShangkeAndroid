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
	    "���п���ѧԺ",
	    "����ѧԺ",
	    "����ϵ",
	    "��ѧ��ѧѧԺ",
	    "�����ѧѧԺ",
	    "��ѧ�뻯ѧ����ѧԺ",
	    "������ѧѧԺ",
	    "�����ѧѧԺ",
	    "���������ƹ���ѧԺ",
	    "��Դ�뻷��ѧԺ",
	    "����ѧԺ",
	    "����������",
	    "������ͨ�Ź���ѧԺ",
	    "���̹�������Ϣ����ѧԺ",
	    "���Ͽ�ѧ����ѧԺ",
	    "��������ѧԺ"
	};
	
	String[] rqstDepartmentNames = new String[]{
		"X", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "T", "E", "G", "M", "C"
	};

	String[] strCourseTypes = new String[]{
		"����γ�����",
		"�������޿�",
		"����ѡ�޿�",
		"ѧ��רҵ��"
	};
	
	String[] rqstCourseTypes = new String[]{
    		"X", "GB", "GX", "S"
    };

	String[] strTeachingWays = new String[]{
		"�����ڿη�ʽ",
		"���ý���Ϊ��",
		"�ڿΡ�����",
		"���Ρ��ϻ�",
		"���Ρ�ʵ��",
		"��������˵����"
	};

	String[] strExamWays = new String[]{
		"���⿼�Է�ʽ",
		"�վ���",
		"���ÿ���",
		"�󿪾�",
		"���鱨��",
		"��������˵����"
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
        searchView.setQueryHint("������γ�����");
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
				String stringOfCondition = "����������";
				
				String keywords = searchView.getQuery().toString();
				Log.v(Tag, "after getting editText's text. keywords: " + keywords);
				
				// Campus
				request = request  + IShangkeHeader.RQST_CAMPUS + "=";
				stringOfCondition += "У����";
				if(!ckbCampusY.isChecked() && !ckbCampusZ.isChecked() && !ckbCampusH.isChecked()){
					ckbCampusY.setChecked(true);
					ckbCampusZ.setChecked(true);
					ckbCampusH.setChecked(true);
				}
				if(ckbCampusY.isChecked() && ckbCampusZ.isChecked() && ckbCampusH.isChecked()){
					stringOfCondition += "����У��";
					request += "YZH";
				}else{
					if(ckbCampusY.isChecked()){	request += "Y"; stringOfCondition += " ��Ȫ·";}
					if(ckbCampusZ.isChecked()){ request += "Z"; stringOfCondition += " �йش�";}
					if(ckbCampusH.isChecked()){ request += "H"; stringOfCondition += " ���ܺ�";}
				}
				Log.v(Tag, "after chekBoxes.");
				
				// Department
				request = request + "&" + IShangkeHeader.RQST_DEPARTMENT + "=";
				request += rqstDepartmentNames[spnDepartment.getSelectedItemPosition()];
				stringOfCondition += "��ѧԺ��" + spnDepartment.getSelectedItem().toString();
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
				stringOfCondition += "�����ͣ�" + spnCourseType.getSelectedItem().toString();
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
				stringOfCondition += "���ڿΣ�" + spnTeachingWay.getSelectedItem().toString();
				
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
				stringOfCondition += "�����ԣ�" + spnExamWay.getSelectedItem().toString();

				// Credit
				request = request + "&" + IShangkeHeader.RQST_CREDIT_RESTRICTION + "=";
				request += rqstCreditRestrictions[spnCreditRestriction.getSelectedItemPosition()];
				request = request + "&" + IShangkeHeader.RQST_CREDIT + "=";
				request += spnCredit.getSelectedItem().toString();
				stringOfCondition += "��" + "ѧ�֣�" + spnCreditRestriction.getSelectedItem().toString() + spnCredit.getSelectedItem().toString();
				
				// Conflict
				if(ckbCollision.isChecked()){
					request += "&chose=" + chosenConfigIDs;		
					stringOfCondition += "��ʱ�䣺����ѡ�γ̲���ͻ";
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
