package backend.app.secureapppractices.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.List;

import backend.app.secureapppractices.model.Comment;

public class CommentsDao {
    CommentsDbHelper dbHelper;
    SQLiteDatabase db;
    boolean instantiated;

    public CommentsDao(Context context) {
        if (instantiated) {
            System.out.println("Already instantiated");
        } else {
            this.dbHelper = new CommentsDbHelper(context);
            this.db = this.dbHelper.getWritableDatabase();
        }
    }

    public CommentsDbHelper getDbHelper() {
        return dbHelper;
    }

    public long insert(String commentator, String comment) {
        ContentValues values = new ContentValues();
        values.put(CommentsContract.CommentsEntry.COLUMN_NAME_COMMENTATOR, commentator);
        values.put(CommentsContract.CommentsEntry.COLUMN_NAME_COMMENT, comment);
        return db.insert(CommentsContract.CommentsEntry.TABLE_NAME, null, values);
    }

    public List<Comment> findAll() {
        String[] projection = {
                BaseColumns._ID,
                CommentsContract.CommentsEntry.COLUMN_NAME_COMMENTATOR,
                CommentsContract.CommentsEntry.COLUMN_NAME_COMMENT
        };

        Cursor cursor = db.query(
                CommentsContract.CommentsEntry.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                null,              // The columns for the WHERE clause
                null,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null               // The sort order
        );

        List<Comment> items = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                long itemId = cursor.getLong(cursor.getColumnIndexOrThrow(CommentsContract.CommentsEntry._ID));
                String commentator, comment;
                commentator = cursor.getString(cursor.getColumnIndex(CommentsContract.CommentsEntry.COLUMN_NAME_COMMENTATOR));
                comment = cursor.getString(cursor.getColumnIndex(CommentsContract.CommentsEntry.COLUMN_NAME_COMMENT));
                Comment item = new Comment(String.valueOf(itemId), commentator, comment);
                System.out.println("Item id: " + itemId + ", commentator: " + commentator + ", comment: " + comment);
                items.add(item);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return items;
    }

    public List<Comment> findByCommentator(String selectedCommentator) {
        String[] projection = {
                BaseColumns._ID,
                CommentsContract.CommentsEntry.COLUMN_NAME_COMMENTATOR,
                CommentsContract.CommentsEntry.COLUMN_NAME_COMMENT
        };

        String selection = CommentsContract.CommentsEntry.COLUMN_NAME_COMMENTATOR + " = ?";
        String[] selectionArgs = {selectedCommentator};

        String sortOrder =
                CommentsContract.CommentsEntry.COLUMN_NAME_COMMENTATOR + " DESC";

        Cursor cursor = db.query(
                CommentsContract.CommentsEntry.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                sortOrder               // The sort order
        );

        List<Comment> items = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                long itemId = cursor.getLong(cursor.getColumnIndexOrThrow(CommentsContract.CommentsEntry._ID));
                String commentator, comment;
                commentator = cursor.getString(cursor.getColumnIndex(CommentsContract.CommentsEntry.COLUMN_NAME_COMMENTATOR));
                comment = cursor.getString(cursor.getColumnIndex(CommentsContract.CommentsEntry.COLUMN_NAME_COMMENT));
                Comment item = new Comment(String.valueOf(itemId), commentator, comment);
                System.out.println("Item id: " + itemId + ", commentator: " + commentator + ", comment: " + comment);
                items.add(item);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return items;
    }

    public long deleteByCommentator(String commentator) {
        String selection = CommentsContract.CommentsEntry.COLUMN_NAME_COMMENTATOR + " LIKE ?";
        String[] selectionArgs = {commentator};
        return db.delete(CommentsContract.CommentsEntry.TABLE_NAME, selection, selectionArgs);
    }

    public long deleteById(String id) {
        String selection = CommentsContract.CommentsEntry._ID + " LIKE ?";
        String[] selectionArgs = {id};
        return db.delete(CommentsContract.CommentsEntry.TABLE_NAME, selection, selectionArgs);
    }

    public long updateByCommentator(String commentator, String comment) {
        ContentValues values = new ContentValues();
        values.put(CommentsContract.CommentsEntry.COLUMN_NAME_COMMENT, comment);

        String selection = CommentsContract.CommentsEntry.COLUMN_NAME_COMMENTATOR + " LIKE ?";
        String[] selectionArgs = {commentator};

        return db.update(
                CommentsContract.CommentsEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs);
    }

    public long updateById(String id, String comment) {
        ContentValues values = new ContentValues();
        values.put(CommentsContract.CommentsEntry.COLUMN_NAME_COMMENT, comment);

        String selection = CommentsContract.CommentsEntry._ID + " LIKE ?";
        String[] selectionArgs = {id};

        return db.update(
                CommentsContract.CommentsEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs);
    }

}
