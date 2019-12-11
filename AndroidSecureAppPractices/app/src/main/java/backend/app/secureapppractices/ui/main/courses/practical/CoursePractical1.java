package backend.app.secureapppractices.ui.main.courses.practical;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

import backend.app.secureapppractices.R;
import backend.app.secureapppractices.model.Course;
import backend.app.secureapppractices.ui.main.courses.CustomAsyncTask;

public class CoursePractical1 extends PracticalCourseFragment {
    private View rootView;
    private TextView textView1;
    private TextView textView2;
    private TextInputLayout textInputLayout_query1;
    private TextInputLayout textInputLayout_query2;
    private TextInputEditText textInputEditText_query1;
    private TextInputEditText textInputEditText_query2;
    private EditText editText1;
    private EditText editText2;
    private Button runBtn1;
    private Button runBtn2;
    private Button prevBtn;
    private Button nextBtn;

    private UUID safeCourseUUID = null;
    private UUID unSafeCourseUUID = null;
    private int counter = 0;
//    private String localhost = "localhost";
//    private String localhost = "192.168.0.54"; // ifconfig
    private String localhost = "10.0.2.2";

    private String[][] descriptions = new String[][]{
            {
                "Let's start off easy. This are examples of 2 GET request for 2 different APIs. 1st API is secure." +
                        " secure API is using PostgreSQL user \"safe\" with privileges to \"safe\" schema, and table inside this schema called \"courses\"." +
                        " We address the table \"safe.courses\". Try using this API by running given query.",
                "Now let's try something else. We'll ask APIs for only one of the courses using as filter course's ID in form of UUID. " +
                        "The given ID will be converted and verified as soon as it reaches the API's controller.",
                "Now we will achieve the same effect but... We'll ask APIs for only one of the courses using as filter course's ID in form of STRING. " +
                        "The given ID will NOT be converted and verified as soon as it reaches the API's controller." +
                        "The ID will only be converted to UUID on the database side and only in case of user privileged to use \"safe.courses\" table. So from here.",
                "Now we will do the same thing as in the first example but in a different way. " +
                        "As you can see we have a query containing PostgreSQL statement. " +
                        "This should never be allowed but we creating such endpoint to illustrate what could happen. " +
                        "This EXACT request is safe and will result in same outcome as in the first examples.",
                "Now we will use the same endpoint as in last example but we are changing the PostgreSQL statement. " +
                        "This statement DESTROYS our precious table. " +
                        "But in this case it will be stopped because of database user privileges and will result in error."
            },
            {
                "2nd API is unSecure. unSecure API is using PostgreSQL user \"unsafe\" with privileges to \"unsafe\" schema, and table inside this schema called \"courses\"." +
                        " We address the table as \"unsafe.courses\". Try using this API by running given query.",
                "SAME MESSAGE WARNING: Now let's try something else. We'll ask APIs for only one of the courses using as filter course's ID in form of UUID. " +
                        "The given ID will be converted and verified as soon as it reaches the API's controller.",
                "Here ID will not be converted to UUID because \"unsafe.courses\" table have different structure. " +
                        "It's IDs are stored as STRING and are NOT verified in any way. " +
                        "So we could have course with ID equal to \"I'M NOT id ESPECIALLY NOT UUID, you've been tricked\".",
                "This EXACT request is safe and will result in same outcome as in the first examples.",
                "This statement DESTROYS our precious table. " +
                    "In this case it will NOT be stopped because database user is owner of the table. " +
                    "Privileges will not save us."
            }
    };

    private String[][] queries = new String[][]{
            {
                "http://" + localhost + ":8080/secureApi/courses/",
                "http://" + localhost + ":8080/secureApi/courses/asUUID/",
                "http://" + localhost + ":8080/secureApi/courses/asString/",
                "http://" + localhost + ":8080/secureApi/courses/runQueryGetsResultsFromDatabase?query=SELECT * FROM safe.courses;",
                "http://" + localhost + ":8080/secureApi/courses/runQueryGetsResultsFromDatabase?query=DROP TABLE IF EXISTS safe.courses;"
            },
            {
                "http://" + localhost + ":8080/unSecureApi/courses/",
                "http://" + localhost + ":8080/unSecureApi/courses/asUUID/",
                "http://" + localhost + ":8080/unSecureApi/courses/asString/",
                "http://" + localhost + ":8080/unSecureApi/courses/runQueryGetsResultsFromDatabase?query=SELECT * FROM unsafe.courses;",
                "http://" + localhost + ":8080/unSecureApi/courses/runQueryGetsResultsFromDatabase?query=DROP TABLE IF EXISTS unsafe.courses;"
            }
    };

