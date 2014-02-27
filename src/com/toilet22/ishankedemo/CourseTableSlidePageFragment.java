package com.toilet22.ishankedemo;


import com.toilet22.ishankedemo.Course.CourseTimeLocation;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.AbsoluteLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CourseTableSlidePageFragment extends Fragment {
	public static final String Tag = "CourseTableSlidePageFragment";
	
    public static final String ARG_WEEK = "page";
    private int mWeekNumber;  
    public ViewGroup rootView;
	private AbsoluteLayout absLayout;
	private AbsoluteLayout.LayoutParams lpCourse;
	private LinearLayout lnLayout;
	private ViewTreeObserver vto;
	private OnCourseClickListener courseClickListener;
	
	int xStart, yStart, heightBlock, widthBlock;
	boolean isLinearLayoutDrawn = false;
	FileHelper fh;
	CourseList coursesChosen;
	LayoutInflater mInflater;
	Activity mActivity;
    
    /**
     * Factory method for this fragment class. Constructs a new fragment for the given page number.
     */
    public static CourseTableSlidePageFragment create(int pageNumber) {
        CourseTableSlidePageFragment fragment = new CourseTableSlidePageFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_WEEK, pageNumber);
        fragment.setArguments(args);
        return fragment;
    }
    
    public CourseTableSlidePageFragment() {
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mWeekNumber = getArguments().getInt(ARG_WEEK);

    	mActivity = getActivity();
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout containing a title and body text.
		Log.v(Tag, "Fragment" + Integer.toString(mWeekNumber) +": onCreateView().");
    	mInflater = inflater;
        rootView = (ViewGroup) inflater
                .inflate(R.layout.layout_coursestable_linearlayout, container, false);
        
        /**
         * 
         */
		absLayout = (AbsoluteLayout)rootView.findViewById(R.id.courses_container);
		lnLayout = (LinearLayout)rootView.findViewById(R.id.table);
		if (absLayout == null) {Log.e(Tag, "absLayout == null");}
		if (lnLayout == null) {Log.e(Tag, "lnLayout == null");}
		
		/***************************************************************
		 * Get all the chosen courses from local file.
		 * If the file does not exist, create it.
		 ***************************************************************/
		coursesChosen = new CourseList();
		Log.v(Tag, "before new FileHelper().");
		fh = new FileHelper(mActivity);
		try {
			Log.v(Tag, "before readCoursesChosenFromFile().");
			coursesChosen = fh.readCoursesChosenFromFile();
		} catch (Exception e2) {
			Log.e(Tag, "Error in readCoursesChosenFromFile().");
			e2.printStackTrace();
			coursesChosen = null;
		}
		
		
		vto = lnLayout.getViewTreeObserver();
		if (vto == null) {Log.e(Tag, "vto == null");}
//		Log.v(Tag, "before vto.addOnPreDrawListener.");
		vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener(){
			@Override
			public void onGlobalLayout() {
//				Log.v("Toilet22","Fragment" + Integer.toString(mWeekNumber)+": vto.onGlobalLayout. ");
				TextView tvSun1 = (TextView)rootView.findViewById(R.id.tv_sun_1);
				LinearLayout llSun = (LinearLayout)rootView.findViewById(R.id.ll_sunday);
				xStart = llSun.getLeft();
				yStart = tvSun1.getTop();
				heightBlock = tvSun1.getHeight();
				widthBlock = tvSun1.getWidth();
//				Log.v("toilet", "x: " + Integer.toString(xStart) + ", y:"+Integer.toString(yStart)
//						+ ", width:" + Integer.toString(widthBlock) + ", height: " + Integer.toString(heightBlock));
				
			}
		});
		
		vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
			@Override
			public boolean onPreDraw() {
//				Log.v("Toilet22","Fragment" + Integer.toString(mWeekNumber)+": vto.onPreDraw. "
//						+ "isLinearLayoutDrawn:" + Boolean.toString(isLinearLayoutDrawn));
				
				if(!isLinearLayoutDrawn){
//					Log.v(Tag, "PreDraw: before drawCourses(coursesChosen)");
					drawCourses(coursesChosen);

					isLinearLayoutDrawn = true;
				}
				return true;
			}
		});
