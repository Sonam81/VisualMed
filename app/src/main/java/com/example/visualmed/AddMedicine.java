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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseObject;

import java.util.List;


public class AddMedicine extends AppCompatActivity {

    TtsService ttsService;
    boolean bounded;
    private SpeechRecognizer mySpeechRecognizer;
    private EditText add_medicine_name_tf;
    private Button add_medicine_name_button;
    private String medicine_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_medicine);

        add_medicine_name_tf = findViewById(R.id.add_medicine_name_textfield);
        add_medicine_name_tf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS,1);
                mySpeechRecognizer.startListening(intent);
            }

        });

        medicine_name = add_medicine_name_tf.getText().toString();
        add_medicine_name_button = findViewById(R.id.add_medicine_name_button);
        add_medicine_name_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddMedicine.this, AddMedicineDose.class);
                intent.putExtra("medicine_name",medicine_name);
                startActivity(intent);
            }
        });
        initializeSpeechRecognizer();
    }


    public void addToParse(){
//        ParseObject gameScore = new ParseObject("GameScore");
//        gameScore.put("score", 1337);
//        gameScore.put("playerName", "Sean Plott");
//        gameScore.put("cheatMode", false);
//        gameScore.saveInBackground();
        Log.i("BaSS","Method called");
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
            Toast.makeText(AddMedicine.this, "Service is disconnected add medicine", Toast.LENGTH_LONG).show();
            bounded = false;
            ttsService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Toast.makeText(AddMedicine.this, "Service is CConnected in add medicine page", Toast.LENGTH_LONG).show();
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

    public void processResult(String command) {
        command = command.toLowerCase();
        medicine_name = command;
        add_medicine_name_tf.setText(command);
        ttsService.speak(command);
        Log.i("medicine_name",medicine_name);
    }

}


