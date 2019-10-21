package stir.ac.uk.leaguestatsapp;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

public class ChampionStats extends Activity {
    private Context context = ChampionStats.this;
    private TextView errorText1;
    private TextView errorText2;
    private ImageView errorImg;
    private ImageView champSquare;
    private TextView championName;
    private TextView winRate;
    private TextView tagLine;
    private TextView averageAssists;
    private TextView averageKills;
    private TextView averageDeaths;
    private ImageView summonerSpell1;
    private ImageView summonerSpell2;
    private ImageView item1;
    private ImageView item2;
    private ImageView item3;
    private ProgressBar loadingBar;
    private String data = "";
    private String championID;

    private float overallAVGK;
    private float overallAVGD;
    private float overallAVGA;
    private float overallAVGCS;
    private float overallAVGG;
    private float championAVGK;
    private float championAVGD;
    private float championAVGA;
    private float championAVGG;
    private float championAVGCS;

    BarChart barChart;
    String[] labels = {"Kills", "Deaths", "Assists"};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_champion_stats);
        String passedChampionName = getIntent().getStringExtra("PASSED_CHAMPNAME_NAME");
        barChart = findViewById(R.id.bar_chart);
        new getChampionInfoFromDB().execute(passedChampionName);


    }

    private class getChampionInfoFromDB extends AsyncTask<String, Boolean, Boolean> {

        protected void onPreExecute() {
            //Error handling views
            errorText1 = findViewById(R.id.error1);
            errorText2 = findViewById(R.id.error2);
            errorImg = findViewById(R.id.errorImage);
            //


            championName = findViewById(R.id.cnameTextView);
            winRate = findViewById(R.id.winRateTextView);
            champSquare = findViewById(R.id.champSquare);
            loadingBar = findViewById(R.id.loadingBar);
            tagLine = findViewById(R.id.ctag);
            averageAssists = findViewById(R.id.AVGA);
            averageKills = findViewById(R.id.AVGK);
            averageDeaths = findViewById(R.id.AVGD);

            summonerSpell1 = findViewById(R.id.Csummonerspell1);
            summonerSpell2 = findViewById(R.id.Csummonerspell2);

            item1 = findViewById(R.id.Citem1);
            item2 = findViewById(R.id.Citem2);
            item3 = findViewById(R.id.Citem3);

        }

        protected Boolean doInBackground(String... args) {
            displayInformationFromJSON(getChampionInfo(args[0]));
            displayAVGSFromJSON(getPHPScript(championID, "https://lamp0.cs.stir.ac.uk/~rgi/MobileApp/champAverages.php?"));
            displayItem1FromJSON(getPHPScript(championID, "https://lamp0.cs.stir.ac.uk/~rgi/MobileApp/commonItem1.php?"));
            displayItem2FromJSON(getPHPScript(championID, "https://lamp0.cs.stir.ac.uk/~rgi/MobileApp/commonItem2.php?"));
            displayItem3FromJSON(getPHPScript(championID, "https://lamp0.cs.stir.ac.uk/~rgi/MobileApp/commonItem3.php?"));
            displaySum1FromJSON(getPHPScript(championID, "https://lamp0.cs.stir.ac.uk/~rgi/MobileApp/commonSummoner1.php?"));
            displaySum2FromJSON(getPHPScript(championID, "https://lamp0.cs.stir.ac.uk/~rgi/MobileApp/commonSummoner2.php?"));
            displayOverallsFromJSON(getAverages("https://lamp0.cs.stir.ac.uk/~rgi/MobileApp/overallavgs.php" ));
            return true;
        }

        protected void onPostExecute(Boolean response) {
            loadingBar.setVisibility(View.GONE);
            if(response) {

            } else {
                errorText1.setVisibility(View.VISIBLE);
                errorText2.setVisibility(View.VISIBLE);
                errorImg.setVisibility(View.VISIBLE);

            }
           /* RadarDataSet dataSet1 = new RadarDataSet(averagesOverall(overallAVGK, overallAVGD, overallAVGA), "Average Stats" );
            RadarDataSet dataSet2 = new RadarDataSet(averagesChampion(championAVGK, championAVGD, championAVGA), "Champion Stats" );*/


            barChart.setDrawBarShadow(false);
            barChart.setDrawValueAboveBar(true);
            barChart.getDescription().setText("");
            barChart.setMaxVisibleValueCount(50);
            barChart.setPinchZoom(false);
            barChart.setDrawGridBackground(false);
            XAxis xl = barChart.getXAxis();
            xl.setGranularity(1f);
            xl.setCenterAxisLabels(true);
            String[] labels = new String[]{"Kills", "Deaths", "Assists"};
            xl.setValueFormatter(new IndexAxisValueFormatter(labels));
            xl.setCenterAxisLabels(true);
            YAxis leftAxis = barChart.getAxisLeft();
            leftAxis.setDrawGridLines(false);
            leftAxis.setSpaceTop(30f);
            leftAxis.setAxisMinValue(0f); // this replaces setStartAtZero(true
            barChart.getAxisRight().setEnabled(false);

            ArrayList<BarEntry> barEntries1 = new ArrayList<>();
            barEntries1.add(new BarEntry(1,  overallAVGK));
            barEntries1.add(new BarEntry(2,  overallAVGD));
            barEntries1.add(new BarEntry(3,  overallAVGA));
            System.out.println(barEntries1);

            ArrayList<BarEntry> barEntries2 = new ArrayList<>();
            barEntries2.add(new BarEntry(1,  championAVGK));
            barEntries2.add(new BarEntry(2,  championAVGD));
            barEntries2.add(new BarEntry(3,  championAVGA));
            System.out.println(barEntries2);

            BarDataSet barDataSet1 = new BarDataSet(barEntries1, "Average");
            barDataSet1.setColor(Color.RED);
            BarDataSet barDataSet2 = new BarDataSet(barEntries2, "Champion");
            barDataSet2.setColor(Color.BLUE);

            BarData data = new BarData(barDataSet1, barDataSet2);
            barChart.setData(data);




            barChart.setDragEnabled(false);
            barChart.setVisibleXRangeMaximum(2.5f);

            float groupSpace = 0.04f;
            float barSpace = 0.02f; // x2 dataset
            float barWidth = 0.46f; // x2 dataset

            barChart.getBarData().setBarWidth(barWidth);
            barChart.getXAxis().setAxisMinValue(0);
            barChart.groupBars(0,groupSpace, barSpace );
            barChart.invalidate();
        }
    }

    private void displayAVGSFromJSON(JSONObject phpScript) {
        try {
            if (phpScript != null) {
                final String AverageKills = "" + phpScript.get("AVGK");
                System.out.println("Avergae kills =" +AverageKills);
                final String AverageDeaths = "" + phpScript.get("AVGD");
                final String AverageAssists = "" + phpScript.get("AVGA");
                championAVGK = Float.parseFloat(""+ phpScript.get("AVGK"));
                championAVGD = Float.parseFloat(""+ phpScript.get("AVGD"));
                championAVGA = Float.parseFloat(""+ phpScript.get("AVGA"));
                championAVGG = Float.parseFloat(""+ phpScript.get("AVGG"));
                championAVGCS = Float.parseFloat(""+ phpScript.get("AVGCS"));
                System.out.println("Got here 1");
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {

                        DecimalFormat df = new DecimalFormat("0.00");
                        System.out.println("Got here 2");
                        averageKills.setText("Average Kills: " + df.format(championAVGK) );
                        averageDeaths.setText("Average Deaths: " + df.format(championAVGD));
                        averageAssists.setText("Average Assists: " + df.format(championAVGA));
                        // Stuff that updates the UI

                    }
                });

            }
        } catch (JSONException e) {

        }
    }

    private void displaySum1FromJSON(JSONObject phpScript) {
        try {
            if (phpScript != null) {
                String sumoner1ID = "" + phpScript.get("Sum1");
                final int intCurrImageResourceID = context.getResources().getIdentifier("s_"+sumoner1ID, "drawable", context.getPackageName());
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        summonerSpell1.setImageResource(intCurrImageResourceID);
                        // Stuff that updates the UI

                    }
                });

            }
        } catch (JSONException e) {

        }
    }

    private void displaySum2FromJSON(JSONObject phpScript) {
        try {
            if (phpScript != null) {
                String sumoner2ID = "" + phpScript.get("Sum2");
                final int intCurrImageResourceID = context.getResources().getIdentifier("s_"+sumoner2ID, "drawable", context.getPackageName());
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {

                        // Stuff that updates the UI
                        summonerSpell2.setImageResource(intCurrImageResourceID);

                    }
                });

            }
        } catch (JSONException e) {

        }
    }

    private void displayItem1FromJSON(JSONObject phpScript) {
        try {
            if (phpScript != null) {
                String item1id = "" + phpScript.get("Item1");
                final int intCurrImageResourceID = context.getResources().getIdentifier("i_"+item1id, "drawable", context.getPackageName());
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {

                        // Stuff that updates the UI
                        item1.setImageResource(intCurrImageResourceID);
                    }
                });

            }
        } catch (JSONException e) {

        }
    }

    private void displayItem2FromJSON(JSONObject phpScript) {
        try {
            if (phpScript != null) {
                String item2id = "" + phpScript.get("Item2");
                final int intCurrImageResourceID = context.getResources().getIdentifier("i_"+item2id, "drawable", context.getPackageName());
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {

                        // Stuff that updates the UI
                        item2.setImageResource(intCurrImageResourceID);

                    }
                });

            }
        } catch (JSONException e) {

        }
    }

    private void displayItem3FromJSON(JSONObject phpScript) {
        try {
            if (phpScript != null) {
                String item3id = "" + phpScript.get("Item3");
                final int intCurrImageResourceID = context.getResources().getIdentifier("i_"+item3id, "drawable", context.getPackageName());
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {

                        // Stuff that updates the UI
                        item3.setImageResource(intCurrImageResourceID);

                    }
                });

            }
        } catch (JSONException e) {

        }
    }


    private JSONObject getChampionInfo(String passedChampionName) {
        String passedChampName = passedChampionName;
        String link = "https://lamp0.cs.stir.ac.uk/~rgi/MobileApp/index.php?championName=" + passedChampName;
        System.out.println("Champion Name:" + passedChampName);
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
            inputStream.close();
            JSONArray JA = new JSONArray(data);
            for (int i = 0; i < JA.length(); i++) {
                JSONObject JO = (JSONObject) JA.get(i);
                System.out.println("Im still sending u jo btw");
                return JO;
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return null;
    }

    private void displayInformationFromJSON(final JSONObject jo) {
        try {
            if (jo != null) {
                championID = "" + jo.get("ID");
                final String champName = "" + jo.get("Name");
                String champImage = "" + jo.get("imageName");
                final String championTagLine = "" + jo.get("Title");
                champImage = champImage.toLowerCase();
                final int intCurrImageResourceID = context.getResources().getIdentifier(champImage, "drawable", context.getPackageName());
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {

                        // Stuff that updates the UI
                        championName.setText("" + champName);
                        tagLine.setText("" + championTagLine);
                        champSquare.setImageResource(intCurrImageResourceID);

                    }
                });


            } else {
            }
        } catch (JSONException e) {

        }
    }

    private JSONObject getPHPScript(String championID, String passedURL) {
        String passedID = championID;
        String link = passedURL+ "championID=" + passedID;
        System.out.println(link);
        try {
            URL url = new URL(link);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            String datadata = "";
            while (line != null) {
                line = bufferedReader.readLine();
                datadata = datadata + line;
            }
            inputStream.close();
            JSONArray JA = new JSONArray(datadata);
            for (int i = 0; i < JA.length(); i++) {
                JSONObject JO = (JSONObject) JA.get(i);
                return JO;
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private ArrayList<RadarEntry> averagesOverall(float overallAVGK, float overallAVGD, float overallAVGA){
        ArrayList<RadarEntry> dataVals = new ArrayList<>();
        dataVals.add(new RadarEntry(overallAVGK));
        dataVals.add(new RadarEntry(overallAVGD));
        dataVals.add(new RadarEntry(overallAVGA));
        return dataVals;
    }

    private ArrayList<RadarEntry> averagesChampion(float championAVGK, float championAVGD, float championAVGA){
        ArrayList<RadarEntry> dataVals = new ArrayList<>();
        dataVals.add(new RadarEntry(championAVGK));
        dataVals.add(new RadarEntry(championAVGD));
        dataVals.add(new RadarEntry(championAVGA));
        return dataVals;
    }

    private JSONObject getAverages(String passedURL) {
        String link = passedURL;
        System.out.println(link);
        try {
            URL url = new URL(link);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            String datadata = "";
            while (line != null) {
                line = bufferedReader.readLine();
                datadata = datadata + line;
            }
            inputStream.close();
            JSONArray JA = new JSONArray(datadata);
            for (int i = 0; i < JA.length(); i++) {
                JSONObject JO = (JSONObject) JA.get(i);
                return JO;
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void displayOverallsFromJSON(JSONObject phpScript) {
        try {
            if (phpScript != null) {
                overallAVGK = Float.parseFloat(""+ phpScript.get("AVGK"));
                overallAVGD = Float.parseFloat(""+ phpScript.get("AVGD"));
                overallAVGA = Float.parseFloat(""+ phpScript.get("AVGA"));
                overallAVGG = Float.parseFloat(""+ phpScript.get("AVGG"));
                overallAVGCS = Float.parseFloat(""+ phpScript.get("AVGCS"));
            }
        } catch (JSONException e) {

        }
    }
}