package backend.app.secureapppractices.ui.main.courses.comments;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.util.List;

import backend.app.secureapppractices.R;
import backend.app.secureapppractices.dao.CommentsDao;
import backend.app.secureapppractices.dao.ListDataAdapter;
import backend.app.secureapppractices.model.Comment;

public class CommentsFragment extends Fragment implements AdapterView.OnItemClickListener {

    private CommentsDao dao;
    private List<Comment> mPersonList;
    private ListDataAdapter mListDataAdapter;
    private Button mButton;
    private EditText mEditCommentator;
    private EditText mEditComment;
    private LayoutInflater inflater;
    private View view;
    private ViewGroup container;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.inflater = inflater;
        this.container = container;
        view = inflater.inflate(R.layout.fragment_comments, container, false);
        ListView listView = view.findViewById(R.id.lv_comment);
        dao = new CommentsDao(getContext());
        updateListView(listView);

        mButton = view.findViewById(R.id.button);
        mEditCommentator = view.findViewById(R.id.edit_text_commentator);
        mEditComment = view.findViewById(R.id.edit_text_comment);

        mButton.setOnClickListener(view1 -> {
            dao.insert(mEditCommentator.getText().toString(), mEditComment.getText().toString());
            updateListView(listView);
        });

        return view;
    }

    private void updateListView(ListView listView) {
        mPersonList = dao.findAll();
        mListDataAdapter = new ListDataAdapter(getContext(), R.layout.row_comment, mPersonList);
        listView.setAdapter(mListDataAdapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(getActivity(), "Item: " + position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            view = inflater.inflate(R.layout.fragment_comments_landscape, container, false);
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            view = inflater.inflate(R.layout.fragment_comments, container, false);
        }
    }

}
