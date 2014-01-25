package com.mirabellehegnet.campus;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.GridView;
import android.widget.Toast;

import com.mirabellehegnet.campus.ui.UniversityGridViewAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by thomas on 1/25/14.
 */
public class UniversityTask extends AsyncTask<String, Void, Boolean> {
    private ProgressDialog dialog;
    private Activity activity;
    private GridView gridView;
    private ArrayList<University> universities = new ArrayList<University>();

    // private List<Message> messages;
    public UniversityTask(Activity activity, GridView gridView) {
        this.activity = activity;
        this.gridView = gridView;
        dialog = new ProgressDialog(activity);
    }

    protected void onPreExecute() {
        this.dialog.setMessage("Progress start");
        this.dialog.show();
    }

    @Override
    protected void onPostExecute(final Boolean success) {
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
        UniversityGridViewAdapter adapter = new UniversityGridViewAdapter(activity, R.layout.list_row, universities);
        gridView.setAdapter(adapter);
        Toast.makeText(activity, String.format("Found %,d universities", universities.size()), Toast.LENGTH_SHORT).show();
    }

    protected Boolean doInBackground(final String... args) {
        JSONParser jParser = new JSONParser();
        String url = "https://www.coursera.org/maestro/api/university/list";
        JSONArray json = jParser.getJSONFromUrl(url);
        try {
            for (int i = 0; i < json.length(); i++) {
                JSONObject jsonObject = json.getJSONObject(i);
                String name = jsonObject.getString("name");
                if (name != null) {
                    University university = new University();
                    university.setName(name);
                    university.setIcon(jsonObject.getString("logo"));
                    university.setShortName(jsonObject.getString("abbr_name"));
                    String homepage = jsonObject.getString("home_link");
                    if (homepage == null || homepage.equals("null"))
                        homepage = "";
                    university.setUrl(homepage);
                    universities.add(university);
                }
            }
        } catch (Exception ex) {
            Log.e("Banana", ex.getMessage());
        }

        return null;
    }
}
