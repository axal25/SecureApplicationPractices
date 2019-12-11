package backend.app.secureapppractices.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CommentsDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Comments.db";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + CommentsContract.CommentsEntry.TABLE_NAME + " (" +
                    CommentsContract.CommentsEntry._ID + " INTEGER PRIMARY KEY," +
                    CommentsContract.CommentsEntry.COLUMN_NAME_COMMENTATOR + " TEXT," +
                    CommentsContract.CommentsEntry.COLUMN_NAME_COMMENT + " TEXT)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + CommentsContract.CommentsEntry.TABLE_NAME;

    public CommentsDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}