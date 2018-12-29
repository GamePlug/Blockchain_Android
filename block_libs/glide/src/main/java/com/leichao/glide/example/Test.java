package com.leichao.glide.example;

import android.app.Activity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

public class Test {

    public static void test(Activity activity, ImageView imageView) {
        Glide.with(imageView).asBitmap().load("").apply(RequestOptions.bitmapTransform(new RoundedCorners(6))).into(imageView);
    }

}
