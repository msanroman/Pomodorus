package org.gps.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class CatTaskDbAdapter {

		// Database fields
		public static final String KEY_ROWID = "_id";
		public static final String KEY_IDCAT = "idCategory";
		public static final String KEY_IDTASK = "idTask";
		private static final String DB_TABLE = "catTask";
		private Context context;
		private SQLiteDatabase db;
		private CatTaskDatabaseHelper dbHelper;

		public CatTaskDbAdapter(Context context) {
			this.context = context;
		}

		public CatTaskDbAdapter open() throws SQLException {
			dbHelper = new CatTaskDatabaseHelper(context);
			db = dbHelper.getWritableDatabase();
			return this;
		}

		public void close() {
			dbHelper.close();
		}
		
		public long createCatTask(long idCategory, long idTask) {
			
			ContentValues values = createContentValues(idTask, idCategory);
			return db.insert(DB_TABLE, null, values);
		}
		
		public boolean deleteCatTask(long idCategory, long idTask) {
			return db.delete(DB_TABLE, KEY_IDCAT + "=" + idCategory + "and" + KEY_IDTASK + "=" + idTask, null) > 0;
		}
		
		public boolean deleteCat(long idCategory) {
			return db.delete(DB_TABLE, KEY_IDCAT + "=" + idCategory, null) > 0;
		}
		
		public boolean deleteTask(long idTask) {
			return db.delete(DB_TABLE, KEY_IDTASK + "=" + idTask, null) > 0;
		}

		/**
		 * Return a Cursor over the list of all todo in the database
		 * 
		 * @return Cursor over all notes
		 */

		public Cursor fetchAllCatTask() {
			return db.query(DB_TABLE, new String[] { KEY_ROWID, KEY_IDTASK, KEY_IDCAT }, null, null, null, null, null);
		}
		
		public Cursor fetchCatTask(long idTask, long idCategory) throws SQLException {
			Cursor mCursor = db.query(true, DB_TABLE, new String[] { KEY_ROWID, KEY_IDCAT, KEY_IDTASK }, KEY_IDCAT + "=" + idCategory 
					+ "and " + KEY_IDTASK + "="	+ idTask, null, null, null, null, null);
			if (mCursor != null) {
				mCursor.moveToFirst();
			}
			return mCursor;
		}
		
		public long[] fetchCat(long idTask) throws SQLException {
			Cursor mCursor = db.query(true, DB_TABLE, new String[] { KEY_ROWID, KEY_IDCAT, KEY_IDTASK }, KEY_IDTASK + "=" + idTask, 
					null, null, null, null, null);
			if (mCursor != null) {
				mCursor.moveToFirst();
			}
			long[] rowIdCat = new long[mCursor.getCount()];
			int i = 0;
			//mCursor.moveToFirst();
			while (!mCursor.isAfterLast()) {
				rowIdCat[i] = mCursor.getLong(1);
				++i;
				mCursor.moveToNext();
			}
			return rowIdCat;
		}
		
		public long[] fetchTask(long idCategory) throws SQLException {
			Cursor mCursor = db.query(true, DB_TABLE, new String[] { KEY_ROWID, KEY_IDCAT, KEY_IDTASK }, KEY_IDCAT + "=" + idCategory, 
					null, null, null, null, null);
			if (mCursor != null) {
				mCursor.moveToFirst();
			}
			long[] rowIdTask = new long[mCursor.getCount()];
			for (int i = 0; !mCursor.isAfterLast(); ++i) {
				rowIdTask[i] = mCursor.getLong(2);
				mCursor.moveToNext();
			}
			return rowIdTask;
		}

		private ContentValues createContentValues(long idTask, long idCategory) {
			ContentValues values = new ContentValues();
			values.put(KEY_IDCAT, idCategory);
			values.put(KEY_IDTASK, idTask);
			return values;
		}
}
