package com.example.visualmed;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPrefs();
    }

    public void sharedPrefs(){
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        if (sharedPref.getBoolean(SettingsActivity.KEY_PREF_EXAMPLE_SWITCH, false)) {
            Intent intent = new Intent(this, AppIntro.class); // Call the AppIntro java class
            startActivity(intent);
            Toast.makeText(getApplicationContext(), "True", Toast.LENGTH_SHORT).show();

        }
        else{
            Intent intent = new Intent(this,ManageMedicineActivity.class);
            startActivity(intent);
        }
    }

}