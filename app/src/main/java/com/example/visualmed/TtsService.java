package com.example.visualmed;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.Toast;

public class TtsService extends Service {

    TextToSpeech textToSpeech;
    IBinder mBinder = new LocalBinder();
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
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

    @Override
    public void onStart(Intent intent, int startId){
        Log.i("Service","onStart_service");
        super.onStart(intent,startId);
    }

    public class LocalBinder extends Binder {
        public TtsService getServerInstance() {
            return TtsService.this;
        }
    }
    public void speak(String message){
        if(Build.VERSION.SDK_INT >= 21){
            textToSpeech.speak(message, TextToSpeech.QUEUE_FLUSH,null, null);
        }
        else{
            textToSpeech.speak(message, TextToSpeech.QUEUE_FLUSH,null);
        }
    }

    public String getTime() {
        SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return mDateFormat.format(new Date());
    }

}