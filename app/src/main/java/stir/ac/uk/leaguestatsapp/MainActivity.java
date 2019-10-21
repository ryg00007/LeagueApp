package stir.ac.uk.leaguestatsapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class MainActivity extends AppCompatActivity {

    private EditText editText;
    private Button LookUpButtonS;
    private Button LookUpButtonC;
    private Button championButton;

    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        configureStatScreenButton();
        configureChampionButton();
/*        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = mPreferences.edit();
        if(checkForFirstTimeSetup()){
            //DO DATABASE SETUP AND GRAB INFORMATION
            editText.setVisibility(View.GONE);
            LookUpButton.setVisibility(View.GONE);
            championButton.setVisibility(View.GONE);
        }
        if(!checkForFirstTimeSetup()){

        }*/
    }

    @Override
    public void onResume(){
        super.onResume();

    }

    private void configureStatScreenButton() {
        LookUpButtonS = findViewById(R.id.LookUpButtonS);
        LookUpButtonC = findViewById(R.id.LookUpButtonC);
        editText = findViewById(R.id.editText);
        LookUpButtonS.setEnabled(false);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().length()<3) {
                    LookUpButtonS.setEnabled(false);
                } else {
                    LookUpButtonS.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        LookUpButtonS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String SummonerName = editText.getText().toString();
                Intent intent = new Intent(MainActivity.this, StatScreen.class);
                intent.putExtra("PASSED_SUM_NAME", SummonerName);
                startActivity(intent);
            }
        });

        LookUpButtonC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ChampionName = editText.getText().toString();
                Intent intent = new Intent(MainActivity.this, ChampionStats.class);
                intent.putExtra("PASSED_CHAMPNAME_NAME", ChampionName);
                startActivity(intent);
            }
        });
    }

    private void configureChampionButton() {
        championButton = findViewById(R.id.championButton);
        championButton.setEnabled(true);
        championButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ChampionView.class);
                startActivity(intent);
            }
        });
    }

    private boolean checkForFirstTimeSetup(){
        Boolean firstTime = mPreferences.getBoolean("firstTime", true);
        return firstTime;
    }
}
