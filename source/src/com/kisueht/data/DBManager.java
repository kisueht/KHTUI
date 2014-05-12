package com.kisueht.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DBManager {
	
	private DbManager_DataBaseHelper mDbAdapter_DataBaseHelper = null;
	private SQLiteDatabase db = null;
	private DBManagerCallback dbManagerCallback = null;

	public DBManager(Context context, DBManagerCallback dbManagerCallback) {
		this.dbManagerCallback = dbManagerCallback;
		mDbAdapter_DataBaseHelper = new DbManager_DataBaseHelper(context, this.dbManagerCallback.getName(), null, this.dbManagerCallback.getVersion());
	}
	
	public DBManager open() {
		db = mDbAdapter_DataBaseHelper.getWritableDatabase();
		return this;
	}
	
	public void close() {
		mDbAdapter_DataBaseHelper.close();
	}
	
	public SQLiteDatabase getDatabase() {
		return db;
	}
	
	
	private class DbManager_DataBaseHelper extends SQLiteOpenHelper {

		public DbManager_DataBaseHelper(Context context, String name,
				CursorFactory factory, int version) {
			super(context, name, factory, version);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			dbManagerCallback.onCreate(db);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			dbManagerCallback.onUpgrade(db, oldVersion, newVersion);
		}
		
	}
	
	
	public static interface DBManagerCallback {
		public String getName();
		
		public int getVersion();
		
		public void onCreate(SQLiteDatabase db);
		
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion);
	}

}