package ca.tnoah.frc.scouting.helpers;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;

public class BitmapHelper {

    public static Bitmap covertToCircle(Bitmap bitmap) {
        int size = Math.min(bitmap.getWidth(), bitmap.getHeight());

        Bitmap dstBitmap = Bitmap.createBitmap(
                size,
                size,
                Bitmap.Config.ARGB_8888
        );

        Canvas canvas = new Canvas(dstBitmap);
        Paint paint = new Paint();
        paint.setAntiAlias(true);

        Rect rect = new Rect(0, 0, size, size);
        RectF rectF = new RectF(rect);

        canvas.drawOval(rectF, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        float left = (float) ((size - bitmap.getWidth()) /2);
        float top = (float) ((size - bitmap.getHeight()) / 2);
        canvas.drawBitmap(bitmap, left, top, paint);
        bitmap.recycle();

        return dstBitmap;
    }

    public static Bitmap covertToCircle(byte[] image) {
        if (image == null || image.length <= 0) return null;
        return covertToCircle(decodeByteArray(image));
    }

    public static Bitmap decodeByteArray(byte[] image) {
        if (image == null || image.length <= 0) return null;
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

}
