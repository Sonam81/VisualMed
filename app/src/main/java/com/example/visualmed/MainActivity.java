package com.example.visualmed;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.parse.ParseInstallation;

import java.util.Date;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private SpeechRecognizer mySpeechRecognizer;
    public static final String EXTRA_MESSAGE = "com.example.visualmed.extra.MESSAGE";
    TtsService ttsService;
    boolean bounded;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //ttsService.speak("Welcome. Please tap on the screen and command the operation.");

        Button but = findViewById(R.id.button);
        but.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS,1);
                mySpeechRecognizer.startListening(intent);
            }
        });

        ParseInstallation.getCurrentInstallation().saveInBackground();

        initializeSpeechRecognizer();

    }

    @Override
    protected void onStart() {
        super.onStart();

        Intent mIntent = new Intent(this, TtsService.class);
        bindService(mIntent, mConnection, BIND_AUTO_CREATE);
    }

    ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            Toast.makeText(MainActivity.this, "Service is disconnected", Toast.LENGTH_LONG).show();
            bounded = false;
            ttsService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Toast.makeText(MainActivity.this, "Service is CConnected", Toast.LENGTH_LONG).show();
            bounded = true;
            TtsService.LocalBinder mLocalBinder = (TtsService.LocalBinder)service;
            ttsService = mLocalBinder.getServerInstance();
        }
    };



    public void initializeSpeechRecognizer(){
        if(SpeechRecognizer.isRecognitionAvailable(this)){
            mySpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
            mySpeechRecognizer.setRecognitionListener(new RecognitionListener() {
                @Override
                public void onReadyForSpeech(Bundle params) {

                }

                @Override
                public void onBeginningOfSpeech() {

                }

                @Override
                public void onRmsChanged(float rmsdB) {

                }

                @Override
                public void onBufferReceived(byte[] buffer) {

                }

                @Override
                public void onEndOfSpeech() {

                }

                @Override
                public void onError(int error) {

                }

                @Override
                public void onResults(Bundle bundle) {
                    List<String> results = bundle.getStringArrayList(
                            SpeechRecognizer.RESULTS_RECOGNITION
                    );
                    processResult(results.get(0));
                }

                @Override
                public void onPartialResults(Bundle partialResults) {

                }

                @Override
                public void onEvent(int eventType, Bundle params) {

                }
            });
        }
    }

    public void processResult(String command){
        command = command.toLowerCase();
        Log.i("command",command);

        if(command.contains("add")){
            if(command.contains("medicines") || command.contains("medicine")){
                ttsService.speak("You have selected to add medicine.");
                Intent intent = new Intent(this,AddMedicine.class);
                intent.putExtra(EXTRA_MESSAGE,"Add Medicine");
                startActivity(intent);

            }


        } else if(command.contains("delete")){
            if(command.contains("medicines") || command.contains("medicine")){
                ttsService.speak("Delete Medicine Selected.");
                Intent intent = new Intent(this,ManageMedicineActivity.class);
                intent.putExtra(EXTRA_MESSAGE,"Delete Medicine");
                startActivity(intent);
                System.out.println("Delete");
            }

        } else if(command.contains("identify")){
            if(command.contains("medicines") || command.contains("medicine")){
                Intent intent = new Intent(this,OcrActivity.class);
                startActivity(intent);
            }
        } else if(command.contains("what")){
            if(command.contains("time")){
                Date now = new Date();
                String time = DateUtils.formatDateTime(this, now.getTime(), DateUtils.FORMAT_SHOW_TIME);
                ttsService.speak("Current Time is "+ time);
            }
        } else{
            ttsService.speak("Please repeat the command again.");
        }

    }


    @Override
    protected void onPause() {
        super.onPause();
        Toast.makeText(getApplicationContext(), "onPause called", Toast.LENGTH_LONG).show();
    }


}
