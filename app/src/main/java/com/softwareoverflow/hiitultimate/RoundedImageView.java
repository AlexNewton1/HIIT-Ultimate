package com.softwareoverflow.hiitultimate;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;

public class RoundedImageView extends AppCompatImageView
{
    Paint paint = new Paint();

    public RoundedImageView(Context ctx, AttributeSet attrs) {
        super(ctx, attrs);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        paint.setAntiAlias(true);
        paint.setFilterBitmap(false);
    }

    @Override
    public void setBackgroundColor(int color) {
       paint.setColor(color);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        Drawable drawable = getDrawable();

        if (drawable == null)
        {
            return;
        }

        if (getWidth() == 0 || getHeight() == 0)
        {
            return;
        }
        Bitmap b = getBitmapFromVectorDrawable(drawable);
        Bitmap bitmap = b.copy(Config.ARGB_8888, true);

        int w = getWidth(), h = getHeight();

        Bitmap roundBitmap = getRoundedCroppedBitmap(bitmap, w);

        canvas.drawCircle(w /2, h/2, w/2, paint);
        //canvas.drawBitmap(roundBitmap, 0, 0, paint);
    }

    private Bitmap getRoundedCroppedBitmap(Bitmap bitmap, int radius)
    {
        Bitmap finalBitmap;
        if (bitmap.getWidth() != radius || bitmap.getHeight() != radius)
            finalBitmap = Bitmap.createScaledBitmap(bitmap, radius, radius,
                    false);
        else
            finalBitmap = bitmap;
        Bitmap output = Bitmap.createBitmap(finalBitmap.getWidth(),
                finalBitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

//        final Rect rect = new Rect(0, 0, finalBitmap.getWidth(),
//                finalBitmap.getHeight());
//
//
//        canvas.drawARGB(0, 0, 0, 0);
//        canvas.drawCircle(finalBitmap.getWidth() / 2 + 0.7f,
//                finalBitmap.getHeight() / 2 + 0.7f,
//                finalBitmap.getWidth() / 2 + 0.1f, paint);
//        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
//        canvas.drawBitmap(finalBitmap, rect, rect, paint);

        return output;
    }

    private Bitmap getBitmapFromVectorDrawable(Drawable drawable) {
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

}