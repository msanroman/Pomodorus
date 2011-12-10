package org.gps.databases;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CatTaskDatabaseHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "applicationdata_cattask";

	private static final int DATABASE_VERSION = 1;

	public CatTaskDatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Method is called during creation of the database
	@Override
	public void onCreate(SQLiteDatabase database) {
		CatTaskTable.onCreate(database);
	}

	// Method is called during an upgrade of the database,
	// e.g. if you increase the database version
	@Override
	public void onUpgrade(SQLiteDatabase database, int oldVersion,
			int newVersion) {
		CatTaskTable.onUpgrade(database, oldVersion, newVersion);
	}
}
