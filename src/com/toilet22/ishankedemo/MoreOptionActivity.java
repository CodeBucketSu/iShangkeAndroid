package com.toilet22.ishankedemo;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;


public class MoreOptionActivity extends Activity{
	public static final String Tag = "MoreOptionActivity";

	String[] strDepartmentNames = new String[]{
	    "X-���п���ѧԺ",
	    "0-����ѧԺ",
	    "1-����ϵ",
	    "2-��ѧ��ѧѧԺ",
	    "3-�����ѧѧԺ",
	    "4-��ѧ�뻯ѧ����ѧԺ",
	    "5-������ѧѧԺ",
	    "6-�����ѧѧԺ",
	    "7-���������ƹ���ѧԺ",
	    "8-��Դ�뻷��ѧԺ",
	    "9-����ѧԺ",
	    "T-����������",
	    "E-������ͨ�Ź���ѧԺ",
	    "G-���̹�������Ϣ����ѧԺ",
	    "M-���Ͽ�ѧ����ѧԺ",
	    "C-��������ѧԺ"
	};
	
	String[] rqstDepartmentNames = new String[]{
		"X", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "T", "E", "G", "M", "C"
	};

	String[] strCourseTypes = new String[]{
		"X-����γ�����",
		"GB-�������޿�",
		"GX-����ѡ�޿�",
		"S-ѧ��רҵ��"
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
	EditText edtxtSearch;
	Spinner spnDepartment, spnCourseType, spnTeachingWay, spnExamWay;
	Spinner spnCreditRestriction, spnCredit;
	Button btnSearch;
	String keywordFromBundle, chosenConfigIDs;
	Bundle bn;
	
	/*
	 * onCreate: 
	 * 1, Initialize all the components.
	 * 2, When search button pushed, construct a request with the contents of 
	 * 		all the componentsstart, and start the AddCourseActivity with the 
	 * 		request in a Bundle object.
	 */
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_addcourse_moreopts);
		
		/*******************************************************
		 * Initialize all the components.
		 ******************************************************/
		ckbCollision = (CheckBox)findViewById(R.id.checkBox_collision);		
		ckbCampusY = (CheckBox)findViewById(R.id.checkBox_campas_y);
		ckbCampusZ = (CheckBox)findViewById(R.id.checkBox_campas_z);
		ckbCampusH = (CheckBox)findViewById(R.id.checkBox_campas_h);
		ckbCampusY.setChecked(true);
		ckbCampusZ.setChecked(true);
		ckbCampusH.setChecked(true);
		btnSearch = (Button)findViewById(R.id.button_moreoptions_search);

		edtxtSearch = (EditText)findViewById(R.id.editText_search_keywords);
		bn = getIntent().getExtras();
		keywordFromBundle = bn.getString("text");
		chosenConfigIDs = bn.getString("chosenConfigID");
		if(keywordFromBundle != null)	edtxtSearch.setText(keywordFromBundle);
		
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
		
		/**************************************************************
		 * Go searching!
		 **************************************************************/
		btnSearch.setOnClickListener(new Button.OnClickListener(){
			@Override
			public void onClick(View v) {
				Log.v(Tag, "OnClick.");
				String request = "";
				// Keywords
				//request = request + IShangkeHeader.RQST_TEXT + "=";
				//request += edtxtSearch.getText().toString();
				String keywords = edtxtSearch.getText().toString();
				Log.v(Tag, "after getting editText's text. keywords: " + keywords);
				
				// Campus
				if(!ckbCampusY.isChecked() && !ckbCampusZ.isChecked() && !ckbCampusH.isChecked()){
					ckbCampusY.setChecked(true);
					ckbCampusZ.setChecked(true);
					ckbCampusH.setChecked(true);
				}
				//request = request + "&" + IShangkeHeader.RQST_CAMPUS + "=";
				request = request  + IShangkeHeader.RQST_CAMPUS + "=";
				if(ckbCampusY.isChecked()){	request += "Y";}
				if(ckbCampusZ.isChecked()){ request += "Z";}
				if(ckbCampusH.isChecked()){ request += "H";}
				
				Log.v(Tag, "after chekBoxes.");
				
				// Department
				request = request + "&" + IShangkeHeader.RQST_DEPARTMENT + "=";
				request += rqstDepartmentNames[spnDepartment.getSelectedItemPosition()];
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
		
				// Credit
				request = request + "&" + IShangkeHeader.RQST_CREDIT_RESTRICTION + "=";
				request += rqstCreditRestrictions[spnCreditRestriction.getSelectedItemPosition()];
				request = request + "&" + IShangkeHeader.RQST_CREDIT + "=";
				request += spnCredit.getSelectedItem().toString();
				
				// Conflict
				if(ckbCollision.isChecked()){
					request += "&chose=" + chosenConfigIDs;					
				}
				
				// Return the request to the AddCoursesActivity
				Log.v(Tag, "before Intent.");
				Intent iSearch = new Intent(MoreOptionActivity.this, AddCoursesActivity.class);
				Bundle bndl = new Bundle();
				bndl.putString("text", keywords);
				bndl.putString("request", request);
				iSearch.putExtras(bndl);
				startActivity(iSearch);
				MoreOptionActivity.this.finish();
				
			}			
		});

	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event){
		if(keyCode == KeyEvent.KEYCODE_BACK){
			Intent iAdd = new Intent(MoreOptionActivity.this, AddCoursesActivity.class);
			iAdd.putExtras(bn);
			startActivity(iAdd);
			this.finish();
			return true;
		}else{
			return super.onKeyDown(keyCode, event);
		}
	}
	
}
