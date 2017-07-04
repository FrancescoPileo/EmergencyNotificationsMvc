package com.univpm.cpp.emergencynotificationsmvc.utils;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;

import com.univpm.cpp.emergencynotificationsmvc.models.beacon.Beacon;
import com.univpm.cpp.emergencynotificationsmvc.models.node.Node;

import java.util.HashSet;
import java.util.Random;

/**
 * Classe che permette di disegnare e gestire sulla mappa i cerchi che rappresentano i beacon
 */
public class CirclesDrawingView extends View{

    private static final String TAG = "CirclesDrawingView";

    /** Main bitmap */
    private Bitmap mBitmap = null;

    private Rect mMeasuredRect;

    private CircleArea mCircleArea;

    private Node node;

    private Beacon beacon;

    /** Stores data about single circle */
    public static class CircleArea {
        int radius;
        int centerX;
        int centerY;

        public CircleArea(int centerX, int centerY, int radius) {
            this.radius = radius;
            this.centerX = centerX;
            this.centerY = centerY;
        }

        public int getRadius() {
            return radius;
        }

        public void setRadius(int radius) {
            this.radius = radius;
        }

        public int getCenterX() {
            return centerX;
        }

        public void setCenterX(int centerX) {
            this.centerX = centerX;
        }

        public int getCenterY() {
            return centerY;
        }

        public void setCenterY(int centerY) {
            this.centerY = centerY;
        }

        @Override
        public String toString() {
            return "Circle[" + centerX + ", " + centerY + ", " + radius + "]";
        }
    }

    /** Paint to draw circles */
    private Paint mCirclePaint;

    private Paint mCircleBorder;

    private final Random mRadiusGenerator = new Random();
    // Radius limit in pixels
    private final static int RADIUS_LIMIT = 100;

    private static final int CIRCLES_LIMIT = 3;

    /** All available circles */
    private HashSet<CircleArea> mCircles = new HashSet<CircleArea>(CIRCLES_LIMIT);
    private SparseArray<CircleArea> mCirclePointer = new SparseArray<CircleArea>(CIRCLES_LIMIT);

    /**
     * Default constructor
     *
     * @param ct {@link Context}
     */
    public CirclesDrawingView(final Context ct) {
        super(ct);

        init(ct);
    }

    public CirclesDrawingView(final Context ct, final AttributeSet attrs) {
        super(ct, attrs);

        init(ct);
    }

    public CirclesDrawingView(final Context ct, final AttributeSet attrs, final int defStyle) {
        super(ct, attrs, defStyle);

        init(ct);
    }

    private void init(final Context ct) {
        // Generate bitmap used for background
        mCirclePaint = new Paint();

        mCirclePaint.setColor(Color.BLUE);
        mCirclePaint.setStrokeWidth(40);
        mCirclePaint.setStyle(Paint.Style.FILL);
    }

    @Override
    public void onDraw(Canvas canvas) {
        // background bitmap to cover all area
        canvas.drawCircle(mCircleArea.centerX, mCircleArea.centerY, mCircleArea.radius, mCirclePaint);
        canvas.drawCircle(mCircleArea.centerX, mCircleArea.centerY, mCircleArea.radius, mCircleBorder);

    }






    public Bitmap getmBitmap() {
        return mBitmap;
    }

    public void setmBitmap(Bitmap mBitmap) {
        this.mBitmap = mBitmap;
    }

    public Rect getmMeasuredRect() {
        return mMeasuredRect;
    }

    public void setmMeasuredRect(Rect mMeasuredRect) {
        this.mMeasuredRect = mMeasuredRect;
    }

    public Paint getmCirclePaint() {
        return mCirclePaint;
    }

    public void setmCirclePaint(Paint mCirclePaint) {
        this.mCirclePaint = mCirclePaint;
    }

    public Paint getmCircleBorder() {
        return mCircleBorder;
    }

    public void setmCircleBorder(Paint mCircleBorder) {
        this.mCircleBorder = mCircleBorder;
    }

    public CircleArea getmCircleArea() {
        return mCircleArea;
    }

    public void setmCircleArea(CircleArea mCircleArea) {
        this.mCircleArea = mCircleArea;
    }

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public Beacon getBeacon() {
        return beacon;
    }

    public void setBeacon(Beacon beacon) {
        this.beacon = beacon;
    }
}
