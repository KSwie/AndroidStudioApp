package com.example.kamil.aplikacja;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class DrawView extends View {

    private Paint paint = new Paint();
    private Path path = new Path();
    public Button cleanScreenBtn;
    public ViewGroup.LayoutParams params;

    public DrawView(Context context) {
        super(context);
        paint.setAntiAlias(true);
        paint.setColor(Color.BLUE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeWidth(15f);

        cleanScreenBtn = new Button(context);
        cleanScreenBtn.setText(R.string.cleanScreenBtnString);
        params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        cleanScreenBtn.setLayoutParams(params);
        cleanScreenBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                path.reset();
                postInvalidate();
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //return super.onTouchEvent(event);
        float pointX = event.getX();
        float pointY = event.getY();

        switch (event.getAction()){
            case (MotionEvent.ACTION_DOWN):
                path.moveTo(pointX,pointY);
                return true;
            case (MotionEvent.ACTION_MOVE):
                path.lineTo(pointX,pointY);
                break;
            case (MotionEvent.ACTION_UP):
                break;
            default:
                return false;
        }

        postInvalidate();
        return false;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawPath(path,paint);
    }
}
