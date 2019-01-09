package ml.uncoded.margsahayak;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import java.util.Locale;

import android.app.Activity;
import android.content.res.Configuration;

import ml.uncoded.margsahayak.Auth.SharedPrefrenceUser;

public class SettingsActivity extends AppCompatActivity {

    private PrefManager prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        prefManager=new PrefManager(this);



        findViewById(R.id.btn_goto_intro).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prefManager.setfromSettingsToIntro(true);
                startActivity(new Intent(SettingsActivity.this,IntroActivity.class));
            }
        });
        findViewById(R.id.language_hindi).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String languageToLoad = "hi"; // your language
                setAppLanguage("hi");

//                Locale locale = new Locale(languageToLoad);
//                Locale.setDefault(locale);
//                Configuration config = new Configuration();
//                config.locale = locale;
//                getBaseContext().getResources().updateConfiguration(config,
//                        getBaseContext().getResources().getDisplayMetrics());
//                SharedPrefrenceUser.setLanguage(languageToLoad);

            }
        });

        findViewById(R.id.language_english).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAppLanguage("en");
//                String languageToLoad = "en"; // your language
//                Locale locale = new Locale(languageToLoad);
//                Locale.setDefault(locale);
//                Configuration config = new Configuration();
//                config.locale = locale;
//                getBaseContext().getResources().updateConfiguration(config,
//                        getBaseContext().getResources().getDisplayMetrics());
//
//                SharedPrefrenceUser.setLanguage(languageToLoad);
            }
        });
    }



    public void setAppLanguage(String language){
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());

        SharedPrefrenceUser.setLanguage(language);

    }
}
