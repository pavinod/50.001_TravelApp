package com.c50001.travelapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * Created by robin on 28/10/15.
 */

// Custom Adapter Class so that list can be filtered using Fuzzy Search
public class CustomAdapter extends BaseAdapter implements Filterable {

    List<String> arrayList;
    List<String> mOriginalValues; // Original Values
    LayoutInflater inflater;

    public CustomAdapter(Context context, List<String> arrayList) {
        this.arrayList = arrayList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder {
        TextView textView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        //View holder pattern
        ViewHolder holder = null;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(android.R.layout.simple_list_item_1, null);
            holder.textView = (TextView) convertView
                    .findViewById(android.R.id.text1);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.textView.setText(arrayList.get(position));
        return convertView;
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                arrayList = (List<String>) results.values; // has the filtered values
                notifyDataSetChanged();  // notifies the data with new filtered values
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();        // Holds the results of a filtering operation in values
                List<String> FilteredArrList = new ArrayList<String>();

                if (mOriginalValues == null) {
                    mOriginalValues = new ArrayList<String>(arrayList); // saves the original data in mOriginalValues
                }

                /********
                 *
                 *  If constraint(CharSequence that is received) is null returns the mOriginalValues(Original) values
                 *  else does the Filtering and returns FilteredArrList(Filtered)
                 *
                 ********/
                if (constraint == null || constraint.length() == 0) {

                    // set the Original result to return
                    results.count = mOriginalValues.size();
                    results.values = mOriginalValues;
                } else {
                    constraint = constraint.toString().toLowerCase();
//                    for (int i = 0; i < mOriginalValues.size(); i++) {
//                        String data = mOriginalValues.get(i);
//                        if (data.toLowerCase().startsWith(constraint.toString())) {
//                            FilteredArrList.add(data);
//                        }
//                    }

                    //using Levenshtein Distance
                    List<Result> items = search((String) constraint);
                    for(Result item: items) {
                        FilteredArrList.add(item.name);
                    }

                    // set the Filtered result to return
                    results.count = FilteredArrList.size();
                    results.values = FilteredArrList;
                }
                return results;
            }
        };
        return filter;
    }

    private List<Result> search(String input) {
        List<Result> results = new ArrayList<Result>();
        Queue<Result> minHeap = new PriorityQueue<Result>(mOriginalValues.size(), Collections.reverseOrder()); //reverse priority queue. so maxheap?
        for (String name : mOriginalValues) {
//            int levenshteinDistance = StringUtils.getLevenshteinDistance(name.toLowerCase(), input);
            int fuzzyWuzzy = StringUtils.getFuzzyDistance(name.toLowerCase(), input, Locale.ENGLISH);
            Result r = new Result();
            r.name = name;
            r.distance = fuzzyWuzzy;
            minHeap.add(r);
        }
        for (int i = 0; i < 5 && !minHeap.isEmpty(); i++) {
            results.add(minHeap.poll());
        }
        return results;
    }

    private class Result implements Comparable<Result> {
        int distance;
        String name;

        @Override
        public int compareTo(Result another) {
            return this.distance - another.distance;
        }

        @Override
        public String toString() {
            return "name=" + name + ", distance=" + distance + "\n";
        }
    }

}
