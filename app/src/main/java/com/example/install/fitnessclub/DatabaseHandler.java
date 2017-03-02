package com.example.install.fitnessclub;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by web on 2017-02-16.
 */

public class DatabaseHandler extends SQLiteOpenHelper {
    /**
     * Track version
     */
    private static final int DATABASE_VERSION = 6;

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
    private static final String KEY_SET = "description";
    private static final String KEY_REP = "rep";

    /**
     * Create statements for the tables
     */

    private static final String CREATE_EXERCISE_TABLE = "CREATE TABLE " + TABLE_EXERCISE + "("
            + KEY_ID + " INTEGER PRIMARY KEY, " + KEY_NAME + " TEXT, " + KEY_SET + " TEXT, "
            + KEY_REP + " TEXT " + ");";

    public DatabaseHandler(Context context) {
        super(context, DATABASAE_NAME, null, DATABASE_VERSION);
    }

    /**
     * create table inside database
     */
    public void onCreate(SQLiteDatabase db){
        db.execSQL(CREATE_EXERCISE_TABLE);
    }

    /**
     * when the database update delete the old table and recreate them
     */
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXERCISE);
        onCreate(db);

    }

    public void addExercise(Exercise exercise){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, exercise.getName());
        values.put(KEY_SET, exercise.getDescription());
        values.put(KEY_REP, exercise.getLocation());
        db.insert(TABLE_EXERCISE, null, values);
    }

    public Exercise getExercise(int id){
        /**
         * Create a readable database
         */
        SQLiteDatabase db = this.getReadableDatabase();
        /**
         * Create a cursor
         * (which is able to move through and access database records)
         * Have it store all the records retrieved from the db.query()
         * cursor starts by pointing at record 0
         * Databases do not have a record 0
         * we use cursor.moveToFirst() to have it at the first record returned
         */
        Cursor cursor = db.query(TABLE_EXERCISE,
                new String[] {KEY_ID, KEY_NAME, KEY_SET, KEY_REP},
                "=?", new String[]{String.valueOf(id)}, null, null, null, null);
        if(cursor != null)
            cursor.moveToFirst();
        /**
         * We create a location object using the cursor record
         */
        Exercise exercise = new Exercise(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2), cursor.getString(3));
        return exercise;
    }

    public ArrayList<Exercise> getAllExercises(){
        ArrayList<Exercise> ExerciseList = new ArrayList<Exercise>();
        String selectQuery = "SELECT * FROM " + TABLE_EXERCISE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if(cursor.moveToFirst()){
            do{
                Exercise exercise = new Exercise();
                exercise.setId(Integer.parseInt(cursor.getString(0)));
                exercise.setName(cursor.getString(1));
                exercise.setDescription(cursor.getString(2));
                exercise.setLocation(cursor.getString(3));
                ExerciseList.add(exercise);

            } while(cursor.moveToNext());
        }
        return  ExerciseList;
    }


    /**
     * UPDATE OPERATIONS
     */

    public int updateExercise(Exercise exercise){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, exercise.getName());
        values.put(KEY_SET, exercise.getDescription());
        values.put(KEY_REP, exercise.getLocation());
        return db.update(TABLE_EXERCISE, values, KEY_ID + " = ?",
                new String[]{String.valueOf(exercise.getId())});
    }
    /**
     * DELETE OPERATIONS
     */

    public void deleteExercise(long exercose_id){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_EXERCISE, KEY_ID + " = ?",
                new String[]{String.valueOf(exercose_id)});
    }

    /**
     * Closing the database connection
     */
    public void closeDB(){
        SQLiteDatabase db =this.getReadableDatabase();
        if(db != null && db.isOpen()){
            db.close();
        }
    }
}
