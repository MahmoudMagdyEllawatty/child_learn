package com.water.learnchild.utils;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.*;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.util.Log;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class TracingView extends View {
    private Path drawPath;
    private Paint drawPaint;
    private Paint canvasPaint;
    private int paintColor = Color.BLACK;
    private float brushSize = 15f;

    public TracingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initDrawing();
    }

    private void initDrawing() {
        drawPath = new Path();
        drawPaint = new Paint();
        drawPaint.setColor(paintColor);
        drawPaint.setAntiAlias(true);
        drawPaint.setStrokeWidth(brushSize);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);

        // This is your canvas background paint (white background)
        canvasPaint = new Paint(Paint.DITHER_FLAG);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // Draw the currently stored path(s)
        canvas.drawPath(drawPath, drawPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float touchX = event.getX();
        float touchY = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                drawPath.moveTo(touchX, touchY);
                break;
            case MotionEvent.ACTION_MOVE:
                drawPath.lineTo(touchX, touchY);
                break;
            case MotionEvent.ACTION_UP:
                // Optionally, you can do extra processing when the stroke ends.
                break;
            default:
                return false;
        }
        // Force a view to draw again (i.e., update the view)
        invalidate();
        return true;
    }

    // Clear the canvas (called from your Activity when "Clear" is pressed)
    public void clear() {
        drawPath.reset();
        invalidate();
    }

}
