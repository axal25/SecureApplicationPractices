package backend.app.secureapppractices.ui.main.courses;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Build;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.RequiresApi;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.stream.Collectors;

public class CustomAsyncTask extends AsyncTask {
    private Activity activity;
    private View view;
    private int elementId;
    private String stringUrl;

    public CustomAsyncTask(Activity activity, View view, int elementId, String stringUrl) {
        System.out.println("CustomAsyncTask(...)");
        this.activity = activity;
        this.view = view;
        this.elementId = elementId;
        this.stringUrl = stringUrl;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected Object doInBackground(Object[] objects) {
        for(Object object : objects) System.out.println("doInBackground(...): object: " + object);
        String responseString = null;
        try {
            URL url = new URL(this.stringUrl);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            if(httpURLConnection.getResponseCode() != 200) {
                InputStream errorStream = new BufferedInputStream(httpURLConnection.getErrorStream());
                if(errorStream != null) responseString = inputStreamToString(errorStream);
            }
            else {
                InputStream inputStream = new BufferedInputStream(httpURLConnection.getInputStream());
                if(inputStream != null) responseString = inputStreamToString(inputStream);
            }
        } catch (Exception e) {
            e.printStackTrace();
            responseString = e.toString();
        }
        System.out.println("doInBackground(...): responseString: "+ responseString);
        return responseString;
    }

    @Override
    protected void onPostExecute(Object o) {
        String responseString = (String) o;
        System.out.println("onPostExecute(...): responseString: "+ responseString);
        EditText editText = (EditText) this.view.findViewById(this.elementId);
        editText.setText(responseString);
        super.onPostExecute(o);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private String inputStreamToString(InputStream inputStream) {
        return new BufferedReader(new InputStreamReader(inputStream)).lines().collect(Collectors.joining("\n"));
    }
}
