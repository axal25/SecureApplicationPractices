package backend.app.secureapppractices.dao;

import android.provider.BaseColumns;

public class CommentsContract {

    private CommentsContract() {}

    public static class CommentsEntry implements BaseColumns {
        public static final String TABLE_NAME = "comments";
        public static final String COLUMN_NAME_COMMENTATOR = "commentator";
        public static final String COLUMN_NAME_COMMENT = "comment";
    }
}