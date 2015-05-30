package com.inomma.kandu.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class KanduSQLiteHelper extends SQLiteOpenHelper {

	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_LABEL_FIELD = "label_field";
	public static final String COLUMN_DATA = "data";

	private static final String DATABASE_NAME = "submissions.db";
	private static final int DATABASE_VERSION = 1;

	// Database creation sql statement
	private static final String TABLE_CREATE = "create table " + "%s" + "("
			+ COLUMN_ID + " integer primary key autoincrement, "
			+ COLUMN_LABEL_FIELD + " text not null," + COLUMN_DATA
			+ " text not null);";

	public KanduSQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	public void checkCreateTables(String... tables) {
		SQLiteDatabase database = getWritableDatabase();
		for (String table : tables) {

			try {
				database.execSQL(String.format(TABLE_CREATE, table));
				//database.delete(table, null, null);

			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		// database.execSQL(TABLE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(KanduSQLiteHelper.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_CREATE);
		onCreate(db);
	}

}
