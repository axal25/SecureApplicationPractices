package backend.app.secureapppractices.ui.main.courses;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public abstract class CourseFragment extends Fragment {
    private int layout_id;

    public CourseFragment(int layout_id) {
        this.layout_id = layout_id;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(this.layout_id, container, false);
    }
}
