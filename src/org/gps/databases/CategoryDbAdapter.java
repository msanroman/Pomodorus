package org.gps.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class CategoryDbAdapter {

		// Database fields
		public static final String KEY_ROWID = "_id";
		public static final String KEY_NAME = "name";
		private static final String DB_TABLE = "category";
		private Context context;
		private SQLiteDatabase db;
		private CategoryDatabaseHelper dbHelper;

		public CategoryDbAdapter(Context context) {
			this.context = context;
		}

		public CategoryDbAdapter open() throws SQLException {
			dbHelper = new CategoryDatabaseHelper(context);
			db = dbHelper.getWritableDatabase();
			return this;
		}

		public void close() {
			dbHelper.close();
		}
		
		public long createCategory(String name) {
			
			ContentValues values = createContentValues(name);
			return db.insert(DB_TABLE, null, values);
		}
		
		public boolean deleteCategory(long rowId) {
			return db.delete(DB_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
		}

		/**
		 * Return a Cursor over the list of all todo in the database
		 * 
		 * @return Cursor over all notes
		 */

		public Cursor fetchAllCategories() {
			return db.query(DB_TABLE, new String[] { KEY_ROWID, KEY_NAME }, null, null, null, null, null);
		}
		
		public Cursor fetchCategories(long[] rowId) {
			String rowId_aux = "";
			for(int i = 0; i < rowId.length - 1; ++i) rowId_aux += String.valueOf(rowId[i]) + ", ";
			if (rowId.length > 0) rowId_aux += String.valueOf(rowId[rowId.length - 1]);
			return db.query(DB_TABLE, new String[] { KEY_ROWID, KEY_NAME }, KEY_ROWID + " IN(" + rowId_aux + ")", null, null, null, null);
		}
		
		public Cursor fetchCategory(long rowId) throws SQLException {
			Cursor mCursor = db.query(true, DB_TABLE, new String[] { KEY_ROWID, KEY_NAME }, KEY_ROWID + "="
					+ rowId, null, null, null, null, null);
			if (mCursor != null) {
				mCursor.moveToFirst();
			}
			return mCursor;
		}

		private ContentValues createContentValues(String name) {
			ContentValues values = new ContentValues();
			values.put(KEY_NAME, name);
			return values;
		}

        public boolean updateCategory(long id, String name) {
            ContentValues values = createContentValues(name);
            return db.update(DB_TABLE, values, KEY_ROWID + "=" + id, null) > 0;            
        }

	
}
