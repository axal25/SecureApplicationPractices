package backend.app.secureapppractices.dao;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import backend.app.secureapppractices.R;
import backend.app.secureapppractices.model.Comment;

public class ListDataAdapter extends ArrayAdapter {

    private CommentsDao dao;
    List<Comment> mlist;

    public ListDataAdapter(Context context, int resource, List<Comment> list) {
        super(context, resource);
        mlist = list;
        dao = new CommentsDao(getContext());
    }

    static class LayoutHandler {
        TextView row_id, row_commentator, row_comment;
    }

    @Override
    public int getCount() {
        return mlist.size();
    }

    @Override
    public Object getItem(int position) {
        return mlist.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View mview = convertView;
        LayoutHandler layoutHandler;
        if (mview == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mview = layoutInflater.inflate(R.layout.row_comment, parent, false);
            layoutHandler = new LayoutHandler();
            layoutHandler.row_id = mview.findViewById(R.id.row_id);
            layoutHandler.row_commentator = mview.findViewById(R.id.row_commentator);
            layoutHandler.row_comment = mview.findViewById(R.id.row_comment);
            mview.setTag(layoutHandler);
        } else {
            layoutHandler = (LayoutHandler) mview.getTag();
        }
        Comment comment = (Comment) this.getItem(position);
        Button deleteButton = mview.findViewById(R.id.delete_button);
        deleteButton.setTag(comment.getId());
        deleteButton.setOnClickListener(
                v -> {
                    dao.deleteById(String.valueOf(v.getTag()));
                    System.out.println(v.getTag());
                }
        );
        layoutHandler.row_id.setText(comment.getId());
        layoutHandler.row_commentator.setText(comment.getCommentator());
        layoutHandler.row_comment.setText(comment.getComment());
        return mview;
    }
}