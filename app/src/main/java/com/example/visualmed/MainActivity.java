package com.example.visualmed;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.Date;
import java.util.List;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {

    private TextToSpeech textToSpeech;
    private SpeechRecognizer mySpeechRecognizer;
    public static final String EXTRA_MESSAGE = "com.example.visualmed.extra.MESSAGE";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button but = findViewById(R.id.button);
        but.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS,1);
                mySpeechRecognizer.startListening(intent);
            }
        });

        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int ttsLang = textToSpeech.setLanguage(Locale.US);

                    if (ttsLang == TextToSpeech.LANG_MISSING_DATA
                            || ttsLang == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "The Language is not supported!");
                    } else {
                        Log.i("TTS", "Language Supported.");
                    }
                    Log.i("TTS", "Initialization success.");
                    speak("Welcome. Please tap on the screen and command the operation.");
                } else {
                    Toast.makeText(getApplicationContext(), "TTS Initialization failed!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        initializeSpeechRecognizer();

    }
//
//    public void initializeTextToSpeech() {
//        myTTS = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
//            @Override
//            public void onInit(int status) {
//                if(myTTS.getEngines().size() == 0){
//                    Toast.makeText(MainActivity.this,"There is no TTS in this device.",Toast.LENGTH_LONG).show();
//                    finish();
//                } else{
//                    myTTS.setLanguage(Locale.US);
//                }
//            }
//        });
//    }

    public void speak(String message){
        if(Build.VERSION.SDK_INT >= 21){
            textToSpeech.speak(message, TextToSpeech.QUEUE_FLUSH,null, null);
        }
        else{
            textToSpeech.speak(message, TextToSpeech.QUEUE_FLUSH,null);
        }
    }

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
                speak("You have selected to add medicine.");
                //Intent intent = new Intent(this,AddMedicine.class);
                //intent.putExtra(EXTRA_MESSAGE,"Add Medicine");
                //startActivity(intent);

            }

        } else if(command.contains("delete")){
            if(command.contains("medicines") || command.contains("medicine")){
                speak("Delete Medicine Selected.");
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
                speak("Current Time is "+ time);
            }
        } else{
            speak("Please repeat the command again.");
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
    }
}