    private String[][] messages = new String[][]{
            {
                "Don't be scared, it's just a list of items from the previously mentioned table. Just scroll down.",
                "Now you should be able to see course data in json string format.",
                "Now you should be able to see course data in json string format.",
                "Don't be scared, it's just a list of items from the previously mentioned table. Just scroll down.",
                "Don't worry, our table is still standing. " +
                    "This is just an error message from API stating we don't have ownership of the table."
            },
            {
                "Don't be scared, it's just a list of items from the previously mentioned table. Just scroll down.",
                "Now you should be able to see course data in json string format.",
                "Now you should be able to see course data in json string format.",
                "Don't be scared, it's just a list of items from the previously mentioned table. Just scroll down.",
                "Say good bye to our little table. Now you can't count on unSecure API. " +
                    "It don't have the table it needs. You have to wait for database wipe."
            }
    };

    public CoursePractical1() {
        super(R.layout.fragment_course_1_practical);
    }

    public CoursePractical1(int counter) {
        super(R.layout.fragment_course_1_practical);
        this.counter = counter;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.rootView = super.onCreateView(inflater, container, savedInstanceState);
        initReferences();
        makeTextView1();
        makeTextView2();
        makeTextInputEditText_query1();
        makeTextInputEditText_query2();
        makeRunBtn1();
        makeRunBtn2();
        makeNextBtn();
        makePrevBtn();
        getAndSetUUIDsIfNeeded();
        return this.rootView;
    }

    private void initReferences() {
        if(this.rootView!=null) {
            this.textView1 = (TextView) this.rootView.findViewById(R.id.textView1);
            this.textInputLayout_query1 = (TextInputLayout) this.rootView.findViewById(R.id.textInputLayout_query1);
            this.textInputEditText_query1 = (TextInputEditText) this.rootView.findViewById(R.id.textInputEditText_query1);
            this.editText1 = (EditText) this.rootView.findViewById(R.id.editTextResult1);
            this.runBtn1 = (Button) this.rootView.findViewById(R.id.runBtn1);

            this.textView2 = (TextView) this.rootView.findViewById(R.id.textView2);
            this.textInputLayout_query2 = (TextInputLayout) this.rootView.findViewById(R.id.textInputLayout_query2);
            this.textInputEditText_query2 = (TextInputEditText) this.rootView.findViewById(R.id.textInputEditText_query2);
            this.editText2 = (EditText) this.rootView.findViewById(R.id.editTextResult2);
            this.runBtn2 = (Button) this.rootView.findViewById(R.id.runBtn2);

            this.nextBtn = (Button) this.rootView.findViewById(R.id.nextBtn);
            this.prevBtn = (Button) this.rootView.findViewById(R.id.prevBtn);
        }
    }

    private void makeRunBtn1() {
        if(this.runBtn1!=null){
            this.runBtn1.setOnClickListener(v -> {
                String url = ((EditText) this.textInputEditText_query1).getText().toString();

                if(this.counter >= 0 && this.counter < getMinLength() && this.counter < messages[0].length) alert(messages[0][this.counter]);
                else alert("Going to make a request via asyncTask for address: " + url);
                makeAsyncRequest(url, R.id.editTextResult1);
                if(this.counter >= 0 && this.counter < getMinLength() && this.counter < messages[0].length) alert(messages[0][this.counter]);
                else alert("Did make a request via asyncTask for address: " + url);

                this.editText1.setText("Waiting for response...");
            });
        }
        else {
            alert("this.runBtn1 == null");
        }
    }

    private void makeRunBtn2() {
        if(this.runBtn2!=null){
            this.runBtn2.setOnClickListener(v -> {
                String url = ((EditText) this.textInputEditText_query2).getText().toString();

                if(this.counter >= 0 && this.counter < getMinLength() && this.counter < messages[1].length) alert(messages[1][this.counter]);
                else alert("Going to make a request via asyncTask for address: " + url);
                makeAsyncRequest(url, R.id.editTextResult2);
                if(this.counter >= 0 && this.counter < getMinLength() && this.counter < messages[1].length) alert(messages[1][this.counter]);
                else alert("Did make a request via asyncTask for address: " + url);

                this.editText2.setText("Waiting for response...");
            });
        }
        else {
            alert("this.runBtn2 == null");
        }
    }

