package com.example.install.fitnessclub;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by web on 2017-02-16.
 */

public class DatabaseHandler extends SQLiteOpenHelper {
    /**
     * Track version
     */
    private static final int DATABASE_VERSION = 1;

    /**
     * Name
     */
    private static final String DATABASAE_NAME = "fitnessclub";

    /**
     * create table name
     */
    private static final String TABLE_EXERCISE = "exercise";

    /**
     * column name
     */
    private static final String KEY_ID = "id";

    /**
     * Exercise table name
     */
    private static final String KEY_NAME = "name";
    private static final String KEY_SET = "set";
    private static final String KEY_REP = "rep";

    /**
     * Create statements for the tables
     */

    private static final String CREATE_EXERCISE_TABLE = "CREATE TABLE " + TABLE_EXERCISE + "("
            + KEY_ID + "INTEGER PRIMARY KEY," + KEY_NAME + " TEXT," + KEY_SET + "TEXT, "
            + KEY_REP + "TEXT " + ")";

    public DatabaseHandler(Context context) {
        super(context, DATABASAE_NAME, null, DATABASE_VERSION);
    }

    /**
     * create table inside database
     */
    public void onCreate(SQLiteDatabase db){
        db.execSQL(TABLE_EXERCISE);
    }

    /**
     * when the database update delete the old table and recreate them
     */
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXERCISE);
        onCreate(db);

    }
}
