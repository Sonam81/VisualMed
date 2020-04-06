package com.example.visualmed;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;

import com.github.paolorotolo.appintro.AppIntroFragment;
import com.github.paolorotolo.appintro.model.SliderPage;

import androidx.fragment.app.Fragment;

public class AppIntro extends com.github.paolorotolo.appintro.AppIntro {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addSlide(AppIntroFragment.newInstance("To Add Medicine","Tap on screen and  command 'add medicine *your medicine name*'",R.drawable.images,Color.BLACK));
        addSlide(AppIntroFragment.newInstance("To Delete Medicine","Tap on screen and command 'delete medicine *your medicine name*'",R.drawable.images,Color.BLACK));
        addSlide(AppIntroFragment.newInstance("To View Medicine","Tap on screen and command 'view medicine'",R.drawable.images,Color.BLACK));
        addSlide(AppIntroFragment.newInstance("To Read Medicine","Tap on the medicine list on screen to hear name of medicine and its time",R.drawable.images,Color.BLACK));
        addSlide(AppIntroFragment.newInstance("To Identify Medicine","Tap on screen and command 'identify medicine'",R.drawable.images,Color.BLACK));
        addSlide(AppIntroFragment.newInstance("To Click Image","Tap anywhere in the surface to take image and process",R.drawable.images,Color.BLACK));
        addSlide(AppIntroFragment.newInstance("Now what?","Tap on screen to add or delete medicine using add and delete command",R.drawable.images,Color.BLACK));
        addSlide(AppIntroFragment.newInstance("Adding time and saving medicine","Follow the system generated command to add time and save medicine",R.drawable.images,Color.BLACK));

        showSkipButton(true);
        showBackButtonWithDone = true;
        setFadeAnimation();
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        Intent intent = new Intent(AppIntro.this,ManageMedicineActivity.class);
        startActivity(intent);
        // Do something when users tap on Skip button.
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        Intent intent = new Intent(AppIntro.this,ManageMedicineActivity.class);
        startActivity(intent);
        // Do something when users tap on Done button.
    }}
