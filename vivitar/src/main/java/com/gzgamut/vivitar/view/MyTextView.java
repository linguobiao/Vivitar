package com.gzgamut.vivitar.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.TextView;

public class MyTextView extends TextView{
	 private int m_rotate;  
     public MyTextView (Context context) {
             super(context);                        
     }
     
     public MyTextView(Context context, AttributeSet attrs) {
             super(context, attrs);
//             TypedArray array = context.obtainStyledAttributes(attrs,R.styleable.LabelView);
//             this.m_rotate = array.getInt(0, 90);
     }
     
     public MyTextView(Context context, AttributeSet attrs,int defStyle) {
             super(context, attrs,defStyle);                        
     }
     
     public int getMrotate(){
             return m_rotate;
     }
     public void setMrotate(int mrotate){
             m_rotate=mrotate;
     }
     
     protected void onDraw(Canvas canvas){
             canvas.translate(0, 0);
             canvas.rotate(90, getWidth()/2, getHeight()/2);
             super.onDraw(canvas);
     }
     
     protected void onMeasury(int widthMeasureSpec,int heightMeasureSpec){
             super.measure(widthMeasureSpec, heightMeasureSpec);
             setMeasuredDimension(400, 400);
     }

}
