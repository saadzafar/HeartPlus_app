package com.sgh.swinburne.heartplus;

/**
 * Created by Saad on 10/26/2015.
 */


import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;


import com.sgh.swinburne.heartplus.PillEducation.Interaction;
import com.sgh.swinburne.heartplus.PillEducation.Pill;
import com.sgh.swinburne.heartplus.R;

import com.sgh.swinburne.heartplus.helper.DatabaseHelper;
import com.sgh.swinburne.heartplus.helper.SQLiteHandler;
import com.sgh.swinburne.heartplus.helper.SessionManager;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class ProfileActivity extends ListActivity {
    private ProgressDialog pDialog;
    JSONParser jParser = new JSONParser();

    ArrayList<HashMap<String, String>> pillsList;

    private static String url_all_pills = "http://188.166.237.51/android_login_api/get_pill_all.php";

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PILL = "pill";
    private static final String TAG_PID = "pid";
    private static final String TAG_NAME = "name";

    JSONArray pill = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_pills);

        pillsList = new ArrayList<HashMap<String, String>>();
        new LoadAllPills().execute();

        ListView lv = getListView();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String pid = ((TextView) view.findViewById(R.id.pid)).getText().toString();

                Intent in = new Intent(getApplicationContext(), EditPill.class);
                in.putExtra(TAG_PID, pid);
                startActivityForResult(in, 100);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 100) {
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }
    }

    class LoadAllPills extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ProfileActivity.this);
            pDialog.setMessage("Loading pills. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected String doInBackground(String... args) {
            List<NameValuePair> params = new ArrayList<NameValuePair>();

            JSONObject json = jParser.makeHttpRequest(url_all_pills, "GET", params);
            Log.d("All Pills", json.toString());

            try {
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    pill = json.getJSONArray(TAG_PILL);

                    for (int i = 0; i < pill.length(); i++) {
                        JSONObject c = pill.getJSONObject(i);

                        String id = c.getString(TAG_PID);
                        String name = c.getString(TAG_NAME);

                        HashMap<String, String> map = new HashMap<String, String>();

                        map.put(TAG_PID, id);
                        map.put(TAG_NAME, name);

                        pillsList.add(map);
                    }
                } else {
                    Intent i = new Intent(getApplicationContext(), NewPillActivity.class);
                    startActivity(i);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String file_url) {
            pDialog.dismiss();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ListAdapter adapter = new SimpleAdapter(
                            ProfileActivity.this, pillsList, R.layout.list_item, new String[]{TAG_PID, TAG_NAME}, new int[]{R.id.pid, R.id.name});
                    setListAdapter(adapter);

                }
            });
        }
    }

}




