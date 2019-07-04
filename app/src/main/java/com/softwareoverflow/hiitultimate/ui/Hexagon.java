package com.softwareoverflow.hiitultimate.ui;

import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.toRadians;

public class Hexagon {

    private static final float angle = 60;

    private float centreX, centreY;
    private int selectedSegmentOffset;
    private int viewSize;

    private int selectedSegment = -1; //TODO - implement bigger selected segment

    private List<Path> triangularSegments;
    private List<PointF> textBounds;


    void setSelectedSegmentOffset(int selectedSegmentOffset) {
        this.selectedSegmentOffset = selectedSegmentOffset;
    }

    void setViewSize(int viewSize) {
        this.viewSize = viewSize;
    }

    void setCentre(float cx, float cy){
        centreX = cx;
        centreY = cy;

        Log.d("hiit-ult-debug", "centre: (" + centreX + ", " + centreY + ")");
    }

    List<Path> getTriangularSegments() {
        return triangularSegments;
    }

    void recalculatePaths(){
        triangularSegments = new ArrayList<>();

        boolean addOffset = false;
        for(int i=0; i<6; i++)
        {
            addOffset = i == selectedSegment;

            Path path = calculatePath(i, addOffset);
            triangularSegments.add(path);
        }
    }

    private Path calculatePath(int i, boolean addOffset) {
        float halfRadius = viewSize / 2f;
        float hypotenuseLength = (float) (halfRadius * Math.cos(toRadians(angle / 2)));

        float x1, y1, x2, y2;
        float angle1, angle2;

        angle1 = (i + 0.5f) * angle;
        angle2 = (i - 0.5f) * angle;

        x1 = (float) (centreX + hypotenuseLength * Math.sin(toRadians(angle1)));
        y1 = (float) (centreY + hypotenuseLength * Math.cos(toRadians(angle1)));

        x2 = (float) (centreX + hypotenuseLength * Math.sin(toRadians(angle2)));
        y2 = (float) (centreY + hypotenuseLength * Math.cos(toRadians(angle2)));

        Path path = new Path();
        path.moveTo(centreX, centreY);
        path.lineTo(x1, y1);
        path.lineTo(x2, y2);
        path.close();

        Matrix scaleMatrix = new Matrix();
        RectF rectF = new RectF();
        path.computeBounds(rectF, true);

        float scale = 1f;
        if (addOffset) scale = 1.1f;

        scaleMatrix.setScale(scale, scale, centreX, centreY);
        path.transform(scaleMatrix);


        // TODO - calc centre for text
        float textCentreX = (centreX + x1 + x2) / 3f;
        float textCentreY = (centreY + y1 + y2) / 3f;
        textBounds.add(new PointF(textCentreX, textCentreY));

        return path;
    }
}
