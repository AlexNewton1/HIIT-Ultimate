/*
package com.softwareoverflow.hiitultimate.workout.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.softwareoverflow.hiitultimate.R;

import static android.content.res.Configuration.ORIENTATION_LANDSCAPE;
import static android.content.res.Configuration.ORIENTATION_PORTRAIT;

*/
/**
 * This creates a dashed outline of triangular breadcrumbs which will later be filled through
 * animation.
 * The breadcrumbs act as both a visual aid and can also be used to trigger navigation.
 * When the device is in landscape mode, the view will draw the breadcrumbs vertically for side
 * navigation.
 * When the device is in portrait mode, the view will draw the breadcrumbs horizontally for top or
 * bottom navigation.
 *//*

*/
/* TODO - make the numBreadcrumbs configurable. Ensure views can be transformed into the breadcrumb
        outline. This may require storing each path separately so that the transformed view can be
        clipped to the path bounds. Will need validation / logic to ensure the view being
        transformed is of an appropriate size (scaled animation so that
        Math.min(transformedView.width, transformedView.height) == triangleSize)

        ?????????????????????????????????????????????????????????????????????????????????????????
   TODO - For this, maybe abstract away the single triangle objects to contain their own path and
        their enclosing view so that this class is not taking on multiple responsibilities, purely
        is responsible for drawing the triangles
 *//*


public class TriangularBreadcrumbs extends View {

    private Context context;
    private int orientation; // Breadcrumbs are vertical for landscape. Horizontal for portrait.
    private int numBreadcrumbs = 5; // default the number of breadcrumbs to 5

    private Paint outlinePaint;
    private Path[] paths = new Path[numBreadcrumbs];

    // TODO abstract this away
    private Bitmap bitmapToAddToBreadcrumb;
    private Integer bitmapToAddToBreadcrumbIndex;
    private Matrix matrix;

    private int viewWidth;
    private int viewHeight;

    private int triangleSize; // The side lengths of the triangles
    private int internalPadding; // The padding between triangles

    public TriangularBreadcrumbs(Context context) {
        super(context);
        setup(context);
    }

    public TriangularBreadcrumbs(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setup(context);
    }

    public TriangularBreadcrumbs(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setup(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public TriangularBreadcrumbs(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setup(context);
    }

    private void setup(Context context) {
        this.context = context;

        //set up the paint object
        outlinePaint = new Paint();
        outlinePaint.setColorIndex(getResources().getColorIndex(R.color.colorPrimary));
        outlinePaint.setStrokeWidth(2);
        outlinePaint.setPathEffect(new DashPathEffect(new float[]{4, 4}, 0));
        outlinePaint.setAntiAlias(true);
        outlinePaint.setStyle(Paint.Style.STROKE);

        // Default the view orientation to match the view
        orientation = getResources().getConfiguration().orientation;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        // Set the new size of the view, accounting for padding
        viewWidth = w - (int) (getPaddingLeft() + getPaddingRight());
        viewHeight = h - (int) (getPaddingTop() + getPaddingBottom());

        recalculateSizes();
        recalculatePaths();
    }

    private void recalculateSizes() {
        int internalPaddingCount = numBreadcrumbs - 1;
        // Allow the padding to be a multiple of the triangle size
        int trianglePaddingSizeFactor = 8;

        // Account for alternating triangle rotation causing tessellation
        int numFullTrianglesToFit = (int) (1 + 0.5 * numBreadcrumbs);

        // create the view as if were a number of cells. Each triangle takes up
        // trianglePaddingSizeFactor cells and each internalPadding is a single cell.
        if (orientation == ORIENTATION_LANDSCAPE) {
//            internalPadding = viewHeight / ((numFullTrianglesToFit * trianglePaddingSizeFactor) + internalPaddingCount);
//            triangleSize = Math.min(viewWidth, internalPadding * trianglePaddingSizeFactor);
        } else if (orientation == ORIENTATION_PORTRAIT) {
            internalPadding = viewWidth / ((numFullTrianglesToFit * trianglePaddingSizeFactor) + internalPaddingCount);
            triangleSize = Math.min(
                    (int) (viewHeight * Math.cos(Math.toRadians(30)) - outlinePaint.getStrokeWidth()),
                    internalPadding * trianglePaddingSizeFactor);
        }

        Log.d("argh", "width: " + viewWidth + ", height: " + viewHeight);
    }

    private void recalculatePaths() {

        if (orientation == ORIENTATION_LANDSCAPE) {
            throw new UnsupportedOperationException(
                    "The view has not been created for landscape layouts yet");
        } else if (orientation == ORIENTATION_PORTRAIT) {

            for (int i = 0; i < numBreadcrumbs; i++) {

                Path path = new Path();
                int x, y;
                if (i % 2 == 0) {
                    x = (int) this.getX() + this.getPaddingLeft() + (i * internalPadding + (i + 1) * (triangleSize / 2));
                    y = (int) (this.getY() + this.getPaddingTop());

                    Log.d("argh", x + ", " + (x + triangleSize) + ", " + y + ", " + (y + triangleSize));

                    path.moveTo(x, y);
                    path.rLineTo(-triangleSize / 2, triangleSize);
                    path.rLineTo(triangleSize, 0);
                    path.lineTo(x, y);
                    path.close();
                } else {
                    x = (int) this.getX() + this.getPaddingLeft() + (int) Math.ceil(i / 2f) * triangleSize + i * internalPadding;
                    y = (int) (this.getY() + this.getPaddingTop() + triangleSize);

                    path.moveTo(x, y);
                    path.rLineTo(-triangleSize / 2, -triangleSize);
                    path.rLineTo(triangleSize, 0);
                    path.lineTo(x, y);
                    path.close();
                }

                paths[i] = path;
            }
        }
    }

    public void setViewToBreadcrumb(View view, int index){
        setViewToBreadcrumb(view, index, false);
    }

    public void setViewToBreadcrumb (View view, int index, boolean moveOriginal){
        bitmapToAddToBreadcrumb = ViewUtils.createBitmapFromView(view, view.getWidth(), view.getHeight());
        bitmapToAddToBreadcrumbIndex = index;

        matrix = new Matrix();
        matrix.setTranslate(view.getX(), view.getY());
        matrix.setScale(0.5f, 0.5f);
        invalidate();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for (Path path : paths)
            canvas.drawPath(path, outlinePaint);

        if (bitmapToAddToBreadcrumb != null && matrix != null)
            canvas.drawBitmap(bitmapToAddToBreadcrumb, matrix, null);
    }
}
*/
