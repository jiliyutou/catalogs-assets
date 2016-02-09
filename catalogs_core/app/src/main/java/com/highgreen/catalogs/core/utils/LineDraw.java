package com.highgreen.catalogs.core.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

/**
 * Created by tihong on 16-2-8.
 */
public class LineDraw extends View {

    private Paint mPaint;
    private Canvas mCanvas;
    private Bitmap mBitmap;
    private int width;
    private int height;

    public LineDraw(Context context, int width, int height){
        super(context);
        // 声明画笔
        mPaint = new Paint();
        // 设置颜色
        mPaint.setColor(Color.WHITE);
        // 设置抗锯齿
        mPaint.setAntiAlias(true);
        // 设置线宽
        mPaint.setStrokeWidth(4);
        // 设置非填充
        // mPaint.setStyle(Paint.Style.STROKE);
        // 声明位图
        mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        // 声明画布
        mCanvas = new Canvas(mBitmap);

        this.width = width;
        this.height = height;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(mBitmap,0,0,null);
    }

    public Bitmap drawLine(){
        mCanvas.drawLine(0, height, width, 0, mPaint);
        return mBitmap;
    }
    public Bitmap drawLine2(){
        mCanvas.drawLine(0, 0, width, height, mPaint);
        return mBitmap;
    }
}
