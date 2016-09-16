package com.sw.vali.noteit;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.sw.vali.noteit.model.Note;
import com.sw.vali.noteit.model.enums.NoteCategory;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Vali on 15-Sep-16.
 */
public class NoteItDbAdapter {

    private static final String DATABASE_NAME = "noteit.db";
    private static final int DATABASE_VERSION = 1;

    public static final String NOTE_TABLE = "note";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_MESSAGE = "message";
    public static final String COLUMN_CATEGORY = "category";
    public static final String COLUMN_DATE = "date";

    private String[] allColumns = {COLUMN_ID, COLUMN_TITLE, COLUMN_MESSAGE, COLUMN_CATEGORY, COLUMN_DATE};

    public static final String CREATE_NOTE_TABLE = "create table " + NOTE_TABLE + " ( "
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_TITLE + " text not null, "
            + COLUMN_MESSAGE + " text not null, "
            + COLUMN_CATEGORY + " text not null, "
            + COLUMN_DATE + ");";

    private SQLiteDatabase sqlDB;
    private Context context;

    private NoteItDbHelper noteItDbHelper;

    public NoteItDbAdapter(Context context) {
        this.context = context;
    }

    public NoteItDbAdapter open() throws android.database.SQLException {
        noteItDbHelper = new NoteItDbHelper(context);

        sqlDB = noteItDbHelper.getWritableDatabase();

        return this;
    }

    public void close() {
        noteItDbHelper.close();
    }

    public Note createNote(String title, String message, NoteCategory noteCategory) {

        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, title);
        values.put(COLUMN_MESSAGE, message);
        values.put(COLUMN_CATEGORY, noteCategory.name());
        values.put(COLUMN_DATE, Calendar.getInstance().getTimeInMillis() + ""); // we're adding '+ ""' to transform the millis in a String

        long insertId = sqlDB.insert(NOTE_TABLE, null, values);

        Cursor cursor = sqlDB.query(NOTE_TABLE, allColumns, COLUMN_ID + " = " + insertId, null, null, null, null);

        cursor.moveToFirst(); // cursor will now be positioned on our only element from the result set
        Note newNote = cursorToNote(cursor);

        cursor.close();

        return newNote;
    }

    public long updateNote(long idToUpdate, String newTitle, String newMessage, NoteCategory newNoteCategory) {

        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, newTitle);
        values.put(COLUMN_MESSAGE, newMessage);
        values.put(COLUMN_CATEGORY, newNoteCategory.name());
        values.put(COLUMN_DATE, Calendar.getInstance().getTimeInMillis() + "");

        return sqlDB.update(NOTE_TABLE, values, COLUMN_ID + " = " + idToUpdate, null);
    }

    public ArrayList<Note> getAllNotes() {
        ArrayList<Note> notes = new ArrayList<>();

        // grab all of the information in our database for the notes in it
        Cursor cursor = sqlDB.query(NOTE_TABLE, allColumns, null, null, null, null, null);

        // loop through all of the rows (notes) in our database and create new note objects from those
        // rows and add them to our array list
        for(cursor.moveToLast(); !cursor.isBeforeFirst(); cursor.moveToPrevious()) { // adding from back to front => the most recent will be displayed first
            Note note = cursorToNote(cursor);
            notes.add(note);
        }

        cursor.close();

        return notes;
    }

    private Note cursorToNote(Cursor cursor) {

        return new Note(cursor.getLong(0), cursor.getString(1),
                cursor.getString(2), NoteCategory.valueOf(cursor.getString(3)),
                cursor.getLong(4));
    }

    private static class NoteItDbHelper extends SQLiteOpenHelper {

        NoteItDbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            // create Note table
            sqLiteDatabase.execSQL(CREATE_NOTE_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
            Log.w(NoteItDbHelper.class.getName(), "Upgrading database from version " + oldVersion
                    + " to " + newVersion + ", which will destroy all old data");

            // destroys data
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + NOTE_TABLE);

            onCreate(sqLiteDatabase);
        }
    }
}
