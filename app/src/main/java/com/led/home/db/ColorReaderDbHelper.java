package com.led.home.db;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import com.led.home.exceptions.DBException;

import java.util.ArrayList;
import java.util.List;

public class ColorReaderDbHelper extends SQLiteOpenHelper {

    private static class ColorEntry implements BaseColumns {
        private static final String TABLE_NAME = "color";
        private static final String COLOR = "color";
        private static final String COLOR_REPRESENTATION = "color_representation";
    }

    private static ColorReaderDbHelper sInstance;

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + ColorEntry.TABLE_NAME + " (" +
                    ColorEntry._ID + " INTEGER PRIMARY KEY," +
                    ColorEntry.COLOR + " INTEGER," +
                    ColorEntry.COLOR_REPRESENTATION + " TEXT)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + ColorEntry.TABLE_NAME;

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "ColorReader.db";

    private ColorReaderDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public static synchronized ColorReaderDbHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new ColorReaderDbHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    public void close() {
        this.close();
    }

    /*
    *************************** CRUD METHODS *********************************
     */

    // Insert a post into the database

    public void addColor(Color color) throws DBException {
        // Create and/or open the database for writing
        SQLiteDatabase db = getWritableDatabase();

        // It's a good idea to wrap our insert in a transaction. This helps with performance and ensures
        // consistency of the database.
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(ColorEntry.COLOR, color.getColor());
            values.put(ColorEntry.COLOR_REPRESENTATION, color.getColorRepresentation());

            // Notice how we haven't specified the primary key. SQLite auto increments the primary key column.
            db.insertOrThrow(ColorEntry.TABLE_NAME, null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            throw new DBException(e.getMessage());
        } finally {
            db.endTransaction();
        }
    }

    public List<Color> getAllColors() throws DBException {
        List<Color> colors = new ArrayList<>();
        String COLORS_SELECT_QUERY =
                String.format("SELECT * FROM %s", ColorEntry.TABLE_NAME);

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(COLORS_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(cursor.getColumnIndex(ColorEntry._ID));
                    int color = cursor.getInt(cursor.getColumnIndex(ColorEntry.COLOR));
                    String colorRepresentation = cursor.getString(cursor.getColumnIndex(ColorEntry.COLOR_REPRESENTATION));
                    colors.add(new Color(id, color, colorRepresentation, false));
                } while(cursor.moveToNext());
            }
        } catch (Exception e) {
            throw new DBException(e.getMessage());
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return colors;
    }

    public void deleteColor(int id) throws DBException {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            // Order of deletions is important when foreign key relationships exist.
            db.delete(ColorEntry.TABLE_NAME, ColorEntry._ID + "=" + id, null);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            throw new DBException(e.getMessage());
        } finally {
            db.endTransaction();
        }
    }
}
