package com.c50001.travelapp;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.c50001.travelapp.R;

/**
 * Created by robin on 27/10/15.
 */

// Search bar class for XML.
public class SearchableBar extends RelativeLayout{

    public SearchableBar(Context context) {
        super(context);
        initializeViews(context);
    }

    public SearchableBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeViews(context);
    }

    public SearchableBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initializeViews(context);
    }

    private void initializeViews(Context context) {
//        LayoutInflater inflater = (LayoutInflater) context
//                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        inflater.inflate(R.layout.sidespinner_view, this);
        inflate(context, R.layout.search_layout, this);
    }
}
