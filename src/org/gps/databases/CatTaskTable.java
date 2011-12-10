package org.gps.databases;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class CatTaskTable {
	
	private static String DATABASE_CREATE = 
			"create table catTask (_id integer primary key autoincrement, " +
			"idCategory integer not null, " +
 			"idTask integer not null);";

	public static void onCreate(SQLiteDatabase database){
		database.execSQL(DATABASE_CREATE);
	}
	
	public static void onUpgrade(SQLiteDatabase database, int oldVersion,
			int newVersion) {
		Log.w(CatTaskTable.class.getName(), "Upgrading database from version "
				+ oldVersion + " to " + newVersion
				+ ", which will destroy all old data");
		database.execSQL("DROP TABLE IF EXISTS category");
		onCreate(database);
	}

}
