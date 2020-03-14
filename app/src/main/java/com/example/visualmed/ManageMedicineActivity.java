package com.example.visualmed;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import androidx.room.Room;

public class ManageMedicineActivity extends Activity {

    public static MyAppDatabase myAppDatabase;
    private SpeechRecognizer mySpeechRecognizer;
    private ListView mListView;
    private ChatMessageAdapter mAdapter;
    private TextToSpeech textToSpeech;
    Intent speechIntent;
    List<String> speeches = new ArrayList<>();
    List<String> medicine_time = new ArrayList<>();
    List<MedicineTime> store_medicine_time = new ArrayList<>();
    boolean name_set = false;
    boolean time_set = false;

    public String medicine_name = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_medicine);
        myAppDatabase = Room.databaseBuilder(getApplicationContext(),MyAppDatabase.class,"medicineDB").allowMainThreadQueries().fallbackToDestructiveMigration().build();
        mListView = findViewById(R.id.listView);
        mAdapter = new ChatMessageAdapter(this, new ArrayList<ChatMessage>());
        mListView.setAdapter(mAdapter);
        initializeTextToSpeech();
        initializeSpeechRecognizer();

        speechIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS,1);

        mimicOtherMessage("Welcome! Please tap and speak the name of medicine using command medicine name");
        mListView.setSelection(mAdapter.getCount() - 1);

        //
//        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
//        Intent intent = new Intent(this, AlertReciever.class);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//        alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, System.currentTimeMillis() + AlarmManager.INTERVAL_HOUR, AlarmManager.INTERVAL_HOUR, pendingIntent);


    }

    public void speak(String message){
        if(Build.VERSION.SDK_INT >= 21){
            textToSpeech.speak(message, TextToSpeech.QUEUE_FLUSH,null, null);
        }
        else{
            textToSpeech.speak(message, TextToSpeech.QUEUE_FLUSH,null);
        }
    }

    private void sendMessage(String message) {
        ChatMessage chatMessage = new ChatMessage(message, true, false);
        mAdapter.add(chatMessage);

        //mimicOtherMessage(message);
    }

    private void mimicOtherMessage(String message) {
        ChatMessage chatMessage = new ChatMessage(message, false, false);
        mAdapter.add(chatMessage);
        speak(message);
    }

    private void sendMessage() {
        ChatMessage chatMessage = new ChatMessage(null, true, true);
        mAdapter.add(chatMessage);

        mimicOtherMessage();
    }

    private void mimicOtherMessage() {
        ChatMessage chatMessage = new ChatMessage(null, false, true);
        mAdapter.add(chatMessage);
    }

    public void clickLayout(View view) {
        if(textToSpeech != null)
            textToSpeech.stop();
        mySpeechRecognizer.startListening(speechIntent);
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

    public void initializeSpeechRecognizer() {
        if (SpeechRecognizer.isRecognitionAvailable(this)) {
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
                    assert results != null;
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
        sendMessage(command);
        speeches.add(command);
        replies(command);
    }

    public void replies(String obtainedString){

        int stringLength = obtainedString.length();
        if(obtainedString.startsWith("medicine name") && !time_set){
            medicine_name = obtainedString.substring(13, stringLength);
            name_set = true;
            mimicOtherMessage("Your medicine name is " + medicine_name + ". Tap and enter the time to take medicine.");
            mListView.setSelection(mAdapter.getCount() - 1);

        } else if(obtainedString.startsWith("delete medicine") && !name_set){
            String delMedicine = obtainedString.substring(15, stringLength);
            Log.i("delete",delMedicine);
            List<MedicineWithTime> medDetail = ManageMedicineActivity.myAppDatabase.medicineDAO().findMedicine(delMedicine);
            if (delMedicine.equals("")){
                mimicOtherMessage("Mention the name of medicine after command 'Delete medicine'");
            } else if(!medDetail.isEmpty()){
                Medicine medicine = medDetail.get(0).getMedicine();
                ManageMedicineActivity.myAppDatabase.medicineDAO().deleteMedicine(medicine);
                mimicOtherMessage(delMedicine + " is deleted.");
                Toast.makeText(getApplicationContext(),"Deleted.",Toast.LENGTH_LONG).show();
            } else{
                mimicOtherMessage(delMedicine + " is not found.");
            }
            mListView.setSelection(mAdapter.getCount() - 1);

        } else if(obtainedString.contains("medicine") && !obtainedString.contains("name") && !obtainedString.contains("delete")){
            if(!name_set && obtainedString.contains("view") || obtainedString.contains("display") || obtainedString.contains("read")){
//                direct to read medicine page
                Intent readMedicineActivity = new Intent(ManageMedicineActivity.this, ReadMedicine.class);
                startActivity(readMedicineActivity);

            } else if(!name_set && obtainedString.contains("add")){
                //cannot perform task
                mimicOtherMessage("You are currently on add medicine page.");
                mListView.setSelection(mAdapter.getCount() - 1);

            } else{
                mimicOtherMessage("Unrecognized command");
                mListView.setSelection(mAdapter.getCount() - 1);
            }
        }


        else if(obtainedString.contains("a.m.") || obtainedString.contains("p.m") && name_set){
            TimeValidator timeValidator = new TimeValidator();
            boolean validate = timeValidator.validate(obtainedString);
            if(validate){
                medicine_time.add(obtainedString);
                MedicineTime medTime = new MedicineTime();
                medTime.setMedicineTime(obtainedString);
                store_medicine_time.add(medTime);
                mimicOtherMessage("Time added as " + obtainedString + ". Add more time by tapping.\n Command \"save\" to save medicine.");
                time_set = true;
            } else{
                mimicOtherMessage("Please enter the valid time.");
            }
            mListView.setSelection(mAdapter.getCount() - 1);
        }

        else if(obtainedString.contains("save")){
            if(name_set && time_set){
                Medicine medicine = new Medicine();
                medicine.setMedicineName(medicine_name);
                medicine.setMedicineTimes(store_medicine_time);
                mimicOtherMessage("Medicine Name :"+ medicine_name + "\n Medicine Time: " + medicine_time.get(0));
                mListView.setSelection(mAdapter.getCount() - 1);
                ManageMedicineActivity.myAppDatabase.medicineDAO().insertMedicineWithTime(medicine);
                name_set = false;
                time_set = false;
                Toast.makeText(getApplicationContext(),"Insert Successfully!",Toast.LENGTH_LONG).show();
            }
            else if(!name_set){
                mimicOtherMessage("Please enter name and time first.");
                mListView.setSelection(mAdapter.getCount() - 1);
            }
            else {
                mimicOtherMessage("Please set the time first.");
                mListView.setSelection(mAdapter.getCount() - 1);
            }
        } else{
            mimicOtherMessage("Unrecognized Command");
            mListView.setSelection(mAdapter.getCount() - 1);
        }

    }

}
