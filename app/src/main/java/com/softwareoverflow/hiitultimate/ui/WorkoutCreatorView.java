package com.softwareoverflow.hiitultimate.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.softwareoverflow.hiitultimate.R;

import java.util.ArrayList;
import java.util.List;

public class WorkoutCreatorView extends View {

    private int centreX, centreY;

    private List<Path> triangularSegments = new ArrayList<>();

    private Hexagon hexagon = new Hexagon();

    private Paint fillPaint = new Paint();
    private Paint outlinePaint = new Paint();
    private Paint textPaint = new Paint();

    Path trianglePath = new Path();

    public WorkoutCreatorView(Context context) {
        super(context);
    }

    public WorkoutCreatorView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public WorkoutCreatorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public WorkoutCreatorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        fillPaint.setColor(getResources().getColor(R.color.colorAccent));
        fillPaint.setStrokeWidth(1);
        fillPaint.setAntiAlias(true);
        fillPaint.setStyle(Paint.Style.FILL);

        outlinePaint.setColor(getResources().getColor(R.color.colorPrimary));
        outlinePaint.setStrokeWidth(2);
        outlinePaint.setAntiAlias(true);
        outlinePaint.setStyle(Paint.Style.STROKE);

        textPaint.setColor(getResources().getColor(R.color.design_default_color_primary));
        textPaint.setStrokeWidth(1);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTextSize(16);
        textPaint.setAntiAlias(true);


        // Account for padding
        float xPad = (float)(getPaddingLeft() + getPaddingRight());
        float yPad = (float)(getPaddingTop() + getPaddingBottom());

        float width = (float) w - xPad;
        float height = (float) h - yPad;

        int maxSize = (int) Math.min(width, height);
        int selectedSegmentOffset = (int) (maxSize * 0.05);
        int viewSize = maxSize - 2 * selectedSegmentOffset;

        hexagon.setSelectedSegmentOffset(selectedSegmentOffset);
        hexagon.setViewSize(viewSize);
        hexagon.setCentre(getPaddingLeft() + viewSize / 2, getPaddingTop() + viewSize / 2);
        hexagon.recalculatePaths();

        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for(Path p : hexagon.getTriangularSegments()){
            canvas.drawPath(p, fillPaint);
            canvas.drawPath(p, outlinePaint);
        }

        for(RectF bounds : hexagon.getTextBounds()){
            canvas.drawText("6", (bounds.left + bounds.right) / 2,
                    (bounds.top + bounds.bottom) / 2, textPaint);
        }
    }
}
