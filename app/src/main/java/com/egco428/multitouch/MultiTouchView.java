package com.egco428.multitouch;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by 6272user on 10/27/2016 AD.
 */
public class MultiTouchView extends View{
    private static final int SIZE = 60;
    private SparseArray<PointF> mActivativePointers;//เวลาเก็บใน memory ไม่ได้เก็บติดกันเหมือน ArrayAdapter ทำให้ประหยัดพื้นที่ memory กว่า
    private Paint mPaint;
    private int[]colors ={Color.BLUE,Color.GREEN,Color.MAGENTA,Color.BLACK,Color.CYAN,Color.GRAY,Color.RED,Color.DKGRAY,Color.LTGRAY,Color.YELLOW};
    private Paint textPaint;

    public MultiTouchView(Context context, AttributeSet attrs){
        super(context, attrs);
        initView();
    }
    private void initView(){
        mActivativePointers = new SparseArray<PointF>();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.BLUE);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextSize(20);

    }
    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        for(int size = mActivativePointers.size(),i=0;i<size;i++){
            PointF point = mActivativePointers.valueAt(i);
            if(point != null){
                mPaint.setColor(colors[i%9]);//เกิน 9 ให้วนสีซ้ำ
                canvas.drawCircle(point.x,point.y,SIZE,mPaint);
            }
            canvas.drawText("Total pointers:"+mActivativePointers.size(),10,40,textPaint);
        }
    }
    @Override
    public  boolean onTouchEvent(MotionEvent event){
        int pointerIndex = event.getActionIndex();
        int pointerId=event.getPointerId(pointerIndex);
        int maskedAction =event.getActionMasked();
        switch (maskedAction){
            case MotionEvent.ACTION_POINTER_DOWN: //จะได้แยก index ได้ (เป็น multitouch)
                PointF f =new PointF();
                f.x = event.getX(pointerIndex);
                f.y = event.getY(pointerIndex);
                mActivativePointers.put(pointerId, f); // check which finger active now
                break;
            case MotionEvent.ACTION_MOVE:
                for(int size = event.getPointerCount(),i=0;i<size;i++){
                    PointF point = mActivativePointers.get(event.getPointerId(i));
                    if (point!=null){
                        point.x = event.getX(i);
                        point.y = event.getY(i);
                    }
                }
                break;
            case MotionEvent.ACTION_CANCEL: {
                mActivativePointers.remove(pointerId);
                break;
            }
        }
        invalidate();
        return true;
    }
}
