package com.pw.schoolknow.widgets;



import com.pw.schoolknow.R;
import com.pw.schoolknow.activity.PhotoView;

import android.content.Context;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.widget.Gallery;

/**
 * android.widget.Gallery的子函数。
 */
public class QGallery extends Gallery {
	/**
	 * GestureDetector类
	 * 在onTouch()方法中，我们调用GestureDetector的onTouchEvent()方法，
	 * 将捕捉到的MotionEvent交给GestureDetector  
	 * 来分析是否有合适的callback函数来处理用户的手势  
	 */
	public Context context;
	private GestureDetector gestureScanner; 
	private QImageView imageView;
	private boolean isScroll = false;

	public QGallery(Context context) {
		super(context);
		this.context = context;

	}

	public QGallery(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
	}

	public QGallery(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;

		gestureScanner = new GestureDetector(new MySimpleGesture());
		this.setOnTouchListener(new OnTouchListener() {

			float baseValue;
			float originalScale;

			//重写onTouch方法实现缩放
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				View view = QGallery.this.getSelectedView();
				view=view.findViewById(R.id.item_photoview_img);
				if (view instanceof QImageView) {
					imageView = (QImageView) view;

					if (event.getAction() == MotionEvent.ACTION_DOWN) {
						baseValue = 0;
						originalScale = imageView.getScale();
					}
					if (event.getAction() == MotionEvent.ACTION_MOVE) {
						//处理双指拖动
						if (event.getPointerCount() == 2) {
							isScroll = false;
							float x = event.getX(0) - event.getX(1);
							float y = event.getY(0) - event.getY(1);
							float value = (float) Math.sqrt(x * x + y * y);// 计算两点的距离
							if (baseValue == 0) {
								baseValue = value;
							} else {
								float scale = value / baseValue;// 当前两点间的距离除以手指落下时两点间的距离就是需要缩放的比例。
								imageView.zoomTo(originalScale * scale);								
							}
						}
					}
					if(event.getAction() == MotionEvent.ACTION_UP){
						float ScaleRate = imageView.getScaleRate();
						if(imageView.getScale() < ScaleRate){ //缩放回放
							imageView.zoomTo(ScaleRate, event.getX(0), event.getY(0),500);								
							imageView.layoutToCenter();
						}
						isScroll = true;
					}
				}
				return false;
			}

		});
	}
	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
		View view = QGallery.this.getSelectedView();
		view=view.findViewById(R.id.item_photoview_img);
		if (view instanceof QImageView) {
			imageView = (QImageView) view;

			float v[] = new float[9];
			Matrix m = imageView.getImageMatrix();
			m.getValues(v);
			// 图片实时的上下左右坐标
			float left, right;
			// 图片的实时宽，高
			float width, height;
			width = imageView.getScale() * imageView.getImageWidth();
			height = imageView.getScale() * imageView.getImageHeight();
			// 下面逻辑为移动图片和滑动gallery换屏的逻辑。
			if ((int) width <= PhotoView.screenWidth && (int) height <= PhotoView.screenHeight)// 如果图片当前大小<屏幕大小，直接处理滑屏事件
			{
				if(isScroll)
					super.onScroll(e1, e2, distanceX, distanceY);
			} else {
				left = v[Matrix.MTRANS_X];
				right = left + width;	
				
				if (distanceX > 0)// 手势向左滑动，下一张图
				{
					if (right <= PhotoView.screenWidth) {
						super.onScroll(e1, e2, distanceX, distanceY);
					}else {
						imageView.postTranslate(-distanceX, -distanceY);
					}
				} else if (distanceX < 0)// 手势向右滑动，上一张图
				{
					if (left >= 0) {
						super.onScroll(e1, e2, distanceX, distanceY);
					} else {
						imageView.postTranslate(-distanceX, -distanceY);
					}
				}
			}

		} else {
			super.onScroll(e1, e2, distanceX, distanceY);
		}
		return false;
	}
	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
//		int position = MyGallery.this.getSelectedItemPosition();
//		int minV = ViewConfiguration.get(context).getScaledMinimumFlingVelocity();	
//         
//		if(velocityX > minV){			
//			if(position > 0)
//				MyGallery.this.setSelection(position-1);
//		}else if(velocityX < -minV){
//			if(position < MyGallery.this.getCount()-1)
//				MyGallery.this.setSelection(position+1);
//		}
		if (e2.getX() > e1.getX()) {
			// 往左边滑动
			super.onKeyDown(KeyEvent.KEYCODE_DPAD_LEFT, null);
		} else {
			// 往右边滑动
			super.onKeyDown(KeyEvent.KEYCODE_DPAD_RIGHT, null);
		}
		return false;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		gestureScanner.onTouchEvent(event);
		switch (event.getAction()) {
		case MotionEvent.ACTION_UP:
			// 判断上下边界是否越界
			View view = QGallery.this.getSelectedView();
			view=view.findViewById(R.id.item_photoview_img);
			if (view instanceof QImageView) {
				imageView = (QImageView) view;
				float width = imageView.getScale() * imageView.getImageWidth();
				float height = imageView.getScale() * imageView.getImageHeight();
				if ((int) width <= PhotoView.screenWidth && (int) height <= PhotoView.screenHeight)// 如果图片当前大小<屏幕大小，判断边界
				{
					break;
				}
				float v[] = new float[9];
				Matrix m = imageView.getImageMatrix();
				m.getValues(v);
				float top = v[Matrix.MTRANS_Y];
				float bottom = top + height;
				if (top > 0) {
					imageView.postTranslateDur(-top, 200f);
				}
				if (bottom < PhotoView.screenHeight) {
					imageView.postTranslateDur(PhotoView.screenHeight - bottom, 200f);
				}
			}
			break;
		}
		return super.onTouchEvent(event);
	}

	private class MySimpleGesture extends SimpleOnGestureListener {
		// 按两下的第二下Touch down时触发
		public boolean onDoubleTap(MotionEvent e) {
			View view = QGallery.this.getSelectedView();
			view=view.findViewById(R.id.item_photoview_img);
			if (view instanceof QImageView) {
				imageView = (QImageView) view;
				if (imageView.getScale() > imageView.getScaleRate()) {
					imageView.zoomTo(imageView.getScaleRate(), PhotoView.screenWidth / 2, PhotoView.screenHeight / 2, 200f);
					// imageView.layoutToCenter();
				} else {
					imageView.zoomTo(1.0f, PhotoView.screenWidth / 2, PhotoView.screenHeight / 2, 200f);
				}
			} else {
			}
			return true;
		}
	}
}
