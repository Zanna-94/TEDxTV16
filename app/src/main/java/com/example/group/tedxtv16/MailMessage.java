package com.example.group.tedxtv16;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by simone_mancini on 09/03/16.
 */
public class MailMessage extends EditText{

    private Paint paint;

    public MailMessage(Context context) {
        super(context);
    }

    public MailMessage(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MailMessage(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MailMessage(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
            super(context, attrs, defStyleAttr, defStyleRes);
        }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint(Paint.UNDERLINE_TEXT_FLAG);
        setupPaint();
        //top
        //canvas.drawLine(0, 0, this.getWidth()-1, 0, paint);
        //left
        //canvas.drawLine(this.getWidth()-1,0,this.getWidth()-1,this.getHeight()-1,paint);
        //bottom
        //canvas.drawLine(0,this.getHeight()-1,this.getWidth()-1,this.getHeight()-1,paint);
        //right
        //canvas.drawLine(0, 0, 0, this.getHeight() - 1, paint);
        drawBackground(canvas, paint);
    }

    private void setupPaint() {
        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(50);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
    }

    private void drawBackground(Canvas canvas, Paint paint) {
        int width = canvas.getWidth()-2;
        int height = canvas.getHeight()-2;

        Point a = new Point(0, 0);
        Point a1 = new Point(width, 0);
        Point a2 = new Point(width, 10);
        Point a3 = new Point(0, 10);
        Point b = new Point(width, 0);
        Point b1 = new Point(width, height);
        Point b2 = new Point(width-10, height);
        Point b3 = new Point(width-10, 0);
        Point c = new Point(width, height);
        Point c1 = new Point(0, height);
        Point c2 = new Point(0, height-10);
        Point c3 = new Point(width, height-10);
        Point d = new Point(0, height);
        Point d1 = new Point(0, 0);
        Point d2 = new Point(10, 0);
        Point d3 = new Point(10, height);


        Path path = new Path();
        path.moveTo(a.x, a.y);
        path.lineTo(a1.x, a1.y);
        path.lineTo(a2.x, a2.y);
        path.lineTo(a3.x, a3.y);
        canvas.drawPath(path, paint);
        path = new Path();
        path.moveTo(b.x, b.y);
        path.lineTo(b1.x, b1.y);
        path.lineTo(b2.x, b2.y);
        path.lineTo(b3.x, b3.y);
        canvas.drawPath(path, paint);
        path = new Path();
        path.moveTo(c.x, c.y);
        path.lineTo(c1.x, c1.y);
        path.lineTo(c2.x, c2.y);
        path.lineTo(c3.x, c3.y);
        canvas.drawPath(path, paint);
        path = new Path();
        path.moveTo(d.x, d.y);
        path.lineTo(d1.x, d1.y);
        path.lineTo(d2.x, d2.y);
        path.lineTo(d3.x, d3.y);
        canvas.drawPath(path,paint);
    }
}
