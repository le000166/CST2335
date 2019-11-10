package com.example.androidlabs;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MyDatabaseOpenHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "MyDatabaseFile";
    public static final int VERSION_NUM = 11;
    public static final String TABLE_NAME = "Messages";
    public static final String COL_MESSAGE = "MESSAGE";
    public static final String COL_ID = "_ID";
    public static final String COL_BOOLEAN = "SEND_OR_RECEIVE";
    public static final String CREATE_TABLE = "CREATE TABLE "+TABLE_NAME
            + "( "+COL_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+COL_MESSAGE+" TEXT, "+COL_BOOLEAN+" INTEGER)";


    public MyDatabaseOpenHelper(Activity ctx){
        //The factory parameter should be null, unless you know a lot about Database Memory management
        /**Context ctx: the Activity where the database is being opened
         * String databaseName – this is the file that will contain the data.
         * CursorFactory – An object to create Cursor objects, normally this is null.
         * Int version – What is the version of your database
         */
        super(ctx, DATABASE_NAME, null, VERSION_NUM );
    }


    public void onCreate(SQLiteDatabase db)
    {
//        db.execSQL("CREATE TABLE " + TABLE_NAME + "( "
//                + COL_ID +" INTEGER PRIMARY KEY AUTOINCREMENT,"
//                + COL_NAME +" TEXT, " + COL_EMAIL + " TEXT)");

        //Make sure you put spaces between SQL statements and Java strings:
        db.execSQL(CREATE_TABLE);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        Log.i("Database upgrade", "Old version:" + oldVersion + " newVersion:"+newVersion);

        //Delete the old table:
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        //Create a new table:
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        Log.i("Database downgrade", "Old version:" + oldVersion + " newVersion:"+newVersion);

        //Delete the old table:
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        //Create a new table:
        onCreate(db);
    }


}
