package org.gps.databases;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class TaskTable {
	
	private static String DATABASE_CREATE = 
			"create table tasks (_id integer primary key autoincrement, " +
			"name text not null, description text not null, " +
			"total_pomodoros integer not null, " +
			"remaining_pomodoros integer not null, " +
			"finished boolean not null);";

	public static void onCreate(SQLiteDatabase database){
		database.execSQL(DATABASE_CREATE);
	}
	
	public static void onUpgrade(SQLiteDatabase database, int oldVersion,
			int newVersion) {
		Log.w(TaskTable.class.getName(), "Upgrading database from version "
				+ oldVersion + " to " + newVersion
				+ ", which will destroy all old data");
		database.execSQL("DROP TABLE IF EXISTS tasks");
		onCreate(database);
	}

}