    private void makeNextBtn() {
        if(this.nextBtn!=null){
            this.nextBtn.setOnClickListener(v -> {
                if(this.counter < getMinLength()) openNewFragment(new CoursePractical1(this.counter+1));
            });
        }
        else {
            alert("this.nextBtn == null");
        }
    }

    private void makePrevBtn() {
        if(this.prevBtn!=null){
            this.prevBtn.setOnClickListener(v -> {
                if(this.counter > 0) openNewFragment(new CoursePractical1(this.counter-1));
            });
        }
        else {
            alert("this.nextBtn == null");
        }
    }

    private void makeTextView1() {
        if(this.counter >=0 && this.counter < getMinLength()) this.textView1.setText(this.descriptions[0][this.counter]);
        else this.textView1.setText(this.textView1.getText() + " Page " + (this.counter + 1));
    }

    private void makeTextView2() {
        if(this.counter >=0 && this.counter < getMinLength()) this.textView2.setText(this.descriptions[1][this.counter]);
        else this.textView2.setText(this.textView2.getText() + " Page " + (this.counter + 1));
    }

    private void makeTextInputEditText_query1() {
        if(this.counter >= 0 && this.counter < getMinLength()) this.textInputEditText_query1.setText( this.queries[0][this.counter] );
    }

    private void makeTextInputEditText_query2() {
        if(this.counter >= 0 && this.counter < getMinLength()) this.textInputEditText_query2.setText( this.queries[1][this.counter] );
    }

    private int getMinLength() {
        int minLength = Integer.MAX_VALUE;
        if(this.queries[0].length < minLength) minLength = this.queries[0].length;
        if(this.queries[1].length < minLength) minLength = this.queries[1].length;
        if(this.descriptions[0].length < minLength) minLength = this.descriptions[0].length;
        if(this.descriptions[1].length < minLength) minLength = this.descriptions[1].length;
        return minLength;
    }

    private void makeAsyncRequest(String url, int elementId) {
        new CustomAsyncTask(getActivity(), this.rootView, elementId, url).execute("is this Objects[]? 1", "is this Objects[]? 2");
    }

    private void alert(String message) {
        Toast.makeText(this.rootView.getContext(), message, Toast.LENGTH_LONG).show();
    }

    private void openNewFragment(Fragment fragmentInstance) {
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragmentInstance).commit();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void getAndSetUUIDsIfNeeded() {
        if(this.counter==1 || this.counter==2) {
            String stringListOfSafeCourses = makeSyncRequestOnSeperateThread(queries[0][0]);
            String stringListOfUnSafeCourses = makeSyncRequestOnSeperateThread(queries[1][0]);
            System.out.println("stringListOfSafeCourses: " + stringListOfSafeCourses);
            System.out.println("stringListOfUnSafeCourses: " + stringListOfUnSafeCourses);
            List<Course> listOfSafeCourses = jsonStringToListOfCourses(stringListOfSafeCourses);
            List<Course> listOfUnSafeCourses = jsonStringToListOfCourses(stringListOfUnSafeCourses);
            this.safeCourseUUID = listOfSafeCourses.get(0).getId();
            this.unSafeCourseUUID = listOfUnSafeCourses.get(0).getId();
            this.textInputEditText_query1.setText(this.textInputEditText_query1.getText().toString() + this.safeCourseUUID);
            this.textInputEditText_query2.setText(this.textInputEditText_query2.getText().toString() + this.unSafeCourseUUID);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private String makeSyncRequestOnSeperateThread(String stringUrl) {
        final String[] responseString = new String[1];
        final CountDownLatch latch = new CountDownLatch(1);
        Thread thread = new Thread(() -> {
            responseString[0] = makeSyncRequest(stringUrl);
            latch.countDown();
        });
        thread.start();
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
            try {
                thread.join();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
        return responseString[0];
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private String makeSyncRequest(String stringUrl) {
        String responseString = null;
        try {
            URL url = new URL(stringUrl);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = new BufferedInputStream(httpURLConnection.getInputStream());
            responseString = inputStreamToString(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
            responseString = e.toString();
        }
        System.out.println("doInBackground(...): responseString: "+ responseString);
        return responseString;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private String inputStreamToString(InputStream inputStream) {
        return new BufferedReader(new InputStreamReader(inputStream)).lines().collect(Collectors.joining("\n"));
    }

    private List<Course> jsonStringToListOfCourses(String stringListOfCourses) {
        Type listOfCoursesType = new TypeToken<ArrayList<Course>>(){}.getType();
        return new Gson().fromJson(stringListOfCourses, listOfCoursesType);
    }
}
