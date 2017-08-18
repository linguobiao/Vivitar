package com.gzgamut.vivitar.view;

import com.gzgamut.vivitar.R;
import com.gzgamut.vivitar.database.DatabaseProvider;
import com.gzgamut.vivitar.global.Global;
import com.gzgamut.vivitar.main.ChartActivity;
import com.gzgamut.vivitar.main.UserDetailsTableActivity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ViewFlipper;

public class MyViewFlipper extends ViewFlipper {
	public static int a = 0;
	private float startX;
	private Context mContext;
	int index = 0;

	public MyViewFlipper(Context paramContext, AttributeSet paramAttributeSet) {
		super(paramContext, paramAttributeSet);
		this.mContext = paramContext;

	}

	@Override
	public boolean onTouchEvent(MotionEvent paramMotionEvent) {
		Log.i("test", "*********444444444 = ");
		switch (paramMotionEvent.getAction()) {

		case MotionEvent.ACTION_DOWN:
			this.startX = paramMotionEvent.getX();
			return true;
		case MotionEvent.ACTION_MOVE:
			return true;

		case MotionEvent.ACTION_UP:
			if ((Math.abs(paramMotionEvent.getX() - this.startX) < 10.0F)) {
				int id = this.getId();
				Intent intent = new Intent(mContext, ChartActivity.class);
				// Intent intent = new Intent(mContext,
				// UserDetailsTableActivity.class);

				intent.putExtra(Global.KEY_USER_TYPE, id);

				mContext.startActivity(intent);
				Log.i("test", "*********id = " + id);
			}
			if (paramMotionEvent.getX() - this.startX > 50) {
				if (index > 0) {
					setInAnimation(this.mContext, R.anim.slide_right_in);
					setOutAnimation(this.mContext, R.anim.slide_right_out);
					showPrevious();
					index--;
				}
			}
			if (this.startX - paramMotionEvent.getX() > 50.0F) {
				if (index < 1) {
					setInAnimation(this.mContext, R.anim.slide_left_in);
					setOutAnimation(this.mContext, R.anim.slide_left_out);
					showNext();
					index++;
				}
			}

			break;
		}
		return false;
	}
}
