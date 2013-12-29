package com.toilet22.ishankedemo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;


public class MoreOptionActivity extends Activity{

	String[] strDepartmentNames = new String[]{
	    "���п���ѧԺ",
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
	
	String[] strCredits = new String[]{
		"0", "0.5", "1", "1.5", "2", "2.5", "3"
	};
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_addcourse_moreopts);
		
		/*
		 * This is the editText for searching key words.
		 */
		EditText edtxtSearch = (EditText)findViewById(R.id.editText_search);

		
		/*
		 * This is the checkBoxes for collision.
		 */
		CheckBox ckbCollision = (CheckBox)findViewById(R.id.checkBox_collision);
		
		/*
		 * This is the checkBoxes for campus.
		 */
		CheckBox ckbCampusY = (CheckBox)findViewById(R.id.checkBox_campas_y);
		CheckBox ckbCampusZ = (CheckBox)findViewById(R.id.checkBox_campas_z);
		CheckBox ckbCampusH = (CheckBox)findViewById(R.id.checkBox_campas_h);
		
		/*
		 * This is the button for searching.
		 */
		Button btnSearch = (Button)findViewById(R.id.editText_search_keywords);
		
		/*
		 * This is the spinner for department
		 */
		Spinner spnDepartment= (Spinner) findViewById(R.id.spinner_department);
		ArrayAdapter<String> departmentAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, strDepartmentNames);
		spnDepartment.setAdapter(departmentAdapter);
		
		/*
		 * This is the spinner for course types
		 */
		Spinner spnCourseType= (Spinner) findViewById(R.id.spinner_coursetype);
		ArrayAdapter<String> CourseTypeAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, strCourseTypes);
		spnCourseType.setAdapter(CourseTypeAdapter);
		
		/*
		 * This is the spinner for TeachingWay
		 */
		Spinner spnTeachingWay= (Spinner) findViewById(R.id.spinner_teachingway);
		ArrayAdapter<String> TeachingWayAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, strTeachingWays);
		spnTeachingWay.setAdapter(TeachingWayAdapter);
		
		/*
		 * This is the spinner for exam ways
		 */
		Spinner spnExamWay= (Spinner) findViewById(R.id.spinner_examway);
		ArrayAdapter<String> ExamWayAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, strExamWays);
		spnExamWay.setAdapter(ExamWayAdapter);
		
		/*
		 * This is the spinner for credit restrictions.
		 */
		Spinner spnCreditRestriction= (Spinner) findViewById(R.id.spinner_creditrestrc);
		ArrayAdapter<String> CreditRestrictionAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, strCreditRestrictions);
		spnCreditRestriction.setAdapter(CreditRestrictionAdapter);
		
		/*
		 * This is the spinner for exam ways
		 */
		Spinner spnCredit= (Spinner) findViewById(R.id.spinner_credit);
		ArrayAdapter<String> CreditAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, strCredits);
		spnCredit.setAdapter(CreditAdapter);
		
		// Go searching! 
		btnSearch.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				String str = " " + " ";
				
			}			
		});

	}
}
