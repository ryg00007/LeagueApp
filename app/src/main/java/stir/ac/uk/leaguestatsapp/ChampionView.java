package stir.ac.uk.leaguestatsapp;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import net.rithms.riot.api.RiotApiException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class ChampionView extends Activity {

    private ArrayList<String> mName = new ArrayList<>();
    private ArrayList<String> mImage = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_champion_view);
        new getChampionNamesFromDBInternetTask().execute();

    }

    private void initRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(mName, mImage, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        adapter.notifyDataSetChanged();
    }

    private void getChampionNames() {
        String link = "https://lamp0.cs.stir.ac.uk/~rgi/MobileApp/nameList.php";
        String data = "";
        try {
            URL url = new URL(link);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            while (line != null) {
                line = bufferedReader.readLine();
                data = data + line;
            }

            JSONArray JA = new JSONArray(data);
            for (int i = 0; i < JA.length(); i++) {
                JSONObject JO = (JSONObject) JA.get(i);
                System.out.println(""+ JO.get("Name"));
                String nameFromJSONArray = "" + JO.get("Name");
                mName.add(nameFromJSONArray);

               String imageFROMJSONArray = "" + JO.get("imageName");
               imageFROMJSONArray = imageFROMJSONArray.toLowerCase();
               mImage.add(imageFROMJSONArray);
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private class getChampionNamesFromDBInternetTask extends AsyncTask<Void, Void, Boolean> {


        protected Boolean doInBackground(Void... params) {
            getChampionNames();
            return true;
        }

        protected void onPostExecute(Boolean result) {
            if(result){
                System.out.println("Task Succesful");
                initRecyclerView();
            }
        }
    }
}
