package stir.ac.uk.leaguestatsapp;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import net.rithms.riot.api.ApiConfig;
import net.rithms.riot.api.RiotApi;
import net.rithms.riot.api.RiotApiException;
import net.rithms.riot.api.endpoints.league.dto.LeaguePosition;
import net.rithms.riot.api.endpoints.match.dto.Match;
import net.rithms.riot.api.endpoints.match.dto.MatchList;
import net.rithms.riot.api.endpoints.summoner.dto.Summoner;
import net.rithms.riot.constant.Platform;

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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class StatScreen extends AppCompatActivity {

    //Possible API Error Messages here
    private final int BAD_REQUEST = 400;
    private final int UNAUTHORISED = 401;
    private final int FORBIDDEN = 403;
    private final int DATA_NOT_FOUND = 404;

    private final int SERVICE_UNAVAILABLE = 503;
    private final int INTERNAL_SERVER_ERROR = 500;
    private final int GATEWAY_TIMEOUT = 504;
    //

    //Error text and Image view
    private TextView error1;
    private TextView error2;
    private ImageView errorimg;
    private String returnedErrorMessage;
    //

    private Context context = StatScreen.this;
    private String passedSummonerName;

    //Riot API Objects
    private Summoner summoner;
    private Set<LeaguePosition> leaguePosition;
    private MatchList matchList;
    private List<LeaguePosition> listOfRankedGames;
    //

    private TextView SummonerName;
    private TextView SummonerLevel;
    private TextView Rank;
    private TextView WinLoss;
    private TextView WinPercent;
    private TextView Last10;
    private ImageView ProfileIcon;
    private ImageView RankIcon;
    private ProgressBar WinRateBar;
    private ProgressBar netLoadBar;
    private String summonerName;
    private int iconNo;
    private int levelNo;
    private String accountID;
    private String summonerID;
    private int wins;
    private int losses;
    private float gamesPlayed;
    private float winRate;
    private int winPercent;
    private int resID;
    private final String API_KEY = "";
    private ApiConfig config = new ApiConfig().setKey(API_KEY);
    private String rank;

    private ImageView GameTrack1;
    private ImageView GameTrack2;
    private ImageView GameTrack3;
    private ImageView GameTrack4;
    private ImageView GameTrack5;

    private ArrayList<String> champNames = new ArrayList<>();
    private ArrayList<String> champImageNames = new ArrayList<>();
    private ArrayList<String> roleNames = new ArrayList<>();
    private ArrayList<String> killList = new ArrayList<>();
    private ArrayList<String> deathList = new ArrayList<>();
    private ArrayList<String> assistList = new ArrayList<>();
    private ArrayList<Boolean> tickList = new ArrayList<>();
    private ArrayList<Integer> sumspell1List = new ArrayList<>();
    private ArrayList<Integer> sumspell2List = new ArrayList<>();
    private ArrayList<Integer> runePrimList = new ArrayList<>();
    private ArrayList<Integer> runeSecList = new ArrayList<>();
    private ArrayList<Integer> item1List = new ArrayList<>();
    private ArrayList<Integer> item2List = new ArrayList<>();
    private ArrayList<Integer> item3List = new ArrayList<>();
    private ArrayList<Integer> item4List = new ArrayList<>();
    private ArrayList<Integer> item5List = new ArrayList<>();
    private ArrayList<Integer> item6List = new ArrayList<>();
    private ArrayList<Boolean> victoryList = new ArrayList<>();
    private ArrayList<Long> durationList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stat_screen);
        passedSummonerName = getIntent().getStringExtra("PASSED_SUM_NAME");
        netLoadBar = findViewById(R.id.netloadProgressBar);

        SummonerName = findViewById(R.id.SummonerName);
        SummonerLevel = findViewById(R.id.SummonerLevel);
        ProfileIcon = findViewById(R.id.ProfileIcon);
        RankIcon = findViewById(R.id.rankImageView);
        WinLoss = findViewById(R.id.winsTextView);
        WinRateBar = findViewById(R.id.winrateBar);
        WinPercent = findViewById(R.id.winPercentTextView);
        Rank = findViewById(R.id.rankTextView);
        Last10 = findViewById(R.id.last10TextView);
        GameTrack1 = findViewById(R.id.GameTrack1);
        GameTrack2 = findViewById(R.id.GameTrack2);
        GameTrack3 = findViewById(R.id.GameTrack3);
        GameTrack4 = findViewById(R.id.GameTrack4);
        GameTrack5 = findViewById(R.id.GameTrack5);

        SummonerName.setVisibility(View.INVISIBLE);
        SummonerLevel.setVisibility(View.INVISIBLE);
        ProfileIcon.setVisibility(View.INVISIBLE);
        RankIcon.setVisibility(View.INVISIBLE);
        WinLoss.setVisibility(View.INVISIBLE);
        WinRateBar.setVisibility(View.INVISIBLE);
        WinPercent.setVisibility(View.INVISIBLE);
        Rank.setVisibility(View.INVISIBLE);
        Last10.setVisibility(View.INVISIBLE);

        error1 = findViewById(R.id.statscreenErrorText1);
        error2 = findViewById(R.id.statscreenErrorText2);
        errorimg = findViewById(R.id.statscreenErrorImage);
        new getBasicSummonerInternetTask().execute(passedSummonerName);
    }

    private class getBasicSummonerInternetTask extends AsyncTask<String, Boolean, Boolean> {

        protected void onPreExecute() {

        }

        protected Boolean doInBackground(String... args) {
            if(getSummonerInformation(args[0])){
                if(getLeagueInformation(accountID,summonerID)){
                    return getLast10Games(summonerID);
                }
            }
            return false;
        }

        protected void onPostExecute(Boolean response) {

            if (response) {
                summonerInformationText();
                leagueInformationText();
                last10GameText();
                netLoadBar.setVisibility(View.INVISIBLE);
                Last10.setVisibility(View.VISIBLE);
            } else {
                netLoadBar.setVisibility(View.INVISIBLE);
                errorimg.setVisibility(View.VISIBLE);
                error1.setVisibility(View.VISIBLE);
                error2.setText(returnedErrorMessage);
                error2.setVisibility(View.VISIBLE);
            }

        }
    }

    private boolean getSummonerInformation(String searchName) {
        RiotApi api = new RiotApi(config);
        try {
            summoner = api.getSummonerByName(Platform.EUW, searchName);
            summonerName = summoner.getName();
            iconNo = summoner.getProfileIconId();
            levelNo = summoner.getSummonerLevel();
            summonerID = summoner.getAccountId();
            accountID = summoner.getId();

            //Proceed to get information on where the player has placed.
            return true;

        } catch (RiotApiException errorMessage) {
            returnedErrorMessage = configureErrorMessage(errorMessage);
            return false;
        }
    }

    private boolean getLeagueInformation(String accountID, String summonerID) {
        try {
            RiotApi api = new RiotApi(config);
            leaguePosition = api.getLeaguePositionsBySummonerId(Platform.EUW, accountID);
            // Index 0 is Ranked Solo/Duo, Index 1 is Flex.

            if (!leaguePosition.isEmpty()) {
                listOfRankedGames = new ArrayList<>();
                listOfRankedGames.addAll(leaguePosition);
                wins = listOfRankedGames.get(0).getWins();
                losses = listOfRankedGames.get(0).getLosses();
                rank = listOfRankedGames.get(0).getTier();
                System.out.println("Rank is:" + rank);
            } else {
                wins = 0;
                losses = 0;
                rank = "UNRANKED";
            }

            //rank = rankedGameModeList.get(0).getLeagueName();
            //Proceed to get a short match history from the player.

            return true;

        } catch (RiotApiException e) {
            e.printStackTrace();
            returnedErrorMessage = configureErrorMessage(e);
            return false;
        }
    }

    private boolean getLast10Games(String summonerID) {
        RiotApi api = new RiotApi(config);
        Set<Integer> queue = new HashSet<Integer>();
        queue.add(420);
        Set<Integer> season = new HashSet<Integer>();
        season.add(13);
        //Queue 420 is Ranked Solo Queue, Season 13 is 2019 Season.
        try {
            matchList = api.getMatchListByAccountId(Platform.EUW, summonerID, null, null, null, -1, -1, -1, 10);

            // Building our recycler view for games
            // Need multiple stats per game
            // Add them to our lists

            // Loop through the matchList for individual matches
            for (int i = 0; i < matchList.getMatches().size(); i++) {
                long gameID = matchList.getMatches().get(i).getGameId();
                Match game = api.getMatch(Platform.EUW, gameID);

                //Champion ID
                int champIDFromMatch = matchList.getMatches().get(i).getChampion();
                String[] champNameAndImage = getChampNameByID(champIDFromMatch);
                String champName = champNameAndImage[0];
                String champImageName = champNameAndImage[1];
                if (champName == null) {
                    champNames.add("Aatrox");
                } else {
                    champNames.add(champName);
                    champImageNames.add(champImageName);
                }


                // Role Name - Currently unused -
                String roleFromMatch = matchList.getMatches().get(i).getLane();
                System.out.println(roleFromMatch);
                roleNames.add(roleFromMatch);
                // Kills
                int pkFromGame = game.getParticipantBySummonerName(summonerName).getStats().getKills();
                killList.add("" + pkFromGame);
                // Deaths
                int pdFromGame = game.getParticipantBySummonerName(summonerName).getStats().getDeaths();
                deathList.add("" + pdFromGame);
                // Assists
                int assFromGame = game.getParticipantBySummonerName(summonerName).getStats().getAssists();
                assistList.add("" + assFromGame);
                // Summoner spells
                int summonerspell1 = game.getParticipantBySummonerName(summonerName).getSpell1Id();
                sumspell1List.add(summonerspell1);
                int summonerspell2 = game.getParticipantBySummonerName(summonerName).getSpell2Id();
                sumspell2List.add(summonerspell2);
                // Runes
                int runeprimary = game.getParticipantBySummonerName(summonerName).getStats().getPerkPrimaryStyle();
                runePrimList.add(runeprimary);
                int runesecondary = game.getParticipantBySummonerName(summonerName).getStats().getPerkSubStyle();
                runeSecList.add(runesecondary);
                // Items
                int item1 = game.getParticipantBySummonerName(summonerName).getStats().getItem0();
                item1List.add(item1);
                int item2 = game.getParticipantBySummonerName(summonerName).getStats().getItem1();
                item2List.add(item2);
                int item3 = game.getParticipantBySummonerName(summonerName).getStats().getItem2();
                item3List.add(item3);
                int item4 = game.getParticipantBySummonerName(summonerName).getStats().getItem3();
                item4List.add(item4);
                int item5 = game.getParticipantBySummonerName(summonerName).getStats().getItem4();
                item5List.add(item5);
                int item6 = game.getParticipantBySummonerName(summonerName).getStats().getItem5();
                item6List.add(item6);
                //System.out.print(item1 + " " + item2 + " " + item3 + " " + item4 + " " + item5 + " " + item6);
                // Outcome

                boolean victory = game.getParticipantBySummonerName(summonerName).getStats().isWin();
                victoryList.add(victory);
                if(i<=4) {
                    tickList.add(victory);
                    System.out.println("i = :" + i + " tickList is:" + tickList);
                }
                // Duration
                long duration = game.getGameDuration();
                durationList.add(duration);
            }
            return true;

        } catch (RiotApiException e) {
            e.printStackTrace();
            returnedErrorMessage = configureErrorMessage(e);
            return false;
        }
    }


    public void summonerInformationText() {
        if (!rank.equals("UNRANKED")) {
            int intCurrImageResourceID = context.getResources().getIdentifier("emblem_" + rank.toLowerCase(), "drawable", context.getPackageName());
            RankIcon.setImageResource(intCurrImageResourceID);
            RankIcon.setVisibility(View.VISIBLE);
        }
        if (rank.equals("UNRANKED")) {
            RankIcon.setImageResource(R.drawable.emblem_unranked);
            RankIcon.setVisibility(View.VISIBLE);
        }
        Rank.setText(rank);

        SummonerName.setText("" + summonerName);
        SummonerLevel.setText("Level: " + levelNo);
        String url = "https://ddragon.leagueoflegends.com/cdn/9.7.1/img/profileicon/" + iconNo + ".png";
        Glide
                .with(context)
                .load(url)
                .apply(RequestOptions.circleCropTransform())
                .into(ProfileIcon);


        SummonerName.setVisibility(View.VISIBLE);
        SummonerLevel.setVisibility(View.VISIBLE);
        ProfileIcon.setVisibility(View.VISIBLE);
    }

    public void leagueInformationText() {

        WinLoss.setText("W: " + wins + " / L: " + losses);
        WinRateBar.setMax(100);
        float GamesPlayed = wins + losses;
        winRate = (wins / GamesPlayed) * 100;
        winPercent = Math.round(winRate);
        System.out.println(winPercent);
        WinRateBar.setProgress(winPercent);
        if (winPercent >= 50) {
            WinRateBar.getProgressDrawable().setColorFilter(Color.rgb(0, 100, 0), PorterDuff.Mode.SRC_IN);
        } else {
            WinRateBar.getProgressDrawable().setColorFilter(Color.rgb(140, 0, 0), PorterDuff.Mode.SRC_IN);
        }
        WinPercent.setText("WIN PERCENT " + winPercent + "%");


        WinLoss.setVisibility(View.VISIBLE);
        WinRateBar.setVisibility(View.VISIBLE);
        WinPercent.setVisibility(View.VISIBLE);
        Rank.setVisibility(View.VISIBLE);
    }

    public void last10GameText() {
        initRecyclerView();
        ImageView[] imageViews = {GameTrack1, GameTrack2, GameTrack3, GameTrack4, GameTrack5};
        if (tickList.size() == 5) {
            for (int i = 0; i<tickList.size(); i++){
                if(tickList.get(i)){
                    imageViews[i].setImageResource(R.drawable.gamewintick);
                } else {
                    imageViews[i].setImageResource(R.drawable.gamelosecross);
                }

            }
        }
        for (ImageView temp : imageViews) {
            temp.setVisibility(View.VISIBLE);
        }
    }

    public String[] getChampNameByID(int id) {
        String link = "https://lamp0.cs.stir.ac.uk/~rgi/MobileApp/championIDs.php?championID=" + id;
        try {
            URL url = new URL(link);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            String data = "";
            while (line != null) {
                line = bufferedReader.readLine();
                data = data + line;
            }

            JSONArray JA = new JSONArray(data);
            for (int i = 0; i < JA.length(); i++) {
                JSONObject JO = (JSONObject) JA.get(i);
                String championNameToReturn = (String) JO.get("Name");
                String championImageNameToReturn = (String) JO.get("imageName");
                String[] nameAndImage = {championNameToReturn, championImageNameToReturn};
                return nameAndImage;
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

    private void initRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.RecyclerViewGames);
        RecyclerViewGamesAdapter adapter = new RecyclerViewGamesAdapter(champNames,
                champImageNames,
                roleNames,
                killList,
                deathList,
                assistList,
                sumspell1List,
                sumspell2List,
                runePrimList,
                runeSecList,
                item1List,
                item2List,
                item3List,
                item4List,
                item5List,
                item6List,
                victoryList,
                durationList,
                this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
    }

    private String configureErrorMessage(RiotApiException errorMessage) {
        int errorCode = errorMessage.getErrorCode();


        if (errorCode == BAD_REQUEST) {
            return "Request was bad! Try again.";
        }

        if (errorCode == UNAUTHORISED) {
            return "API Key expired!";
        }

        if (errorCode == FORBIDDEN) {
            return "API Key expired!";
        }

        if (errorCode == DATA_NOT_FOUND) {
            return "Summoner not found! Check spelling!";
        }

        if (errorCode == SERVICE_UNAVAILABLE) {
            return "Riot API Servers down!";
        }

        if (errorCode == INTERNAL_SERVER_ERROR) {
            return "Riot API experienced an issue. Try again.";
        }

        if (errorCode == GATEWAY_TIMEOUT) {
            return "Request took too long. Check internet connection";
        }
        System.out.print(errorCode);
        return "Unknown Error. Check your connection and try again.";
    }
}

