package com.example.fr0zen.rocketlaunch;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView itemsList;
    private List<ListItem> items;
    private String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        items = new ArrayList<>();
        itemsList = findViewById(R.id.itemsList);

        new ParseTask().execute();
    }

    private class ParseTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(MainActivity.this,"Json Data is downloading",
                    Toast.LENGTH_LONG).show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            HttpHandler handler = new HttpHandler();
            String url = "https://api.spacexdata.com/v2/launches?launch_year=2017";
            String jsonString = handler.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonString);

            if (jsonString != null) {
                try {
                    JSONArray dataJsonArray = new JSONArray(jsonString);
                    Log.d(TAG, dataJsonArray.toString());
                    Log.d(TAG, "Size: " + String.valueOf(dataJsonArray.length()));

                    for (int i = 0; i < dataJsonArray.length(); i++) {
                        JSONObject object = dataJsonArray.getJSONObject(i);
                        long launchDate = object.getLong("launch_date_unix");
                        Log.d(TAG, "Launch date: " + launchDate);

                        JSONObject rocket = object.getJSONObject("rocket");
                        String rocketName = rocket.getString("rocket_name");
                        Log.d(TAG, "Rocket name: " + rocketName);

                        JSONObject links = object.getJSONObject("links");
                        String missionImage = links.getString("mission_patch");
                        Log.d(TAG, "Image: " + missionImage);
                        URL missionURL = null;
                        Bitmap loadedImg = null;
                        Bitmap img = null;
                        try {
                            missionURL = new URL(missionImage);
                            loadedImg = BitmapFactory.decodeStream(missionURL.openConnection()
                                    .getInputStream());
                            img = Bitmap.createScaledBitmap(loadedImg, 200, 200, false);
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        String articleLink = links.getString("article_link");
                        Log.d(TAG, "Article link: " + articleLink);

                        String details = object.getString("details");
                        Log.d(TAG, "Details: " + details);

                        ListItem item = new ListItem(rocketName, launchDate, img, details);
                        item.setArticleLink(articleLink);
                        items.add(item);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            ItemAdapter adapter = new ItemAdapter(MainActivity.this,
                    R.layout.activity_list_item, items);
            itemsList.setAdapter(adapter);

            AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Uri address = Uri.parse(items.get(i).getArticleLink());
                    Intent intent = new Intent(Intent.ACTION_VIEW, address);
                    startActivity(intent);
                }
            };

            itemsList.setOnItemClickListener(listener);
        }
    }
}
