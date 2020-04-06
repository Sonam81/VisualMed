package com.example.visualmed;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.ahocorasick.trie.Emit;
import org.ahocorasick.trie.Token;
import org.ahocorasick.trie.Trie;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class IdentifyMedicine extends AppCompatActivity {

    SpeechRecognizer mySpeechRecognizer;
    Intent speechIntent;
    String information = "";
    ArrayList<String> userList;
    TextView textView;
    TextView identifyCommandTextView;
    String medicine_name = "";
    TextToSpeech textToSpeech;
    final String TAG = "medicine_identified";
    final String OPERATION = "operation";
    String databaseChild = "1tWCAGLC6skVixISB0mVcwKAJ5ZbsukTy_fuVBpbmpEo/medicine";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identify_medicine);
        textView = findViewById(R.id.identifyMedicineTextView);
        identifyCommandTextView = findViewById(R.id.identifyCommand);
        initializeTextToSpeech();
        initializeSpeechRecognizer();

        speechIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS,1);


        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child(databaseChild);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userList = new ArrayList<>();
                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                    Map map = (Map) dsp.getValue();
                    userList.add(String.valueOf(map.get("medicine_name"))); //add result into array list
//                    System.out.println(userList.toString());
                }
                ahoSearch(information);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }


    public void initializeSpeechRecognizer() {
        if (SpeechRecognizer.isRecognitionAvailable(this)) {
            mySpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
            mySpeechRecognizer.setRecognitionListener(new RecognitionListener() {
                @Override
                public void onReadyForSpeech(Bundle params) {}

                @Override
                public void onBeginningOfSpeech() {}

                @Override
                public void onRmsChanged(float rmsdB) {}

                @Override
                public void onBufferReceived(byte[] buffer) {}

                @Override
                public void onEndOfSpeech() {}

                @Override
                public void onError(int error) {}

                @Override
                public void onResults(Bundle bundle) {
                    List<String> results = bundle.getStringArrayList(
                            SpeechRecognizer.RESULTS_RECOGNITION
                    );
                    processResult(results.get(0));
                }

                @Override
                public void onPartialResults(Bundle partialResults) {}

                @Override
                public void onEvent(int eventType, Bundle params) {}
            });
        }
    }

    public void processResult(String command) {
        command = command.toLowerCase();
        identifyCommandTextView.setText(command);
        if(command.startsWith("add medicine")){
            Intent intent = new Intent(IdentifyMedicine.this, ManageMedicineActivity.class);
            intent.putExtra(TAG,medicine_name);
            intent.putExtra(OPERATION,"add medicine");
            startActivity(intent);
        }
        else if(command.startsWith("remove medicine")){
            Intent intent = new Intent(IdentifyMedicine.this, ManageMedicineActivity.class );
            intent.putExtra(TAG,medicine_name);
            intent.putExtra(OPERATION,"delete medicine");
            startActivity(intent);
        }
        else{
            speak("Unrecognized command");
        }
     }

    @Override
    public void onStart() {
        super.onStart();
        Intent intent = getIntent();
        try {
            information = intent.getExtras().getString("detail");
        } catch (NullPointerException e){
            speak("Medicine string unavailable");
        }
    }

    public void ahoSearch(String details){
        Trie trie = Trie.builder().ignoreOverlaps().onlyWholeWords().ignoreCase()
                .addKeywords(userList)
                .build();

        Emit firstMatch = trie.firstMatch(details);
        try{
            medicine_name = firstMatch.getKeyword();
            String med = medicine_name.substring(0,1).toUpperCase()+medicine_name.substring(1);
            textView.setText(med);
            speak(medicine_name);
        } catch (NullPointerException e){
            speak("Medicine name not found.");
            System.out.println(e.getMessage());
        }

    }

    public void identifyClick(View view){
//        speak(medicine_name + " Command add medicine or remove medicine to add or remove this medicine from your list.");
        mySpeechRecognizer.startListening(speechIntent);

        }

    public void speak(String message){
        if(Build.VERSION.SDK_INT >= 21){
            textToSpeech.speak(message, TextToSpeech.QUEUE_FLUSH,null, null);
        }
        else{
            textToSpeech.speak(message, TextToSpeech.QUEUE_FLUSH,null);
        }
    }
    public void initializeTextToSpeech(){
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
                } else {
                    Toast.makeText(getApplicationContext(), "TTS Initialization failed!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

}
