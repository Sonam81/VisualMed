package com.example.visualmed;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

public class ManageMedicineActivity extends AppCompatActivity{


    public static MyAppDatabase myAppDatabase;
    private ListView mListView;
    private ChatMessageAdapter mAdapter;
    private TextToSpeech textToSpeech;
    List<String> speeches = new ArrayList<>();
    List<String> medicine_time = new ArrayList<>();
    List<MedicineTime> store_medicine_time = new ArrayList<>();
    boolean name_set = false;
    boolean time_set = false;
    boolean identifiedMedicine = false;
    boolean updateMedicine = false;
    boolean callOnResult = false;
    int stringLength;
    String medicine_name_received = "";
    String operation = "";
    Calendar calendar;
    MedicineTime medTime;

    public String medicine_name = "";
    String result = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_medicine);
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
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
        mListView.setSelection(mAdapter.getCount() - 1);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.setting) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void speak(String message){
        if(Build.VERSION.SDK_INT >= 21){
            textToSpeech.speak(message, TextToSpeech.QUEUE_FLUSH,null, null);
        }
        else{
            textToSpeech.speak(message, TextToSpeech.QUEUE_FLUSH,null);
        }
    }

    private void displayCommand(String message) {
        ChatMessage chatMessage = new ChatMessage(message, true);
        mAdapter.add(chatMessage);
    }

    private void displaySystemMessage(String message) {
        ChatMessage chatMessage = new ChatMessage(message, false);
        mAdapter.add(chatMessage);
        speak(message);
    }

    public void clickLayout(View view) {
        Log.i("clickLayout", "clickLayout: clicked");
        if(textToSpeech != null) textToSpeech.stop();
        callOnResult = true;
//        mySpeechRecognizer.startListening(speechIntent);
        startSpeechRecognizer();
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
                        displaySystemMessage("Welcome! Please tap and add the name of medicine using command add medicine");
                } else {
                    Toast.makeText(getApplicationContext(), "TTS Initialization failed!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void startSpeechRecognizer() {
        Intent intent = new Intent
                (RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        startActivityForResult(intent, 2000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2000) {
            if (resultCode == RESULT_OK) {
                List<String> results = data.getStringArrayListExtra
                        (RecognizerIntent.EXTRA_RESULTS);
                result = results.get(0);
                processResult(result);
            }
        }
    }
    //Obtained text processing
    public void processResult(String command) {
        command = command.toLowerCase();
        //display the text on the right side of the screen on Listview
        displayCommand(command);
        Log.i("commands", "processResult: "+command);
        //add this speech to List
        speeches.add(command);
        //send the text to identify the command
        replies(command);
    }

    public void replies(String obtainedString){
        //get length of string
        stringLength = obtainedString.length();
        //if the command starts with 'add medicine'
        //and if medicine time is not set
        //and if there is text after 'add medicine' command
        if(obtainedString.startsWith("add medicine") && !time_set && stringLength > 12)
            //save medicine name
            storeMedicineName(obtainedString);

            //else if the command is to clear time
        else if(obtainedString.startsWith("clear time")){
            //clear medicine time
            store_medicine_time.clear();
            mListView.setSelection(mAdapter.getCount() - 1);
        }

        //else if command starts with delete medicine and name is not set then delete the medicine
        else if(obtainedString.startsWith("delete medicine") && !name_set)
            deleteMedicine(obtainedString);

            //else if commmand contains view, display or read then open read medicnie page
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

        //to add medicine time of the medicine
        else if(obtainedString.contains("a.m.") || obtainedString.contains("p.m") && name_set)
            addMedicineTime(obtainedString);

            //to save the medicine in local database
        else if(obtainedString.contains("save"))
            saveMedicine();

            //if user wants to know the time
        else if(obtainedString.contains("what")) {
            if (obtainedString.contains("time")) {
                Date now = new Date();
                String time = DateUtils.formatDateTime(this, now.getTime(), DateUtils.FORMAT_SHOW_TIME);
                speak("Current Time is " + time);
            }
        }

        //if feature of text does not match with any above
        else
            unrecognizedCommand();
    }

    //display that the system doesnot understand the given command
    public void unrecognizedCommand(){
        displaySystemMessage("Unrecognized Command");
        mListView.setSelection(mAdapter.getCount() - 1);
    }

    /*
     *Function to save name of medicine
     *Identifies if medicineName is coming from main page or identify medicine page.
     *If medicine name is coming from main page trim and get only name of medicine.
     *Check if medicine is already available in database
     *If it is available in database ask user to update with new time
     *Else ask to add time to new medicine.
     *Set the name_set boolean to true
     *@param medName name of medicine as command
     */

    public void storeMedicineName(String medName){
        if(!identifiedMedicine) {
            String text = medName.substring(13).trim();
            medicine_name = text;
        }
        else medicine_name = medName;

        List<MedicineWithTime> medicineWithTimes = ManageMedicineActivity.myAppDatabase.medicineDAO().findMedicine(medicine_name);

        if(medicineWithTimes.size() > 0) {
            displaySystemMessage(medicine_name.substring(0, 1).toUpperCase() + medicine_name.substring(1) + " already on list.Tap and enter the time to update new details.");
        }
        else {
            displaySystemMessage("Your medicine name is " + medicine_name.substring(0, 1).toUpperCase() + medicine_name.substring(1) + ". Tap and enter the time to take medicine.");
        }
        name_set = true;
        mListView.setSelection(mAdapter.getCount() - 1);
    }

    /*
     *Function to add medicine time
     *Check if the time is valid or not
     *If it is valid save time in list and display the added time to user
     *Else ask user to enter valid time again
     *@param time the time obtained from command
     */
    public void addMedicineTime(String time){
        TimeValidator timeValidator = new TimeValidator();
        boolean validate = timeValidator.validate(time);
        if(validate){
            medicine_time.add(time);
            medTime = new MedicineTime();
            medTime.setMedicineTime(time);
            displaySystemMessage("Time added as " + time + ". Add more time by tapping.\n Command \"save\" to save medicine.");
            time_set = true;
            setAlarm();
        } else{ displaySystemMessage("Please enter the valid time."); }
        mListView.setSelection(mAdapter.getCount() - 1);
    }

    /*
    Function to add alarm
        1. Split the time into hr(format of 24hr) and min
        2. Call the alarm function to save the alarm

    */
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

    /*

     */
    public void alarm(int hr ,int min){

        calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, hr);
        calendar.set(Calendar.MINUTE, min);

        int currentTime = (int) (System.currentTimeMillis() % Integer.MAX_VALUE);
        medTime.setMedicineAlarmId(currentTime);
        store_medicine_time.add(medTime);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        intent.putExtra("medicine_name",medicine_name);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,currentTime, intent, 0);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        medicine_time.clear();
    }

    public void saveMedicine(){
        if(name_set && time_set){
            Medicine medicine = new Medicine();
            medicine.setMedicineName(medicine_name);
            medicine.setMedicineTimes(store_medicine_time);
            displaySystemMessage("Medicine Name :"+ medicine_name);

            List<MedicineWithTime> medicineWithTimes = ManageMedicineActivity.myAppDatabase.medicineDAO().findMedicine(medicine_name);

            if(medicineWithTimes.size() > 0) {
                Medicine m = medicineWithTimes.get(0).getMedicine();
                List<MedicineTime> mt = medicineWithTimes.get(0).getMedicineTimes();
                removeAlarm(medicineWithTimes.get(0).getMedicineTimes());
                ManageMedicineActivity.myAppDatabase.medicineDAO().deleteMedicine(m);
                ManageMedicineActivity.myAppDatabase.medicineDAO().deleteTime(mt);
            }

            ManageMedicineActivity.myAppDatabase.medicineDAO().insertMedicineWithTime(medicine);
            store_medicine_time.clear();
            name_set = false;
            time_set = false;
            mListView.setSelection(mAdapter.getCount() - 1);
        }
        else {
            displaySystemMessage("Please set medicine name and time first.");
            mListView.setSelection(mAdapter.getCount() - 1);
        }
    }

    public void deleteMedicine(String obtainedString){
        if(!identifiedMedicine)
            medicine_name = obtainedString.substring(16, stringLength);
        else
            medicine_name = obtainedString.trim();
        Log.i("delete",medicine_name);
        List<MedicineWithTime> medDetail = ManageMedicineActivity
                .myAppDatabase.medicineDAO().findMedicine(medicine_name);
        if (medicine_name.equals("")){
            displaySystemMessage("Mention the name of medicine after command 'Delete medicine'");
        } else if(!medDetail.isEmpty()){
            Medicine medicine = medDetail.get(0).getMedicine();
            List<MedicineTime> medTimes = medDetail.get(0).getMedicineTimes();
            removeAlarm(medDetail.get(0).getMedicineTimes());
            ManageMedicineActivity.myAppDatabase.medicineDAO().deleteMedicine(medicine);
            ManageMedicineActivity.myAppDatabase.medicineDAO().deleteTime(medTimes);

            displaySystemMessage(medicine_name + " is deleted.");
            Toast.makeText(getApplicationContext(),"Deleted.",Toast.LENGTH_LONG).show();
        } else{
            displaySystemMessage(medicine_name + " is not found.");
        }
        mListView.setSelection(mAdapter.getCount() - 1);
    }

    public void removeAlarm(List<MedicineTime> medicineTimes){
        int allAlarms = medicineTimes.size();
        for(int i = 0; i < allAlarms; i++) {
            int reqCode = medicineTimes.get(i).getMedicineAlarmId();
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            Intent intent = new Intent(getApplicationContext(), AlertReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),reqCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
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
//        mySpeechRecognizer.stopListening();
        if(textToSpeech != null)
            textToSpeech.stop();
    }

    @Override
    protected void onPause(){
        super.onPause();
//        mySpeechRecognizer.stopListening();
        if(textToSpeech != null)
            textToSpeech.stop();
    }

}
