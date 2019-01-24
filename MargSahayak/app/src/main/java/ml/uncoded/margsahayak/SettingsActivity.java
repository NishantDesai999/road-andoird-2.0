package ml.uncoded.margsahayak;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.Locale;

import android.app.Activity;
import android.content.res.Configuration;
import android.widget.Button;

import ml.uncoded.margsahayak.Auth.SharedPrefrenceUser;
import ml.uncoded.margsahayak.Input.InputActivity2;

public class SettingsActivity extends AppCompatActivity {

//    private PrefManager prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
//        prefManager=new PrefManager(this);
setToolBar();



        findViewById(R.id.settings_change_lan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog mChooseLanguageDialog = new Dialog(SettingsActivity.this);
                mChooseLanguageDialog.setContentView(R.layout.choose_language_dialog);
                mChooseLanguageDialog.getWindow().setBackgroundDrawable(new ColorDrawable((Color.TRANSPARENT)));
                mChooseLanguageDialog.show();

                mChooseLanguageDialog.findViewById(R.id.language_hindi).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setAppLanguage("hi");
                    }
                });

                mChooseLanguageDialog.findViewById(R.id.language_english).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setAppLanguage("en");
                    }
                });
                ((Button) mChooseLanguageDialog.findViewById(R.id.choose_language_save)).setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        mChooseLanguageDialog.dismiss();
                    }
                });

            }
        });
    }


    public void setAppLanguage(String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());

        SharedPrefrenceUser.setLanguage(language);

    }

    private void setToolBar() {
        Toolbar mSettingsToolbar = (Toolbar) findViewById(R.id.toolbar_settings_activity);
        setSupportActionBar(mSettingsToolbar);
        mSettingsToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back_arrow_white));
        mSettingsToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingsActivity.this, MainActivity.class));
                finish();
            }
        });
    }


}
