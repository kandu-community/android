package com.inomma.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

public class ContentUtils {

	public static List<Uri> getImagesFromDir(File dir, ContentResolver resolver) {
		return getImagesFromDir(dir.getAbsolutePath(), resolver);
	}

	public static List<Uri> getImagesFromDir(String dirPath, ContentResolver resolver) {
		String[] projection = { MediaStore.Images.Thumbnails.DATA };
		Cursor cursor = resolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, MediaStore.Images.Media.DATA + " like ? ",
				new String[] { dirPath + "/%" }, null);

		cursor.moveToFirst();
		List<Uri> files = new ArrayList<Uri>();
		for (int i = 1; i < cursor.getCount(); i++) {

			int columnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
			files.add(Uri.parse(cursor.getString(columnIndex)));
			cursor.moveToNext();
		}

		cursor.close();

		return files;
	}

	/**
	 * 
	 * @param dir
	 * @param resolver
	 * @param size use sizes of MediaStore.Images.Thumbnails class
	 * @return
	 */
	public static Bitmap getImageThumbnailFromFile(File dir, ContentResolver resolver, int size) {
		return getImageThumbnailFromFile(dir.getAbsolutePath(), resolver, size);
	}

	/**
	 * 
	 * @param dir
	 * @param resolver
	 * @param size use sizes of MediaStore.Images.Thumbnails class
	 * @return
	 */
	public static Bitmap getImageThumbnailFromFile(String dirPath, ContentResolver resolver, int size) {
		Cursor cursor = resolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[] { MediaStore.MediaColumns._ID }, MediaStore.MediaColumns.DATA
				+ "=?", new String[] { dirPath }, null);
		
		Bitmap result = null;
		if (cursor != null && cursor.moveToFirst()) {
			int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
			result = MediaStore.Images.Thumbnails.getThumbnail(resolver, id, size, null);
		}

		cursor.close();
		return result;
	}

}
