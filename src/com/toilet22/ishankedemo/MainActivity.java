package com.toilet22.ishankedemo;


import com.toilet22.ishankedemo.Course.CourseTimeLocation;
import com.toilet22.ishankedemo.CourseTableSlidePageFragment.OnCourseClickListener;


import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuInflater;

import android.os.Bundle;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AbsoluteLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends  SherlockFragmentActivity implements OnCourseClickListener {
	public static final String Tag = "MainActivity";
	public static final int NUM_WEEKS = 16;
	/**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private ViewPager mPager;
    
    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private CourseTableSlidePagerAdapter mWeekAdapter;

	LayoutInflater inflater;
	
	/*
	 * onCreate:
	 * 1, Initialize all the components.
	 * 2, Get all the chosen courses and display them in the table.
	 */
	@SuppressWarnings("deprecation")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/***************************************************************
		 * Initialize all the components. 
		 ***************************************************************/
		setContentView(R.layout.activity_main);
		Log.v(Tag, "before get fragment");
		CourseTableSlidePageFragment fragment = (CourseTableSlidePageFragment) getSupportFragmentManager()
				.findFragmentById(R.id.main_table_pager);
		if(fragment == null){Log.e(Tag, "fragment == null");}
		Log.v(Tag, "after get fragment");

		// Instantiate a ViewPager and a PagerAdapter.
        mPager = (ViewPager) findViewById(R.id.main_table_pager);
        mWeekAdapter = new CourseTableSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mWeekAdapter);
        mPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
            	setTitle("µÚ"+Integer.toString(position+1)+"ÖÜ");	
            }
        });
        mPager.setCurrentItem(5);
		
		inflater = LayoutInflater.from(this);		
			
	}
	
	public boolean onCreateOptionsMenu(Menu menu){

		// Inflate the menu items for use in the action bar
		    MenuInflater inflater = getSupportMenuInflater();
		    inflater.inflate(R.menu.main_actionbar_actions_menu, menu);
		    return super.onCreateOptionsMenu(menu);

	}
	
	public boolean onOptionsItemSelected(MenuItem item){
		// Handle presses on the action bar items
		switch (item.getItemId()){
			case R.id.main_action_add:
				Intent itentAddCourse = new Intent(MainActivity.this, SearchCourseActivity.class);
				startActivity(itentAddCourse);
				return true;
			case R.id.main_action_overflow:
				return true;
			case R.id.main_action_contactus:
				return true;
			
			default:
				return super.onOptionsItemSelected(item);
		}
		
	}
	
	
	/**
     * A simple pager adapter that represents 5 {@link ScreenSlidePageFragment} objects, in
     * sequence.
     */
    private class CourseTableSlidePagerAdapter extends android.support.v4.app.FragmentStatePagerAdapter {
        public CourseTableSlidePagerAdapter(android.support.v4.app.FragmentManager fragmentManager) {
            super(fragmentManager);
            
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            return CourseTableSlidePageFragment.create(position);
        }

        @Override
        public int getCount() {
            return NUM_WEEKS;
        }
    }

	@Override
	public void onCourseClick(Bundle bundle) {
		// TODO Auto-generated method stub
		Intent iDetail = new Intent(MainActivity.this, CourseDetailActivity.class);
		iDetail.putExtras(bundle);
		startActivity(iDetail);		
	}

	
}
