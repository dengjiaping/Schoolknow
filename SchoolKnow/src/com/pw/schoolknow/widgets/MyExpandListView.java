package com.pw.schoolknow.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;


/**
 * µ¯ÐÔListView
 * @author flyouting
 * ×ªÔØ×Ô:http://flyouting.iteye.com/blog/1054720
 */
public class MyExpandListView extends ListView {
	
	private Context context;  
    private boolean outBound = false;  
    private int distance;  
    private int firstOut;  

	public MyExpandListView(Context context) {
		super(context);
		this.context = context;  
		// TODO Auto-generated constructor stub
	}

	public MyExpandListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;  
		// TODO Auto-generated constructor stub
	}

	public MyExpandListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;  
		// TODO Auto-generated constructor stub
	}

	 GestureDetector mGestureDetector = new GestureDetector(context,  
	            new GestureDetector.OnGestureListener() {

					@Override
					public boolean onDown(MotionEvent arg0) {
						// TODO Auto-generated method stub
						return false;
					}

					@Override
					public boolean onFling(MotionEvent arg0, MotionEvent arg1,
							float arg2, float arg3) {
						// TODO Auto-generated method stub
						return false;
					}

					@Override
					public void onLongPress(MotionEvent arg0) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public boolean onScroll(MotionEvent e1, MotionEvent e2,  
	                        float distanceX, float distanceY) {
						 int firstPos = getFirstVisiblePosition();  
		                    int lastPos = getLastVisiblePosition();  
		                    int itemCount = getCount();  
		                    //outbound Top  
		                    if (outBound && firstPos != 0 && lastPos != (itemCount - 1)) {  
		                        scrollTo(0, 0);  
		                        return false;  
		                    }  
		                    View firstView = getChildAt(firstPos);  
		                    if (!outBound)  
		                        firstOut = (int) e2.getRawY();  
		                    if (firstView != null  
		                            && (outBound || (firstPos == 0  
		                                    && firstView.getTop() == 0 && distanceY < 0))) {  
		                        //Record the length of each slide  
		                        distance = firstOut - (int) e2.getRawY();  
		                        scrollTo(0, distance/2);  
		                        return true;  
		                    }  
		                    //outbound Bottom  
		  
		                    return false;  
					}

					@Override
					public void onShowPress(MotionEvent arg0) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public boolean onSingleTapUp(MotionEvent arg0) {
						// TODO Auto-generated method stub
						return false;
					} 
		 
	 });
	
	@Override  
    public boolean dispatchTouchEvent(MotionEvent event) {  
  
        int act = event.getAction();  
        if ((act == MotionEvent.ACTION_UP || act == MotionEvent.ACTION_CANCEL)  
                && outBound) {  
            outBound = false;  
            //scroll back  
        }  
        if (!mGestureDetector.onTouchEvent(event)) {  
            outBound = false;  
        } else {  
            outBound = true;  
        }  
        return super.dispatchTouchEvent(event);  
    }  
	
	

}
