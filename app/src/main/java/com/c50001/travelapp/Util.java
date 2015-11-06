package com.c50001.travelapp;

import android.content.Context;

/**
 * Created by robin on 27/10/15.
 */

//Utilties la.
public class Util {
    public static float dpFromPx(final Context context, final float px) {
        return px / context.getResources().getDisplayMetrics().density;
    }

    public static float pxFromDp(final Context context, final float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }
}
