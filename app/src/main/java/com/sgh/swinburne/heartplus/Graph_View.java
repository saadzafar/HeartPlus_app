package com.sgh.swinburne.heartplus;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.sgh.swinburne.heartplus.helper.SQLiteHandler;
import com.sgh.swinburne.heartplus.helper.SessionManager;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.Vector;

/**
 * Created by VivekShah.
 */
public class Graph_View extends Activity {

    private static final Random RANDOM = new Random();
    private static final String TAG_SUCCESS = "success";
    private static Vector<Double> inrList;
    private static Vector<DataPoint> test;
    private static String url_create_graph = "http://188.166.237.51/android_login_api/create_graph.php";
    /*private static final String TAG_INR = "inr";
    private static final String TAG_VALUE = "value";
    private static final String TAG_DATE = "date";*/
    JSONParser jParser = new JSONParser();
    private LineGraphSeries<DataPoint> series;
    private int lastX = 0;
    private int lastX2 = 0;
    private SQLiteHandler db;
    private SessionManager session;
    private ProgressDialog pDialog;
    private ArrayList<HashMap<String, String>> inr_series;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.graph_view);
        inrList = new Vector<Double>();
        test = new Vector<DataPoint>();

        inr_series = new ArrayList<HashMap<String, String>>();
        new LoadINR().execute();
        //Log.d("INR Results", json.toString());
        Log.d("Dragons:", "Here be Dragons!");

        //doInBackground(null);

        // we get graph view instance
        GraphView graph = (GraphView) findViewById(R.id.graph);
        // data
        series = new LineGraphSeries<DataPoint>();
        graph.addSeries(series);


        Viewport viewport = graph.getViewport();
        viewport.setYAxisBoundsManual(true);
        viewport.setMinY(0);
        viewport.setMaxY(4);
        viewport.setScrollable(true);


    }


    @Override
    protected void onResume() {
        super.onResume();
        // we're going to simulate real time with thread that append data to the graph
        new Thread(new Runnable() {

            @Override
            public void run() {
                // we add 100 new entries
                //for (int i = 0; i < 100; i++) {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        //  addEntry();
                    }
                });


            }
        }).start();
    }

    // add random data to graph
    private void addEntry() {

        for (int i = 0; i < test.size(); i++)

            series.appendData(test.get(i)/*inrList.get(i).doubleValue())*/, true, 10);

    }
    // here, we choose to display max 10 points on the viewport and we scroll to end

    private class LoadINR extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Graph_View.this);
            pDialog.setMessage("Loading graph. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected String doInBackground(String... args) {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            db = new SQLiteHandler(getApplicationContext());
            session = new SessionManager(getApplicationContext());
            HashMap<String, String> user = db.getUserDetails();
            String email = user.get("email");
            params.add(new BasicNameValuePair("email", email));

            JSONObject json = jParser.makeHttpRequest(url_create_graph, "POST", params);
            Log.d("INR Results", json.toString());

            try {
                int success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    String in = json.toString();
                    String newStr = in.substring(62);

                    StringTokenizer st = new StringTokenizer(newStr, ",");

                    while (st.hasMoreTokens()) {
                        String inr = st.nextToken();
                        //System.out.println(key+"****");
                        String temp = inr.substring(10, 13);
                        String date = st.nextToken();
                        String temp2 = date.substring(8, 17);
                        //System.out.println(temp+ " " +temp2);

                        Double x = Double.parseDouble(temp);
                        inrList.add(x);
                        test.add(new DataPoint(lastX2++, x));
                        //double y = Double.parseDouble(temp2);
                        //System.out.println(x);
                    }

                    for (int i = 0; i < inrList.size(); i++)
                        if (inrList.get(i) instanceof Double)
                            System.out.println("Double");

                    // finish();
                } else {

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
                    addEntry();
                }
            });
        }

    }
}