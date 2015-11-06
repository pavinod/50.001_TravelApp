package com.c50001.travelapp;

import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;

import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;


import com.c50001.travelapp.R;

import java.util.ArrayList;
import java.util.List;

public class SearchableActivity extends ListActivity implements SearchView.OnQueryTextListener,
        SearchView.OnCloseListener {

    private Toolbar toolbar;
    private SearchView searchView;
    private List<String> arrayList;
    private SearchHelper mDbHelper;
    private String[] values;
    private CustomAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_layout);

        // Get the intent, verify the action and get the query
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
//            doMySearch(query);
        }

        //setup toolbar
        setUpToolBar();

        //setup list
        values = new String[]{"Singapore", "Sydney", "Marina Bay Sands",
                "Blackberry", "Singapore University Of Technology and Design (SUTD)", "Resorts World Sentosa", "Singapore Flyer", "Buddha Tooth Relic Temple",
                "Linux", "Zoo", "Android", "iPhone", "WindowsMobile",
                "Blackberry", "WebOS", "Ubuntu"};


        searchView = (SearchView) findViewById(R.id.search);

        //expanded view of searchview
        searchView.onActionViewExpanded();

        //set hint
        searchView.setQueryHint("Search Places");

        //remove ugly underline
        View searchplate = searchView.findViewById(android.support.v7.appcompat.R.id.search_plate);
        searchplate.setBackgroundColor(Color.TRANSPARENT);

        searchView.setOnQueryTextListener(this);
        searchView.setOnCloseListener(this);

        //database stuff
        mDbHelper = new SearchHelper(this);
        mDbHelper.open();

        //Clear all names
        mDbHelper.deleteAll();

        // Create the list of names which will be displayed on search
        for (String val : values) {
            mDbHelper.createList(val);
        }

        //inital load of list
        Cursor cursor = mDbHelper.getAll();
        arrayList = new ArrayList<String>();
        if (cursor != null) {

            while (cursor.moveToNext()) {
                arrayList.add(cursor.getString(1));
            }

            adapter = new CustomAdapter(this, arrayList);
            setListAdapter(adapter);
        }

    }


    private void setUpToolBar() {

        toolbar = (Toolbar) findViewById(R.id.toolbar1);
        //Navigation Icon
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (!newText.isEmpty()) {
//            displayResults(newText);
            adapter.getFilter().filter(newText.toString());
        } else {
            adapter.getFilter().filter(null);
        }
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        displayResults(query);
        Toast.makeText(SearchableActivity.this, query, Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public boolean onClose() {
//        setListAdapter(adapter);
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mDbHelper != null) {
            mDbHelper.close();
        }
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        String item = (String) getListAdapter().getItem(position);
        Toast.makeText(this, item + " selected", Toast.LENGTH_LONG).show();

        finishWithResult(item);
    }


    //update listview
    private void displayResults(String query) {

    }


    //return result
    private void finishWithResult(String place) {
        Bundle conData = new Bundle();
        conData.putString("placeToFind", place);
        Intent intent = new Intent();
        intent.putExtras(conData);
        setResult(RESULT_OK, intent);
        finish();
    }
}
