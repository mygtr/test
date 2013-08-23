package com.uestc.newone.db;


import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
//import android.database.DatabaseErrorHandler;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.provider.BaseColumns;

public class DatabaseHelper extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "jobs.db";
	private static final int DATABASE_VERSION = 1;
	public static final String TABLE_NAME = "jobs";
	public static final String ID = BaseColumns._ID;
	public static final String NAME = "name";
	public static final String WORK_TIME = "work_time";

	public DatabaseHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}
	
	public DatabaseHelper(Context context){
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		
	}

//	public DatabaseHelper(Context context, String name, CursorFactory factory,
//			int version, DatabaseErrorHandler errorHandler) {
//		super(context, name, factory, version, errorHandler);
//		// TODO Auto-generated constructor stub
//	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String sql = "CREATE TABLE " + TABLE_NAME + " (" 
		+ ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
		+ NAME + " TEXT NOT NULL, "
		+ WORK_TIME + " INTEGER" + ");";
		
		db.execSQL(sql);
		

	} 
	
	//更新数据库的方式。。。
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE IF EXISTS" + TABLE_NAME);
		onCreate(db);

	}
	
	public void insert(String name, int workTime ){
		SQLiteDatabase db = getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(NAME, name);
		values.put(WORK_TIME, workTime);
		
		db.insertOrThrow(TABLE_NAME, null, values);
		
	}
	
	public int delete(int id){
		SQLiteDatabase db = getWritableDatabase();
		String[] args = {String.valueOf(id)};
		int flag = -1;
		try{
			flag = db.delete(TABLE_NAME, "_ID=?", args); 
		}
		catch(SQLException e){
			flag = 0;
			System.out.println(e);
		}
		System.out.println("get int the delete funciton ,the id = " + String.valueOf(id) + " flag = " + String.valueOf(flag));
		
		return flag;
		
		
//		return db.delete(TABLE_NAME, "ID=?", args);
        
	}
	
	public Cursor all(Activity activity){
		String[] from = {ID,NAME,WORK_TIME};
		String orderBy = NAME;
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.query(TABLE_NAME, from, null, null, null, null, orderBy);
		//activity.startManagingCursor(cursor);
		
		return cursor;
		
	}
	
	//计算表中数据的行数。。。
	public long count(){
		SQLiteDatabase db = getReadableDatabase();
		return DatabaseUtils.queryNumEntries(db, TABLE_NAME);
	}
	

}
