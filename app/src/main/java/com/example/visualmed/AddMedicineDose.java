package com.example.visualmed;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;
import java.util.Locale;

public class AddMedicineDose extends AppCompatActivity {

    TtsService ttsService;
    private SpeechRecognizer mySpeechRecognizer;
    private EditText add_medicine_dose_tf;
    private Button add_medicine_dose_button;
    private String medicine_name = "";
    private String nemp = "MEpa";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_medicine_dose);

        add_medicine_dose_tf = findViewById(R.id.add_medicine_dose_textfield);
        add_medicine_dose_tf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS,1);
                mySpeechRecognizer.startListening(intent);
            }

        });

        Intent intent = getIntent();
        String text = intent.getStringExtra("medicine_name");
        medicine_name = getIntent().getExtras().getString("medicine_name");
        Log.i("txt",text);
        Log.i("errr",nemp);


        initializeSpeechRecognizer();
        //initializeTextToSpeech();


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

    public void processResult(String command) {
        command = command.toLowerCase();
        add_medicine_dose_tf.setText(command);
        ttsService.speak(command);
        Log.i("medicine_dose",command);
        Log.i("medicine_name", medicine_name);
    }

//    public void initializeTextToSpeech() {
//        myTTS = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
//            @Override
//            public void onInit(int status) {
//                if(myTTS.getEngines().size() == 0){
//                    Toast.makeText(AddMedicineDose.this,"There is no TTS in this device.",Toast.LENGTH_LONG).show();
//                    finish();
//                } else{
//                    myTTS.setLanguage(Locale.US);
//                }
//            }
//        });
//
//    }
//
//    public void speak(String message){
//        if(Build.VERSION.SDK_INT >= 21){
//            myTTS.speak(message, TextToSpeech.QUEUE_FLUSH,null, null);
//        }
//        else{
//            myTTS.speak(message, TextToSpeech.QUEUE_FLUSH,null);
//        }
//    }


}
