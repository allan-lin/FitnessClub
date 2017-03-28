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
    private static final String TABLE_PICTURES = "picture";
    private static final String TABLE_TRIPS = "trip";
    private static final String TABLE_IMAGELOCATION = "image_location";

    /**
     * column name
     */
    private static final String COLUMN_ID = "id";

    /**
     * Exercise table name
     */
    private static final String KEY_NAME = "name";
    private static final String KEY_SET = "description";
    private static final String KEY_REP = "rep";

    /**
     *Trip Table Column Names
     */
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_LOCATION = "id_location";

    /**
     *Picture Table Column Names
     */
    private static final String COLUMN_RESOURCE = "resource";

    /**
     *image_trip Table Column Names
     */
    private static final String COLUMN_PICTURE = "id_picture";

    /**
     * Create statements for the tables
     */

    private static final String CREATE_EXERCISE_TABLE = "CREATE TABLE " + TABLE_EXERCISE + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT , " + KEY_NAME + " TEXT, " + KEY_SET + " TEXT, "
            + KEY_REP + " TEXT " + ");";

    private static final String CREATE_TRIPS_TABLE = "CREATE TABLE " + TABLE_EXERCISE + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ," + COLUMN_DATE + " TEXT,"
            + COLUMN_LOCATION + " INTEGER REFERENCES " + TABLE_EXERCISE + "(" +COLUMN_ID +")" + ")";

    private static final String CREATE_PICTURES_TABLE = "CREATE TABLE " + TABLE_PICTURES + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ," + COLUMN_RESOURCE + " TEXT" + ")";

    private static final String CREATE_IMAGE_LOCATION_TABLE = "CREATE TABLE " + TABLE_IMAGELOCATION + "("
            + COLUMN_LOCATION + " INTEGER REFERENCES " + TABLE_EXERCISE + "(" +COLUMN_ID +"),"
            + COLUMN_PICTURE + " INTEGER REFERENCES " + TABLE_PICTURES + "(" +COLUMN_ID +")" +")";

    public DatabaseHandler(Context context) {
        super(context, DATABASAE_NAME, null, DATABASE_VERSION);
    }

    /**
     * create table inside database
     */
    public void onCreate(SQLiteDatabase db){
        db.execSQL(CREATE_EXERCISE_TABLE);
        db.execSQL(CREATE_PICTURES_TABLE);
        db.execSQL(CREATE_TRIPS_TABLE);
        db.execSQL(CREATE_IMAGE_LOCATION_TABLE);
    }

    /**
     * when the database update delete the old table and recreate them
     */
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_IMAGELOCATION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRIPS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PICTURES);
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

    //We modified addPicture to return the rowNumber it was added into
    public int addPicture(Picture picture) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_RESOURCE, picture.getResource());
        db.insert(TABLE_PICTURES, null, values);
        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT last_insert_rowid()", null);
        if(cursor.moveToFirst()) {
            int location = Integer.parseInt(cursor.getString(0));
            System.out.println("Record ID " + location);
            db.close();
            return location;
        }
        return -1;
    }

    public void addTrip(Trip trip) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_DATE, trip.getDate());
        values.put(COLUMN_LOCATION, trip.getLocation());
        db.insert(TABLE_TRIPS, null, values);
        db.close();
    }

    //Added a method that will add an image location record
    public void addImageLocation(int image, int location){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_LOCATION, location);
        values.put(COLUMN_PICTURE, image);
        db.insert(TABLE_IMAGELOCATION, null, values);
        db.close();
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
                new String[] {COLUMN_ID, KEY_NAME, KEY_SET, KEY_REP},
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

    public Picture getPicture(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_PICTURES, new String[] {COLUMN_ID, COLUMN_RESOURCE}, COLUMN_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Picture picture = new Picture(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1));

        return picture;
    }

    public ArrayList<Picture> getAllPictures() {
        ArrayList<Picture> pictureList = new ArrayList<Picture>();
        String selectQuery = "SELECT  * FROM " + TABLE_PICTURES;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Picture picture = new Picture();
                picture.setId(Integer.parseInt(cursor.getString(0)));
                picture.setResource(cursor.getString(1));
                pictureList.add(picture);
            } while (cursor.moveToNext());
        }
        return pictureList;
    }

    /**
     * The second getAllPictures is used to grab all images associated with an location
     * @param location
     * @return
     */
    public ArrayList<Picture> getAllPictures(int location) {
        ArrayList<Picture> pictureList = new ArrayList<Picture>();
        String selectQuery = "SELECT  * FROM " + TABLE_IMAGELOCATION + " WHERE " + COLUMN_LOCATION + " = " + location;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                String innerQuery = "SELECT * FROM " + TABLE_PICTURES + " WHERE " + COLUMN_ID + "=" + cursor.getInt(1);
                Cursor innerCursor = db.rawQuery(innerQuery, null);
                if (innerCursor.moveToFirst()) {
                    do {
                        Picture picture = new Picture();
                        picture.setId(Integer.parseInt(innerCursor.getString(0)));
                        picture.setResource(innerCursor.getString(1));
                        pictureList.add(picture);
                    } while (innerCursor.moveToNext());
                }
            }while (cursor.moveToNext());
        }
        return pictureList;
    }

    public Trip getTrip(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_TRIPS, new String[] {COLUMN_ID, COLUMN_DATE, COLUMN_LOCATION}, COLUMN_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Trip trip = new Trip(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), Integer.parseInt(cursor.getString(2)));
        return trip;
    }

    public ArrayList<Trip> getAllTrips() {
        ArrayList<Trip> tripList = new ArrayList<Trip>();
        String selectQuery = "SELECT  * FROM " + TABLE_TRIPS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Trip trip = new Trip();
                trip.setId(Integer.parseInt(cursor.getString(0)));
                trip.setDate(cursor.getString(1));
                trip.setLocation(Integer.parseInt(cursor.getString(2)));
                tripList.add(trip);
            } while (cursor.moveToNext());
        }
        return tripList;
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
        return db.update(TABLE_EXERCISE, values, COLUMN_ID + " = ?",
                new String[]{String.valueOf(exercise.getId())});
    }

    public int updatePicture(Picture picture) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_RESOURCE, picture.getResource());
        return db.update(TABLE_PICTURES, values, COLUMN_ID + " = ?", new String[] { String.valueOf(picture.getId()) });
    }
    public int updateTrip(Trip trip) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_DATE, trip.getDate());
        values.put(COLUMN_LOCATION, trip.getLocation());
        return db.update(TABLE_TRIPS, values, COLUMN_ID + " = ?", new String[] { String.valueOf(trip.getId()) });
    }

    /**
     * DELETE OPERATIONS
     */

    public void deleteExercise(long exercose_id){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_EXERCISE, COLUMN_ID + " = ?",
                new String[]{String.valueOf(exercose_id)});
    }

    public void deletePicture(long picture_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PICTURES, COLUMN_ID + " = ?",
                new String[] { String.valueOf(picture_id) });
    }
    public void deleteTrip(long trip_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TRIPS, COLUMN_ID + " = ?",
                new String[] { String.valueOf(trip_id) });
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
