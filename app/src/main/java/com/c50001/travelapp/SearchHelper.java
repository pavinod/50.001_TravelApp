package com.c50001.travelapp;

/**
 * Created by robin on 28/10/15.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


// SQL database helper, originally was using FTS3 virtual table which i got from some blog cos i thought can do queries faster
// But then it doesnt support prefix search so ya changed back to ordinary sql.
public class SearchHelper {
    public static final String COLUMN_NAME = "name";

    private static final String TAG = "SearchHelper";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    private static final String DATABASE_NAME = "Data";
    private static final String FTS_VIRTUAL_TABLE = "Info";
    private static final int DATABASE_VERSION = 1;

    //Create a FTS3 Virtual Table for fast searches
    //FTS Virtual Table only allows postfix wild cards eg. lin* will match linux while *nu* will not.
//    private static final String DATABASE_CREATE =
//            "CREATE VIRTUAL TABLE " + FTS_VIRTUAL_TABLE + " USING fts3("
//                    + COLUMN_NAME
//                    + " UNIQUE (" + COLUMN_NAME + "));";
    private static final String DATABASE_CREATE =
            "CREATE TABLE " + FTS_VIRTUAL_TABLE + "(id integer primary key, name text)";



    private final Context context;

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }


        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.w(TAG, DATABASE_CREATE);
            db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + FTS_VIRTUAL_TABLE);
            onCreate(db);
        }
    }

    public SearchHelper(Context ctx) {
        this.context = ctx;
    }

    public SearchHelper open() throws SQLException {
        mDbHelper = new DatabaseHelper(context);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        if (mDbHelper != null) {
            mDbHelper.close();
        }
    }


    public long createList(String name) {

        ContentValues initialValues = new ContentValues();


        initialValues.put(COLUMN_NAME, name);

        return mDb.insert(FTS_VIRTUAL_TABLE, null, initialValues);

    }


    public Cursor searchByInputText(String inputText) throws SQLException {

//        String query = "SELECT docid as _id," +
//                COLUMN_NAME + " from " + FTS_VIRTUAL_TABLE +
//                " where " + COLUMN_NAME + " MATCH '" + inputText + "';";

        String query = "SELECT id as _id, name from "+ FTS_VIRTUAL_TABLE+
                " where " +  COLUMN_NAME + " LIKE '%" +inputText + "%';";
        Log.d("SQLITESTUFF", query);

        Cursor mCursor = mDb.rawQuery(query, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }

    public Cursor getAll() throws SQLException {

//        String query = "SELECT docid as _id," +
//                COLUMN_NAME + " from " + FTS_VIRTUAL_TABLE +
//                " where " + COLUMN_NAME + " MATCH '" + inputText + "';";

        String query = "SELECT id as _id, name from "+ FTS_VIRTUAL_TABLE;
        Log.d("SQLITESTUFF", query);

        Cursor mCursor = mDb.rawQuery(query, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }




    public boolean deleteAll() {

        int doneDelete = mDb.delete(FTS_VIRTUAL_TABLE, null, null);
        return doneDelete > 0;

    }
}