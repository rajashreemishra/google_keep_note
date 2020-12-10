package com.example.google_keep_note;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    public final static String TABLE_NAME="task_table";

    public final static String COL_ID="id";
    public final static String COL_TITLE="title";
    public final static String COL_ARRAYITEMS="items";

    public final static String CREATE_TABLE="CREATE TABLE "+TABLE_NAME+"("+COL_ID +" INTEGER PRIMARY KEY AUTOINCREMENT," +COL_TITLE +" TEXT," +COL_ARRAYITEMS +" TEXT)";

    public DatabaseHelper(@Nullable Context context) {
        super(context, "task.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
       db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public void InsertToDatabase(SQLiteDatabase database,Task task){
        ContentValues cv=new ContentValues();
        cv.put(COL_TITLE,task.task_title);
        cv.put(COL_ARRAYITEMS,task.tasklist);
        database.insert(TABLE_NAME,null,cv);
    }
    public ArrayList<Task> RetrieveDataFromDatabase(SQLiteDatabase database){
        ArrayList<Task> task_items=new ArrayList<>();
        Cursor cursor=database.rawQuery("SELECT * FROM "+TABLE_NAME,null);
        if (cursor.moveToFirst()){
            do {
                Task task_event=new Task();
                task_event.id=cursor.getInt(cursor.getColumnIndex(COL_ID));
                task_event.task_title=cursor.getString(cursor.getColumnIndex(COL_TITLE));
                task_event.tasklist=cursor.getString(cursor.getColumnIndex(COL_ARRAYITEMS));
                task_items.add(task_event);
            }while (cursor.moveToNext());
        }

        return task_items;
    }
    public void UpdateItemsToDatabase(SQLiteDatabase database,Task task){
        ContentValues cv=new ContentValues();
        cv.put(COL_TITLE,task.task_title);
        cv.put(COL_ARRAYITEMS,task.tasklist);
        database.update(TABLE_NAME,cv,COL_ID+ "="+task.id,null);
    }
    public void DeleteItemsFromDatabase(Task task,SQLiteDatabase database){
        database.delete(TABLE_NAME,COL_ID + "="+task.id,null);
    }
}
