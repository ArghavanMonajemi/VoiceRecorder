package com.android.voicerecorder.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.android.voicerecorder.model.Record;

import java.util.ArrayList;
import java.util.List;

public class SqliteDatabase extends SQLiteOpenHelper {

    Context context;
    private static final String DATABASE_NAME = "voiceRecorderDb";
    private static final int DATABASE_VERSION = 1;

    private final String RECORD_TABLE_NAME = "records";
    final static String RECORD_COL_ID = "id";              //0
    final String RECORD_COL_NAME = "name";                 //1
    final String RECORD_COL_DATE = "date";                 //2
    final String RECORD_COL_LIKE = "liked";                //3
    final String RECORD_COL_PATH = "link";                 //4

    public SqliteDatabase(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String SQL_COMMAND_CREATE_RECORD_TABLE = "CREATE TABLE IF NOT EXISTS " + RECORD_TABLE_NAME + "(" +
                RECORD_COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                RECORD_COL_NAME + " TEXT, " +
                RECORD_COL_DATE + " TEXT, " +
                RECORD_COL_LIKE + " INTEGER, " +
                RECORD_COL_PATH + " TEXT);";
        sqLiteDatabase.execSQL(SQL_COMMAND_CREATE_RECORD_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + RECORD_TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public void insertRecord(Record record) {
        ContentValues cv = new ContentValues();
        cv.put(RECORD_COL_NAME, record.getName());
        cv.put(RECORD_COL_DATE, record.getDate());
        cv.put(RECORD_COL_LIKE, 0);
        cv.put(RECORD_COL_PATH, record.getPath());
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.insert(RECORD_TABLE_NAME, null, cv);
    }

    public void updateRecord(Record record) {
        ContentValues cv = new ContentValues();
        cv.put(RECORD_COL_NAME, record.getName());
        cv.put(RECORD_COL_LIKE, 0);
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.update(RECORD_TABLE_NAME, cv, RECORD_COL_ID + " = ?", new String[]{record.getId()});
    }

    public List<Record> getAllRecords() {
        List<Record> recordList = new ArrayList<>();
        String query = "SELECT " + RECORD_COL_ID + "," + RECORD_COL_NAME + "," + RECORD_COL_DATE + "," + RECORD_COL_LIKE + "," + RECORD_COL_PATH + " FROM " + RECORD_TABLE_NAME;
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            while (!cursor.isAfterLast()) {
                recordList.add(new Record(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getInt(3) == 1, cursor.getString(4)));
                cursor.moveToNext();
            }
        }
        cursor.close();
        sqLiteDatabase.close();
        return recordList;
    }


    public void deleteRecord(String id) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.delete(RECORD_TABLE_NAME, RECORD_COL_ID + " = ?", new String[]{id});
        sqLiteDatabase.close();
    }
}
