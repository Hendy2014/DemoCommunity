/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.lhy.comm.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;

public class ImageUtils {

    /**
     * The drawable can be a Nine-Patch. If we directly use the same drawable instance for each
     * drawable of different sizes, then the drawable sizes would interfere with each other. The
     * solution here is to create a new drawable instance for every time with the SAME
     * ConstantState (i.e. sharing the same common state such as the bitmap, so that we don't have
     * to recreate the bitmap resource), and apply the different properties on top (nine-patch
     * size and color tint).
     * <p>
     * TODO: we are creating new drawable instances here, but there are optimizations that
     * can be made. For example, message bubbles shouldn't need the mutate() call and the
     * play/pause buttons shouldn't need to create new drawable from the constant state.
     */
    public static Drawable getTintedDrawable(final Context context, final Drawable drawable,
                                             final int color) {
        // For some reason occassionally drawables on JB has a null constant state
        final Drawable.ConstantState constantStateDrawable = drawable.getConstantState();
        final Drawable retDrawable = (constantStateDrawable != null)
                ? constantStateDrawable.newDrawable(context.getResources()).mutate()
                : drawable;
        retDrawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        return retDrawable;
    }

    public static Drawable getTintedDrawable(final Context context, final Drawable drawable) {
        // For some reason occassionally drawables on JB has a null constant state
        final Drawable.ConstantState constantStateDrawable = drawable.getConstantState();
        final Drawable retDrawable = (constantStateDrawable != null)
                ? constantStateDrawable.newDrawable(context.getResources()).mutate()
                : drawable;
        return retDrawable;
    }

    /**
     * 采样率压缩图片
     *
     * @param src        源图片
     * @param destWidth  目标图片宽度
     * @param destHeight 目标图片高度
     */
    public static Bitmap compress(Bitmap src, int destWidth, int destHeight) {
        Bitmap bitmap;
        if (null != src) {
            if (destHeight <= 0 || destWidth <= 0) {
                bitmap = src;
            } else {
                bitmap = src.createScaledBitmap(src, destWidth, destHeight, false);
            }
        } else {
            bitmap = null;
        }
        return bitmap;
    }

    /**
     * 合成两张图片(合成在源图片的中间位置)
     *
     * @param src    源图片
     * @param centre 待合成的图片
     */
    public static Bitmap composition(Bitmap src, Bitmap centre) {
        Bitmap bitmap;
        if (null == centre || null == src) {
            bitmap = src;
        } else {
            bitmap = src.copy(Bitmap.Config.ARGB_8888, true);
            Canvas canvas = new Canvas(bitmap);
            int backWidth = src.getWidth();
            int backHeight = src.getHeight();
            int frontWidth = centre.getWidth();
            int frontHeight = centre.getHeight();
            //合成图片
            canvas.drawBitmap(centre,
                    new Rect(0, 0, frontWidth, frontHeight),
                    new Rect(backWidth / 2 - frontWidth / 2, backHeight / 2 - frontHeight / 2, backWidth / 2 + frontWidth / 2, backHeight / 2 + frontHeight / 2),
                    null);
            src.recycle();
            centre.recycle();
        }
        return bitmap;
    }

    /**
     * 获取透明图
     *
     * @param src    原图
     * @param number ALPHA值(0-100)
     */
    public static Bitmap getTransparent(Bitmap src, int number) {
        Bitmap bitmap = null;
        Bitmap circle = null;
        if (null != src) {
            int width = src.getWidth();
            int height = src.getHeight();
            //argb_8888格式的图片，一个像素占用4个字节
            int[] argb = new int[width * height];
            number = number * 255 / 100;
            src.getPixels(argb,
                    0,
                    width,
                    0,
                    0,
                    width,
                    height);
            for (int i = 0; i < argb.length; i++) {
                //过滤透明度为0的像素点
                int transparent = argb[i] >> 24 & 0xff;
                if (transparent != 0) {
                    //对透明度a 按位操作
                    argb[i] = number << 24 | (argb[i] & 0x00ffffff);
                }
            }
            bitmap = Bitmap.createBitmap(argb, width, height, Bitmap.Config.ARGB_8888);
            src.recycle();
        } else {
            bitmap = null;
        }
        return bitmap;
    }

    /**
     * 获取全屏Bitmap
     *
     * @param src
     * @param context
     * @return
     */
    public static Bitmap getFullScreenBitmap(Bitmap src, Context context) {

        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int screenWidth = dm.widthPixels;
        int screenHeight = dm.heightPixels;
        int srcWidth = src.getWidth();
        int srcHeight = src.getHeight();
        float radio;
        float widthRadio = (float) screenWidth / srcWidth;
        float heightRadio = (float) screenHeight / srcHeight;
        radio = widthRadio >= heightRadio ? heightRadio : widthRadio;
        int destWidth = (int) (srcWidth * radio);
        int destHeight = (int) (destWidth * ((float) srcHeight / srcWidth));
        src = src.createScaledBitmap(src, destWidth, destHeight, false);
        return src;
    }

}
