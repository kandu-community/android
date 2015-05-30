package com.inomma.kandu.sqlite;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Comment;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.inomma.kandu.model.Form;
import com.inomma.kandu.model.FormSubmission;

public class SubmissionsDataSource {

	// Database fields
	private SQLiteDatabase database;
	private KanduSQLiteHelper dbHelper;
	private String[] allColumns = { KanduSQLiteHelper.COLUMN_ID,
			KanduSQLiteHelper.COLUMN_LABEL_FIELD, KanduSQLiteHelper.COLUMN_DATA };

	public SubmissionsDataSource(Context context) {
		dbHelper = new KanduSQLiteHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void init(String... cacheForms){
		dbHelper.checkCreateTables(cacheForms);

	}
	public void close() {
		dbHelper.close();
	}

	public void addSubmission(FormSubmission submission) {
		ContentValues values = new ContentValues();
		values.put(KanduSQLiteHelper.COLUMN_LABEL_FIELD, submission.toString());
		try {
			values.put(KanduSQLiteHelper.COLUMN_DATA, submission.toJson()
					.toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}

		long insertId = database.insert(submission.getForm().getKey(), null,
				values);
		System.out.println(insertId + "");
		return;
	}

	public void addSubmissions(List<FormSubmission> submissions) {
		if(submissions.size()>0){
			deleteAllSubmissions(submissions.get(0).getForm().getKey());
		}
		for (FormSubmission submission : submissions) {
			this.addSubmission(submission);
		}
	}

	public void deleteAllSubmissions(String table) {
		database.delete(table, null, null);
	}

	public List<FormSubmission> getAllSubmissions(Form form,String searchText) {
		List<FormSubmission> formSubmissions = new ArrayList<FormSubmission>();

		Cursor cursor = database.query(form.getKey(), allColumns, searchText!=null?KanduSQLiteHelper.COLUMN_LABEL_FIELD+" LIKE '%"+searchText+"%'":null, null,
				null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			FormSubmission comment = cursorToSubmission(cursor);
			formSubmissions.add(comment);
			cursor.moveToNext();
		}
		// make sure to close the cursor
		cursor.close();
		return formSubmissions;
	}

	private FormSubmission cursorToSubmission(Cursor cursor) {
		FormSubmission formSubmission = new FormSubmission();
		try {
			formSubmission.fromJson(new JSONObject(cursor.getString(2)));
			formSubmission.setDbId(cursor.getInt(0));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return formSubmission;
	}
	
	public void removeSubmission(FormSubmission formSubmission){
		database.delete(formSubmission.getForm().getKey(), KanduSQLiteHelper.COLUMN_ID+"="+formSubmission.getDbId(), null);
	}
}
