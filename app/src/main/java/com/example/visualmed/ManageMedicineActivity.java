package com.example.visualmed;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
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
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.annotation.RequiresApi;
import androidx.room.Room;

public class ManageMedicineActivity extends Activity{

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
    boolean identifiedMedicine = false;
    int stringLength;
    String medicine_name_received = "";
    String operation = "";
    Calendar calendar;
    MedicineTime medTime;

    public String medicine_name = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_medicine);
        myAppDatabase = Room.databaseBuilder(getApplicationContext(),MyAppDatabase.class,"medicineDB").allowMainThreadQueries().fallbackToDestructiveMigration().build();
        mListView = findViewById(R.id.listView);
        mAdapter = new ChatMessageAdapter(this, new ArrayList<ChatMessage>());
        mListView.setAdapter(mAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                clickLayout(view);
            }
        });
        initializeTextToSpeech();
        initializeSpeechRecognizer();

        speechIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS,1);

        mListView.setSelection(mAdapter.getCount() - 1);
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
        ChatMessage chatMessage = new ChatMessage(message, true);
        mAdapter.add(chatMessage);
    }

    private void mimicOtherMessage(String message) {
        ChatMessage chatMessage = new ChatMessage(message, false);
        mAdapter.add(chatMessage);
        speak(message);
    }

    public void clickLayout(View view) {
        if(textToSpeech != null)
            textToSpeech.stop();
        mySpeechRecognizer.startListening(speechIntent);
    }

    public void clickThis(View view){
        Toast.makeText(this, "Clicked", Toast.LENGTH_LONG).show();
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
                    if(!identifiedMedicine)
                    mimicOtherMessage("Welcome! Please tap and speak the name of medicine using command medicine name");
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
                    assert results != null;
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
        sendMessage(command);
        speeches.add(command);
        replies(command);
    }

    public void replies(String obtainedString){
        stringLength = obtainedString.length();
        if(obtainedString.startsWith("add medicine") && !time_set && stringLength > 12)
            storeMedicineName(obtainedString);

        else if(obtainedString.startsWith("delete medicine") && !name_set)
            deleteMedicine(obtainedString);
        else if(obtainedString.contains("medicine") && !obtainedString.contains("add") && !obtainedString.contains("delete")){
            if(!name_set && obtainedString.contains("view") || obtainedString.contains("display") || obtainedString.contains("read")) {
                //direct to read medicine page
                Intent readMedicineActivity = new Intent(ManageMedicineActivity.this, ReadMedicine.class);
                startActivity(readMedicineActivity);
            } else if(obtainedString.contains("identify")){
                //direct to identify medicine page
                Intent identifyMedicine = new Intent(ManageMedicineActivity.this,OcrActivity.class);
                startActivity(identifyMedicine);
            } else unrecognizedCommand();
        }
        else if(obtainedString.contains("a.m.") || obtainedString.contains("p.m") && name_set)
            addMedicineTime(obtainedString);
        else if(obtainedString.contains("save"))
            saveMedicine();
        else if(obtainedString.contains("what")) {
            if (obtainedString.contains("time")) {
                Date now = new Date();
                String time = DateUtils.formatDateTime(this, now.getTime(), DateUtils.FORMAT_SHOW_TIME);
                speak("Current Time is " + time);
            }
        }
        else
            unrecognizedCommand();
    }

    public void unrecognizedCommand(){
        mimicOtherMessage("Unrecognized Command");
        mListView.setSelection(mAdapter.getCount() - 1);
    }

    public void storeMedicineName(String medName){
        if(!identifiedMedicine) {
            String text = medName.substring(13).trim();
            medicine_name = text;
        }
        else medicine_name = medName;
        name_set = true;
        mimicOtherMessage("Your medicine name is " + medicine_name.substring(0,1).toUpperCase()+medicine_name.substring(1) + ". Tap and enter the time to take medicine.");
        mListView.setSelection(mAdapter.getCount() - 1);
    }

    public void addMedicineTime(String time){
        TimeValidator timeValidator = new TimeValidator();
        boolean validate = timeValidator.validate(time);
        if(validate){
            medicine_time.add(time);
            medTime = new MedicineTime();
            medTime.setMedicineTime(time);
            mimicOtherMessage("Time added as " + time + ". Add more time by tapping.\n Command \"save\" to save medicine.");
            time_set = true;
            setAlarm();
        } else{ mimicOtherMessage("Please enter the valid time."); }
        mListView.setSelection(mAdapter.getCount() - 1);
    }

    public void setAlarm(){
        for(int i =0; i < medicine_time.size();i++){
            String[] time = medicine_time.get(i).split (":" );
            int hr = Integer.parseInt(time[0]);
            int min = Integer.parseInt(time[1].substring(0,2));
            if(time[1].substring(2).trim().equals("p.m.")){
                hr+=12;
            }
            alarm(hr,min);
        }
        Toast.makeText(getApplicationContext(),"Alarm Set",Toast.LENGTH_LONG).show();
    }

    public void alarm(int hr ,int min){

        calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, hr);
        calendar.set(Calendar.MINUTE, min);

        int currentTime = (int) (System.currentTimeMillis() % Integer.MAX_VALUE);
        medTime.setMedicineAlarmId(currentTime);
        store_medicine_time.add(medTime);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReciever.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,currentTime, intent, 0);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);

        Toast.makeText(this, "Alarm Added", Toast.LENGTH_SHORT).show();
        medicine_time.clear();
    }

    public void saveMedicine(){
        if(name_set && time_set){
            Medicine medicine = new Medicine();
            medicine.setMedicineName(medicine_name);
            medicine.setMedicineTimes(store_medicine_time);
            mimicOtherMessage("Medicine Name :"+ medicine_name);
            mListView.setSelection(mAdapter.getCount() - 1);
            ManageMedicineActivity.myAppDatabase.medicineDAO().insertMedicineWithTime(medicine);
            store_medicine_time.clear();
            name_set = false;
            time_set = false;
            Toast.makeText(getApplicationContext(),"Insert Successfully!",Toast.LENGTH_LONG).show();
        }
        else {
            mimicOtherMessage("Please set medicine name and time first.");
            mListView.setSelection(mAdapter.getCount() - 1);
        }
    }

    public void deleteMedicine(String obtainedString){
        if(!identifiedMedicine)
            medicine_name = obtainedString.substring(16, stringLength);
        else
            medicine_name = obtainedString.trim();
        Log.i("delete",medicine_name);
        List<MedicineWithTime> medDetail = ManageMedicineActivity.myAppDatabase.medicineDAO().findMedicine(medicine_name);
        if (medicine_name.equals("")){
            mimicOtherMessage("Mention the name of medicine after command 'Delete medicine'");
        } else if(!medDetail.isEmpty()){
            Medicine medicine = medDetail.get(0).getMedicine();
            removeAlarm(medDetail.get(0).getMedicineTimes());
            ManageMedicineActivity.myAppDatabase.medicineDAO().deleteMedicine(medicine);
            mimicOtherMessage(medicine_name + " is deleted.");
            Toast.makeText(getApplicationContext(),"Deleted.",Toast.LENGTH_LONG).show();
        } else{
            mimicOtherMessage(medicine_name + " is not found.");
        }
        mListView.setSelection(mAdapter.getCount() - 1);
    }

    public void removeAlarm(List<MedicineTime> medicineTimes){
        int allAlarms = medicineTimes.size();
        for(int i = 0; i < allAlarms; i++) {
            int reqCode = medicineTimes.get(i).getMedicineAlarmId();
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            Intent intent = new Intent(getApplicationContext(), AlertReciever.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),reqCode, intent, 0);
            alarmManager.cancel(pendingIntent);
            pendingIntent.cancel();
        }
        Toast.makeText(this, "Alarm Removed", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = getIntent();
        if(intent.getExtras() != null){
            medicine_name_received = intent.getExtras().getString("medicine_identified");
            operation = intent.getExtras().getString("operation");
            if(!medicine_name_received.equals("")) {
                identifiedMedicine = true;
                assert operation != null;
                if (operation.equals("add medicine")) {
                    storeMedicineName(medicine_name_received);
                } else if (operation.equals("delete medicine")) {
                    deleteMedicine(medicine_name_received);
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mySpeechRecognizer.destroy();
        if(textToSpeech != null)
            textToSpeech.stop();
    }

}
