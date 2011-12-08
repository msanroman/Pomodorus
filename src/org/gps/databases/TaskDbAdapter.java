package org.gps.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.provider.Settings.System;

public class TaskDbAdapter {

		// Database fields
		public static final String KEY_ROWID = "_id";
		public static final String KEY_NAME = "name";
		public static final String KEY_DESCRIPTION = "description";
		public static final String KEY_TOTAL_POMODOROS = "total_pomodoros";
		public static final String KEY_REMAINING_POMODOROS = "remaining_pomodoros";
		private static final String DB_TABLE = "tasks";
		private Context context;
		private SQLiteDatabase db;
		private TaskDatabaseHelper dbHelper;

		public TaskDbAdapter(Context context) {
			this.context = context;
		}

		public TaskDbAdapter open() throws SQLException {
			dbHelper = new TaskDatabaseHelper(context);
			db = dbHelper.getWritableDatabase();
			return this;
		}

		public void close() {
			dbHelper.close();
		}
		
		public long createTask(String name, String description, int pomodoros) {
			
			ContentValues values = createContentValues(name, description, pomodoros);
			values.put(KEY_REMAINING_POMODOROS, pomodoros);
			return db.insert(DB_TABLE, null, values);
		}
		
		public boolean updateTask(long rowId, String name, String description, int pomodoros) {
			ContentValues values = createContentValues(name, description, pomodoros);
			values.put(KEY_REMAINING_POMODOROS, pomodoros);
			return db.update(DB_TABLE, values, KEY_ROWID + "=" + rowId, null) > 0;
		}

		public boolean deleteTask(long rowId) {
			return db.delete(DB_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
		}

		/**
		 * Return a Cursor over the list of all todo in the database
		 * 
		 * @return Cursor over all notes
		 */

		public Cursor fetchAllTasks() {
			return db.query(DB_TABLE, new String[] { KEY_ROWID, KEY_NAME, KEY_DESCRIPTION, KEY_TOTAL_POMODOROS, KEY_REMAINING_POMODOROS }, null, null, null, null, null);
		}
		
		public Cursor fetchTask(long rowId) throws SQLException {
			Cursor mCursor = db.query(true, DB_TABLE, new String[] { KEY_ROWID, KEY_NAME, KEY_DESCRIPTION, KEY_TOTAL_POMODOROS, KEY_REMAINING_POMODOROS }, KEY_ROWID + "="
					+ rowId, null, null, null, null, null);
			if (mCursor != null) {
				mCursor.moveToFirst();
			}
			return mCursor;
		}

		private ContentValues createContentValues(String name, String description, int pomodoros) {
			// TODO Auto-generated method stub
			ContentValues values = new ContentValues();
			values.put(KEY_NAME, name);
			values.put(KEY_DESCRIPTION, description);
			values.put(KEY_TOTAL_POMODOROS, pomodoros);
			return values;
		}


	
}
