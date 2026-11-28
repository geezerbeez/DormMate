package com.itismob.s02.group9.mco.dormmate;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

/**
 * Fills a round avatar view: the contact's profile photo when they have one,
 * otherwise a colored circle with their initial.
 */
public final class ChatAvatarLoader {
    private static final int SIZE = 192;
    private static final Map<Integer, Bitmap> CACHE = new HashMap<>();

    private ChatAvatarLoader() {}

    public static void bind(TextView view, Conversation c) {
        Bitmap photo = c.getPhotoRes() != 0 ? circlePhoto(view.getResources(), c.getPhotoRes()) : null;
        if (photo != null) {
            view.setText("");
            view.setBackgroundTintList(null);
            view.setBackground(new BitmapDrawable(view.getResources(), photo));
        } else {
            view.setText(c.getInitial());
            view.setBackgroundResource(R.drawable.bg_avatar);
            view.setBackgroundTintList(ColorStateList.valueOf(c.getAvatarColor()));
        }
    }

    /** Center-crops the drawable to a square and masks it to a circle (cached per drawable). */
    private static Bitmap circlePhoto(Resources res, int resId) {
        Bitmap cached = CACHE.get(resId);
        if (cached != null) return cached;

        Bitmap src = BitmapFactory.decodeResource(res, resId);
        if (src == null) return null;

        int side = Math.min(src.getWidth(), src.getHeight());
        Bitmap square = Bitmap.createBitmap(src,
                (src.getWidth() - side) / 2, (src.getHeight() - side) / 2, side, side);
        Bitmap scaled = Bitmap.createScaledBitmap(square, SIZE, SIZE, true);

        Bitmap out = Bitmap.createBitmap(SIZE, SIZE, Bitmap.Config.ARGB_8888);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setShader(new BitmapShader(scaled, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
        new Canvas(out).drawCircle(SIZE / 2f, SIZE / 2f, SIZE / 2f, paint);

        CACHE.put(resId, out);
        return out;
    }
}