//		Log.v(Tag, "after vto.addOnPreDrawListener.");
        

        return rootView;
    }
    
    public void onResume(){
		super.onResume();
		Log.v(Tag, "Fragment" + Integer.toString(mWeekNumber) +": onResume()."
				+ "isLinearLayoutDrawn:" + Boolean.toString(isLinearLayoutDrawn));
		if(isLinearLayoutDrawn){
//			Log.v(Tag, "before new FileHelper().");
			try {
//				Log.v(Tag, "before readCoursesChosenFromFile().");
				coursesChosen = fh.readCoursesChosenFromFile();
			} catch (Exception e2) {
				
//				Log.e(Tag, "Error in readCoursesChosenFromFile().");
				e2.printStackTrace();
			}

			absLayout.removeAllViewsInLayout();
			isLinearLayoutDrawn = false;
		}
	}
    
    public void onPause(){
    	super.onPause();
		Log.v(Tag, "Fragment" + Integer.toString(mWeekNumber) +": onPause()."
				+ "isLinearLayoutDrawn:" + Boolean.toString(isLinearLayoutDrawn));
    }
    
    public void onStop(){
    	super.onStop();
    	Log.v(Tag, "Fragment" + Integer.toString(mWeekNumber) +": onStop");
    }
    
    /**
     * Returns the page number represented by this fragment object.
     */
    public int getPageNumber() {
        return mWeekNumber;
    }
    
    public interface OnCourseClickListener {
    	public void onCourseClick(Bundle bundle);
    }
    
    @Override
    public void onAttach(Activity activity){
    	super.onAttach(activity);
    	Log.v(Tag,"Fragment" + Integer.toString(mWeekNumber) +": onAttach");
    	try{
    		courseClickListener = (OnCourseClickListener)activity;
    	}catch(ClassCastException e){
    		Log.e(Tag, "must implement OnCourseClickListener");
    	}
    }
    
	@SuppressWarnings("deprecation")
	public void drawCourses(CourseList courseList){
		Log.v(Tag, "In drawCourses method. courseList == null?: " + Boolean.toString(courseList == null));
		
		if(courseList != null){
			int len = courseList.length();
			for (int i=0; i<len; i++){
				Course c = courseList.getCourse(i);
				for(int j = 0; j<c.courseTimeLocations.length; j++){
					CourseTimeLocation cTL = c.courseTimeLocations[j];
					if(cTL.hasCourseInTheWeek(mWeekNumber+1)){
						LinearLayout llNew = (LinearLayout) mInflater.inflate(R.layout.layout_course_chosen, null);
						
	//					Log.v(Tag, "before llNew.findViewById");
						llNew.findViewById(R.layout.layout_course_chosen);
	//					Log.v(Tag, "after llNew.findViewById. llNew == null?: " + Boolean.toString(llNew == null));
						
						TextView tv_name = (TextView)llNew.findViewById(R.id.textView_courseChosen_name);
						TextView tv_classroom = (TextView)llNew.findViewById(R.id.textView_courseChosen_classroom);
	//					Log.v(Tag, "after TextViews.findViewById tv_name == null?: " + Boolean.toString(tv_name == null));
						tv_name.setText(c.name);
						tv_classroom.setText(c.courseTimeLocations[j].classroom);
						final Bundle bndl = new Bundle();
						bndl.putInt("position", i);
						llNew.setOnClickListener(new View.OnClickListener() {						
							@Override
							public void onClick(View v) {
								courseClickListener.onCourseClick(bndl);
							}
						});
	//					Log.v(Tag, "after TextViews.setText");
						int x = xStart + widthBlock * (c.courseTimeLocations[j].day);
						int y = yStart + heightBlock * (cTL.getStartEndOrder()[0]-1);
						int width = widthBlock;
						int height = heightBlock * (cTL.getStartEndOrder()[1]-cTL.getStartEndOrder()[0] + 1);
	//					Log.v(Tag, "x: " + Integer.toString(x) + ", y:"+Integer.toString(y)
	//							+ ", width:" + Integer.toString(width) + ", height: " + Integer.toString(height));
						lpCourse = new AbsoluteLayout.LayoutParams(width, height, x, y);
	//					Log.v(Tag, "before addView");
						absLayout.addView(llNew, lpCourse);
					}
				}
				
			}
		}
	}
}
